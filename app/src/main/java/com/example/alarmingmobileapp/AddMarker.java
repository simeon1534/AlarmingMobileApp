package com.example.alarmingmobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.maps.model.LatLng;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddMarker#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddMarker extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private double latitude;
    private double longtitude;

    private Button addMarkerButton;
    private Button goBackBtn;

    private EditText radius;
    private EditText name;





    public AddMarker() {
        // Required empty public constructor
    }

    public static AddMarker newInstance(String param1) {
        AddMarker fragment = new AddMarker();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_add_marker, container, false);
        addMarkerButton=view.findViewById(R.id.addMarkerButton);
        goBackBtn=view.findViewById(R.id.goBack);
        name=view.findViewById(R.id.name);
        radius=view.findViewById(R.id.radius);
        Bundle args=getArguments();
        if(args!=null){
            LatLng cordinates = args.getParcelable("cordinates");
            if (cordinates != null) {
                latitude = cordinates.latitude;
                longtitude = cordinates.longitude;
                TextView latitude = view.findViewById(R.id.latitude);
                TextView longtitude=view.findViewById(R.id.longtitude);
                latitude.setText("Latitude: " + cordinates.latitude);
                longtitude.setText("Longtitude: "+ cordinates.longitude);
            }
        }

        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText=name.getText().toString();
                String radiusText= String.valueOf(radius);
                if(TextUtils.isEmpty(radiusText)||TextUtils.isEmpty(nameText)) {
                    Toast.makeText(getActivity(), "Please enter radius and name", Toast.LENGTH_SHORT).show();
                    return;
                }
                saveMarkerData();

            }
        });
        goBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment mapFragment=new MapsFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, mapFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void saveMarkerData(){
        try {
            MarkerModel marker = new MarkerModel();
            marker.setLatitude(Double.valueOf(String.valueOf(latitude)));
            marker.setLongtitude(Double.valueOf(String.valueOf(longtitude)));
            marker.setName(name.getText().toString());
            marker.setRadius(Integer.valueOf(radius.getText().toString()));
            DBClass.getDatabase(getActivity().getApplicationContext()).getDao().insertAllData(marker);
            Toast.makeText(getActivity(), R.string.data_inserted, Toast.LENGTH_SHORT).show();
            Intent i=new Intent(getActivity(),MainActivity.class);
            startActivity(i);
        }
        catch (Error e){
            Toast.makeText(getActivity(),"Error: "+ e,Toast.LENGTH_SHORT).show();
        }
    }
}