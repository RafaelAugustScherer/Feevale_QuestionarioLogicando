package com.example.feevale_logicando.service;

import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;

public interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(List<Map<String, Object>> formsData);
}
