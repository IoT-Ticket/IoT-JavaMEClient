package com.iotticket.me.api.v1;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import com.iotticket.me.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.me.api.v1.exception.ValidAPIParamException;
import com.iotticket.me.api.v1.model.*;
import com.iotticket.me.api.v1.model.Datanode.DataNodeList;
import com.iotticket.me.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.me.api.v1.model.Datanode.DatanodeReadValue;
import com.iotticket.me.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.me.api.v1.model.Device.DeviceDetails;
import com.iotticket.me.api.v1.validation.ValidationRunner;
import com.iotticket.me.utils.HttpClient;
import com.iotticket.me.utils.HttpResponse;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.sf.microlog.core.Logger;
import net.sf.microlog.core.LoggerFactory;

public class IOTAPIClient {


    private final static String DevicesResource = "devices/";
    private final static String DatanodesResource = "datanodes/";
    private final static String WriteDataResource = "process/write/";
    private final static String ReadDataResource = "process/read/";
    private final static String QuotaAllResource = "quota/all/";
    private final static String QuotaDeviceResource= "quota/";
    private static final Logger log = LoggerFactory.getLogger(IOTAPIClient.class);
    private final ValidationRunner validator = new ValidationRunner();
    private final HttpClient client = new HttpClient();
    private String baseURL = "";


    public IOTAPIClient(String serverUrl, String userName, String password) {
        if (serverUrl == null || userName == null || password == null) {
            throw new IllegalArgumentException("Ensure the serverUrl, username and password are not null");
        }
        baseURL = serverUrl;
        if (!baseURL.substring(baseURL.length()-1, baseURL.length()).equals("/")) {
        	baseURL = baseURL + "/";
        }
        client.setCredentials(userName, password);
    }


    /**
     * @param device provides the description of the device to be registered.
     * @return a <tt>DeviceDetails</tt> object that includes, among other things specified in the device object, the deviceId
     * {@link DeviceDetails#getDeviceId}.and the URI for the newly registered device.
     * @throws ValidAPIParamException if the device does not meet the specified requirements
     * @throws JSONException 
     */

    public DeviceDetails registerDevice(Device device) throws ValidAPIParamException, JSONException {

        validator.runValidation(device);

        String json = device.toJSON();
        
        HttpResponse response = client.post(baseURL + DevicesResource, "application/json; charset=UTF-8", json, "application/json");
        JSONObject jsonobj = handleResponse(response);
        DeviceDetails details = new DeviceDetails();
        details.getFieldCollection().setFromJSON(jsonobj);
        return details;
    }

    private JSONObject handleResponse(HttpResponse response) throws JSONException {
    	if (response.statusCode < 200 || response.statusCode > 299) {
    		ErrorInfo errorResponse = getErrorInfo(response);
    		log.info(errorResponse.toString());
    		throw new IoTServerCommunicationException("Request with server was unsuccesful, check the errorInfo object for further details", errorResponse);
    	}
    	return new JSONObject(response.body);
    }

    private ErrorInfo getErrorInfo(HttpResponse response) {
        ErrorInfo info = new ErrorInfo();
        info.setHttpStatus(response);
        try {
        	JSONObject object = new JSONObject(response.body);
        	info.description = object.optString("description");
        	info.code = object.optInt("code");
        	info.moreInfo = object.optString("moreInfo");
        	info.apiver = object.optInt("apiver");
        	info.exception = response.exception;
            return info;
        } catch (JSONException e) {
        	log.debug("Error info was not obtained from the server. Status was " + response.statusCode);
        }

        return info;

    }

    /**
     *
     * @param deviceId The target Device identifier
     * @param writeValues Collection of DatanodeWriteValue to write to the server
     * @return <tt>WriteDataResponse</tt>
     * @throws ValidAPIParamException If the any of the DatanodeWriteValue doesn't meet specified requirement.
     * @throws JSONException 
     */

    public WriteDataResponse writeData(String deviceId, Collection writeValues) throws ValidAPIParamException, JSONException {

    	Iterator it = writeValues.iterator();
    	while (it.hasNext()) {
    		DatanodeWriteValue writeValue = (DatanodeWriteValue)it.next();
    		validator.runValidation(writeValue);
    	}

        JSONArray array = new JSONArray();
        it = writeValues.iterator();
        while (it.hasNext()) {
        	DatanodeWriteValue writeValue = (DatanodeWriteValue)it.next();
        	JSONObject obj = writeValue.getFieldCollection().getJSON();
        	array.put(obj);
        }
        String json = array.toString(); 

        log.debug(json);
        
        HttpResponse response = client.post(baseURL + WriteDataResource + deviceId + "/", "application/json; charset=UTF-8", json, "application/json");
        
        JSONObject jsonobj = handleResponse(response);
        WriteDataResponse resp = new WriteDataResponse();
        resp.getFieldCollection().setFromJSON(jsonobj);
        
        return resp;
    }
    
    /**
    *
    * @param deviceId The target Device identifier
    * @param writeValue DatanodeWriteValue to write to the server
    * @return <tt>WriteDataResponse</tt>
    * @throws ValidAPIParamException If the DatanodeWriteValue doesn't meet specified requirement.
    * @throws JSONException 
    */

