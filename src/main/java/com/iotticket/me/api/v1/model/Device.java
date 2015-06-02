package com.iotticket.me.api.v1.model;

import com.iotticket.me.api.v1.validation.Validatable;
import com.iotticket.me.utils.Jserializable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Device implements Validatable, Jserializable {

	FieldCollection fieldCollection = new FieldCollection();

    Field name = new Field("name", Field.String, fieldCollection)
    .setMaxLength(100)
    .setNullable(false);

    Field manufacturer = new Field("manufacturer", Field.String, fieldCollection)
    .setMaxLength(100)
    .setNullable(false);

    Field type = new Field("type", Field.String, fieldCollection)
    .setMaxLength(100);

    Field description = new Field("description", Field.String, fieldCollection)
    .setMaxLength(255);

    Field attributes = new Field("attributes", Field.Collection, fieldCollection)
    .setValue(new ArrayList())
    .setMaxLength(50)
    .setTypeHint(DeviceAttribute.class);
    
    
    public String getName() {
        return (String)name.getValue();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getManufacturer() {
        return (String)manufacturer.getValue();
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.setValue(manufacturer);
    }

    public String getType() {
    	return (String)type.getValue();
    }

    public void setType(String type) {
        this.type.setValue(type);
    }

    public String getDescription() {
        return (String)description.getValue();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public Collection getAttributes() {
        return (Collection)attributes.getValue();
    }

    public void setAttributes(Collection attributes) {
        this.attributes.setValue(attributes);
    }

    public String toString() {
        StringBuffer builder = new StringBuffer();
        builder.append("Device [");
        if (name != null) {
            builder.append("name=");
            builder.append(name);
            builder.append(", ");
        }
        if (manufacturer != null) {
            builder.append("manufacturer=");
            builder.append(manufacturer);
            builder.append(", ");
        }
        if (type != null) {
            builder.append("type=");
            builder.append(type);
            builder.append(", ");
        }
        if (description != null) {
            builder.append("description=");
            builder.append(description);
            builder.append(", ");
        }
        if (attributes != null) {
            builder.append("attributes=");
            builder.append(attributes);
        }
        builder.append("]");
        return builder.toString();
    }


    /**
     * Represents a registered device.
     */
    public static class DeviceDetails extends Device {

    	Field uri = new Field("href", Field.String, fieldCollection);

        Field deviceId = new Field("deviceId", Field.String, fieldCollection);

        Field createdAt = new Field("createdAt", Field.Date, fieldCollection);

        public String getUri() {
            return uri.getValue() != null ? (String)uri.getValue() : null;
        }

        public void setUri(String uri) {
            this.uri.setValue(uri);
        }

        public String getDeviceId() {
        	return deviceId.getValue() != null ? (String)deviceId.getValue() : null;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId.setValue(deviceId);
        }

        public Date getCreatedAt() {
            return createdAt.getValue() != null ? (Date)(createdAt.getValue()) : null;
        }

        public void setCreatedAt(Date createdAt) {
            this.createdAt.setValue(createdAt);
        }

        public String toString() {
            return "DeviceDetails{" +
                    "uri='" + uri + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", createdAt='" + createdAt + '\'' + super.toString() +
                    '}';
        }
    }


    public static class DeviceList extends PagedResult {
    	public DeviceList() {
    		Field itemsField = (Field)DeviceList.this.getFieldCollection().getFields().get("items");
    		itemsField.setTypeHint(DeviceDetails.class);
    	}
    }
    
    protected JSONObject getJSON() throws JSONException {
    	return fieldCollection.getJSON();
    }
    
    public String toJSON() throws JSONException {
    	return getJSON().toString();
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }
}
