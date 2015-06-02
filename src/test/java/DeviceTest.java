import java.util.Collection;
import java.util.Iterator;

import com.iotticket.me.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.me.api.v1.exception.ValidAPIParamException;
import com.iotticket.me.api.v1.model.Device;
import com.iotticket.me.api.v1.model.DeviceAttribute;
import com.iotticket.me.api.v1.model.ErrorInfo;
import com.iotticket.me.api.v1.model.PagedResult;
import com.iotticket.me.api.v1.model.Device.DeviceDetails;

import org.json.me.JSONException;


public class DeviceTest extends TestBase {

	public static final String DEVICENAME = "DreamCar";

    public DeviceTest() {
		super(3, "DeviceTest");
	}

	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			newDeviceTest();
			break;
		case 1:
			testDeviceRegister();
			break;
		case 2:
			fetchDevice();
			break;
		}
	}
   
    public void newDeviceTest() {
        try {
            String deviceId = testDeviceRegister();
            fetchDevices(deviceId);
        } catch (IoTServerCommunicationException e) {
            ErrorInfo errorInfo = e.getErrorInfo();
            System.err.println(errorInfo);
            e.printStackTrace();
        } catch (ValidAPIParamException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			e.printStackTrace();
		}
    }


    private String testDeviceRegister() throws IoTServerCommunicationException, ValidAPIParamException, JSONException {
        final String type = "4WD";
        final String manufacturer = "NextGen Car Company";

        Device d = new Device();
        d.setName(DEVICENAME);
        d.setManufacturer(manufacturer);
        d.setType(type);
        d.getAttributes().add(new DeviceAttribute("EngineSize", "2.4"));
        d.getAttributes().add(new DeviceAttribute("FuelType", "Benzene"));


        DeviceDetails device = apiClient.registerDevice(d);

        assertNotNull(device);
        assertNotNull(device.getName());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getManufacturer());
        assertNotNull(device.getDeviceId());
        assertEquals(type, device.getType());
        assertEquals(DEVICENAME, device.getName());
        assertEquals(2, device.getAttributes().size());


        return device.getDeviceId();

    }


    private void fetchDevices(String newDeviceId) throws IoTServerCommunicationException, JSONException {


        PagedResult deviceList = apiClient.getDeviceList(0, 20);
        assertTrue(deviceList.getTotalCount() >= 1);
        assertEquals(0, deviceList.getSkip());
        assertEquals(20, deviceList.getRequestedCount());

        boolean found = false;
        Collection results = deviceList.getResults();
        Iterator it = results.iterator();
        while (it.hasNext()) {
        	DeviceDetails device = (DeviceDetails)it.next();
            if (device.getDeviceId().equals(newDeviceId)) {
                assertEquals(DEVICENAME, device.getName());
                found = true;
                break;
            }
        }
        assertTrue(found);

    }


    public void fetchDevice() throws IoTServerCommunicationException, JSONException {

        DeviceDetails device = apiClient.getDevice(deviceId);

        assertNotNull(device);
        assertNotNull(device.getName());
        assertNotNull(device.getCreatedAt());
        assertNotNull(device.getManufacturer());
        assertEquals(device.getDeviceId(), deviceId);
        String serverURL = SERVER_URL;
        if (!serverURL.substring(serverURL.length()-1, serverURL.length()).equals("/")) {
        	serverURL = serverURL + "/";
        }
        assertEquals(device.getUri().toString(), serverURL + "devices/" + deviceId);


    }


}
