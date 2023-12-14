package com.example.alarmingmobileapp;

import static androidx.core.app.ActivityCompat.recreate;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.example.alarmingmobileapp.DaoClass.DaoClass;
import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import java.util.List;
import java.util.Locale;


public class MapsFragment extends Fragment {

    private Marker clickedMarker;
    private GoogleMap map;
    Toolbar toolbar;


    FloatingActionButton add_btn;

    FusedLocationProviderClient fusedLocationProviderClient;

    View mapView;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            mapView=getView();
            LatLng sydney = new LatLng(43, 27);
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001);

            }else{
                map.setMyLocationEnabled(true);
                //map.getUiSettings().setMyLocationButtonEnabled(false);
            }
            if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) { //pozicionirane na butona za MyLocation
                View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
                RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);rlp.setMargins(0,0,30,30);
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            DaoClass markerDao = DBClass.getDatabase(getContext()).getDao();
            List<MarkerModel> markers = markerDao.getAllData();
            for (MarkerModel marker : markers) {
                LatLng latLng = new LatLng(marker.getLatitude(), marker.getLongtitude());
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
                            .title("Clicked Location")
                            .draggable(true));
                    map.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                        @Override
                        public void onMarkerDrag(@NonNull Marker marker) {

                        }

                        @Override
                        public void onMarkerDragEnd(@NonNull Marker marker) {
                            LatLng cords = marker.getPosition();
                            clickedMarker.setPosition(cords);
                        }


                        @Override
                        public void onMarkerDragStart(@NonNull Marker clickedMarker) {

                        }
                    });
                    add_btn.show();
                    add_btn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Fragment addMarkerFr = new AddMarker();
                            Bundle args = new Bundle();
                            args.putParcelable("cordinates", clickedMarker.getPosition());
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
        loadLocale();
        View rootView = inflater.inflate(R.layout.fragment_maps, container, false);
        add_btn=rootView.findViewById(R.id.add_btn);
        toolbar = rootView.findViewById(R.id.toolbar);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext());

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
            ActionBar actionBar=((AppCompatActivity) activity).getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
                actionBar.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.map_layouts_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.normal) {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        if (id == R.id.satellite) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }
        if(id==R.id.terrain){
            map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        }
        if(id==R.id.language){
            changeLanguage();
        }
        return true;

    }
    private void changeLanguage() {
        final String[] langs = {"English", "Български"};
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Choose language");
        mBuilder.setSingleChoiceItems(langs, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                switch (i) {
                    case 0:
                        setLocale("en");
                        break;
                    case 1:
                        setLocale("bg");
                        break;
                }
                getActivity().recreate();
                dialog.dismiss();
            }
        });
        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    private void setLocale(String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        Resources resources = getResources();
        //getActivity().getApplicationContext().getResources().updateConfiguration(config,getActivity().getApplicationContext().getResources().getDisplayMetrics());
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE).edit();
        editor.putString("Language", language);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = prefs.getString("Language", "en");
        Log.d("LAng",language);
        setLocale(language);
    }


}