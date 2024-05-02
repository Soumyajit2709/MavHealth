package com.soumyajit.mavhealth;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import dmax.dialog.SpotsDialog;

public class ProfilePatientActivity extends AppCompatActivity {
    private MaterialTextView doctorName;
    private MaterialTextView doctorPhone;
    private MaterialTextView doctorEmail;
    private MaterialTextView doctorAddress;
    private ImageView doctorImage;
    StorageReference pathReference;
    final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("Patient").document(doctorID);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_patient);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        doctorName = findViewById(R.id.doctor_name);
        doctorPhone = findViewById(R.id.doctor_phone);
        doctorEmail = findViewById(R.id.doctor_email);
        doctorAddress = findViewById(R.id.doctor_address);
        doctorImage = findViewById(R.id.imageView3);
        Drawable defaultImage = getResources().getDrawable(R.drawable.user);

        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setCancelable(true).build();
        dialog.show();

        String imageId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId + ".jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(doctorImage);
                dialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                dialog.dismiss();
            }
        });

        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (documentSnapshot != null) {
                    doctorName.setText(documentSnapshot.getString("name"));
                    doctorPhone.setText(documentSnapshot.getString("tel"));
                    doctorEmail.setText(documentSnapshot.getString("email"));
                    doctorAddress.setText(documentSnapshot.getString("address"));
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.edit:
                startEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startEditActivity() {
        Intent intent = new Intent(this, EditProfilePatientActivity.class);
        intent.putExtra("CURRENT_NAME", doctorName.getText().toString());
        intent.putExtra("CURRENT_PHONE", doctorPhone.getText().toString());
        intent.putExtra("CURRENT_ADDRESS", doctorAddress.getText().toString());
        startActivity(intent);
    }
}