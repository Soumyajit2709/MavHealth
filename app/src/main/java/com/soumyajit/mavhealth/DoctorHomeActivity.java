package com.soumyajit.mavhealth;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.soumyajit.mavhealth.Common.Common;

import java.util.Calendar;

import butterknife.OnClick;

public class DoctorHomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    static String doc;
    Button SignOutBtn2;
    Button BtnRequst;
    Button listPatients;
    Button appointementBtn;
    Button profile;
    Button myCalendarBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);
        //unbinder = ButterKnife.bind(this,this);
        Common.CurreentDoctor = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        Common.CurrentUserType = "doctor";
        listPatients = findViewById(R.id.listPatients);
        BtnRequst=findViewById(R.id.btnRequst);
        SignOutBtn2=findViewById(R.id.signOutBtn);
        appointementBtn = findViewById(R.id.appointment);
        myCalendarBtn = findViewById(R.id.myCalendarBtn);
        profile = findViewById(R.id.profile);
        SignOutBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        BtnRequst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, ConfirmedAppointmensActivity.class);
                startActivity(k);
            }
        });
        listPatients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, MyPatientsActivity.class);
                startActivity(k);
            }
        });
        appointementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // doc = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
                //showDatePickerDialog(v.getContext());
                Intent k = new Intent(DoctorHomeActivity.this, DoctorAppointementActivity.class);
                startActivity(k);
            }
        });
        myCalendarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, MyCalendarDoctorActivity.class);
                startActivity(k);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(DoctorHomeActivity.this, ProfileDoctorActivity.class);
                startActivity(k);
            }
        });
    }

    public void showDatePickerDialog(Context wf){
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                wf,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month_day_year: " + month + "_" + dayOfMonth + "_" + year;
        openPage(view.getContext(),doc,date);
    }

    private void openPage(Context wf, String d,String day){
        Intent i = new Intent(wf, AppointementActivity.class);
        i.putExtra("key1",d+"");
        i.putExtra("key2",day);
        i.putExtra("key3","doctor");
        wf.startActivity(i);
    }
}