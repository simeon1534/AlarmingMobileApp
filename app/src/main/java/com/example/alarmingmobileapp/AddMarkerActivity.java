package com.example.alarmingmobileapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.maps.model.LatLng;

public class AddMarkerActivity extends AppCompatActivity {

    private double latitude;
    private double longtitude;

    private Button addMarkerButton;
    private Button goBackBtn;

    private EditText radius;
    private EditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);
        addMarkerButton=findViewById(R.id.addMarkerButton);
        goBackBtn=findViewById(R.id.goBack);
        name=findViewById(R.id.name);
        radius=findViewById(R.id.radius);
            LatLng cordinates = getIntent().getParcelableExtra("cordinates");
            if (cordinates != null) {
                latitude = cordinates.latitude;
                longtitude = cordinates.longitude;
                TextView latitude = findViewById(R.id.latitude);
                TextView longtitude=findViewById(R.id.longtitude);
                latitude.setText("Latitude: " + cordinates.latitude);
                longtitude.setText("Longtitude: "+ cordinates.longitude);
        }

        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText=name.getText().toString();
                String radiusText= String.valueOf(radius);
                if(TextUtils.isEmpty(radiusText)||TextUtils.isEmpty(nameText)) {
                    Toast.makeText(AddMarkerActivity.this, "Please enter radius and name", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveMarkerData();

            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void saveMarkerData(){
        try {
            MarkerModel marker = new MarkerModel();
            marker.setLatitude(Double.valueOf(String.valueOf(latitude)));
            marker.setLongtitude(Double.valueOf(String.valueOf(longtitude)));
            marker.setName(name.getText().toString());
            marker.setRadius(Integer.valueOf(radius.getText().toString()));
            DBClass.getDatabase(getApplicationContext()).getDao().insertAllData(marker);
            Toast.makeText(AddMarkerActivity.this, R.string.data_inserted, Toast.LENGTH_SHORT).show();
            Intent i=new Intent(AddMarkerActivity.this,MainActivity.class);
            startActivity(i);
            finish();
        }
        catch (Error e){
            Toast.makeText(AddMarkerActivity.this,"Error: "+ e,Toast.LENGTH_SHORT).show();
        }
    }

}