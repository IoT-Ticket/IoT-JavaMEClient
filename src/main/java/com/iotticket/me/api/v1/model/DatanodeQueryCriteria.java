package com.iotticket.me.api.v1.model;

import java.util.*;

/**
 * This class contains various attributes that can be used to query process values
 * from the server.
 * <p>
 * The deviceId and the names or full path of the datanodes to be queried most be supplied
 * </p>
 */

public class DatanodeQueryCriteria {

    public static final int None = -1;
    public static final int Ascending = 0;
    public static final int Descending = 1;
	
    private String deviceId;
    private int sortOrder = None;
    private Integer limit;
    private Long fromDate;
    private Long toDate;
    private Set dataPaths = new HashSet();
    
    public DatanodeQueryCriteria(String deviceId, Collection datapoints) {
        setDeviceId(deviceId);
        setDataPaths(datapoints);
    }
    
    public DatanodeQueryCriteria(String deviceId, String datapoint) {
        setDeviceId(deviceId);
        Collection datapoints = new ArrayList();
        datapoints.add(datapoint);
        setDataPaths(datapoints);
    }

    public Long getFromDate() {
        return fromDate;
    }


    public void setFromDate(Date fromDate) {
        this.fromDate = new Long(fromDate.getTime());
    }


    /**
     * Unix Timestamp. Number of milliseconds since the Epoch.
     * Defines the begining time from which the process values are fetched.
     *
     * @param fromDate
     */

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Date date) {
        this.toDate = new Long(date.getTime());
    }


    /**
     * @param toDate Unix Timestamp. Number of milliseconds since the Epoch.
     *               Defines the ending time from which the process values are fetched.
     */

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public String getDeviceId() {
        return deviceId;
    }


    /**
     * @param deviceId The id of the device to be queried.
     */
    public void setDeviceId(String deviceId) {
        if (deviceId == null || deviceId.length() == 0)
            throw new IllegalArgumentException("DeviceId must be set");
        this.deviceId = deviceId;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }
    
    public String getSortName() {
    	if (this.sortOrder == Ascending) {
    		return "Ascending";
    	} else if (this.sortOrder == Descending) {
    		return "Descending";
    	}
    	return "";
    }

    public Integer getLimit() {
        return limit;
    }

    /**
     * @param limit Maximum number of result to return for each datanode read.
     */

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
    
    public void setLimit(int limit) {
        this.limit = new Integer(limit);
    }

    public String getDataNodePaths() {
        StringBuffer sb = new StringBuffer();

        String[] strings = new String[dataPaths.size()];
        dataPaths.toArray(strings);
        int len = strings.length;
        for (int i = 0; i < len; i++) {
            sb.append(strings[i]);
            if (i != (len - 1)) {
                sb.append(",");
            }
        }
        return sb.toString();
    }

    public Set getDataPaths() {
        return dataPaths;
    }


    /**
     *
     * Example for a device that has only these three datanodes.
     * <p>
     * <pre>
     *           NAME     |  PATH
     * 1.    Temperature  |
     * 2.    Temperature  | Engine/Core
     * 3.    Temperature  | Engine/Auxilliary
     * </pre>
     * </p>
     * To query specifically for the first,  the dataPath will be "/Temperature" <br/>
     * To query specifically for the second, the dataPath will be "/Engine/Core/Temperature"<br/>
     * To query specifically for the third,  the dataPath will be "/Engine/Auxilliary/Temperature"<br/>
     * <p/>
     * To make a combined operation to read the process values for all three datanodes, the three dataPaths above
     * can be added to the param dataPaths. Alternatively, the dataPath "Temperature" without the slash could be used
     * as they all have the name "Temperature".
     * <p/>
     * <p/>
     * NOTE A query using /Engine or /Engine/core as a dataPath will not return any result.
     * </P>
     *
     * @param dataPaths Collection of fullpaths or datanode name.
     */
    public void setDataPaths(Collection dataPaths) {
        if (dataPaths.isEmpty()) {
            throw new IllegalArgumentException("At least one datapoint needs to be defined");
        }
        this.dataPaths.clear();
        this.dataPaths.addAll(dataPaths);
    }

}