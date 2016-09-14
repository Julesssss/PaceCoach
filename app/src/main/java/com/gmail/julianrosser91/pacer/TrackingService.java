package com.gmail.julianrosser91.pacer;

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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.gmail.julianrosser91.pacer.events.StopServiceEvent;
import com.gmail.julianrosser91.pacer.events.UpdateTrackedRouteEvent;
import com.gmail.julianrosser91.pacer.model.TrackedRoute;
import com.gmail.julianrosser91.pacer.utils.Constants;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class TrackingService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    // Service Threading handlers todo - simplify?
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    // Object references
    private TrackedRoute trackedRoute;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long mLastUpdatedTimeMillis;

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
        trackedRoute = new TrackedRoute();

        // Start up the thread running the service.  Note that we create a
        // separate thread because the service normally runs in the process's
        // main thread, which we don't want to block.  We also make it
        // background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Thread.MAX_PRIORITY);
        thread.start();

        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();

        // Send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        mServiceHandler.sendMessage(msg);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        Toast.makeText(this, "service stopped", Toast.LENGTH_SHORT).show();
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
        // Permission check
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            Toast.makeText(TrackingService.this, "NO PERMISSION", Toast.LENGTH_SHORT).show();
//           // todo --------> askForLocationPermission();
            return;
        }
        // Start listening for location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, new com.google.android.gms.location.LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        handleUpdatedLocation(location);
                        Log.i(getClass().getSimpleName(), "(L) new loc: " + location.getLatitude() + " | " + location.getLongitude());
                    }
                });
    }

    private void handleUpdatedLocation(Location location) {
        trackedRoute.addLocation(location);
        mLastUpdatedTimeMillis = System.currentTimeMillis();
        EventBus.getDefault().post(new UpdateTrackedRouteEvent(trackedRoute));
    }

    public void stopTrackingLocation() {
        if (mGoogleApiClient.isConnected() || mGoogleApiClient.isConnecting()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Event listeners
     */

    @Subscribe
    public void stopService(StopServiceEvent event) {
        stopTrackingLocation();
        stopSelf();
    }

    /**
     * Implemented LocationListener methods
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(getClass().getSimpleName(), "onGoogleAPiConnected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(getClass().getSimpleName(), "onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(getClass().getSimpleName(), "onConnectionFailed");
    }

}
