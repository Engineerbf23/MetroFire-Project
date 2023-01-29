package com.example.metrofire;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FireguardDashboard extends AppCompatActivity {
    private final FirebaseFirestore fireStore = FirebaseFirestore.getInstance();
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser user = auth.getCurrentUser();
    DocumentReference documentReference;

    private TextInputLayout fg_comment_layout;
    private TextInputEditText fg_editFullName, fg_editLicense;
    private String q1, q2, q3, q4, q5, q6, q7, q8, q9;
    private String date, full_name, license, time, site, userEmail, userPassword;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fireguard_dashboard);



        fg_editFullName = findViewById(R.id.fg_full_name_input);
        fg_editLicense = findViewById(R.id.fg_license_input);
        Spinner hours_spinner = findViewById(R.id.fg_spinner_hours);
        Spinner sites_spinner = findViewById(R.id.fg_spinner_sites);
        fg_comment_layout = findViewById(R.id.fg_comments_layout);

        // DATE HANDLING
        Calendar calendar = Calendar.getInstance();
        date = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar.getTime());
        TextInputEditText fg_editDate = findViewById(R.id.fg_date_input);
        fg_editDate.setText(date);

        ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.hours, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        hours_spinner.setAdapter(arrayAdapter);
        time = hours_spinner.getSelectedItem().toString();
        documentReference = fireStore.collection("Users").document(user.getUid());
        hours_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                time = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        documentReference.get().addOnSuccessListener(documentSnapshot -> {
            userEmail = Objects.requireNonNull(documentSnapshot.getString("Email"));
            userPassword = Objects.requireNonNull(documentSnapshot.getString("Password"));

            license = Objects.requireNonNull(documentSnapshot.getString("License"));
            full_name = Objects.requireNonNull(documentSnapshot.getString("FirstName")).concat(" "+ Objects.requireNonNull(documentSnapshot.getString("LastName")));
            fg_editFullName.setText(full_name);
            fg_editLicense.setText(license);
        });

        ArrayAdapter<CharSequence> sitesAdapter = ArrayAdapter.createFromResource(this, R.array.sites, android.R.layout.simple_spinner_item);
        sitesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sites_spinner.setAdapter(sitesAdapter);
        site = sites_spinner.getSelectedItem().toString();
        sites_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                site = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                sites_spinner.setSelection(0);
            }
        });

    }

    @SuppressLint("NonConstantResourceId")
    public void pass_btn(View view) {
        switch (view.getId()){
            case R.id.pass1:
                q1 = "Pass";
                break;
            case R.id.pass2:
                q2 = "Pass";
                break;
            case R.id.pass3:
                q3 = "Pass";
                break;
            case R.id.pass4:
                q4 = "Pass";
                break;
            case R.id.pass5:
                q5 = "Pass";
                break;
            case R.id.pass6:
                q6 = "Pass";
                break;
            case R.id.pass7:
                q7 = "Pass";
                break;
            case R.id.pass8:
                q8 = "Pass";
                break;
            case R.id.pass9:
                q9 = "Pass";
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void fail_btn(View view) {
        switch (view.getId()){
            case R.id.fail1:
                q1 = "Fail";
                break;
            case R.id.fail2:
                q2 = "Fail";
                break;
            case R.id.fail3:
                q3 = "Fail";
                break;
            case R.id.fail4:
                q4 = "Fail";
                break;
            case R.id.fail5:
                q5 = "Fail";
                break;
            case R.id.fail6:
                q6 = "Fail";
                break;
            case R.id.fail7:
                q7 = "Fail";
                break;
            case R.id.fail8:
                q8 = "Fail";
                break;
            case R.id.fail9:
                q9 = "Fail";
                break;
        }
    }

    @SuppressLint("NonConstantResourceId")
    public void na_btn(View view) {
        switch (view.getId()){
            case R.id.na1:
                q1 = "N/A";
                break;
            case R.id.na2:
                q2 = "N/A";
                break;
            case R.id.na3:
                q3 = "N/A";
                break;
            case R.id.na4:
                q4 = "N/A";
                break;
            case R.id.na5:
                q5 = "N/A";
                break;
            case R.id.na6:
                q6 = "N/A";
                break;
            case R.id.na7:
                q7 = "N/A";
                break;
            case R.id.na8:
                q8 = "N/A";
                break;
            case R.id.na9:
                q9 = "N/A";
                break;
        }
    }

    public void submit_inspection(View view) {
        String datePath = date.replaceAll("/", ".");
        datePath+= "/" +site + "/"+time;
        DocumentReference df = fireStore.collection("Fire watches").document(datePath);
        String comments = Objects.requireNonNull(fg_comment_layout.getEditText()).getText().toString();
        // Create HashMap (watch) + Put All the data in it
        Map<String,Object> watch = new HashMap<>();
        watch.put("Full Name", full_name);
        watch.put("License", license);
        watch.put("Date", date);
        watch.put("Time", time);
        watch.put("Site", site);
        watch.put("Question1", q1);
        watch.put("Question2", q2);
        watch.put("Question3", q3);
        watch.put("Question4", q4);
        watch.put("Question5", q5);
        watch.put("Question6", q6);
        watch.put("Question7", q7);
        watch.put("Question8", q8);
        watch.put("Question9", q9);
        watch.put("Comments", comments);
        String isApproved = "Pending approval";
        watch.put("IsApproved", isApproved);
        df.set(watch);

        Intent intent = new Intent(FireguardDashboard.this, InspectionSubmitted.class);
        intent.putExtra("Email", userEmail);
        intent.putExtra("Password", userPassword);
        startActivity(intent);
    }
}
