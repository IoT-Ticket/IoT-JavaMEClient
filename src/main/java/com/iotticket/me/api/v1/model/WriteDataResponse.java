package com.iotticket.me.api.v1.model;

import java.util.Collection;

import com.iotticket.me.utils.Jserializable;

/**
 * Response from server after a write.
 *
 */
public class WriteDataResponse implements Jserializable {
	
	private FieldCollection fieldCollection = new FieldCollection();

    private Field writeResults = new Field("writeResults", Field.Collection, fieldCollection)
    .setTypeHint(WriteResult.class);

    private Field totalWritten = new Field("totalWritten", Field.Number, fieldCollection);

    public Collection getWriteResults() {
        return writeResults.getValue() != null ? (Collection)writeResults.getValue() : null;
    }

    public void setWriteResults(Collection writeResults) {
        this.writeResults.setValue(writeResults);
    }

    /**
     *
     * @return Total number of data points written
     */
    public int getTotalWritten() {
        return ((Integer)totalWritten.getValue()).intValue();
    }

    public void setTotalWritten(int totalWritten) {
        this.totalWritten.setValue(new Integer(totalWritten));
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }

}
