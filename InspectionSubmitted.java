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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class InspectionSubmitted extends AppCompatActivity {
    Button logOut, newSubmission;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    DocumentReference documentReference;
    FirebaseUser user = auth.getCurrentUser();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou);

        logOut = findViewById(R.id.logOut);
        newSubmission = findViewById(R.id.newSubmission);

        documentReference = fStore.collection("Users").document(user.getUid());
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent logOut = new Intent(InspectionSubmitted.this, LoginActivity.class);
                startActivity(logOut);
            }
        });

        newSubmission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = getIntent().getStringExtra("Email");
                String userPassword = getIntent().getStringExtra("Password");
                loginUser(userEmail,userPassword);
            }
        });

    }
    public void loginUser(String email, String password){

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(authResult -> {
            Toast.makeText(InspectionSubmitted.this,"Login Successfully !", Toast.LENGTH_LONG).show();
            documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if ( Objects.requireNonNull(documentSnapshot.getString("IsSupervisor")).equals("yes") ){
                        startActivity(new Intent(InspectionSubmitted.this, FireguardDashboard.class));
                    }
                    else{
                        startActivity(new Intent(InspectionSubmitted.this, FireguardDashboard.class));
                    }
                }
            });


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(InspectionSubmitted.this,"Login Failed !", Toast.LENGTH_LONG).show();
            }
        })
        ;

    }
}
