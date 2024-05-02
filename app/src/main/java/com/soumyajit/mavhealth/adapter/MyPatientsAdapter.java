package com.soumyajit.mavhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.soumyajit.mavhealth.MedicalFiles;
import com.soumyajit.mavhealth.R;
import com.soumyajit.mavhealth.model.Patient;
import com.squareup.picasso.Picasso;

public class MyPatientsAdapter extends FirestoreRecyclerAdapter<Patient, MyPatientsAdapter.MyPatientsHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyPatientsAdapter(@NonNull FirestoreRecyclerOptions<Patient> options) {
        super(options);
    }


    @Override
    protected void onBindViewHolder(@NonNull final MyPatientsHolder myPatientsHolder, int position, @NonNull final Patient patient) {
        myPatientsHolder.textViewTitle.setText(patient.getName());
        myPatientsHolder.textViewTelephone.setText("Tel : "+patient.getTel());

        myPatientsHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatientMedicalFolder(v.getContext(),patient);

            }
        });

        String imageId = patient.getEmail()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myPatientsHolder.imageViewPatient);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }
    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPatientMedicalFolder(Context medicalFolder, Patient patient){
        Intent intent = new Intent(medicalFolder, MedicalFiles.class);
        intent.putExtra("patient_name", patient.getName());
        intent.putExtra("patient_email",patient.getEmail());
        intent.putExtra("patient_phone", patient.getTel());
        medicalFolder.startActivity(intent);
    }
    @NonNull
    @Override
    public MyPatientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_patient_item, parent, false);
        return new MyPatientsHolder(v);
    }

    class MyPatientsHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        TextView textViewTelephone;
        ImageView imageViewPatient;
        Button contactButton;
        RelativeLayout parentLayout;
        public MyPatientsHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.patient_view_title);
            textViewTelephone = itemView.findViewById(R.id.text_view_telephone);
            imageViewPatient = itemView.findViewById(R.id.patient_item_image);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}