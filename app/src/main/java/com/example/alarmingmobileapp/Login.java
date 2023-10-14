package com.example.alarmingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.ktx.Firebase;

import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser=auth.getCurrentUser();
        if(currentUser != null){
            Toast.makeText(Login.this,"User is not null",Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}