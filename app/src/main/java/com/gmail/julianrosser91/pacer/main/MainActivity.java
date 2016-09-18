package com.gmail.julianrosser91.pacer.main;


import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gmail.julianrosser91.pacer.R;
import com.gmail.julianrosser91.pacer.model.TrackingStatusEnum;

/**
 * VIEW layer of MVP pattern
 * MainActivity - View shows infomation about meters, time and pace
 */
public class MainActivity extends AppCompatActivity implements MainInterfaces.RequiredViewOps, View.OnClickListener {

    // Responsible to maintain the object's integrity
    // during configurations change
    private final StateMaintainer mStateMaintainer =
            new StateMaintainer(getFragmentManager(), MainActivity.class.getName());
    // MPV Presenter
    private TextView textTrackingState;
    // Views
    private MainPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpViews();
        setupMVP();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setUpViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textTrackingState = (TextView) findViewById(R.id.text_tracking_state);
        Button buttonStart = (Button) findViewById(R.id.button_start_tracking);
        if (buttonStart != null) {
            buttonStart.setOnClickListener(this);
        }
        Button buttonStop = (Button) findViewById(R.id.button_stop_tracking);
        if (buttonStop != null) {
            buttonStop.setOnClickListener(this);
        }
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
    public void updateViewWithPace(String pace) {
        TextView textLastPace = (TextView) findViewById(R.id.text_last_pace);
        if (textLastPace != null) {
            textLastPace.setText(pace);
        }
    }

    //- todo - STATES!!!!!!!!!!!!!!!!!!
    @Override
    public void updateTrackingStatus(TrackingStatusEnum status) {
        textTrackingState.setText(status.toString());
        if (status == TrackingStatusEnum.STOPPED) {
            textTrackingState.setTextColor(getResources().getColor(R.color.red));
        } else {
            textTrackingState.setTextColor(getResources().getColor(R.color.green));
        }
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
}
