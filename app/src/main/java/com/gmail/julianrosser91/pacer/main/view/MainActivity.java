package com.gmail.julianrosser91.pacer.main.view;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.julianrosser91.pacer.R;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;
import com.gmail.julianrosser91.pacer.main.model.MainModel;
import com.gmail.julianrosser91.pacer.main.model.MainModel.MainState;
import com.gmail.julianrosser91.pacer.main.presenter.MainPresenter;
import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;
import com.gmail.julianrosser91.pacer.utils.StateMaintainer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * VIEW layer of MVP pattern
 * MainActivity - View shows information about meters, time and pace
 */
public class MainActivity extends AppCompatActivity implements MainInterfaces.RequiredViewOps, View.OnClickListener {

    // Responsible to maintain the object's integrity
    // during configurations change
    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getFragmentManager(), MainActivity.class.getName());

    // MPV Presenter. Only link to data level
    private MainPresenter mPresenter;

    // Binding views
    @BindView(R.id.text_tracking_state) private TextView textTrackingState;
    @BindView(R.id.text_last_speed) private TextView textLastSpeed;
    @BindView(R.id.text_total_distance) private TextView textTotalDistance;
    @BindView(R.id.text_total_time) private TextView textTotalTime;
    @BindView(R.id.text_total_pace) private TextView textTotalPace;
    @BindView(R.id.button_start_tracking) private Button buttonStart;
    @BindView(R.id.button_stop_tracking) private Button buttonStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        setupMVP();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy(isChangingConfigurations());
    }

    private void setUpViews() {
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonStart.setOnClickListener(this);
        buttonStop.setOnClickListener(this);
    }

    /**
     * Setup Model View Presenter pattern.
     * Use a {@link StateMaintainer} to maintain the
     * Presenter and Model instances between configuration changes.
     * Could be done differently,
     * using a dependency injection for example.
     */
    private void setupMVP() {
        // Check if StateMaintainer has been created
        if (mStateMaintainer.firstTimeIn()) {
            // Create the Presenter
            MainPresenter presenter = new MainPresenter(this);
            // Create the Model
            MainModel model = new MainModel(presenter);
            // Set Presenter model
            presenter.setModel(model);
            // Add Presenter and Model to StateMaintainer
            mStateMaintainer.put(presenter);
            mStateMaintainer.put(model);
            // Set the Presenter as a interface
            // To limit the communication with it
            mPresenter = presenter;
            mPresenter.updateViewState();
        }
        // get the Presenter from StateMaintainer
        else {
            // Get the Presenter
            mPresenter = mStateMaintainer.get(MainPresenter.class.getName());
            // Updated the View in Presenter
            mPresenter.setView(this);
        }
    }

    @Override
    public void updateRouteInfo(RouteUpdate routeUpdate) {
        if (textLastSpeed != null) {
            textLastSpeed.setText(routeUpdate.getSpeed());
        }
        if (textTotalDistance != null) {
            textTotalDistance.setText(routeUpdate.getDistanceMeters());
        }
        if (textTotalTime != null) {
            textTotalTime.setText(routeUpdate.getDuration());
        }
        if (textTotalPace != null) {
            textTotalPace.setText(routeUpdate.getPace());
        }
    }

    @Override
    public void updateTrackingStatus(MainState state) {
        textTrackingState.setText(state.toString());
        if (state == MainState.STOPPED) {
            textTrackingState.setTextColor(getResources().getColor(R.color.red));
        } else {
            textTrackingState.setTextColor(getResources().getColor(R.color.green));
        }
    }

    @Override
    public void startActivityIntent(Intent activityIntent) {
        startActivity(activityIntent);
    }

    @Override
    public void startServiceIntent(Intent serviceIntent) {
        startService(serviceIntent);
    }

    @Override
    public Context getActivityContext() {
        return this;
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_start_tracking:
                mPresenter.clickStartTrackingButton();
                break;
            case R.id.button_stop_tracking:
                mPresenter.clickStopTrackingButton();
        }
    }

    /**
     * Settings
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

        if (id == R.id.action_reset_route) {
            mPresenter.resetRouteOptionSelected();
            return true;
        } else if (id == R.id.action_dump_log) {
            mPresenter.dumpGpsOptionSelected();
            return true;
        } else if (id == R.id.action_settings) {
            mPresenter.settingsButtonPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
