package com.example.RSRpechhulpTest.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.example.RSRpechhulpTest.R;
import com.example.RSRpechhulpTest.utility.ActivityUtils;
import com.example.RSRpechhulpTest.utility.CustomLocationManager;
import com.example.RSRpechhulpTest.utility.MapLayoutManager;
import com.example.RSRpechhulpTest.utility.PermissionManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import java.util.Map;

/**
 * Keeps track of the devices current location and displays it on a map with address information
 * Can dial number for calling for user pickup
 */
public class MapsActivity extends AppCompatActivity {
    //Finals
    private final static int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final static int PERMISSIONS_REQUEST_CALL = 2;

    private MapPresenter mapPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        //Replace ActionBar with Toolbar with back arrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (mapFragment == null){
            mapFragment = MapFragment.newInstance();
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(), mapFragment, R.id.contentFrame);
        }

        mapPresenter = new MapPresenter(mapFragment, this);
    }

    /**
     * Called after permission dialog, change permission fields according to choice
     * @param requestCode to call the right switch case
     * @param permissions the permissions that were requested
     * @param grantResults results of the permission Requests
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is somehow cancelled, the array is empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapPresenter.updateLocationPermission();
                    mapPresenter.requestLocation();
                    mapPresenter.startLocationUpdates();
                }
                break;
            }case PERMISSIONS_REQUEST_CALL:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mapPresenter.updateCallPermission();
                    mapPresenter.requestDial();
                }
                break;
            }
        }
    }
}
