import jmunit.framework.cldc11.TestCase;

import com.iotticket.me.api.v1.IOTAPIClient;

public abstract class TestBase extends TestCase {

    public TestBase(int totalOfTests, String name) {
		super(totalOfTests, name);
	}
    
	public void test(int testNumber) throws Throwable {

	}

	final static String SERVER_URL = "https://my.iot-ticket.com/api/v1";
    final static String deviceId = "<device-id>";
    final static String userName = "<your-username>";
    final static String password = "<your-password>";
    final static IOTAPIClient apiClient = new IOTAPIClient(SERVER_URL, userName, password);

}
