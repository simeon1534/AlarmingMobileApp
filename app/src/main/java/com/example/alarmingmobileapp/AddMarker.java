package com.example.alarmingmobileapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public AddMarker() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment AddMarker.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_add_marker, container, false);
        Bundle args=getArguments();
        if(args!=null){
            LatLng cordinates = args.getParcelable("cordinates");
            if (cordinates != null) {
                TextView latitude = view.findViewById(R.id.latitude);
                TextView longtitude=view.findViewById(R.id.longtitude);
                latitude.setText("Latitude: " + cordinates.latitude);
                longtitude.setText("Longtitude: "+ cordinates.longitude);
            }
        }

        return view;
    }
}