package com.gmail.julianrosser91.pacer;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.julianrosser91.pacer.events.StopServiceEvent;
import com.gmail.julianrosser91.pacer.events.UpdateTrackedRouteEvent;
import com.gmail.julianrosser91.pacer.model.TrackedRoute;
import com.gmail.julianrosser91.pacer.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IntentCallback {

    private static final String REQUESTING_LOCATION_UPDATES_KEY = "REQUESTING_LOCATION_UPDATES_KEY";
    private static final String LOCATION_KEY = "LOCATION_KEY";
    private static final String TRACKED_ROUTE_KEY = "TRACKED_ROUTE_KEY";
    private static final String LAST_UPDATED_TIME_MILLIS_KEY = "LAST_UPDATED_TIME_MILLIS_KEY";
    private static final String LAST_UPDATED_TIME_STRING_KEY = "LAST_UPDATED_TIME_STRING_KEY";

    // Object references
    private TrackedRoute trackedRoute;

    // Variables
    private boolean mRequestingLocationUpdates;
    private String mLastUpdatedTimeString;
    private long mLastUpdatedTimeMillis;
    private Location mCurrentLocation;

    // Views
    private TextView mTextCurrentLocation;
    private TextView mTextLastUpdated;
    private TextView mTextListenerState;
    private TextView mTextExerciseNodeCount;
    private Button mButtonStart;
    private Button mButtonStop;
    private TextView mTextSplitTime;
    private TextView mTextSplitDistance;
    private TextView mTextTotalTime;
    private TextView mTextTotalDistance;
    private RecyclerView mRecyclerViewSplits;
    private SplitsRecyclerAdapter mSplitsRecyclerAdapter;
    private TextView mTextKmph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        setUpViews();
        updateValuesFromBundle(savedInstanceState);
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
                mRequestingLocationUpdates);
        savedInstanceState.putParcelable(TRACKED_ROUTE_KEY, trackedRoute);
        savedInstanceState.putParcelable(LOCATION_KEY, mCurrentLocation);
        savedInstanceState.putLong(LAST_UPDATED_TIME_MILLIS_KEY, mLastUpdatedTimeMillis); // find
        savedInstanceState.putString(LAST_UPDATED_TIME_STRING_KEY, mLastUpdatedTimeString);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            trackedRoute = new TrackedRoute();
        } else {
            // Update the TrackedRoute Array of nodes
            if (savedInstanceState.keySet().contains(TRACKED_ROUTE_KEY)) {
                trackedRoute = savedInstanceState.getParcelable(TRACKED_ROUTE_KEY);
            }
            // Update the value of mRequestingLocationUpdates from the Bundle, and
            // make sure that the Start Updates and Stop Updates buttons are
            // correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        REQUESTING_LOCATION_UPDATES_KEY);
                // setButtonsEnabledState(); // todo
            }

            // Update the value of mCurrentLocation from the Bundle
            if (savedInstanceState.keySet().contains(LOCATION_KEY)) {
                mCurrentLocation = savedInstanceState.getParcelable(LOCATION_KEY);
            }

            // Update the value of mLastUpdatedTimeMillis
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_MILLIS_KEY)) {
                mLastUpdatedTimeMillis = savedInstanceState.getLong(
                        LAST_UPDATED_TIME_MILLIS_KEY);
            }
            // Update the value of mLastUpdateTimeString
            if (savedInstanceState.keySet().contains(LAST_UPDATED_TIME_STRING_KEY)) {
                mLastUpdatedTimeString = savedInstanceState.getString(
                        LAST_UPDATED_TIME_STRING_KEY);
            }
            updateUI();
        }
    }

    private void updateUI() {
        if (mCurrentLocation != null) {
            mTextCurrentLocation.setText(mCurrentLocation.getLatitude() + " | " + mCurrentLocation.getLongitude());
        }
        if (mLastUpdatedTimeString != null && !mLastUpdatedTimeString.contains("")) {
            mTextLastUpdated.setText(mLastUpdatedTimeString);
        }
        if (trackedRoute != null) {
            mTextExerciseNodeCount.setText("Nodes: " + trackedRoute.getSize());
        }
    }

    private void setUpViews() {
        mTextCurrentLocation = (TextView) findViewById(R.id.text_current_location);
        mTextLastUpdated = (TextView) findViewById(R.id.text_last_updated);
        mTextListenerState = (TextView) findViewById(R.id.text_listener_state);
        mTextExerciseNodeCount = (TextView) findViewById(R.id.text_exercise_node_count);
        mTextKmph = (TextView) findViewById(R.id.text_kmph);

        mTextSplitTime = (TextView) findViewById(R.id.text_split_time);
        mTextSplitDistance = (TextView) findViewById(R.id.text_split_distance);
        mTextTotalTime = (TextView) findViewById(R.id.text_total_time);
        mTextTotalDistance = (TextView) findViewById(R.id.text_total_distance);

        mButtonStart = (Button) findViewById(R.id.button_start_tracking);
        mButtonStart.setOnClickListener(this);
        mButtonStop = (Button) findViewById(R.id.button_stop_tracking);
        mButtonStop.setOnClickListener(this);

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        mRecyclerViewSplits = (RecyclerView) findViewById(R.id.splitRecyclerView);
        mRecyclerViewSplits.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, true));
        mSplitsRecyclerAdapter = new SplitsRecyclerAdapter(this);
        mRecyclerViewSplits.setAdapter(mSplitsRecyclerAdapter);
    }



    private void dumpGpsLog() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Location l : trackedRoute.getLocationNodes()) {
            stringBuilder.append(l.getLatitude() + ", " + l.getLongitude() + "\n");
        }
        Log.i(getClass().getSimpleName(), stringBuilder.toString());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_start_tracking) {
            // Permission check
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                askForLocationPermission();
                return;
            }
            // GPS enabled check
            if (isGPSEnabled()) {
                Intent intent = new Intent(this, TrackingService.class);
                startService(intent);
                mRequestingLocationUpdates = true;
                updateListenerStateText(LocationListenerState.LISTENING);
            } else {
                Snackbar.make(mTextCurrentLocation, R.string.gps_disabled, Snackbar.LENGTH_LONG).setAction(getString(R.string.enable), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showGpsPrompt();
                    }
                }).show();
            }
        } else if (v.getId() == R.id.button_stop_tracking) {
            EventBus.getDefault().post(new StopServiceEvent());
            mRequestingLocationUpdates = false;
            updateListenerStateText(LocationListenerState.DISCONNECTED);
        }
    }

    public void updateListenerStateText(LocationListenerState state) {
        switch (state) {
            case LISTENING:
                mTextListenerState.setText(R.string.listening);
                mTextListenerState.setTextColor(getResources().getColor(R.color.green));
                return;
            case DISCONNECTED:
                mTextListenerState.setText(R.string.disconnected);
                mTextListenerState.setTextColor(getResources().getColor(R.color.orange));
                return;
            default:
                mTextListenerState.setText(R.string.error);
                mTextListenerState.setTextColor(getResources().getColor(R.color.red));
        }
    }

    private void updateViewsWithLocation(Location location) {
        mTextCurrentLocation.setText(location.getLatitude() + " | " + location.getLongitude());
        mLastUpdatedTimeString = Constants.DATE_FORMAT_LAST_UPDATED.format(location.getTime());
        mTextLastUpdated.setText(mLastUpdatedTimeString);
        mTextExerciseNodeCount.setText("Nodes: " + trackedRoute.getSize());
        mTextKmph.setText("" + trackedRoute.getKmphFromLastVector() + " km/h");
    }

    private void updateSplitsWithLocation() {
        if (trackedRoute.getDistanceBetweenLastTwoNodes() > 0) {
            mTextSplitTime.setText(Constants.DATE_FORMAT_SPLIT_TIME.format(trackedRoute.getTimeBetweenLastTwoNodes()));
            mTextTotalTime.setText(Constants.DATE_FORMAT_SPLIT_TIME.format(trackedRoute.getTotalTimeInMillis()));
            mTextSplitDistance.setText("" + trackedRoute.getDistanceBetweenLastTwoNodes() + "m");
            mTextTotalDistance.setText("" + trackedRoute.getTotalDistance() + "m");
        }
    }

    public void askForLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                Constants.LOCATION_PERMISSION_REQUEST);
    }

    public boolean isGPSEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void showGpsPrompt() {
        //prompt user to enable gps
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
    }

    /**
     * Options
     */
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
        if (id == R.id.action_reset_route) {
            trackedRoute.resetData();
            mSplitsRecyclerAdapter.clearData();
            return true;
        } else if (id == R.id.action_dump_log) {
            dumpGpsLog();
            return true;
        } else if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Event listeners
     */
    @Subscribe
    public void onLocationUpdated(UpdateTrackedRouteEvent event) {
        trackedRoute = event.getTrackedRoute();
        mCurrentLocation = event.getTrackedRoute().getLastLocation();
        mLastUpdatedTimeMillis = System.currentTimeMillis();
        updateViewsWithLocation(mCurrentLocation);
        updateSplitsWithLocation();
        mSplitsRecyclerAdapter.addSplit(trackedRoute);
        updateListenerStateText(LocationListenerState.LISTENING);
    }

    /**
     * Callbacks from split times RecyclerView
     */
    @Override
    public void onIntentReceived(Intent chatIntent) {
        // Start Activity
        Toast.makeText(this, "intentClicked", Toast.LENGTH_SHORT).show(); // todo ????
    }

    @Override
    public void onSplitAdded() {
        int firstPosition = mSplitsRecyclerAdapter.getItemCount()-1;
        if (firstPosition < 0) firstPosition = 0;
        mRecyclerViewSplits.smoothScrollToPosition(firstPosition);
    }

    /**
     * Enum for possible location listener states
     */
    public enum LocationListenerState {
        LISTENING,
        DISCONNECTED
    }
}
