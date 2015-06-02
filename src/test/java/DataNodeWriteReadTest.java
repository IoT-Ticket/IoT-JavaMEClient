import com.iotticket.me.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.me.api.v1.exception.ValidAPIParamException;
import com.iotticket.me.api.v1.model.*;
import com.iotticket.me.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.me.api.v1.model.Datanode.DatanodeReadValue;
import com.iotticket.me.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.me.api.v1.model.Device.DeviceDetails;

import org.json.me.JSONException;

import java.util.*;

public class DataNodeWriteReadTest extends TestBase {

	private final static byte[] testByteValue = new byte[]{1, 2, 3, 4, 5, 6, 7, 9, 10};
    public static String firstPath = "Engine/Auxillary";
    public static String secondPath = "Engine/Main";
    public static String numericDatanodeName = "AirVolume";
    private static boolean testBooleanValue = false;
    private static String boolDatanodeName = "LightOn";
    private String byteDatanodeName = "MP3";

    public void setup() {
        boolDatanodeName = "LightOn";
        byteDatanodeName = "CANMessage";
        numericDatanodeName = "AirFlow";
        firstPath = "Engine/Auxillary";
        secondPath = "Engine/Main";
    }

    public DataNodeWriteReadTest() {
		super(3, "DataNodeWriteReadTest");
		setup();
	}
    
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			writeBooleanValue();
			break;
		case 1:
			writeBinaryValue();
			break;
		case 2:
			writeNumericValue();
			break;
		}
	}

    public void writeBooleanValue() throws ValidAPIParamException, JSONException {
        DatanodeWriteValue bv = new DatanodeWriteValue();
        bv.setName(boolDatanodeName);
        bv.setValue(new Boolean(testBooleanValue));
        
        Collection collection = new ArrayList();
        collection.add(bv);
        WriteDataResponse writeResult = apiClient.writeData(deviceId, collection);
        assertNotNull(writeResult);
        assertTrue(writeResult.getTotalWritten() > 0);
        assertNotNull(writeResult.getWriteResults());
        assertTrue(((WriteResult)writeResult.getWriteResults().iterator().next()).getWrittenCount() > 0);
        assertNotNull(((WriteResult)writeResult.getWriteResults().iterator().next()).getHref());
        readBooleanValue();
    }


    public void readBooleanValue() throws JSONException {

    	Collection collection = new ArrayList();
    	collection.add(boolDatanodeName);
        DatanodeQueryCriteria criteria = new DatanodeQueryCriteria(deviceId, collection);
        criteria.setDeviceId(deviceId);
        ProcessValues processData = apiClient.readProcessData(criteria);
        assertNotNull(processData.getUri());
        assertTrue(processData.getDatanodeReads().size() > 0);
        assertTrue(((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDatanodeReadValues().size() > 0);
        assertEquals(DataType.BooleanType, ((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDataType());
        Boolean boolValue = (Boolean)((DatanodeReadValue)((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDatanodeReadValues().iterator().next()).getConvertedValue();
        assertEquals(testBooleanValue, boolValue.booleanValue());
        assertTrue(((DatanodeReadValue)((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDatanodeReadValues().iterator().next()).getTimestampMilliSecond() > 0L);

    }


    public void writeBinaryValue() throws ValidAPIParamException, JSONException {

        DatanodeWriteValue writeValue = new DatanodeWriteValue();
        writeValue.setName(byteDatanodeName);
        writeValue.setValue(testByteValue);


        WriteDataResponse writeResult = apiClient.writeData(deviceId, writeValue);
        assertNotNull(writeResult);
        assertTrue(writeResult.getTotalWritten() > 0);
        assertNotNull(writeResult.getWriteResults());
        assertTrue(((WriteResult)(writeResult.getWriteResults().iterator().next())).getWrittenCount() > 0);
        assertNotNull(((WriteResult)(writeResult.getWriteResults().iterator().next())).getHref());

        readBinaryValue();

    }


    private void readBinaryValue() throws JSONException {

        DatanodeQueryCriteria criteria = new DatanodeQueryCriteria(deviceId, byteDatanodeName);
        criteria.setDeviceId(deviceId);
        ProcessValues processData = apiClient.readProcessData(criteria);
        assertNotNull(processData.getUri());
        assertTrue(processData.getDatanodeReads().size() > 0);
        assertTrue(((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDatanodeReadValues().size() > 0);

        assertEquals(DataType.BinaryType, ((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDataType());
        boolean equals = false;
        byte[] byteValue = (byte[]) ((DatanodeReadValue)((DatanodeRead)processData.getDatanodeReads().iterator().next()).getDatanodeReadValues().iterator().next()).getConvertedValue();
        if (testByteValue.length == byteValue.length) {
        	equals = true;
	        for (int i=0; i < testByteValue.length; i++) {
	        	if (testByteValue[i] != byteValue[i]) {
	        		equals = false;
	        		break;
	        	}
	        }
        }
        assertTrue(equals);
    }


    public void writeNumericValue() throws IoTServerCommunicationException, ValidAPIParamException, JSONException {

        DeviceDetails device = apiClient.getDevice(deviceId);


        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date(cal.getTime().getTime() - 1 * 1000));
        Random r = new Random();

        Collection valuesToWrite = new ArrayList();
        valuesToWrite.size();
        for (int i = 0; i < 30; i++) {
            cal.setTime(new Date(cal.getTime().getTime() + 1));

            DatanodeWriteValue dnwrite = new DatanodeWriteValue();
            dnwrite.setName(numericDatanodeName);
            dnwrite.setPath(firstPath);
            dnwrite.setUnit("l/s");
            dnwrite.setValue(new Integer(r.nextInt(10)));
            dnwrite.setTimestampMiliseconds(new Long(cal.getTime().getTime()));

            valuesToWrite.add(dnwrite);


            dnwrite = new DatanodeWriteValue();
            dnwrite.setName(numericDatanodeName);
            dnwrite.setPath(secondPath);
            dnwrite.setUnit("l/s");
            dnwrite.setDoubleValue(10 * r.nextDouble());
            dnwrite.setTimestampMiliseconds(new Long(cal.getTime().getTime()));

            valuesToWrite.add(dnwrite);

        }

        WriteDataResponse writeResult = apiClient.writeData(device.getDeviceId(), valuesToWrite);
        assertNotNull(writeResult);
        assertEquals(60, writeResult.getTotalWritten());
        assertNotNull(writeResult.getWriteResults());
        assertTrue(((WriteResult)writeResult.getWriteResults().iterator().next()).getWrittenCount() > 0);
        assertNotNull(((WriteResult)writeResult.getWriteResults().iterator().next()).getHref());


        readNumericValue();

    }

    private void readNumericValue() throws JSONException {
        readFirstNumericValue();
        readSecondNumericValues();

        /**Two datanodes are expected, since there are two datanodes with the name <numericDatanodeName>
         * but with different paths
         */
        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, numericDatanodeName);
        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection datanodeReads = processValues.getDatanodeReads();
        assertTrue(datanodeReads.size() == 2);


    }

    private void readSecondNumericValues() throws JSONException {


        String fullPath = DataPathUtil.getFullPath(numericDatanodeName, secondPath);

        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, fullPath);
        crit.setSortOrder(DatanodeQueryCriteria.Descending);


        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection datanodeReads = processValues.getDatanodeReads();
        Iterator it = datanodeReads.iterator();
        while (it.hasNext()) {
        	DatanodeRead datanodeRead = (DatanodeRead)it.next();

            Collection values = datanodeRead.getDatanodeReadValues();
            assertEquals(DataType.DoubleType, datanodeRead.getDataType());
            assertTrue(values.size() == 1);

            Iterator v_it = values.iterator();
            while (v_it.hasNext()) {
            	DatanodeReadValue value = (DatanodeReadValue)v_it.next();
                long ts = value.getTimestampMilliSecond();
                double val = ((Double)value.getConvertedValue()).doubleValue();

                assertTrue(ts > 0);
                assertTrue(val < 10);

            }
        }
    }

    private void readFirstNumericValue() throws JSONException {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.setTime(new Date(cal.getTime().getTime() - 5 * 1000));

        String fullPath = DataPathUtil.getFullPath(numericDatanodeName, firstPath);
        DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, fullPath);
        crit.setFromDate(cal.getTime());
        crit.setLimit(new Integer(100));
        crit.setSortOrder(DatanodeQueryCriteria.Ascending);

        ProcessValues processValues = apiClient.readProcessData(crit);
        Collection datanodeReads = processValues.getDatanodeReads();
        Iterator it = datanodeReads.iterator();
        while (it.hasNext()) {
        	DatanodeRead datanodeRead = (DatanodeRead)it.next();
            Collection values = datanodeRead.getDatanodeReadValues();
            assertEquals(DataType.LongType, datanodeRead.getDataType());
            assertTrue(values.size() >= 30);

            Iterator v_it = values.iterator();
            while (v_it.hasNext()) {
            	DatanodeReadValue value = (DatanodeReadValue)v_it.next();
                long ts = value.getTimestampMilliSecond();
                long val = ((Long)value.getConvertedValue()).longValue();

                assertTrue(val < 10);
                assertTrue(ts > 0);

            }
        }
    }


}