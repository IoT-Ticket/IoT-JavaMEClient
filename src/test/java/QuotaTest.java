import com.iotticket.me.api.v1.exception.IoTServerCommunicationException;
import com.iotticket.me.api.v1.model.DeviceQuota;
import com.iotticket.me.api.v1.model.Quota;

import org.json.me.JSONException;

public class QuotaTest extends TestBase {

    public QuotaTest() {
		super(2, "QuotaTest");
	}
    
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			getClientQuota();
			break;
		case 1:
			getDeviceQuota();
			break;
		}
	}

	public void getClientQuota() throws IoTServerCommunicationException, JSONException {

        Quota quota = apiClient.getQuota();
        assertNotNull(quota);
        assertNotNull(quota.getMaxDataNodePerDevice());
        assertNotNull(quota.getMaxNumberOfDevices());
        assertNotNull(quota.getMaxStorageSize());
        assertNotNull(quota.getTotalDevices());
        assertNotNull(quota.getUsedStorageSize());

    }

    public void getDeviceQuota() throws IoTServerCommunicationException, JSONException {

        DeviceQuota deviceQuota = apiClient.getDeviceQuota(deviceId);
        assertNotNull(deviceQuota);
        assertNotNull(deviceQuota.getDeviceId());
        assertEquals(deviceQuota.getDeviceId(), deviceId);
        assertNotNull(deviceQuota.getMaxReadRequestPerDay());
        assertNotNull(deviceQuota.getNumberOfDataNodes());
        assertNotNull(deviceQuota.getStorageSize());
        assertNotNull(deviceQuota.getTotalRequestToday());


    }

}
