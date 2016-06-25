package com.gmail.julianrosser91.pacer;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {

    /**
     * Enum for possible location listener states
     */
    public enum LocationListenerState {
        CONNECTED,
        CONNECTING,
        DISCONNECTED,
        ERROR
    }

    // Constants
    private static final int MILLISECONDS_PER_SECOND = 1000;
    public static final int UPDATE_INTERVAL_IN_SECONDS = 1;
    private static final long UPDATE_INTERVAL = MILLISECONDS_PER_SECOND
            * UPDATE_INTERVAL_IN_SECONDS;
    private static final int FASTEST_INTERVAL_IN_MILLISECONDS = 100;
    private final static int LOCATION_PERMISSION_REQUEST = 100;
    final static public SimpleDateFormat DATE_FORMAT_LAST_UPDATED = new SimpleDateFormat("hh:m.s");

    // Views
    private TextView mTextCurrentLocation;
    private TextView mTextLastUpdated;
    private TextView mTextListenerState;
    private Button mButtonStart;
    private Button mButtonStop;

    // Objects
    public Context mContext;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private long mLastUpdateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setUpViews();
        setUpLocationListener();
    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        updateListenerStateText(LocationListenerState.DISCONNECTED);
        super.onStop();
    }

    private void setUpViews() {
        mTextCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        mTextLastUpdated = (TextView) findViewById(R.id.text_last_updated);
        mTextListenerState = (TextView) findViewById(R.id.text_listener_state);
        mButtonStart = (Button) findViewById(R.id.button_start_tracking);
        mButtonStart.setOnClickListener(this);
        mButtonStop = (Button) findViewById(R.id.button_stop_tracking);
        mButtonStop.setOnClickListener(this);
    }

    private void setUpLocationListener() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mLocationRequest = createLocationRequest();
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.button_start_tracking) {
            if (! mGoogleApiClient.isConnected() && ! mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        } else if (v.getId() == R.id.button_stop_tracking) {
            if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.disconnect();
                updateListenerStateText(LocationListenerState.DISCONNECTED);
            }
        }
    }

    public void updateListenerStateText(LocationListenerState state) {
        switch (state) {
            case CONNECTED:
                mTextListenerState.setText("CONNECTED");
                mTextListenerState.setTextColor(getResources().getColor(R.color.green));
                return;
            case DISCONNECTED:
                mTextListenerState.setText("DISCONNECTED");
                mTextListenerState.setTextColor(getResources().getColor(R.color.orange));
                return;
            default:
                mTextListenerState.setText("ERROR");
                mTextListenerState.setTextColor(getResources().getColor(R.color.red));

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(getClass().getSimpleName(), "onGoogleAPiConnected");

        updateListenerStateText(LocationListenerState.CONNECTED);

//        if (mRequestingLocationUpdates) {
        startLocationUpdates();
//        }
    }

    protected void startLocationUpdates() {
        // Permission check
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            askForLocationPermission();
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        handleUpdatedLocation(location);
                    }
                });
    }

    private void handleUpdatedLocation(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = new Date().getTime();

        Log.i(getClass().getSimpleName(), "(L) new loc: " + location.getLatitude() + " | " + location.getLongitude());
        System.out.print("(S) new loc: " + location.getLatitude() + " | " + location.getLongitude());

        mTextCurrentLocation.setText(location.getLatitude() + " | " + location.getLongitude());
        mTextLastUpdated.setText(DATE_FORMAT_LAST_UPDATED.format(new Date()));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(getClass().getSimpleName(), "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "onConnectionFailed");
    }

    public void askForLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                LOCATION_PERMISSION_REQUEST);
    }
}
