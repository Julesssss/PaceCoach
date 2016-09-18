package com.gmail.julianrosser91.pacer.main;

import android.util.Log;

import com.gmail.julianrosser91.pacer.model.TrackingStatusEnum;
import com.tinmegali.mvp.mvp.GenericPresenter;

import java.lang.ref.WeakReference;

public class MainPresenter
        extends GenericPresenter<MainInterfaces.RequiredPresenterOps,
        MainInterfaces.ProvidedModelOps,
        MainInterfaces.RequiredViewOps,
        MainModel>
        implements
        MainInterfaces.RequiredPresenterOps,
        MainInterfaces.ProvidedPresenterOps {

    // Layer View reference
    private WeakReference<MainInterfaces.RequiredViewOps> mView;
    // Layer Model reference
    private MainInterfaces.ProvidedModelOps mModel;

    public MainPresenter(MainInterfaces.RequiredViewOps mView) {
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
    public void onCreate(MainInterfaces.RequiredViewOps view) {
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
    public void onConfigurationChanged(MainInterfaces.RequiredViewOps view) {
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
        Log.i(getClass().getSimpleName(), "stop tracking...");
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onTrackingServiceStarted() {
        if (mView != null) {
            mView.get().updateTrackingStatus(TrackingStatusEnum.TRACKING);
        }
        return false;
    }

    @Override
    public boolean onTrackingServiceStopped() {
        if (mView != null) {
            mView.get().updateTrackingStatus(TrackingStatusEnum.STOPPED);
        }
        return false;
    }

    @Override
    public void onLocationUpdated(MainModel.Split split) {
        if (mView != null) {
            mView.get().updateViewWithPace(split.getKmPerHour());
        }
    }
}
