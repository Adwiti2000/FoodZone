package com.project.foodzone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.project.foodzone.databinding.ActivityLoginBinding;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding activityLoginBinding;
    private FirebaseAuth lAuth;
    private static final String TAG = "Login Activity";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = ActivityLoginBinding.inflate(getLayoutInflater());
        View view = activityLoginBinding.getRoot();
        setContentView(view);
        lAuth = FirebaseAuth.getInstance();
        activityLoginBinding.sendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String country_code = activityLoginBinding.enterCountryCode.getText().toString();
                String phone = activityLoginBinding.enterPhone.getText().toString();
                String phoneNumber = "+" + country_code + "" + phone;
                if (!country_code.isEmpty() || !phone.isEmpty()) {
                    PhoneAuthOptions options = PhoneAuthOptions.newBuilder(lAuth)
                            .setPhoneNumber(phoneNumber)
                            .setTimeout(60L, TimeUnit.SECONDS)
                            .setActivity(LoginActivity.this)
                            .setCallbacks(mCallBacks)
                            .build();
                    PhoneAuthProvider.verifyPhoneNumber(options);
                } else {
                    activityLoginBinding.desText.setText("Please enter code and phone number");
                    activityLoginBinding.desText.setTextColor(Color.RED);
                    activityLoginBinding.desText.setVisibility(View.VISIBLE);
                }
            }
        });
        mCallBacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signIn(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                activityLoginBinding.desText.setText(e.getMessage());
                activityLoginBinding.desText.setTextColor(Color.RED);
                activityLoginBinding.desText.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                activityLoginBinding.desText.setText("OTP has been Sent");
                activityLoginBinding.desText.setVisibility(View.VISIBLE);
                new Handler().postDelayed(() -> {
                    Intent otpIntent = new Intent(LoginActivity.this, OtpActivity.class);
                    Log.d(TAG, "String OTP: " + s);
                    otpIntent.putExtra("auth", s);
                    startActivity(otpIntent);
                }, 10000);
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = lAuth.getCurrentUser();
        if (user != null) {
            sendToMain();
        }
    }

    private void sendToMain() {
        Intent mainIntent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private void signIn(PhoneAuthCredential credential) {
        lAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    sendToMain();
                } else {
                    activityLoginBinding.desText.setText(task.getException().getMessage());
                    activityLoginBinding.desText.setTextColor(Color.RED);
                    activityLoginBinding.desText.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}