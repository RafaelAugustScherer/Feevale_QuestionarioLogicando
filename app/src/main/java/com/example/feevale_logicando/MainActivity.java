package com.example.feevale_logicando;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.feevale_logicando.domain.Form;
import com.example.feevale_logicando.service.FormService;
import com.example.feevale_logicando.service.OnGetDataListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        FormService formService = new FormService(db);

        ArrayList<Form> formList = new ArrayList<>();
        formService.getAvailableForms(new OnGetDataListener() {
            @Override
            public void onSuccess(QuerySnapshot dataSnapshot) {
                Log.d("TEST", dataSnapshot.toString());
                for (QueryDocumentSnapshot document : dataSnapshot) {
                    Map<String, Object> data = document.getData();
                    formList.add(Form.fromData(data));
                }
                Log.d("TEST", String.format("List size: %d", formList.size()));
            }
        });
    }
}