package com.soumyajit.mavhealth;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.soumyajit.mavhealth.model.UploadImage;
import com.squareup.picasso.Picasso;

public class EditProfilePatientActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final String TAG = "EditProfileDoctorActivity";
    private ImageView profileImage;
    private ImageButton selectImage;
    private Button updateProfile;
    private TextInputEditText doctorName;
    private TextInputEditText doctorPhone;
    private TextInputEditText doctorAddress;
    private String currentDoctorUID;
    private Uri uriImage;

    private StorageReference pStorageRef;
    private FirebaseFirestore doctorRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_patient);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        currentDoctorUID = mAuth.getCurrentUser().getEmail();

        doctorRef = FirebaseFirestore.getInstance();
        pStorageRef = FirebaseStorage.getInstance().getReference("DoctorProfile");

        profileImage = findViewById(R.id.image_profile);
        selectImage = findViewById(R.id.select_image);
        updateProfile = findViewById(R.id.update);
        doctorName = findViewById(R.id.nameText);
        doctorPhone = findViewById(R.id.phoneText);
        doctorAddress = findViewById(R.id.addressText);

        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFileChooser();
            }
        });

        updateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String updateAddress = doctorAddress.getText().toString();
                String updateName = doctorName.getText().toString();
                String updatePhone = doctorPhone.getText().toString();
                updateDoctorInfos(updateName, updateAddress, updatePhone);
                if (uriImage != null) {
                    uploadProfileImage();
                }
            }
        });

        // Load doctor's profile image if available
        loadDoctorProfileImage();
    }

    private void loadDoctorProfileImage() {
        // Retrieve doctor's profile image from Firebase Storage
        StorageReference pathReference = pStorageRef.child(currentDoctorUID + ".jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).placeholder(R.drawable.doctor).into(profileImage);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle any errors
                Log.e(TAG, "Failed to load profile image: " + e.getMessage());
            }
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            Picasso.get().load(uriImage).into(profileImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadProfileImage() {
        if (uriImage != null) {
            StorageReference fileReference = pStorageRef.child(currentDoctorUID + "." + getFileExtension(uriImage));
            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // Upload successful
                    Toast.makeText(EditProfilePatientActivity.this, "Profile image uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Handle any errors
                    Toast.makeText(EditProfilePatientActivity.this, "Upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void updateDoctorInfos(String name, String address, String phone) {
        DocumentReference documentReference = doctorRef.collection("Patient").document(currentDoctorUID);
        documentReference.update("address", address);
        documentReference.update("name", name);
        documentReference.update("tel", phone)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditProfilePatientActivity.this, "Information updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                        Log.e(TAG, "Error updating information: " + e.getMessage());
                        Toast.makeText(EditProfilePatientActivity.this, "Error updating information", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}