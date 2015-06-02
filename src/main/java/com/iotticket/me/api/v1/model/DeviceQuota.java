package com.iotticket.me.api.v1.model;

import com.iotticket.me.utils.Jserializable;

public class DeviceQuota implements Jserializable {

	private FieldCollection fieldCollection = new FieldCollection();

    private Field totalRequestToday = new Field("totalRequestToday", Field.Long, fieldCollection);

    private Field maxReadRequestPerDay = new Field("maxReadRequestPerDay", Field.Number, fieldCollection);

    private Field deviceId = new Field("deviceId", Field.String, fieldCollection);

    private Field storageSize = new Field("storageSize", Field.Long, fieldCollection);

    private Field numberOfDataNodes = new Field("numberOfDataNodes", Field.Number, fieldCollection);

    /**
     * @return Total number of request made to the API to the device.
     * Any request url that includes the device Id is added to this count.
     */
    public Long getTotalRequestToday() {
        return totalRequestToday.getValue() != null ? (Long)totalRequestToday.getValue() : null;
    }

    public void setTotalRequestToday(Long totalRequestToday) {
        this.totalRequestToday.setValue(totalRequestToday);
    }


    /**
     * @return The max number of read request allowed to the client for this device.
     */
    public Integer getMaxReadRequestPerDay() {
        return maxReadRequestPerDay.getValue() != null ? (Integer)maxReadRequestPerDay.getValue() : null;
    }

    public void setMaxReadRequestPerDay(Integer maxReadRequestPerDay) {
        this.maxReadRequestPerDay.setValue(maxReadRequestPerDay);
    }

    /**
     * @return The device id for which this DeviceQuota belongs to.
     */
    public String getDeviceId() {
        return deviceId.getValue() != null ? (String)deviceId.getValue() : null;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId.setValue(deviceId);
    }


    /**
     * @return The total size in bytes that the client has written to the server for the specified device.
     */
    public Long getStorageSize() {
        return storageSize.getValue() != null ? (Long)storageSize.getValue() : null;
    }

    public void setStorageSize(Long storageSize) {
        this.storageSize.setValue(storageSize);
    }


    /**
     * @return The number of data node created for the specified device.
     */
    public Integer getNumberOfDataNodes() {
        return numberOfDataNodes.getValue() != null ? (Integer)numberOfDataNodes.getValue() : null;
    }

    public void setNumberOfDataNodes(Integer numberOfDataNodes) {
        this.numberOfDataNodes.setValue(numberOfDataNodes);
    }

    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }
    
    public String toString() {
        return "DeviceQuota{" +
                "totalRequestToday=" + totalRequestToday.getValue() +
                ", maxReadRequestPerDay=" + maxReadRequestPerDay.getValue() +
                ", deviceId='" + deviceId.getValue() + '\'' +
                ", storageSize=" + storageSize.getValue() +
                ", numberOfDataNodes=" + numberOfDataNodes.getValue() +
                '}';
    }


}