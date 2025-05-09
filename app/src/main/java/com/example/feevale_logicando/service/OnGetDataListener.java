package com.example.feevale_logicando.service;

import com.google.firebase.firestore.QuerySnapshot;

public interface OnGetDataListener {
    //this is for callbacks
    void onSuccess(QuerySnapshot dataSnapshot);
}
