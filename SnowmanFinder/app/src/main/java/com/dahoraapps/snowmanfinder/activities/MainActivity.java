package com.dahoraapps.snowmanfinder.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.dahoraapps.snowmanfinder.R;
import com.dahoraapps.snowmanfinder.adapters.SnowmanInfoWindowAdapter;
import com.dahoraapps.snowmanfinder.dtos.SnowmanDTO;
import com.dahoraapps.snowmanfinder.fragments.SnowmanFavoritedFragment;
import com.dahoraapps.snowmanfinder.fragments.SnowmanFragment;
import com.dahoraapps.snowmanfinder.helpers.ApiHelper;
import com.dahoraapps.snowmanfinder.models.Snowman;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, Callback<SnowmanDTO> {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private ArrayList<Snowman> snowmen = new ArrayList<>();
    private SnowmanFragment snowmanFragment = null;
    private Double latitude;
    private Double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        TabLayout.Tab tab = tabLayout.getTabAt(1);
        if (tab != null)
            tab.select();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("snowmen", snowmen);
        outState.putSerializable("latitude", mLastLocation.getLatitude());
        outState.putSerializable("longitude", mLastLocation.getLongitude());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if(savedInstanceState!=null){
            latitude = savedInstanceState.getDouble("latitude");
            longitude = savedInstanceState.getDouble("longitude");
            if(mMap!=null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 100.0f));
            }

            this.snowmen = (ArrayList<Snowman>) savedInstanceState.getSerializable("snowmen");
            if(mMap!=null)
                setMarkers();
            if (snowmanFragment != null)
                snowmanFragment.updateSnowmen(this.snowmen);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if(mMap!=null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 100.0f));
        }

        ApiHelper.getApi().getSnowmen(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1000).enqueue(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResponse(Call<SnowmanDTO> call, Response<SnowmanDTO> response) {
        if(response.isSuccessful() && mMap!=null){
            snowmen = (ArrayList<Snowman>) response.body().getResults();
            if(mMap!=null) {
                setMarkers();
            }
            if(snowmanFragment!=null)
                snowmanFragment.updateSnowmen(snowmen);
        }
    }

    private void setMarkers() {
        for(Integer i=0; i<snowmen.size(); i++) {
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(snowmen.get(i).getLatitude(), snowmen.get(i).getLongitude()))
                    .title(String.valueOf(i)));
        }
        mMap.setInfoWindowAdapter(new SnowmanInfoWindowAdapter(snowmen, this));
    }

    @Override
    public void onFailure(Call<SnowmanDTO> call, Throwable t) {
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    snowmanFragment = SnowmanFragment.newInstance(snowmen);
                    return snowmanFragment;
                case 1:
                    return getMapFragment();
                case 2:
                    return SnowmanFavoritedFragment.newInstance();
                default:
                    return getMapFragment();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "LISTA";
                case 1:
                    return "MAPA";
                case 2:
                    return "FAVORITOS";
            }
            return null;
        }
    }

    private Fragment getMapFragment() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        return mapFragment;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);

        if(mLastLocation!=null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()), 100.0f));
        }else if(latitude!=null && longitude!=null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 100.0f));
        }

        if(snowmen!=null && !snowmen.isEmpty()){
            setMarkers();
        }
    }

}
