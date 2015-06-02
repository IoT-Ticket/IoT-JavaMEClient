# IOT-JavaMeClient

## Getting started

Create your own account at https://my.iot-ticket.com/Dashboard/.

### Register device

A device Id is automatically assigned to a newly registered device; the API Client should store this
device Id as it uniquely identifies the device and will be used in subsequent calls. Client should
avoid multiple registration call as this might result to duplicate devices being created. When
in doubt, a good flow will be to get the list of already created devices and validate the device’s
existence on the server through its name and attributes. Once the device is registered and the
device id is obtained, clients can immediately start sending measurement values to the server.

<pre><code>

    final String userName = "username";
    final String pwd = "password";
    final IOTAPIClient client = new IOTAPIClient("https://my.iot-ticket.com/api/v1", userName, pwd);

    Device d = new Device();
    d.setName("DeviceName");
    d.setManufacturer("Devicemanufacturer");
    d.setType("DeviceType");
    d.getAttributes().add(new DeviceAttribute("EngineSize", "2.4"));
    d.getAttributes().add(new DeviceAttribute("FuelType", "Benzene"));

    DeviceDetails deviceDetails = client.registerDevice(d);
    System.out.println("This is the device id that should be used in subsequent calls when sending measurement data: " + deviceDetails.getDeviceId());
</code></pre>

### Send measurement data

<pre><code>

    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

    DatanodeWriteValue dnwrite = new DatanodeWriteValue();
    dnwrite.setName("Airflow");
    dnwrite.setPath("Engine/Primary");
    dnwrite.setUnit("l/s");
    dnwrite.setValue(new Float(85.2f));
    dnwrite.setTimestampMiliseconds(cal.getTime().getTime());

    WriteDataResponse writeResult = client.writeData(deviceId, dnwrite);
     
</code></pre>

### Read measurement data
<pre><code>

    Collection collection = new ArrayList();
    collection.add("Engine/Primary/Airflow");
    collection.add("datapoint2");
    collection.add("datapoint3");
    DatanodeQueryCriteria crit = new DatanodeQueryCriteria(deviceId, collection);
    crit.setLimit(50);
    crit.setSortOrder(DatanodeQueryCriteria.Descending);


    ProcessValues processValues = client.readProcessData(crit);
    Collection datanodeReads = processValues.getDatanodeReads();
    Iterator dataReadIt = datanodeReads.iterator();
    while (dataReadIt.hasNext()) {
        DatanodeRead datanodeRead = (DatanodeRead)dataReadIt.next();
        Iterator dataReadValueIt = datanodeRead.getDatanodeReadValues().iterator();
        while (dataReadValueIt.hasNext()) {
            DatanodeReadValue value = (DatanodeReadValue)dataReadValueIt.next();
            System.out.println("Value=" + value.getValue() + " @" +value.getTimestampMilliSecond());

        }
    }
</code></pre>

## Java ME specific details

This version of the library is designed for Java ME 3.x platform. Due to the old age of the platform, 
issues with outdated encryption libraries of the platform can arise. This can be solved by relaying http
traffic through https tunnel.

## API documentation

https://www.iot-ticket.com/images/Files/IoT-Ticket.com_IoT_API.pdf
