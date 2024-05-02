package com.soumyajit.mavhealth.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.soumyajit.mavhealth.R;
import com.soumyajit.mavhealth.model.Doctor;
import com.squareup.picasso.Picasso;

public class MyDoctorsAdapter extends FirestoreRecyclerAdapter<Doctor, MyDoctorsAdapter.MyDoctorAppointementHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyDoctorsAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointementHolder myDoctorsHolder, int position, @NonNull final Doctor doctor) {
        myDoctorsHolder.textViewTitle.setText(doctor.getName());
        myDoctorsHolder.textViewDescription.setText("Speciality : "+doctor.getSpeciality());
//
        String imageId = doctor.getEmail()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get()
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(myDoctorsHolder.imageViewDoctor);//Image location

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

    @NonNull
    @Override
    public MyDoctorAppointementHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_doctor_item, parent, false);
        return new MyDoctorAppointementHolder(v);
    }

    class MyDoctorAppointementHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewStatus;
        ImageView imageViewDoctor;
        Button sendMessageButton;
        Button callBtn;
        Button contactButton;
        public MyDoctorAppointementHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.doctor_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            imageViewDoctor = itemView.findViewById(R.id.doctor_item_image);
        }
    }
}