package com.gmail.julianrosser91.pacer.mvp;

import android.util.Log;

import com.tinmegali.mvp.mvp.GenericPresenter;

public class MainPresenter
        extends GenericPresenter<MVPInterfaces.RequiredPresenterOps,
        MVPInterfaces.ProvidedModelOps,
        MVPInterfaces.RequiredViewOps,
        MainModel>
        implements
        MVPInterfaces.RequiredPresenterOps,
        MVPInterfaces.ProvidedPresenterOps {

    /**
     * Operation called during VIEW creation in
     * {@link com.tinmegali.mvp.mvp.GenericMVPActivity#onCreate(Class, Object)} </br>
     * Responsible to initialize MODEL.
     *
     * @param view The current VIEW instance
     */
    @Override
    public void onCreate(MVPInterfaces.RequiredViewOps view) {
        super.onCreate(MainModel.class, this);
//         super.onCreate(<Model.class>, <RequiredPresenterOps>);
        setView(view);
    }

    /**
     * Operation called by VIEW after its reconstruction.
     *
     * @param view The current VIEW instance
     */
    @Override
    public void onConfigurationChanged(MVPInterfaces.RequiredViewOps view) {
        setView(view);
    }

    @Override
    public void clickStartTrackingButton() {
        Log.i(getClass().getSimpleName(), "start tracking...");
    }

    @Override
    public void clickStopTrackingButton() {
        Log.i(getClass().getSimpleName(), "start tracking...");
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onTrackingServiceStarted() {
        return false;
    }

    @Override
    public boolean onTrackingServiceStopped() {
        return false;
    }

    @Override
    public boolean onLocationUpdated() {
        return false;
    }
}
