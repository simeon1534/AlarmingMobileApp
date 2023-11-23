package com.example.alarmingmobileapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alarmingmobileapp.DaoClass.DaoClass;
import com.example.alarmingmobileapp.Models.MarkerModel;

import java.util.List;
import java.util.TooManyListenersException;


public class ListMarkers extends Fragment {




    private ListView listViewMarkers;

    private ArrayAdapter<String> adapter;

    String markerData;

    DaoClass markerDao=DBClass.getDatabase(getContext()).getDao();
    List<MarkerModel> markers=markerDao.getAllData();

    TextView message;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_list_markers, container, false);
        listViewMarkers = rootView.findViewById(R.id.listViewMarkers);
        message=rootView.findViewById(R.id.text_msg);
        listViewMarkers.setLongClickable(true);
        adapter = new ArrayAdapter<>(requireContext(), R.layout.fragment_item_marker, R.id.textViewMarkerName);
        listViewMarkers.setAdapter(adapter);
        loadData();


        listViewMarkers.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                if (index>=0 && index<markers.size()){
                    MarkerModel removeMarker=markers.get(index);
                    markers.remove(index);
                    adapter.clear();
                    DBClass.getDatabase(getActivity().getApplicationContext()).getDao().deleteMarker(removeMarker);
                    Toast.makeText(requireContext(),"Marker successfully removed!",Toast.LENGTH_SHORT).show();
                    loadData();
                }

                return true;
            }
        });


        return rootView;
    }

    private void loadData(){
        for (MarkerModel marker:markers){
            String markerName="Marker name: "+marker.getName();
            String cordinates="Cordinates: "+marker.getLatitude()+", "+marker.getLongtitude();
            String markerRadius="Marker Radius: " +String.valueOf(marker.getRadius());
            markerData=markerName+ "\n" +markerRadius+ "\n"+cordinates;
            adapter.add(markerData);
            adapter.notifyDataSetChanged();
        }
        if(markers.isEmpty()){
            message.setVisibility(View.VISIBLE);
        }

    }

}