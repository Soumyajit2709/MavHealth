package com.soumyajit.mavhealth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.soumyajit.mavhealth.fireStoreApi.DoctorHelper;
import com.soumyajit.mavhealth.fireStoreApi.PatientHelper;
import com.soumyajit.mavhealth.fireStoreApi.UserHelper;

public class FirstSigninActivity extends AppCompatActivity {
    private static final String TAG = "FirstSigninActivity";
    private EditText fullName;
    private EditText birthday;
    private EditText teL;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_signin);
        btn = (Button) findViewById(R.id.confirmeBtn);
        fullName = (EditText) findViewById(R.id.firstSignFullName);
        birthday = (EditText) findViewById(R.id.firstSignBirthDay);
        teL = (EditText) findViewById(R.id.firstSignTel);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Spinner specialityList = (Spinner) findViewById(R.id.speciality_spinner);
        ArrayAdapter<CharSequence> adapterSpecialityList = ArrayAdapter.createFromResource(this,
                R.array.speciality_spinner, android.R.layout.simple_spinner_item);
        adapterSpecialityList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialityList.setAdapter(adapterSpecialityList);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                Log.e(TAG, "onItemSelected:" + selected);
                if (selected.equals("Doctor")) {
                    specialityList.setVisibility(View.VISIBLE);
                } else {
                    specialityList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                specialityList.setVisibility(View.GONE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullname, birthDay, tel, type, speciality;
                fullname = fullName.getText().toString();
                birthDay = birthday.getText().toString();
                tel = teL.getText().toString();
                type = spinner.getSelectedItem().toString();
                speciality = specialityList.getSelectedItem().toString();
                UserHelper.addUser(fullname, birthDay, tel, type);
                if (tel.length() != 10) {
                    teL.setError("Telephone number must be 10 digits");
                    return;
                }
                if (type.equals("Patient")) {
                    PatientHelper.addPatient(fullname, "address", tel);
                    System.out.println("Add patient " + fullname + " to patient collection");

                } else {
                    DoctorHelper.addDoctor(fullname, "address", tel, speciality);
                }
                Intent k = new Intent(FirstSigninActivity.this, MainActivity.class);
                startActivity(k);
            }


        });
    }

}