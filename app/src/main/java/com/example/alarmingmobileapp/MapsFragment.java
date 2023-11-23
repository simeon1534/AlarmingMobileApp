package com.example.alarmingmobileapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alarmingmobileapp.DaoClass.DaoClass;
import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


public class MapsFragment extends Fragment {

    private Marker clickedMarker;
    private boolean isMarkerVisible = false;
    private Button addMarkerButton;
    private GoogleMap map;
    Toolbar toolbar;




    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            LatLng sydney = new LatLng(43, 27);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            DaoClass markerDao=DBClass.getDatabase(getContext()).getDao();
            List<MarkerModel> markers=markerDao.getAllData();
            for (MarkerModel marker:markers){
                LatLng latLng=new LatLng(marker.getLatitude(),marker.getLongtitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(marker.getName());
                Marker googleMarker = googleMap.addMarker(markerOptions);

                CircleOptions circleOptions = new CircleOptions();
                circleOptions.center(latLng)
                        .radius(marker.getRadius())
                        .strokeWidth(1)
                        .strokeColor(Color.BLACK)
                        .fillColor(Color.parseColor("#25FFFF00"));
                Circle circle = googleMap.addCircle(circleOptions);
            }

            googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    // Remove the previous clicked marker (if any)
                    if (clickedMarker != null) {
                        clickedMarker.remove();
                    }

                    clickedMarker = googleMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title("Clicked Location"));
                    addMarkerButton.setVisibility(View.VISIBLE);
                    addMarkerButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment addMarkerFr=new AddMarker();
                            Bundle args=new Bundle();
                            args.putParcelable("cordinates",latLng);
                            addMarkerFr.setArguments(args);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.replace(R.id.frame, addMarkerFr);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }
                    });
                }

            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_maps, container, false);
        addMarkerButton = rootView.findViewById(R.id.addMarkerBtn);
        toolbar=rootView.findViewById(R.id.toolbar);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        setToolbar(toolbar);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

    }


    private void setToolbar(Toolbar toolbar) {
        FragmentActivity activity = getActivity();
        if (activity instanceof AppCompatActivity) {
            ((AppCompatActivity) activity).setSupportActionBar(toolbar);
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.map_layouts_menu,menu);
        super.onCreateOptionsMenu(menu,inflater);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.normal){
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if(id==R.id.satellite){
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        return true;

    }


}