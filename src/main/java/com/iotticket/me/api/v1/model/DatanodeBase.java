package com.iotticket.me.api.v1.model;

import com.iotticket.me.api.v1.validation.Validatable;
import com.iotticket.me.utils.Jserializable;

public abstract class DatanodeBase implements Validatable, Jserializable {

	FieldCollection fieldCollection = new FieldCollection();

    private Field name = new Field("name", Field.String, fieldCollection)
    .setMaxLength(100)
    .setNullable(false);

    private Field unit = new Field("unit", Field.String, fieldCollection)
    .setMaxLength(10);

    private Field dataType = new Field("dataType", Field.String, fieldCollection);

    private Field path = new Field("path", Field.String, fieldCollection)
    .setMaxLength(1000)
    .setRegexPattern("(\\/[a-zA-Z0-9]+){1,10}");

    public String getDataType() {
    	return dataType.getValue() != null ? (String)dataType.getValue() : null;
    }

    public void setDataType(String dt) {
    	dataType.setValue(dt);
    }

    public String getUnit() {
        return unit.getValue() != null ? (String)unit.getValue() : null;
    }

    public void setUnit(String unit) {
        this.unit.setValue(unit);
    }

    public String getName() {
    	return name.getValue() != null ? (String)name.getValue() : null;
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public String getPath() {
        return path.getValue() != null ? (String)path.getValue() : null;
    }

    public void setPath(String path) {
        if (path != null && path.length() != 0 && !path.startsWith("/")) {
            this.path.setValue("/" + path);
        } else {
        	this.path.setValue(path);
        }
    }

    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }
    
}