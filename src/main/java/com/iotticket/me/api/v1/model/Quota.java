package com.iotticket.me.api.v1.model;

import com.iotticket.me.utils.Jserializable;

public class Quota implements Jserializable {
	
	private FieldCollection fieldCollection = new FieldCollection();
	
    public static int UNLIMITED = -1;

    private Field totalDevices = new Field("totalDevices", Field.Number, fieldCollection);

    private Field maxNumberOfDevices = new Field("maxNumberOfDevices", Field.Number, fieldCollection);

    private Field maxDataNodePerDevice = new Field("maxDataNodePerDevice", Field.Number, fieldCollection);

    private Field usedStorageSize = new Field("usedStorageSize", Field.Long, fieldCollection);

    private Field maxStorageSize = new Field("maxStorageSize", Field.Long, fieldCollection);

    /**
     * @return Total number of devices the client owns.
     * NOTE: This is not the same as the total number of devices the client has access to.
     */
    public Integer getTotalDevices() {
        return totalDevices.getValue() != null ? (Integer)totalDevices.getValue() : null;
    }

    public void setTotalDevices(Integer totalDevices) {
        this.totalDevices.setValue(totalDevices);
    }


    /**
     *
     * @return The maximum number of devices the client can create.
     */
    public Integer getMaxNumberOfDevices() {
        return maxNumberOfDevices.getValue() != null ? (Integer)maxNumberOfDevices.getValue() : null;
    }

    public void setMaxNumberOfDevices(Integer maxNumberOfDevices) {
        this.maxNumberOfDevices.setValue(maxNumberOfDevices);
    }

    /**
     *
     * @return The maximum datanodes per device allowed for a client.
     */

    public Integer getMaxDataNodePerDevice() {
        return maxDataNodePerDevice.getValue() != null ? (Integer)maxDataNodePerDevice.getValue() : null;
    }

    public void setMaxDataNodePerDevice(Integer maxDataNodePerDevice) {
        this.maxDataNodePerDevice.setValue(maxDataNodePerDevice);
    }


    /**
     *
     * @return The total size in bytes that the client has written to the server.
     */
    public Long getUsedStorageSize() {
        return usedStorageSize.getValue() != null ? (Long)usedStorageSize.getValue() : null;
    }

    public void setUsedStorageSize(Long usedStorageSize) {
        this.usedStorageSize.setValue(usedStorageSize);
    }

    /**
     *
     * @return The maximum size in bytes that the client has a right to write to the server.
     */
    public Long getMaxStorageSize() {
        return maxStorageSize.getValue() != null ? (Long)maxStorageSize.getValue() : null;
    }

    public void setMaxStorageSize(Long maxStorageSize) {
        this.maxStorageSize.setValue(maxStorageSize);
    }

    public String toString() {
        return "Quota{" +
                "totalDevices=" + totalDevices.getValue() +
                ", maxNumberOfDevices=" + maxNumberOfDevices.getValue() +
                ", maxDataNodePerDevice=" + maxDataNodePerDevice.getValue() +
                ", usedStorageSize=" + usedStorageSize.getValue() +
                ", maxStorageSize=" + maxStorageSize.getValue() +
                '}';
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }

}
