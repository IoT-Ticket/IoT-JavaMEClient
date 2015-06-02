package com.iotticket.me.api.v1.model;

import java.util.Collection;

import com.iotticket.me.utils.Jserializable;

import org.bouncycastle.util.encoders.Base64;

public class Datanode extends DatanodeBase {

    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }


    public static class DatanodeWriteValue extends DatanodeBase {

        Field value = new Field("v", Field.String, fieldCollection)
        .setNullable(false);

        Field timestampMiliseconds = new Field("ts", Field.Number, fieldCollection);

        public String getValue() {
            return (String)value.getValue();
        }

        public void setValue(Object value) {
            if (value == null) {
                this.value.setValue(null);
                return;
            }
            if (value instanceof Double) {
                setDoubleValue(((Double) value).doubleValue());

            } else if (value instanceof Integer) {
                setIntegerValue(((Integer)value).intValue());
            } else if (value instanceof Long) {
                setLongValue(((Long)value).longValue());

            } else if (value instanceof byte[]) {
                setByteValue((byte[]) value);
            } else if (value instanceof Boolean) {
                setBooleanValue(((Boolean) value).booleanValue());

            } else if (value instanceof Float) {
                setFloat((Float) value);
            } else setInternal(String.valueOf(value));

        }

        private void setFloat(Float f) {
            setDataType(DataType.DoubleType);
            setInternal(Float.toString(f.floatValue()));
        }

        public void setDoubleValue(double d) {
            setDataType(DataType.DoubleType);
            setInternal(Double.toString(d));
        }

        private void setInternal(String value) {
            this.value.setValue(value);
        }

        private void setIntegerValue(int i) {
            setDataType(DataType.LongType);
            setInternal(Integer.toString(i));
        }

        public void setLongValue(long l) {
            setDataType(DataType.LongType);
            setInternal(Long.toString(l));
        }

        public void setBooleanValue(boolean bool) {
            setDataType(DataType.BooleanType);
            setInternal(String.valueOf(bool));
        }

        public void setByteValue(byte[] bytes) {
            String encode = Base64.toBase64String(bytes);
            setDataType(DataType.BinaryType);
            setInternal(encode);
        }


        public Long getTimestampMiliseconds() {
            return (Long)timestampMiliseconds.getValue();
        }

        public void setTimestampMiliseconds(Long timestampMiliseconds) {
            this.timestampMiliseconds.setValue(timestampMiliseconds);
        }
        
        public void setTimestampMiliseconds(long timestampMiliseconds) {
            this.timestampMiliseconds.setValue(new Long(timestampMiliseconds));
        }
    }

    public static class DataNodeList extends PagedResult {
    	public DataNodeList() {
    		Field itemsField = (Field)getFieldCollection().getFields().get("items");
    		itemsField.setTypeHint(DataNodeList.class);
    	}
    }

    public static class DatanodeRead extends DatanodeBase {

    	private Field datanodeReadValues = new Field("values", Field.Collection, fieldCollection)
    	.setTypeHint(DatanodeReadValue.class);
    	
        public Collection getDatanodeReadValues() {
            return datanodeReadValues.getValue() != null ? (Collection)datanodeReadValues.getValue() : null;
        }
    }

    public static class DatanodeReadValue implements Jserializable {

    	private FieldCollection fieldCollection = new FieldCollection();
    	
        public static final long UNSET = Long.MIN_VALUE;

        private Field value = new Field("v", Field.Number, fieldCollection);

        private Field timestampMilliSecond = new Field("ts", Field.Number, fieldCollection)
        .setValue(new Long(UNSET));
        private transient String dataType;

        public String getValue() {
            return value.getValue() != null ? (String)value.getValue() : null;
        }

        public void setValue(String value) {
            this.value.setValue(value);
        }

        public long getTimestampMilliSecond() {
            return timestampMilliSecond.getValue() != null ? ((Long)timestampMilliSecond.getValue()).longValue() : 0;
        }

        public void setTimestampMilliSecond(long timestampMilliSecond) {
            this.timestampMilliSecond.setValue(new Long(timestampMilliSecond));
        }

        public Object getConvertedValue() {

            if (getValue() == null) return null;

            return DataType.getTypeValue(this.dataType, getValue());

        }

        public void setDataType(String dataType) {
            this.dataType = dataType;
        }
        
        public FieldCollection getFieldCollection() {
        	return fieldCollection;
        }
    }
}