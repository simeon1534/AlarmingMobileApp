package com.example.alarmingmobileapp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.alarmingmobileapp.Models.MarkerModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListMarkers extends Fragment {




    private ListView listViewMarkers;

    private ArrayAdapter<String> adapter;
    FirebaseUser user;
    FirebaseAuth auth;
    String userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_markers, container, false);
        listViewMarkers = rootView.findViewById(R.id.listViewMarkers);

        // Initialize the ArrayAdapter
        adapter = new ArrayAdapter<>(requireContext(), R.layout.fragment_item_marker, R.id.textViewMarkerName);

        // Set the adapter to the ListView
        listViewMarkers.setAdapter(adapter);
        auth = FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        userId=user.getUid();

        // Fetch marker names from Firebase and populate the adapter
        DatabaseReference markersRef = FirebaseDatabase.getInstance("https://android-project-d7ebc-default-rtdb.europe-west1.firebasedatabase.app").getReference().child("markers").child(userId);
        markersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear(); // Clear previous data
                for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
                    MarkerModel markerModel = markerSnapshot.getValue(MarkerModel.class);
                    if(markerModel!=null){
                        String markerName="Marker name: "+markerModel.getName();
                        String cordinates="Cordinates: "+markerModel.getLatitude()+", "+markerModel.getLongtitude();
                        String markerRadius="Marker Radius: " +String.valueOf(markerModel.getRadius());
                        String marker=markerName+ "\n" +markerRadius+ "\n"+cordinates;
                        adapter.add(marker);
                    }
                }
                adapter.notifyDataSetChanged(); // Notify adapter after data changes
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors here
            }
        });

        return rootView;
    }


//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        // Retrieve marker names associated with the user's UID
//        FirebaseAuth auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//
//        if (currentUser != null) {
//            String uid = currentUser.getUid();
//            DatabaseReference userMarkersRef = FirebaseDatabase.getInstance().getReference().child("markers").child(uid);
//
//            userMarkersRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(DataSnapshot dataSnapshot) {
//                    for (DataSnapshot markerSnapshot : dataSnapshot.getChildren()) {
//                        String markerName = markerSnapshot.child("name").getValue(String.class);
//                        if (markerName != null) {
//                            // Display each marker name using a Toast message
//                            Toast.makeText(getContext(), "Marker Name: " + markerName, Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(DatabaseError databaseError) {
//                    // Handle potential errors here
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "user not signed in", Toast.LENGTH_SHORT).show();
//
//            // User is not signed in
//            // Handle the case where no user is authenticated
//        }
//    }


}