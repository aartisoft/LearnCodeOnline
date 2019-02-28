package com.example.serverfoodapp;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.serverfoodapp.Common.User;
import com.example.serverfoodapp.Retrofit.RetrofitApi.IGeoCoordinates;
import com.example.serverfoodapp.Retrofit.RetrofitClient.RetrofitGMaps;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private final static int LOCATION_PERMISSION_REQUEST = 1001;

    private GoogleMap mMap;
    private Location mLastLocation;
    private LocationRequest locationRequest;
    ;
    private GoogleApiClient mGoogleApiClient;
    ;

    private static int UPDATE_INTERVAL = 1000;
    private static int FASTEST_INTERVAL = 5000;
    private static int DISPLACEMENT =  10;
    private String TAG="mapsactivity";
    private IGeoCoordinates iGeoCoordinates;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == LOCATION_PERMISSION_REQUEST && grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            if(checkPlayServices()){
                buildApiClient();
                createLocationRequest();
                displayLocation();
            }
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (checkPlayServices()) {
                buildApiClient();
                createLocationRequest();
                displayLocation(); 
            }
        } else {
            requestRuntimePermission();

        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void requestRuntimePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_REQUEST);
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(UPDATE_INTERVAL);
        locationRequest.setFastestInterval(FASTEST_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private void displayLocation() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            requestRuntimePermission();
        } else{
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//            Log.d(TAG, "displayLocation: "+mLastLocation.toString());
            if(mLastLocation != null){
                double latitude = mLastLocation.getLatitude();
                double longitude = mLastLocation.getLongitude();
                LatLng yourLocation = new LatLng(latitude, longitude);

                mMap.addMarker(new MarkerOptions().position(yourLocation).title("Your Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(yourLocation));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(17.0f));

            drawRoute(yourLocation, User.currentRequest.getAddress());


            } else {
//                Toast.makeText(this, "Could not get the location", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "displayLocation: "+"");
            }
        }
    }

    private void drawRoute(final LatLng yourLocation, String address) {
        iGeoCoordinates= RetrofitGMaps.getRetrofit(User.baseUrl).create(IGeoCoordinates.class);
        Call<String> addressCall = iGeoCoordinates.getGeoCode(address, User.apiKey);

         addressCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(retrofit2.Call<String> call, Response<String> response) {

                if(response.body() == null){
                    Log.d(TAG, "onResponse: null resp");
                    Toast.makeText(MapsActivity.this, "Address of Shipper not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                    try {
                    Log.d(TAG, "onResponse: "+response.body());
                    final JSONObject jsonObject = new JSONObject(response.body().toString());
                    String lat = ((JSONArray)jsonObject.get("results"))
                                            .getJSONObject(0)
                                            .getJSONObject("geometry")
                                            .getJSONObject("location")
                                            .get("lat").toString();

                    String longi = ((JSONArray)jsonObject.get("results"))
                                                        .getJSONObject(0)
                                                        .getJSONObject("geometry")
                                                        .getJSONObject("location")
                                                        .get("lng").toString();

                        Log.d(TAG, "onResponse: "+lat+"+"+longi);
                         LatLng orderLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(longi));
                    MarkerOptions markerOptions = new MarkerOptions().title("Order of "+User.currentRequest.getName()+"-"+User.currentRequest.getPhone()).position(orderLocation);
                    mMap.addMarker(markerOptions);

                    //get routes directions
                    Call<String> directionCall = iGeoCoordinates.getDirections(yourLocation.latitude+","+yourLocation.longitude, orderLocation.latitude+","+orderLocation.longitude, User.apiKey );

                        Log.d(TAG, "LogginCoord onResponse: "+yourLocation.latitude+","+yourLocation.longitude+"   "+ orderLocation.latitude+","+orderLocation.longitude);

                    directionCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            try {
                                Log.d(TAG, "onResponseDirections: "+response.body().toString());
                                JSONObject jsonObject1 = new JSONObject(response.body().toString());

                                if(jsonObject1.get("status").toString().equals("ZERO_RESULTS")){
                                    Toast.makeText(MapsActivity.this, "Sorry, your search appears to be outside our current coverage area for transit.", Toast.LENGTH_SHORT).show();
                                    return;
                            }
                                new ParserTask().execute(response.body().toString());

                            } catch (JSONException e1) {
                                Toast.makeText(MapsActivity.this, "Sorry..Could not fetch location", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onResponse: "+e1.toString());
                                return;
                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.d(TAG, "onDirectionFetchFailure: "+t.toString());

                        }
                    });




                } catch (JSONException e) {
                    Log.d(TAG, "catched could not fetch lat and lang from response: "+e.toString());
                }
            }

            @Override
            public void onFailure(retrofit2.Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: "+t.toString());
//                Toast.makeText(MapsActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void buildApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();

        mGoogleApiClient.connect();
    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode != ConnectionResult.SUCCESS){
            if(GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST);
            } else{
                Toast.makeText(this, "Your Device does not have Play Services Installed", Toast.LENGTH_SHORT).show();
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment.      This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        displayLocation();

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
    }

    private void startLocationUpdate() {
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mGoogleApiClient != null){
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>>> {
        ProgressDialog mdialog = new ProgressDialog(MapsActivity.this);


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mdialog.setMessage("Please Wait");
            mdialog.show();
        }

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... strings) {
            JSONObject jsonObject;
            List<List<HashMap<String, String>>>  routes  = null;
            try {
                jsonObject = new JSONObject(strings[0]);
                DirectionsParser directionsParser = new DirectionsParser();
                routes = directionsParser.parse(jsonObject);
            } catch (JSONException e) {
                Log.d(TAG, "doInBackground: "+e.toString());
            }
            return routes;
        }
        @Override

        protected void onPostExecute(List<List<HashMap<String, String>>> lists) {
            super.onPostExecute(lists);
            mdialog.dismiss();
            ArrayList points=null;
            PolylineOptions polylineOptions=null;
            for (int i = 0; i < lists.size(); i++) {
                points = new ArrayList();
                polylineOptions = new PolylineOptions();

//                Log.d(TAG, "onPostExecute: "+polylineOptions.toString());

                List<HashMap<String, String>> path = lists.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> pts = path.get(j);

                    String lat = pts.get("lat");
                    String lng = pts.get("lng");
//                    Log.d(TAG, "onPostExecute:lat "+lat);
//                    Log.d(TAG, "onPostExecute:longi "+lng);


                    LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                    points.add(latLng);

                }
                polylineOptions.addAll(points);
                polylineOptions.width(15);
                polylineOptions.color(Color.BLUE);
                polylineOptions.geodesic(true);
            }
                try {
                    mMap.addPolyline(polylineOptions);
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute: "+e.toString());
                }
                }
        }

    }
}
