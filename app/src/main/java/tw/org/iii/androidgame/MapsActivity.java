package tw.org.iii.androidgame;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    private static final int REQUEST_LOCATION = 2;
    private GoogleMap mMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest locationRequest;
    protected static int count = 0,coin = 0;
    private double mylat = 0.0, mylog = 0.0;
    BgmManager bm = new BgmManager();
    private OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedpreferences = getSharedPreferences("data", MODE_PRIVATE);
        count = sharedpreferences.getInt("count", 0);
        coin = sharedpreferences.getInt("coin",2000);
        Log.d("Map", "1");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Log.d("Map", "2");
        mapFragment.getMapAsync(this);
        if (mGoogleApiClient == null) {
            Log.d("Map", "3");
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Log.d("Map", "4");
        createLocationRequest();
        bm.musicCreate(this, R.raw.popin);
        bm.musicPlay();


    }

    @Override
    protected void onPause() {
        super.onPause();
        bm.musicStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bm.musicPlay();
    }



    private void createLocationRequest() {
        Log.d("Map", "5");
        locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Log.d("Map", "6");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
            Log.d("Map", "7");
        } else {
            setupMyLocation();
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);

        Log.d("Map", "8");
        SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);


        for (int i = 0; i < home.mar.size(); i++) {

            int z = sharedPreferences.getInt(home.mar.get(i).getSupplystation_name().toString(), 0);
            if (z == 0) {
                sharedPreferences.edit().putInt(home.mar.get(i).getSupplystation_name().toString(), 0).apply();
                Marker a =
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(Double.parseDouble(home.mar.get(i).getSupplystation_longitude().toString()), Double.parseDouble(home.mar.get(i).getSupplystation_latitude().toString())))
                                .title(home.mar.get(i).getSupplystation_name().toString())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.scrap))
                                .snippet("獲得記憶碎片1塊\n獲得金幣"+home.mar.get(i).getCoin()+"個"));
                int x = sharedPreferences.getInt(home.mar.get(i).getSupplystation_name().toString(), 0);
                sharedPreferences.edit().putInt(home.mar.get(i).getSupplystation_name().toString()+"2",home.mar.get(i).getCoin()).apply();
                a.setTag(x);
                Log.d("sharedPreferences1", x + "");
            }
        }


//        marker.showInfoWindow();
//        mMap.setInfoWindowAdapter(
//                new GoogleMap.InfoWindowAdapter() {
//                    @Override
//                    public View getInfoWindow(Marker marker) {
//                        View view = getLayoutInflater().inflate(
//                                R.layout.info_window, null);
//                        TextView title =
//                                (TextView) view.findViewById(R.id.info_title);
//                        title.setText("Title: " + marker.getTitle());
//                        TextView snippet =
//                                (TextView) view.findViewById(R.id.info_snippet);
//                        snippet.setText(marker.getTitle());
//                        return view;
//                    }
//
//                    @Override
//                    public View getInfoContents(Marker marker) {
//                        return null;
//                    }
//                }
//        );

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setupMyLocation();
                double lat = marker.getPosition().latitude;
                double log = marker.getPosition().longitude;
                SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
                count = sharedPreferences.getInt("count", 0);
                int z = sharedPreferences.getInt(marker.getTitle(), 0);

                if (z == 0) {


                    if (mylat > (lat - 0.5) && mylat < (lat + 0.5) && mylog > (log - 0.5) && mylog < (log + 0.5)) {
                        Log.d("AAAAAAAAAAAAAAAAAA","AAAAAAAAAAAAAAAAAA");
                        marker.remove();
                        count += 1;
                        sharedPreferences.edit().putInt("count", count).apply();

                        Intent intent = getIntent();
                        Bundle bundle = new Bundle();
                        bundle.putInt("count", count);
                        intent.putExtras(bundle);
                        setResult(0, intent);


//                        public static void main(String[] args) throws IOException {
                        String num = Integer.toString(count);
                        String utf8 = null;
                        try {
                            utf8 = URLEncoder.encode(num, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        String strURL = "http://tomcattimetravel.azurewebsites.net/jspProject/timeTravel/webAPI/gamescheduleWebAPI.jsp?member_id="+sharedPreferences.getString("id","1")+"&timeshard_num=" + utf8;
                        Request request = new Request.Builder().url(strURL).build();
                        Call call = client.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d("OKHTTP", e.toString());
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                final String strJson = response.body().string();
                                Log.d("OKHTTP", strJson);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        });



                        new AlertDialog.Builder(MapsActivity.this)
                                .setTitle(marker.getTitle())
                                .setMessage(marker.getSnippet())
                                .setPositiveButton("OK", null)
                                .show();
                        sharedPreferences.edit().putInt(marker.getTitle(), 1).apply();


                        coin += sharedPreferences.getInt(marker.getTitle() + "2", 0);
                        sharedPreferences.edit().putInt("coin",coin) .apply();

                        Log.d("Alpha","Alpha");

                        marker.setTag(1);
                        if (MapsActivity.count == 6 || MapsActivity.count == 11 || MapsActivity.count == 16  || MapsActivity.count == 21  || MapsActivity.count == 26  || MapsActivity.count == 31  || MapsActivity.count == 36) {
                            sharedPreferences.edit().putBoolean("p",false).apply();
                        }
                    } else {
                        new AlertDialog.Builder(MapsActivity.this)
                                .setTitle(marker.getTitle())
                                .setMessage("您未在範圍內")
                                .setPositiveButton("OK", null)
                                .show();
                    }

                } else {
                    new AlertDialog.Builder(MapsActivity.this)
                            .setTitle(marker.getTitle())
                            .setMessage("您已拿過")
                            .setPositiveButton("OK", null)
                            .show();
                }


                Log.d("count", count + "");
                Log.d("mylat", mylat + "");
                Log.d("lat", lat + "");


                return true;

            }

        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Map", "9");
        switch (requestCode) {
            case REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //noinspection MissingPermission
                    setupMyLocation();
                } else {
                    // 使用者拒絕授權,停用MyLocation功能
                }
                break;
        }
    }


    private void setupMyLocation() {
        Log.d("Map", "10");
        //noinspection MissingPermission
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationButtonClickListener(
                new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        LocationManager locationManager =
                                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        Criteria criteria = new Criteria();
                        criteria.setAccuracy(Criteria.ACCURACY_FINE);
                        String provider = locationManager.getBestProvider(criteria, true);
                        //noinspection MissingPermission
                        Location location = locationManager.getLastKnownLocation(provider);
                        if (location != null) {
                            Log.i("LOCATION", location.getLatitude() + "/" +
                                    location.getLongitude());
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(location.getLatitude(), location.getLongitude())
                                    , 18));
                            mylat = location.getLatitude();
                            mylog = location.getLongitude();
                        }
                        return false;
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Map", "11");

        while (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        Location location = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (location != null){
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(location.getLatitude(), location.getLongitude())
                    , 18));
            mylat = location.getLatitude();
            mylog = location.getLongitude();


        }

        //noinspection MissingPermission
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d("Map","12");
        if (location != null) {
            Log.d("LOCATION", location.getLatitude() + "," +
                    location.getLongitude());
            mylat = location.getLatitude();
            mylog = location.getLongitude();
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                    new LatLng(location.getLatitude(), location.getLongitude())
//                    , 15));
        }
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event)
//    {
//        if (keyCode == KeyEvent.KEYCODE_BACK )
//        {
//            finish();
//        }
//
//        return false;
//
//    }


}