package com.example.alarmingmobileapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import com.example.alarmingmobileapp.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private String currentFragmentTag = "mapFragment";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        startService(new Intent(this,LocationService.class));
        replaceFragment(new MapsFragment());
        binding.bottomNavMenu.setBackground(null);
        binding.bottomNavMenu.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.map) {
                replaceFragment(new MapsFragment());
                return true;
            }
            if (itemId == R.id.list) {
                replaceFragment((new ListMarkers()));
                return true;
            }
            if (itemId == R.id.logout) {
                stopService(new Intent(this,LocationService.class));
                finish();
            }
            return false;
        });
    }


    private void replaceFragment(Fragment fragment) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        Fragment currentFragment = fragmentManager.findFragmentByTag(currentFragmentTag);
//        if (currentFragment != null) {
//            transaction.hide(currentFragment);
//        }
//        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
//        if (existingFragment != null) {
//            transaction.show(existingFragment);
//        } else {
//            transaction.add(R.id.frame, fragment, tag);
//        }
//        currentFragmentTag = tag;
//        transaction.addToBackStack(tag);
//        transaction.commit();
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }



    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //onCreate(savedInstanceState);
    }


}