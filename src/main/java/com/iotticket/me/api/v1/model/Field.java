package com.iotticket.me.api.v1.model;

public class Field {
	public static final int UNRESTRICTED = -1;

	private Object obj = null;
	private String fieldName;
	private boolean nullable = true;
	private int maxLength = UNRESTRICTED;
	private String regexPattern = "";
	private int valueType = 0;
	private Class typeHint = null;

	public static final int Number = 1;
	public static final int Long = 2;
	public static final int String = 3;
	public static final int Date = 4;
	public static final int Collection = 5;
	public static final int Table = 6;
	
	public Field(String name, int type, FieldCollection collection) {
		fieldName = name;
		valueType = type;
		collection.addField(this);
	}
	
	public Field setValue(Object object) {
		obj = object;
		return this;
	}

	public Object getValue() {
		return obj;
	}
	
	public String getName() {
		return fieldName;
	}
	
	public Field setType(int type) {
		valueType = type;
		return this;
	}
	
	public int getType() {
		return valueType;
	}
	
	public Field setTypeHint(Class typehint) {
		typeHint = typehint;
		return this;
	}
	
	public Class getTypeHint() {
		return typeHint;
	}

	public boolean isNullable() {
		return nullable;
	}

	public Field setNullable(boolean isNullable) {
		nullable = isNullable;
		return this;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public Field setMaxLength(int length) {
		maxLength = length;
		return this;
	}

	public String getRegexPattern() {
		return regexPattern;
	}

	public Field setRegexPattern(String pattern) {
		regexPattern = pattern;
		return this;
	}
}