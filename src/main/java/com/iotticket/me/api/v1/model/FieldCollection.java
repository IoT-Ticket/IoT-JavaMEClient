package com.iotticket.me.api.v1.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.iotticket.me.utils.DateParser;
import com.iotticket.me.utils.Jserializable;

public class FieldCollection {
	
	Hashtable fields = new Hashtable();

	public void addField(Field field) {
		fields.put(field.getName(), field);
	}
	
	public Hashtable getFields() {
		return fields;
	}
	
	public void setFromJSON(JSONObject json) throws JSONException {
		Enumeration keys = json.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			if (fields.containsKey(key)) {
				Field field = (Field)fields.get(key);
				Object value = getFieldValue(field, json, key);
				field.setValue(value);
			}
		}
	}
	
	private Object getFieldValue(Field field, JSONObject json, String key) throws JSONException {
		Object value = null;
		if (field.getType() == Field.Collection) {
			JSONArray array = json.getJSONArray(key);
			Collection collection = getArrayValues(array, field.getTypeHint());
			value = collection;
		} else if (field.getType() == Field.Table) {
			JSONObject obj = json.getJSONObject(key);
			Hashtable table = getObjectValues(obj);
			value = table;
		} else if (field.getType() == Field.Date) {
			String date = (String)json.getString(key);
			value = DateParser.getISO8601Date(date);
		} else if (field.getType() == Field.Long) {
			value = new Long(json.getLong(key));
		} else {
			value = json.get(key);
		}
		return value;
	}
	
	private Collection getArrayValues(JSONArray array, Class typehint) throws JSONException {
		Collection collection = new ArrayList();
		for (int i=0; i<array.length(); i++) {
			Object value = array.get(i);
			if (value instanceof JSONArray) {
				collection.add(getArrayValues((JSONArray)value, null));
			} else if (value instanceof JSONObject) {
				if (typehint != null && Jserializable.class.isAssignableFrom(typehint)) {
					JSONObject json = (JSONObject)value;
					try {
						Jserializable obj = (Jserializable)typehint.newInstance();
						obj.getFieldCollection().setFromJSON(json);
						collection.add(obj);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
						collection.add(getObjectValues((JSONObject)value));
					} catch (InstantiationException e) {
						e.printStackTrace();
						collection.add(getObjectValues((JSONObject)value));
					}
				} else {
					collection.add(getObjectValues((JSONObject)value));
				}
			} else {
				collection.add(array.get(i));
			}
		}
		return collection;
	}
	
	private Hashtable getObjectValues(JSONObject object) throws JSONException {
		Hashtable table = new Hashtable();
		Enumeration keys = object.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			Object value = object.get(key);
			if (value instanceof JSONArray) {
				table.put(key, getArrayValues((JSONArray)value, null));
			} else if (value instanceof JSONObject) {
				table.put(key, getObjectValues((JSONObject)value));
			} else {
				table.put(key, object.get(key));
			}
		}
		return table;
	}
	
	public JSONObject getJSON() throws JSONException {
		JSONObject json = new JSONObject();
		Enumeration keys = fields.keys();
		while (keys.hasMoreElements()) {
			String key = (String)keys.nextElement();
			Field field = (Field)fields.get(key);
			if (field.getType() == Field.Collection) {
				json.put(field.getName(), getJSONArray(field));
			} else if (field.getType() == Field.Date) {
				Date date = (Date)field.getValue();
				json.put(field.getName(), DateParser.toISO8601(date));
			} else {
				json.put(field.getName(), field.getValue());
			} 
		}
		return json;
	}
	
	public String toJSON() throws JSONException {
		JSONObject json = getJSON();
		return json.toString();
	}
	
	private JSONArray getJSONArray(Field field) throws JSONException {
		JSONArray json = new JSONArray();
		Collection collection = (Collection)field.getValue();
		Iterator it = collection.iterator();
		while (it.hasNext()) {
			Object obj = it.next();
			if (field.getTypeHint() != null && Jserializable.class.isAssignableFrom(field.getTypeHint())) {
				FieldCollection innerCollection = ((Jserializable)obj).getFieldCollection();
				json.put(innerCollection.getJSON());
			} else if (obj instanceof Field) {
				Field f = (Field)obj;
				if (f.getType() != Field.Collection) {
					json.put(f.getValue());
				} else {
					json.put(getJSONArray(f));
				}
			} else {
				json.put(getRawValue(field, obj));
			}
		}
		return json;
	}
	
	private Object getRawValue(Field field, Object obj) throws JSONException {
		if (obj instanceof Hashtable) {
			JSONObject objJSON = new JSONObject();
			Hashtable table = ((Hashtable)obj);
			Enumeration keys = table.keys();
			while (keys.hasMoreElements()) {
				String key = (String)keys.nextElement();
				objJSON.put(key, table.get(key));
			}
			return objJSON;
		} else if (obj instanceof Collection) {
			JSONArray objJSON = new JSONArray();
			Collection collection = ((Collection)obj);
			Iterator it = collection.iterator();
			while (it.hasNext()) {
				objJSON.put(it.next());
			}
			return objJSON;
		} else if (obj instanceof Integer) {
			if (field.getType() == Field.Long) {
				int value = ((Integer)field.getValue()).intValue();
				return new Long(value);
			}
			return field.getValue();
		} else {
			return field.getValue();
		}
	}
	
}