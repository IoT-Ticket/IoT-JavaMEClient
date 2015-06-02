import java.util.ArrayList;
import java.util.Collection;

import com.iotticket.me.api.v1.model.Device;
import com.iotticket.me.api.v1.model.DeviceAttribute;
import com.iotticket.me.api.v1.model.Datanode.DatanodeWriteValue;
import com.iotticket.me.api.v1.validation.Validatable;
import com.iotticket.me.api.v1.validation.ValidationRunner;

public class ValidationTest extends TestBase {

	private static ValidationRunner validator = new ValidationRunner();

    public ValidationTest() {
		super(2, "ValidationTest");
	}
    
	public void test(int testNumber) throws Throwable {
		switch (testNumber) {
		case 0:
			testDataWriteValidation();
			break;
		case 1:
			testDeviceValidation();
			break;
		}
	}
	
    public void testDataWriteValidation() {


        DatanodeWriteValue writeValue = new DatanodeWriteValue();
        failureExpected(writeValue, "No Name set");

        writeValue.setName(repeatNTimes("a", 101));
        writeValue.setValue(new Boolean(true));
        failureExpected(writeValue, "Datanode name should not exceed 100 char");


        writeValue.setValue(null);
        writeValue.setName("Pressure");
        failureExpected(writeValue, "Value not set yet");


        writeValue.setValue(new Double(64.3f));
        failureNotExpected(writeValue);
        assertEquals(writeValue.getValue().substring(0, 4), "64.3");


        writeValue.setValue(new Boolean(true));
        assertEquals(writeValue.getValue(), "true");


        writeValue.setValue(new Integer(Integer.MIN_VALUE));
        assertEquals(writeValue.getValue(), Integer.toString(Integer.MIN_VALUE));


        writeValue.setPath("1/2/3/4/5/6/7/8/9/10/11");
        failureExpected(writeValue, "Number of path exceeded ten");

        writeValue.setPath("/1/2/3/4/5/6/7/8/9/10");
        failureNotExpected(writeValue);

        writeValue.setPath("1/2/3/4/5/6/7/8/9/10");
        failureNotExpected(writeValue);

        writeValue.setUnit("WilliamThomsonK");
        failureExpected(writeValue, "Unit should not have more than 10 char");


        writeValue.setUnit("C");
        failureNotExpected(writeValue);


    }

    private void failureNotExpected(Validatable validatable) {
        try {
            validator.runValidation(validatable);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private void failureExpected(Validatable validatable, String failureReason) {
        try {
            validator.runValidation(validatable);
            fail("It should fail because :" + failureReason);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private static String repeatNTimes(String str, int numberOfTimes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < numberOfTimes; i++) {
            sb.append(str);
        }
        return sb.toString();
    }

    public void testDeviceValidation() {


        Device device = new Device();
        failureExpected(device, "Device has no name set");

        device.setName("device1");
        failureExpected(device, "Device has no manufacturer set");

        device.setManufacturer("ThisDeviceManufacturerHasChosenARatherLongNameThereforeTheAPIDemandsThatItsShortensItNameTo:IOTTICKET");
        failureExpected(device, "manufacturer attribute exceeds 100 character");

        device.setManufacturer("IOTTICKET");
        failureNotExpected(device);

        for (int i = 0; i < 51; i++) {
            device.getAttributes().add(new DeviceAttribute("attribute" + i, "attributevlue" + i));
        }

        failureExpected(device, "Number of Attributes exceeded. Should not be more than 50");


        DeviceAttribute attr = new DeviceAttribute(repeatNTimes("k", 256), "attrvalue");
        Collection testAttributes = new ArrayList();
        testAttributes.add(attr);
        device.setAttributes(testAttributes);
        failureExpected(device, "Attr Key Exceeds 255 char");

        attr = new DeviceAttribute("Attrkey", repeatNTimes("v", 256));
        testAttributes = new ArrayList();
        testAttributes.add(attr);
        device.setAttributes(testAttributes);
        failureExpected(device, "Attr Value Exceeds 255 char");


    }

}
