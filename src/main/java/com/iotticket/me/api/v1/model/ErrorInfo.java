package com.iotticket.me.api.v1.model;

import com.iotticket.me.utils.HttpResponse;

public class ErrorInfo {

    /**
     * This field provides a general description of the error.
     */
    public String description;

    /**
     * The IOT-API server specific code for the error.
     */
    public int code;

    /**
     * This field points to the documentation url where more description about the error code can be found.
     */
    public String moreInfo;

    /**
     * The api version number.
     */
    public int apiver;

    /**
     * Contains inner exception, if occurred
     */
    public Exception exception;
    
    private HttpResponse httpStatus;

    /**
     * @return Actual HttpStatus returned from server
     */
    public HttpResponse getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpResponse httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String toString() {
    	StringBuffer buffer = new StringBuffer();
    	buffer.append(" {\"");
        if (description != null) {
        	buffer.append("description\":\"");
        	buffer.append(description);
        	buffer.append("\",\"");
        }
        buffer.append("code\":");
        buffer.append(code);
        buffer.append(",\"");
        if (moreInfo != null) {
        	buffer.append("moreInfo\":\"");
        	buffer.append(moreInfo);
        	buffer.append("\",\"");
        }
        buffer.append("apiver\":");
        buffer.append(apiver);
        buffer.append("}");
        return buffer.toString();
    }


}