package com.gmail.julianrosser91.pacer.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.gmail.julianrosser91.pacer.R;
import com.tinmegali.mvp.mvp.GenericMVPActivity;

/**
 * VIEW layer of MVP pattern
 * MainActivity - View shows infomation about distance, time and pace
 */
public class MainActivity extends GenericMVPActivity<MVPInterfaces.RequiredViewOps,
        MVPInterfaces.ProvidedPresenterOps,
        MainPresenter>
        implements MVPInterfaces.RequiredViewOps, View.OnClickListener {

    private MainPresenter presenter;

    /**
     * Method that initialized MVP objects
     * {@link super#onCreate(Class, Object)} should always be called
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(MainPresenter.class, this);
        setContentView(R.layout.activity_main);
        presenter = new MainPresenter();
        setUpButtons();
    }

    private void setUpButtons() {
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
    public void updateViewWithPace() {
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
