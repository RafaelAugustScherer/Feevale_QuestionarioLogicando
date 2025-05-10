package com.example.feevale_logicando.adapter;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.Map;

public class FirebaseAdapter {
    public static int handleIntValue(Map<String, Object> data, String key) throws Exception {
        Long longvalue = (Long) data.get(key);

        if (longvalue == null) {
            throw new Exception(String.format("handleIntValue: %s value is null", key));
        }

        return longvalue.intValue();
    }

    public static Date handleDateValue(Map<String, Object> data, String key) throws Exception {
        Timestamp timestampValue = (Timestamp) data.get(key);

        if (timestampValue == null) {
            throw new Exception(String.format("handleDateValue: %s value is null", key));
        }

        return timestampValue.toDate();
    }
}
