package com.example.metrofire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {

    private TextInputLayout first_name, last_name, license, sign_up_code, email, phone, password;
    private String first_name_string, last_name_string, license_string, sign_up_code_string, email_string, phone_string, password_string;
    private String is_supervisor = "no";
    private boolean form_validated = true;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        // Get the FirebaseAuth and FirebaseFirestore Instances
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        // FIND ALL THE INPUT FIELDS
        first_name = findViewById(R.id.first_name_layout);
        last_name = findViewById(R.id.last_name_layout);
        license = findViewById(R.id.license_input_layout);
        sign_up_code = findViewById(R.id.sign_up_code_layout);
        phone = findViewById(R.id.phone_layout);
        email = findViewById(R.id.sign_up_email_layout);
        password = findViewById(R.id.sign_up_password_layout);

        // FIND THE BUTTON
        Button sign_up_btn = findViewById(R.id.sign_up_btn);
    }
    public void sign_up(View view){

        validateForm(); //Checking If all Inputs Are Valid
        if(form_validated){

            fAuth.createUserWithEmailAndPassword(email_string,password_string).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {   // WHEN A USER IS CREATED SUCCESSFULLY
                    FirebaseUser user = fAuth.getCurrentUser();
                    assert user != null;
                    DocumentReference df = fStore.collection("Users").document(user.getUid());
                    // Create HashMap (userInfo) + Put All the data in it
                    Map<String,Object> userInfo = new HashMap<>();
                    userInfo.put("FirstName", first_name_string);
                    userInfo.put("LastName", last_name_string);
                    userInfo.put("License", license_string);
                    userInfo.put("Phone", phone_string);
                    userInfo.put("Email", email_string);
                    userInfo.put("SignUpCode", sign_up_code_string);
                    userInfo.put("Password", password_string);
                    userInfo.put("IsSupervisor", is_supervisor);
                    df.set(userInfo);

                    Toast.makeText(SignUp.this,"Account created successfully !", Toast.LENGTH_LONG).show();
                    // Now Go to the appropriate Dashboard (Fireguard or supervisor)
                    Intent go_fireguard = new Intent(SignUp.this, FireguardDashboard.class);
                    Intent go_supervisor = new Intent(SignUp.this, FireguardDashboard.class);
                    if (is_supervisor.equals("yes")){
                        // Replace with supervisor dashboard if included
                        startActivity(go_fireguard);
                        finish();
                    }
                    else {
                        startActivity(go_fireguard);
                        finish();
                    }

                }
            }).addOnFailureListener(new OnFailureListener() {   // WHEN A USER CREATION IS  NOT SUCCESSFUL
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this,"Account creation unsuccessfully !", Toast.LENGTH_LONG).show();
                }
            });

        }

    }
    private void validateForm(){
        validateFirstName();
        validateLastName();
        validateLicense();
        validatePhone();
        validateEmail();
        validateSignUpCode();
        validatePassword();
    }
    private void validateFirstName(){
        first_name_string = Objects.requireNonNull(first_name.getEditText()).getText().toString();
        if (first_name_string.isEmpty()){
            first_name.setError("Please add your glorious First name!");
            form_validated = false;
        }
        else {
            form_validated = true;
            first_name.setError(null);
        }
    }
    private void validateLastName(){
        last_name_string = Objects.requireNonNull(last_name.getEditText()).getText().toString();
        if (last_name_string.isEmpty()){
            last_name.setError("Please add your glorious Last name!");
            form_validated = false;
        }
        else {
            form_validated = true;
            last_name.setError(null);
        }
    }
    private void validateLicense(){
        license_string = Objects.requireNonNull(license.getEditText()).getText().toString();
        if (license_string.isEmpty()){
            license.setError("Please add your License number!");
            form_validated = false;
        }
        else {
            form_validated = true;
            license.setError(null);
        }
    }
    private void validatePhone(){
        phone_string = Objects.requireNonNull(phone.getEditText()).getText().toString();
        if (phone_string.isEmpty()){
            phone.setError("Please add your phone number");
            form_validated = false;
        }
        else {
            form_validated = true;
            phone.setError(null);
        }
    }
    private void validateEmail(){
        email_string = Objects.requireNonNull(email.getEditText()).getText().toString();

        //Create a Regex to check if email matches a regular email pattern
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email_string);

        if (email_string.isEmpty()){ // WHEN EMAIL STRING IS EMPTY
            email.setError("Please add your email");
            form_validated = false;
        }
        else if(matcher.find()){ // WHEN Pattern Matches Correctly
            form_validated = true;
            email.setError(null);
        }
        else{ // WHEN Pattern is not Matched
            email.setError("Please enter a valid email");
            form_validated = false;
        }
    }
    private void validateSignUpCode(){
        sign_up_code_string = Objects.requireNonNull(sign_up_code.getEditText()).getText().toString();
        //GRANT SUPERVISOR STATUS OR NOT
        if ( (sign_up_code_string.contains("sp")) || (sign_up_code_string.contains("SP"))){
            is_supervisor = "yes";
        }
        if (sign_up_code_string.isEmpty()){
            sign_up_code.setError("Please add your sign up code");
            form_validated = false;
        }
        else {
            form_validated = true;
            sign_up_code.setError(null);
        }
    }
    private void validatePassword(){
        password_string = Objects.requireNonNull(password.getEditText()).getText().toString();
        if (password_string.isEmpty()) {
            password.setError("Please add your Password");
            form_validated = false;
        }
        else {
            form_validated = true;
            password.setError(null);
        }
    }
}
