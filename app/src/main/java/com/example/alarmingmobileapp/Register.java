package com.example.alarmingmobileapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Register extends AppCompatActivity {

    TextView loginLink;
    Button register_button;

    EditText emailInput,passwordInput,usernameInput,confirmpasswordInput;
    String email,password,username,confirmpassword;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        loginLink=findViewById(R.id.login_link);
        register_button=findViewById(R.id.register_button);
        emailInput=findViewById(R.id.email_edittext);
        passwordInput=findViewById(R.id.password_edittext);
        confirmpasswordInput=findViewById(R.id.confirm_password_edittext);
        usernameInput=findViewById(R.id.username_edittext);
        auth=FirebaseAuth.getInstance();

        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                finish();
            }
        });
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=String.valueOf(emailInput.getText());
                password=String.valueOf(passwordInput.getText());
                username=String.valueOf(usernameInput.getText());
                confirmpassword=String.valueOf(confirmpasswordInput.getText());
                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this,"Please enter email!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    Toast.makeText(Register.this,"Please enter password!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(username)){
                    Toast.makeText(Register.this,"Please enter email!",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.length()<6){
                    Toast.makeText(Register.this,"Enter password longer than 6 symbols",Toast.LENGTH_SHORT).show();
                }
                if(!password.equals(confirmpassword)){
                    Toast.makeText(Register.this,"Password dont match!",Toast.LENGTH_SHORT).show();
                    return;
                }

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                UserProfileChangeRequest userProfileChangeRequest=new UserProfileChangeRequest.Builder()
                                        .setDisplayName(username)
                                        .build();
                                FirebaseUser firebaseUser=authResult.getUser();
                                firebaseUser.updateProfile(userProfileChangeRequest);
                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
            }
        });
    }
}