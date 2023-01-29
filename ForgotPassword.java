package com.example.metrofire;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ForgotPassword extends AppCompatActivity {

    TextInputLayout forgot_password;
    Button reset_button;
    FirebaseAuth auth;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);

        forgot_password = findViewById(R.id.forgot_layout);
        reset_button = findViewById(R.id.reset_btn);

        auth = FirebaseAuth.getInstance();

        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        String email = Objects.requireNonNull(forgot_password.getEditText()).getText().toString();
        //Create a Regex to check if email matches a regular email pattern
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern emailPat = Pattern.compile(emailRegex,Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPat.matcher(email);

        if (email.isEmpty()){ // WHEN EMAIL STRING IS EMPTY
            forgot_password.setError("Please add your email");
            forgot_password.requestFocus();
        }
        else if(matcher.find()){ // WHEN Pattern Matches Correctly
            forgot_password.setError(null);
        }
        else{ // WHEN Pattern is not Matched
            forgot_password.setError("Please enter a valid email");
            forgot_password.requestFocus();
        }
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgotPassword.this,"Check Your Email To Reset Your Password",Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(ForgotPassword.this,"Try Again, Something Went Wrong !",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
