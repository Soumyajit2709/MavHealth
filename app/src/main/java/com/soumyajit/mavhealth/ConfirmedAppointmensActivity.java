package com.soumyajit.mavhealth;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.soumyajit.mavhealth.adapter.ConfirmedAppointmentsAdapter;
import com.soumyajit.mavhealth.model.ApointementInformation;

public class ConfirmedAppointmensActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myDoctorsRef = db.collection("Doctor");
    private ConfirmedAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed_appointmens);

        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        //Get the doctors by patient id
        final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Query query = myDoctorsRef.document(""+doctorID+"")
                .collection("calendar").orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<ApointementInformation> options = new FirestoreRecyclerOptions.Builder<ApointementInformation>()
                .setQuery(query, ApointementInformation.class)
                .build();

        adapter = new ConfirmedAppointmentsAdapter(options);
        //List current appointments
        RecyclerView recyclerView = findViewById(R.id.confirmed_appointements_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}