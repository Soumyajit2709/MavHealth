package com.soumyajit.mavhealth;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.soumyajit.mavhealth.R;
import com.squareup.picasso.Picasso;

import dmax.dialog.SpotsDialog;

public class ProfileDoctorActivity extends AppCompatActivity {
    private MaterialTextView doctorName;
    private MaterialTextView doctorSpe;
    private MaterialTextView doctorPhone;
    private MaterialTextView doctorEmail;
    private MaterialTextView doctorAddress;
    private MaterialTextView doctorAbout;
    private ImageView doctorImage;
    private StorageReference pathReference;
    final String doctorID = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef = db.collection("Doctor").document("" + doctorID + "");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_doctor);

        doctorImage = findViewById(R.id.imageView3);
        doctorName = findViewById(R.id.doctor_name);
        doctorSpe = findViewById(R.id.doctor_specialite);
        doctorPhone = findViewById(R.id.doctor_phone);
        doctorEmail = findViewById(R.id.doctor_email);
        doctorAddress = findViewById(R.id.doctor_address);
        doctorAbout = findViewById(R.id.doctor_about);
        AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setCancelable(true).build();
        dialog.show();

        // Display profile image
        String imageId = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/" + imageId + ".jpg");
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
                // Handle any errors and dismiss the loading dialog
                dialog.dismiss();
            }
        });

        docRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error and dismiss the loading dialog
                    dialog.dismiss();
                    Toast.makeText(ProfileDoctorActivity.this, "Failed to fetch data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (documentSnapshot != null && documentSnapshot.exists()) {
                    // Update UI with doctor's data
                    doctorName.setText(documentSnapshot.getString("name"));
                    doctorSpe.setText(documentSnapshot.getString("specialite"));
                    doctorPhone.setText(documentSnapshot.getString("tel"));
                    doctorEmail.setText(documentSnapshot.getString("email"));
                    doctorAddress.setText(documentSnapshot.getString("adresse"));
                } else {
                    // Handle the case where the document doesn't exist
                    dialog.dismiss();
                    Toast.makeText(ProfileDoctorActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_app_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back:
                finish();
                startHomeActivity();
                return true;
            case R.id.edit:
                startEditActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, DoctorHomeActivity.class);
        startActivity(intent);
        finish();
    }

    private void startEditActivity() {
        Intent intent = new Intent(this, EditProfileDoctorActivity.class);
        intent.putExtra("CURRENT_NAME", doctorName.getText().toString());
        intent.putExtra("CURRENT_PHONE", doctorPhone.getText().toString());
        intent.putExtra("CURRENT_ADDRESS", doctorAddress.getText().toString());
        startActivity(intent);
        finish();
    }
}
