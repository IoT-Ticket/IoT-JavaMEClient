package com.iotticket.me.api.v1.model;

import java.util.Collection;

import com.iotticket.me.utils.Jserializable;

/**
 * This class returns  a Collection of results, along with the present offset, the fullSize and the specified limit.
 * Useful for paging.
 */
public class PagedResult implements Jserializable {

	private FieldCollection fieldCollection = new FieldCollection();

    private Field results = new Field("items", Field.Collection, fieldCollection);

    private Field skip = new Field("offset", Field.Number, fieldCollection);

    private Field requestedCount = new Field("limit", Field.Number, fieldCollection);

    private Field totalCount = new Field("fullsize", Field.Number, fieldCollection);

    public Collection getResults() {
        return results != null ? (Collection)results.getValue() : null;
    }

    public void setResults(Collection results) {
        this.results.setValue(results);
    }

    public int getSkip() {
        return skip.getValue() != null ? ((Integer)skip.getValue()).intValue() : 0;
    }

    public void setSkip(int skip) {
        this.skip.setValue(new Integer(skip));
    }

    public int getRequestedCount() {
        return requestedCount.getValue() != null ? ((Integer)requestedCount.getValue()).intValue() : 0;
    }

    public void setRequestedCount(int requestedCount) {
        this.requestedCount.setValue(new Integer(requestedCount));
    }

    public int getTotalCount() {
        return ((Integer)totalCount.getValue()).intValue();
    }

    public void setTotalCount(int totalCount) {
        this.totalCount.setValue(new Integer(totalCount));
    }
    
    public FieldCollection getFieldCollection() {
    	return fieldCollection;
    }
}