    public WriteDataResponse writeData(String deviceId, DatanodeWriteValue writeValue) throws ValidAPIParamException, JSONException {
    	Collection values = new ArrayList();
    	values.add(writeValue);
    	return writeData(deviceId, values);
    }

    /**
     * @param offset The amount to skip from the beginning.
     * @param limit  The maximum amount of result to be returned
     * @return a Collection of client's devices with paging support. Obtain items list using {@link PagedResult#getResults}
     * @throws JSONException 
     */

    public PagedResult getDeviceList(int offset, int limit) throws JSONException {

    	HttpResponse response = client.get(baseURL + DevicesResource + "?limit=" + limit + "&offset=" + offset);
    	JSONObject jsonobj = handleResponse(response);
    	Device.DeviceList deviceList = new Device.DeviceList();
        deviceList.getFieldCollection().setFromJSON(jsonobj);
        return deviceList;
    }

    /**
     *
     * @param deviceId The device to query.
     * @param offset The amount to skip from the beginning.
     * @param limit  The maximum amount of result to be returned
     * @return Get a list of provided device datanodes with Paging support.Obtain items list using {@link PagedResult#getResults}
     * @throws JSONException 
     */
    public PagedResult getDeviceDataNodeList(String deviceId, int offset, int limit) throws JSONException {
    	HttpResponse response = client.get(baseURL + DevicesResource + deviceId + DatanodesResource + "?limit=" + limit + "&offset=" + offset, "application/json");
    	JSONObject jsonobj = handleResponse(response);
    	DataNodeList nodeList = new DataNodeList();
        nodeList.getFieldCollection().setFromJSON(jsonobj);
        return nodeList;
    }

    /**
     * @param deviceId The deviceId for the device to be fetched from the server.
     * @return <tt>DeviceDetails</tt> that describes the device's name, attributes, URI etc.
     * @throws JSONException 
     */

    public Device.DeviceDetails getDevice(String deviceId) throws JSONException {
    	HttpResponse response = client.get(baseURL + DevicesResource + deviceId, "application/json");
    	JSONObject jsonobj = handleResponse(response);
    	DeviceDetails details = new DeviceDetails();
        details.getFieldCollection().setFromJSON(jsonobj);
        return details;
    }


    /**
     * @param criteria The <tt>DatanodeQueryCriteria</tt> object used to query for the process values
     * @return Request <tt>ProcessValues</tt>
     * @throws JSONException 
     * @See DatanodeQueryCriteria
     */
    public ProcessValues readProcessData(DatanodeQueryCriteria criteria) throws JSONException {

    	StringBuffer buffer = new StringBuffer();
    	
    	buffer.append(baseURL + ReadDataResource + criteria.getDeviceId());
        buffer.append("?datanodes=" + criteria.getDataNodePaths());

        if (criteria.getSortOrder() != DatanodeQueryCriteria.None) {
            buffer.append("&order=" + criteria.getSortName());
        }
        if (criteria.getLimit() != null) {
            buffer.append("&limit=" + criteria.getLimit());
        }
        if (criteria.getFromDate() != null) {
            buffer.append("&fromdate=" + criteria.getFromDate());
        }
        if (criteria.getToDate() != null) {
            buffer.append("&todate=" + criteria.getToDate());
        }
        
        HttpResponse response = client.get(buffer.toString(), "application/json");
    	JSONObject jsonobj = handleResponse(response);
    	ProcessValues pv = new ProcessValues();
        pv.getFieldCollection().setFromJSON(jsonobj);

        Collection datanodeReads = pv.getDatanodeReads();
        Iterator it = datanodeReads.iterator();
        while (it.hasNext()) {
        	DatanodeRead datanodeRead = (DatanodeRead)it.next();
            String dataType = datanodeRead.getDataType();
            Collection values = datanodeRead.getDatanodeReadValues();

            Iterator v_it = values.iterator();
            while (v_it.hasNext()) {
            	DatanodeReadValue value = (DatanodeReadValue)v_it.next();
                value.setDataType(dataType);
            }
        }
        return pv;

    }


    /**
     *
     * @return Fetechs the user's <tt>Quota</tt> information from the server
     * @throws JSONException 
     */
    public Quota getQuota() throws JSONException {  
    	HttpResponse response = client.get(baseURL + QuotaAllResource, "application/json");
    	JSONObject jsonobj = handleResponse(response);
    	Quota quota = new Quota();
        quota.getFieldCollection().setFromJSON(jsonobj);
        return quota;
    }


    /**
     *
     * @param deviceId
     * @return Get the <tt>DeviceQuota</tt> from the server for the device with the specified deviceId
     * @throws JSONException 
     */

    public DeviceQuota getDeviceQuota(String deviceId) throws JSONException {
    	HttpResponse response = client.get(baseURL + QuotaDeviceResource + deviceId, "application/json");
    	JSONObject jsonobj = handleResponse(response);
    	DeviceQuota quota = new DeviceQuota();
        quota.getFieldCollection().setFromJSON(jsonobj);
        return quota;
    }
}
