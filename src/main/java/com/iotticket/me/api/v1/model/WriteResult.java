package com.iotticket.me.api.v1.model;

import com.iotticket.me.utils.Jserializable;

public class WriteResult implements Jserializable {
	
	private FieldCollection fieldCollection = new FieldCollection();

    private Field href = new Field("href", Field.String, fieldCollection);

    private Field writtenCount = new Field("writtenCount", Field.Number, fieldCollection);

    /**
     *
     * @return URI to read from the data node targeted in the write.
     */
    public String getHref() {
        return href.getValue() != null ? (String)href.getValue() : null;
    }

    public void setHref(String href) {
        this.href.setValue(href);
    }

    /**
     *
     * @return The number of values written to the particular data node
     */
    public int getWrittenCount() {
        return writtenCount.getValue() != null ? ((Integer)writtenCount.getValue()).intValue() : 0;
    }

    public void setWrittenCount(int writtenCount) {
        this.writtenCount.setValue(new Integer(writtenCount));
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }

}
