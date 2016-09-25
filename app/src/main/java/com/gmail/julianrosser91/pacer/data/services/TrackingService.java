package com.gmail.julianrosser91.pacer.data.services;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gmail.julianrosser91.pacer.Pacer;
import com.gmail.julianrosser91.pacer.data.events.StopServiceEvent;
import com.gmail.julianrosser91.pacer.data.events.LocationEvent;
import com.gmail.julianrosser91.pacer.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Random;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static boolean isTracking = false;

    // Service Threading handlers todo - simplify?
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Object references
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    public TrackingService() {

    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            setUpLocationListener();
            startTrackingLocation();
        }

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        EventBus.getDefault().register(this);

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Thread.MAX_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        // Send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Toast.makeText(this, "tracking stopped", Toast.LENGTH_SHORT).show();
    }

    /**
     * Initialise location listener, API client & set up locationRequest
     */
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
        mLocationRequest.setInterval(Constants.UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(Constants.FASTEST_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    /**
     * Tracking lifecycle methods
     */
    public void startTrackingLocation() {
        if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.connect();
        } else if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        isTracking = true;
        // Start listening for location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        handleUpdatedLocation(location);
                    }
                });
            }
    }

    private void handleUpdatedLocation(Location location) {
        // Save to DB
        Pacer.getRoutesDatabase().addLocationToDatabase(location);
        EventBus.getDefault().post(new LocationEvent(location));
    }

    public void stopTrackingLocation() {
        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.disconnect();
        }
        isTracking = false;
    }

    public static boolean getIsTracking() {
        return isTracking;
    }

    /**
     * Event listeners
     */
    @Subscribe
    public void stopService(StopServiceEvent event) {
        stopTrackingLocation();
        stopRepeatingTask();
        stopSelf();
    }

    /**
     * Implemented LocationListener methods
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {

        boolean useFakeLocation = PreferenceManager.getDefaultSharedPreferences(getApplicationContext())
                .getBoolean("fake_location_switch", false);
        if (useFakeLocation) {
            startReturningFakeLocationData();
        } else {
            startLocationUpdates();
        }
        Log.i(getClass().getSimpleName(), "onGoogleAPiConnected");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(getClass().getSimpleName(), "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "onConnectionFailed");
    }

    /**
     * Temporary method for supplying sample location data
     */
    private Handler mHandler;

    private void startReturningFakeLocationData() {
        isTracking = true;
        if (mHandler != null) {
            mHandler.removeCallbacks(fakePaceGenerater);
        } else {
            mHandler = new Handler();
        }
        startRepeatingTask();
    }

    private void generateFakeLocation() {
        Location fakeLocation = new Location("provider");
        fakeLocation.setTime(System.currentTimeMillis());

        double d = (new Random().nextDouble() / 1000) + 51.510;
        double e = (new Random().nextDouble() / 1000) + (-0.127);

        fakeLocation.setLatitude(d);
        fakeLocation.setLongitude(e);
        fakeLocation.setSpeed(new Random().nextFloat() * 15);

        Pacer.getRoutesDatabase().addLocationToDatabase(fakeLocation);
        EventBus.getDefault().post(new LocationEvent(fakeLocation));
    }

    void startRepeatingTask() {
        fakePaceGenerater.run();
    }

    void stopRepeatingTask() {
        if (mHandler != null) {
            mHandler.removeCallbacks(fakePaceGenerater);
        }
    }

    Runnable fakePaceGenerater = new Runnable() {
        @Override
        public void run() {
            try {
                generateFakeLocation();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(fakePaceGenerater, 3000);
            }
        }
    };

}
