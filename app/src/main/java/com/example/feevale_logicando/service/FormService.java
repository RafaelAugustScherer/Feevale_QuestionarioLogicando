package com.example.feevale_logicando.service;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.domain.FormAnswer;
import com.example.feevale_logicando.domain.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FormService {
    CollectionReference formsCollection;

    public FormService() {

    }

    public FormService(FirebaseFirestore db) {
        this.formsCollection = db.collection("forms");
    }

    public void getAvailableForms(OnGetDataListener listener) {
        Timestamp currentTimestamp = new Timestamp(new Date());

        formsCollection
                .whereLessThan("dateAvailableFrom", currentTimestamp)
                .whereGreaterThan("dateAvailableUntil", currentTimestamp)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> formsData = new ArrayList<>();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Map<String, Object> data = document.getData();
                            data.put("id", document.getId());

                            formsData.add(data);

                            Log.d("DOCUMENT_ID", document.getId());
                        }

                        listener.onSuccess(formsData);
                    } else {
                        Log.d("TEST", "get failed with ", task.getException());
                    }
                });
    }

    public void saveFormAnswer(FormAnswer formAnswer, OnPostDataListener listener) {
        formsCollection
                .document(formAnswer.getFormWithAnswers().getId())
                .collection("answers")
                .add(formAnswer.toData())
                .addOnSuccessListener(documentReference -> listener.onSuccess());
    }
}
