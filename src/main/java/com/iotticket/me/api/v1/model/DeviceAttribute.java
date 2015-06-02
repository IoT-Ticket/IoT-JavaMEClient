package com.iotticket.me.api.v1.model;

import com.iotticket.me.api.v1.validation.Validatable;
import com.iotticket.me.utils.Jserializable;

public class DeviceAttribute implements Jserializable, Validatable {

	FieldCollection fieldCollection = new FieldCollection();

    Field key = new Field("key", Field.String, fieldCollection)
    .setMaxLength(255)
    .setNullable(false);

    Field value = new Field("value", Field.String, fieldCollection)
    .setMaxLength(255)
    .setNullable(false);
    
    public DeviceAttribute(String key, String value) {
        setKey(key);
        setValue(value);
    }

    public DeviceAttribute() {

    }

    public String toString() {
        return "Attribute{" + "key='" + key + '\'' + ", value='" + value + '\'' + '}';
    }

    public String getKey() {
        return key.getValue() != null ? (String)key.getValue() : null;
    }

    public void setKey(String key) {
        this.key.setValue(key);
    }

    public String getValue() {
        return value.getValue() != null ? (String)value.getValue() : null;
    }

    public void setValue(String value) {
        this.value.setValue(value);
    }

	public FieldCollection getFieldCollection() {
		return fieldCollection;
	}

}
