package com.example.feevale_logicando.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class FormService {
    CollectionReference formsCollection;

    public FormService() {

    }

    public FormService(FirebaseFirestore db) {
        this.formsCollection = db.collection("forms");
    }

    public void getAvailableForms(OnGetDataListener listener) {
        formsCollection
                .whereLessThan("availableFrom", new Date())
                .whereGreaterThan("availableUntil", new Date())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listener.onSuccess(task.getResult());
                        } else {
                            Log.d("TEST", "get failed with ", task.getException());
                        }
                    }
                });
    }
}
