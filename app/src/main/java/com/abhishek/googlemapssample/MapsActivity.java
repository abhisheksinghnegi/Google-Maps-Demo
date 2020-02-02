package com.abhishek.googlemapssample;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.internal.TaskApiCall;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {
    private static final String TAG = "bosdk";
    private GoogleMap mMap;
    LocationManager locationManager;
    public static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATION_ON_CODE = 1234;
    FusedLocationProviderClient mfusedLocationProviderClient;
    private static final float ZOOM = 15f;
    public boolean isgranted = false;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.


    }

    void init() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void getDeviceLocation() {

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, this);

    }

    void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: locationPermissionasked");
        String[] location = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "getLocationPermission: everything is available");
                isgranted = true;
                init();
            } else {
                Log.d(TAG, "getLocationPermission: called for permission");
                ActivityCompat.requestPermissions(MapsActivity.this, location, LOCATION_ON_CODE);
            }
        } else {
            Log.d(TAG, "getLocationPermission: called for permisson");
            ActivityCompat.requestPermissions(MapsActivity.this, location, LOCATION_ON_CODE);
        }

    }

    void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: to latlng " + latLng.latitude + " " + latLng.longitude);
        mMap.setMyLocationEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called");
        isgranted = false;
        Log.d(TAG, "onRequestPermissionsResult: " + grantResults.length);
        Log.d(TAG, "onRequestPermissionsResult:" + grantResults[0] + " " + grantResults[1]);
        switch (requestCode) {
            case LOCATION_ON_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    isgranted = true;
                    init();
                } else {
                    isgranted = false;
                    Toast.makeText(MapsActivity.this, "PERMISSION NOT GIVEN", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Toast.makeText(this, "MAP READY", Toast.LENGTH_SHORT).show();
        //  Toast.makeText(MapsActivity.this,"MAP LAUNCHED",Toast.LENGTH_SHORT).show();
        // Add a marker in Sydney and move the camera
        if (isgranted&&latLng!=null) {
            {
                getDeviceLocation();
                LatLng les = latLng;
                mMap.addMarker(new MarkerOptions().position(les).title("CURRENT LOCATION"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(les));

            }
        } else {
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "onLocationChanged: called");
        latLng = new LatLng(location.getLatitude(),location.getLongitude());

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
