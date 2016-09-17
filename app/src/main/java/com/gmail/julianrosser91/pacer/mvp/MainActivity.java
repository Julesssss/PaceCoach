package com.gmail.julianrosser91.pacer.mvp;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gmail.julianrosser91.pacer.R;
import com.tinmegali.mvp.mvp.GenericMVPActivity;

/**
 * VIEW layer of MVP pattern
 * MainActivity - View shows infomation about meters, time and pace
 */
public class MainActivity extends GenericMVPActivity<MVPInterfaces.RequiredViewOps,
        MVPInterfaces.ProvidedPresenterOps,
        MainPresenter>
        implements MVPInterfaces.RequiredViewOps, View.OnClickListener {

    private MainPresenter presenter;
    private TextView textTrackingState;

    /**
     * Method that initialized MVP objects
     * {@link super#onCreate(Class, Object)} should always be called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        super.onCreate(MainPresenter.class, this);
        setContentView(R.layout.activity_main);
        setUpViews();
        presenter = new MainPresenter(this);
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

    @Override
    public void updateViewWithPace(String pace) {
        TextView textLastPace = (TextView) findViewById(R.id.text_last_pace);
        if (textLastPace != null) {
            textLastPace.setText(pace);
        }
    }

    //- todo - STATES!!!!!!!!!!!!!!!!!!
    @Override
    public void updateTrackingStatus(TrackingStatus status) {
        textTrackingState.setText(status.toString());
        if (status == TrackingStatus.STOPPED) {
            textTrackingState.setTextColor(getResources().getColor(R.color.red));
        } else {
            textTrackingState.setTextColor(getResources().getColor(R.color.green));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.button_start_tracking:
                presenter.clickStartTrackingButton();
                break;
            case R.id.button_stop_tracking:
                presenter.clickStopTrackingButton();
        }
    }
}
