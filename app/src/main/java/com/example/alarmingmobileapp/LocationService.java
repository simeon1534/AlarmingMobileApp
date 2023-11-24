package com.example.alarmingmobileapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.alarmingmobileapp.DaoClass.DaoClass;
import com.example.alarmingmobileapp.Models.MarkerModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationRequest;

import java.util.List;


public class LocationService extends Service {
    private final IBinder binder = new LocalBinder();

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback;

    private static final int LOCATION_REQUEST_CODE = 1001;

    private Handler handler = new Handler();
    private Runnable updateMarkersTask;

    private static final String CHANNEL_ID = "markerNot";
    private static final int NOTIFICATION_ID = 1;

    private boolean isNotificationSent = false;






    @Override
    public void onCreate() {
        super.onCreate();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Log.d("Locman", "Service started!");
        createLocationCallback();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startLocationUpdates();
        updateMarkersList();
        scheduleMarkerUpdate();
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }


    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location location = locationResult.getLastLocation();
                Log.d("LocationService", "Received location update: " + location.toString());
                checkMarkerProximity(location);
            }
        };
    }

    private void startLocationUpdates() {
        LocationRequest locationRequest = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(1000);
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
        } else {
            Toast.makeText(getApplicationContext(), "No permissions", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopLocationUpdates() {
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    private void checkMarkerProximity(Location usrLocation) {
        DaoClass markerDao = DBClass.getDatabase(getApplicationContext()).getDao();
        List<MarkerModel> markers = markerDao.getAllData();

        for (MarkerModel marker : markers) {
            Location markerLocation = new Location("marker");
            markerLocation.setLatitude(marker.getLatitude());
            markerLocation.setLongitude(marker.getLongtitude());

            float distance = usrLocation.distanceTo(markerLocation);

            if (distance <= marker.getRadius()) {
                sendNotification(marker.getName(),distance);
            }else{
                cancelNotification();
            }
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "markerNot";
            String description = "urrrr";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @SuppressLint("MissingPermission")
    private void sendNotification(String markerName,float distance) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            createNotificationChannel();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.baseline_location_searching_24)
                    .setContentTitle("You are approaching a marker")
                    .setContentText("You are approaching: " + markerName + " within distance: " + distance + " meters.")
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
    private void cancelNotification(){
        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(this);
        notificationManagerCompat.cancel(NOTIFICATION_ID);
    }

    private void scheduleMarkerUpdate() {
        updateMarkersTask = new Runnable() {
            @Override
            public void run() {
                updateMarkersList();
                handler.postDelayed(this, 5000);
            }
        };
        handler.postDelayed(updateMarkersTask, 5000);
    }

    private void updateMarkersList() {
        DaoClass markerDao = DBClass.getDatabase(getApplicationContext()).getDao();
        List<MarkerModel> markers = markerDao.getAllData();
    }



}