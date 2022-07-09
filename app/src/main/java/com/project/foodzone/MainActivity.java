package com.project.foodzone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.foodzone.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
 ActivityMainBinding activityMainBinding;
 private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding=ActivityMainBinding.inflate(getLayoutInflater());
        View view=activityMainBinding.getRoot();
        setContentView(view);

        auth=FirebaseAuth.getInstance();
        activityMainBinding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(MainActivity.this , LoginActivity.class));
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null){
            startActivity(new Intent(MainActivity.this ,MainActivity.class));
            finish();
        }
    }
}