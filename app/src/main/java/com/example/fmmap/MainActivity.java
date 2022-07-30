package com.example.fmmap;


import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.WindowManager;
import android.webkit.PermissionRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;

public class MainActivity extends AppCompatActivity {
     SupportMapFragment smf;
     FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        getWindow ().setFlags ( WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN );

        smf = (SupportMapFragment) getSupportFragmentManager ().findFragmentById ( R.id.fm_map );
        client = LocationServices.getFusedLocationProviderClient ( this );


        Dexter.withContext ( getApplicationContext () )
                .withPermission ( Manifest.permission.ACCESS_FINE_LOCATION )
                .withListener ( new PermissionListener () {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        getMyLocation ();

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest ();
                    }


                } ).check ();

    }

    public void getMyLocation() {
        if (ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission ( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED);

        Task<Location> task = client.getLastLocation ();
        task.addOnSuccessListener ( new OnSuccessListener<Location> () {
            @Override
            public void onSuccess(Location location) {

                smf.getMapAsync ( new OnMapReadyCallback () {
                    @Override
                    public void onMapReady(@NonNull GoogleMap googleMap) {

                        LatLng latLng = new LatLng ( location.getLatitude (), location.getLongitude () );
                        MarkerOptions markerOptions = new MarkerOptions ().position ( latLng ).title ( "You are hear " );

                        googleMap.addMarker ( markerOptions );
                        googleMap.animateCamera ( CameraUpdateFactory.newLatLngZoom ( latLng, 10 ) );
                    }
                } );
            }
        } );

    }

}
