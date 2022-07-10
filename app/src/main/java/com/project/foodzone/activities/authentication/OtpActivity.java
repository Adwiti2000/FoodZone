package com.project.foodzone.activities.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.foodzone.activities.MainActivity;
import com.project.foodzone.databinding.ActivityOtpBinding;
import com.project.foodzone.models.User;

public class OtpActivity extends AppCompatActivity {
    private String OTP;
    private String phone;
    private String userName;
    private FirebaseAuth firebaseAuth;
    ActivityOtpBinding activityOtpBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityOtpBinding = ActivityOtpBinding.inflate(getLayoutInflater());
        View view = activityOtpBinding.getRoot();
        setContentView(view);
        firebaseAuth = FirebaseAuth.getInstance();
        OTP = getIntent().getStringExtra("auth");
        phone = getIntent().getStringExtra("phone");
        userName = getIntent().getStringExtra("userName");
        activityOtpBinding.verifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String verification_code = activityOtpBinding.enterOtp.getText().toString();
                if (!verification_code.isEmpty()) {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(OTP, verification_code);
                    signIn(credential);
                } else {
                    Toast.makeText(OtpActivity.this, "Please Enter OTP", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser != null) {
            sendToMain();
        }
    }

    private void signIn(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    checkUser();
                    sendToMain();
                } else {
                    Toast.makeText(OtpActivity.this, "Verification Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendToMain() {
        startActivity(new Intent(OtpActivity.this, MainActivity.class));
        finish();
    }

    private void checkUser() {
        String userID=firebaseAuth.getUid();
        User user=new User(userName,phone,userID,"1");
        FirebaseFirestore.getInstance().collection("Users").document(userID).set(user);
    }
}