package com.example.alarmingmobileapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.adapters.ViewGroupBindingAdapter;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;


public class MapsFragment extends Fragment {

    private Marker clickedMarker;
    private boolean isMarkerVisible = false;
    private Button addMarkerButton;
    private OnAddMarkerButtonClickListener buttonClickListener;
    private GoogleMap map;
    private Map<String, Marker> mapMarkers = new HashMap<>();
    private Map<String, Circle> mapCircles = new HashMap<>();

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();

    String userId=currentUser.getUid();
    Toolbar toolbar;


    private int FINE_PERMISSION_CODE=1;

    FirebaseDatabase db = FirebaseDatabase.getInstance("https://android-project-d7ebc-default-rtdb.europe-west1.firebasedatabase.app");
    DatabaseReference dbRef=db.getReference().child("markers").child(userId);


    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            map = googleMap;
            LatLng sydney = new LatLng(43, 27);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        MarkerModel markerModel=dataSnapshot.getValue(MarkerModel.class);
                        LatLng latLng=new LatLng(markerModel.getLatitude(),markerModel.getLongtitude());
                        MarkerOptions markerOptions=new MarkerOptions();
                        markerOptions.position(latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)).title(markerModel.getName());
                        Marker marker = map.addMarker(markerOptions);
                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(latLng)
                                .radius(markerModel.getRadius())
                                .strokeWidth(1)
                                .strokeColor(Color.BLACK)
                                .fillColor(Color.parseColor("#25FFFF00"));
                        Circle circle=map.addCircle(circleOptions);
                        mapMarkers.put(dataSnapshot.getKey(), marker);
                        mapCircles.put(dataSnapshot.getKey(), circle);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getActivity(),"There was an error fetching your data! Please try uploading markers",Toast.LENGTH_SHORT).show();
                }
            });

            dbRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                    String commentKey = snapshot.getKey();
                    Marker deletedMarker=mapMarkers.get(commentKey);
                    if(deletedMarker!=null){
                        deletedMarker.remove();
                        mapMarkers.remove(commentKey);
                    }
                    Circle removeCircle=mapCircles.get(commentKey);
                    if(removeCircle!=null){
                        removeCircle.remove();
                        mapCircles.remove(commentKey);
                    }

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
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

    public interface OnAddMarkerButtonClickListener {
        void onAddMarkerButtonClick(LatLng coordinates);
    }

}