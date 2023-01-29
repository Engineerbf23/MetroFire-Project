package com.example.metrofire;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private String email, password;
    private TextInputLayout email_input, password_input;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser user;
    TextView forgot_password ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        email_input = findViewById(R.id.sign_in_email_layout);
        password_input = findViewById(R.id.sign_in_password_layout);
        Button login_btn = findViewById(R.id.login_btn);

        user = auth.getCurrentUser();
        //Find the collection that contains the Users
        documentReference = fStore.collection("Users").document(user.getUid());

        forgot_password = findViewById(R.id.forgot_password);
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_forgot_password = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(go_forgot_password);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = Objects.requireNonNull(email_input.getEditText()).getText().toString();
                password = Objects.requireNonNull(password_input.getEditText()).getText().toString();

                loginUser(email, password);
                /* ADD CONDITIONS FOR THE LAUNCH OF APPROPRIATE SCREENS -- guard_screen or supervisor_screen*/
                //startActivity(new Intent(LoginActivity.this,));
            }
        });

    }


    public void loginUser(String email, String password){

         auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
             Toast.makeText(LoginActivity.this,"Login Successfully !", Toast.LENGTH_LONG).show();
             documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                 @Override
                 public void onSuccess(DocumentSnapshot documentSnapshot) {

                    if ( Objects.requireNonNull(documentSnapshot.getString("IsSupervisor")).equals("yes") ){
                        //Left for supervisor class
                        startActivity(new Intent(LoginActivity.this, FireguardDashboard.class));
                    }
                    else{
                        startActivity(new Intent(LoginActivity.this, FireguardDashboard.class));
                    }
                     finish();
                 }
             });


         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception e) {
                 Toast.makeText(LoginActivity.this,"Login Failed !", Toast.LENGTH_LONG).show();
             }
         })
         ;

    }

    // SEND USER TO THE SIGN UP PAGE
    public void createAccount(View view) {
        Intent intent_sign_up = new Intent(LoginActivity.this, SignUp.class);
        startActivity(intent_sign_up);

    }

}