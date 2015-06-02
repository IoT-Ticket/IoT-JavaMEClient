package com.iotticket.me.api.v1.model;

import org.bouncycastle.util.encoders.Base64;

public class DataType {

    public static final String DoubleType = "double";
    public static final String LongType = "long";
    public static final String StringType = "string";
    public static final String BinaryType = "binary";
    public static final String BooleanType = "boolean";

    public static Object getTypeValue(String type, String value) throws NumberFormatException {
		if (type.equals(DoubleType)) {
			return new Double(Double.parseDouble(value));
		} else if (type.equals(LongType)) {
			return new Long(Long.parseLong(value));
		} else if (type.equals(StringType)) {
			return value;
		} else if (type.equals(BinaryType)) {
			return Base64.decode(value);
		} else if (type.equals(BooleanType)) {
			return new Boolean(value != null && value.toLowerCase() == "true");
		} else {
			throw new IllegalArgumentException("Unknown datatype " + type);
		}
    }

}