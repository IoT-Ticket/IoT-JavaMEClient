package com.iotticket.me.api.v1.model;

import java.util.ArrayList;
import java.util.Collection;

import com.iotticket.me.api.v1.model.Datanode.DatanodeRead;
import com.iotticket.me.utils.Jserializable;


public class ProcessValues implements Jserializable {

	private FieldCollection fieldCollection = new FieldCollection();

    private Field Uri = new Field("href", Field.String, fieldCollection);

    private Field datanodeReads = new Field("datanodeReads", Field.Collection, fieldCollection)
    .setValue(new ArrayList())
    .setTypeHint(DatanodeRead.class);
    
    public String getUri() {
        return Uri.getValue() != null ? (String)Uri.getValue() : null;
    }

    public void setUri(String uri) {
        Uri.setValue(uri);
    }

    public Collection getDatanodeReads() {
        return datanodeReads.getValue() != null ? (Collection)datanodeReads.getValue() : null;
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }

}
