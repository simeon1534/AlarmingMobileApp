package com.example.alarmingmobileapp;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;



public class Login extends AppCompatActivity {

    private LocationRequest locationRequest;
    public static final int REQUEST_CHECK_SETTING=10;




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestRunTimePermissions();
        Button login_btn = findViewById(R.id.login_button);
        login_btn.setOnClickListener(v->requestRunTimePermissions());
    }
    private void requestRunTimePermissions(){
        if (ActivityCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(Login.this,Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED
                &&ActivityCompat.checkSelfPermission(Login.this,Manifest.permission.POST_NOTIFICATIONS)== PackageManager.PERMISSION_GRANTED
        ){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Intent intent=new Intent(Login.this,MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                enableLocation();
            }
        }else if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.POST_NOTIFICATIONS)
        ){
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION
            ,Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS},1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1001){
            if(grantResults.length>0&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(Login.this,"Permissions granted! ",Toast.LENGTH_SHORT).show();

            }else if(!ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)
            && !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)
            && !ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.POST_NOTIFICATIONS)
            ){
                    Toast.makeText(this,"You need to grant permissions!",Toast.LENGTH_SHORT).show();
            }else {
                requestRunTimePermissions();
            }
        }
    }

    private void enableLocation(){
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder=new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> result= LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());
        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                }catch (ApiException e){
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(Login.this, REQUEST_CHECK_SETTING);
                            }
                            catch (IntentSender.SendIntentException exception){

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CHECK_SETTING){
            switch (resultCode){
                case AppCompatActivity.RESULT_OK:
                    Intent intent=new Intent(Login.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case AppCompatActivity.RESULT_CANCELED:
                    Toast.makeText(Login.this, R.string.gps_location_is_off_you_must_turn_it_on, Toast.LENGTH_SHORT).show();

            }
        }
    }
}
