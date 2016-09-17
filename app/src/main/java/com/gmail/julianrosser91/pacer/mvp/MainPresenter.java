package com.gmail.julianrosser91.pacer.mvp;

import android.util.Log;

import com.tinmegali.mvp.mvp.GenericPresenter;

import java.lang.ref.WeakReference;

public class MainPresenter
        extends GenericPresenter<MVPInterfaces.RequiredPresenterOps,
        MVPInterfaces.ProvidedModelOps,
        MVPInterfaces.RequiredViewOps,
        MainModel>
        implements
        MVPInterfaces.RequiredPresenterOps,
        MVPInterfaces.ProvidedPresenterOps {

    // Layer View reference
    private WeakReference<MVPInterfaces.RequiredViewOps> mView;
    // Layer Model reference
    private MVPInterfaces.ProvidedModelOps mModel;

    public MainPresenter(MVPInterfaces.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
        this.mModel = new MainModel(this);
    }


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
        mView = new WeakReference<>(view);
    }

    @Override
    public void clickStartTrackingButton() {
        Log.i(getClass().getSimpleName(), "start tracking...");
        mModel.startTrackingService();
    }

    @Override
    public void clickStopTrackingButton() {
        mModel.stopTrackingService();
        Log.i(getClass().getSimpleName(), "start tracking...");
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onTrackingServiceStarted() {
        mView.get().showTrackingStartedMessage("Started tracking");
        return false;
    }

    @Override
    public boolean onTrackingServiceStopped() {
        mView.get().showTrackingStoppedMessage("Stopped tracking");
        return false;
    }

    @Override
    public boolean onLocationUpdated() {
        return false;
    }
}
