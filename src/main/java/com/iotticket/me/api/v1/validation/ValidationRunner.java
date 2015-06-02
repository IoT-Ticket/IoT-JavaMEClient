package com.iotticket.me.api.v1.validation;

import com.iotticket.me.api.v1.exception.ValidAPIParamException;
import com.iotticket.me.api.v1.model.Field;
import com.iotticket.me.api.v1.model.FieldCollection;

import java.util.*;

import me.regexp.RE;

public class ValidationRunner {

    final Hashtable PATTERN_CACHE = new Hashtable();

    public void runValidation(Validatable validatable) throws ValidAPIParamException {
    	FieldCollection fieldCollection = validatable.getFieldCollection();
        Hashtable fields = fieldCollection.getFields();
        Enumeration keys = fields.keys();
    	while (keys.hasMoreElements()) {
    		String key = (String)keys.nextElement();
            Field f = (Field)fields.get(key);
            int maxlength = f.getMaxLength();
            String regexPattern = f.getRegexPattern();
            boolean isNullable = f.isNullable();
            int type = f.getType();
            if (type == Field.String) {
                String value = (String)f.getValue();
                checkStringField(value, maxlength, regexPattern, isNullable, f.getName());
            } else if (type == Field.Collection) {
                Collection collection = (Collection)f.getValue();
                checkCollection(collection, maxlength, isNullable, f.getName());
            }
        }
    }

    private void checkCollection(Collection collection, int maxlength, boolean isNullable, String fieldName) throws ValidAPIParamException {
        if (isNullable && collection == null) return;

        if (!isNullable) {

            if (collection == null || collection.isEmpty()) {
                String msg = fieldName + "is needed";
                throw new ValidAPIParamException(msg);
            }
        }

        if (collection.size() > maxlength) {
            String msg = fieldName + " size exceeds " + maxlength + " expected";
            throw new ValidAPIParamException(msg);
        }

        Iterator it = collection.iterator();
        while (it.hasNext()) {
            Object o = it.next();
        	if (o instanceof Validatable) {
                Validatable vo = (Validatable) o;
                runValidation(vo);
            }

        }

    }

    private void checkStringField(String paramValue, int maxChar, String regexPattern, boolean isNullable, String fieldName) throws ValidAPIParamException {

        if (isNullable && paramValue == null) return;
        if (!isNullable && (paramValue == null || paramValue.length() == 0)) {
            String msg = fieldName + " is needed";
            throw new ValidAPIParamException(msg);
        }

        if (maxChar != Field.UNRESTRICTED && paramValue.length() > maxChar) {
            String msg = fieldName + " attribute exceeds " + maxChar + " characters expected";
            throw new ValidAPIParamException(msg);
        }

        if (regexPattern != null && regexPattern.length() != 0) {
        	RE r = getPattern(regexPattern);
        	boolean match = r.match(paramValue);
            if (!match) {
                String msg = fieldName + " attribute value " + paramValue + " is unacceptable. Check documentation";
                throw new ValidAPIParamException(msg);
            }
        }
    }
    
    private RE getPattern(String regexPattern) {
        if (!PATTERN_CACHE.containsKey(regexPattern)) {
            RE r = new RE(regexPattern);
            PATTERN_CACHE.put(regexPattern, r);
        }
        return (RE)PATTERN_CACHE.get(regexPattern);
    }
}


