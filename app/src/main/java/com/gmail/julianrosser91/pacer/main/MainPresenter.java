package com.gmail.julianrosser91.pacer.main;

import android.content.Context;
import android.util.Log;

import com.gmail.julianrosser91.pacer.basicmpv.MVP_Main;
import com.gmail.julianrosser91.pacer.model.TrackingStatusEnum;

import java.lang.ref.WeakReference;

public class MainPresenter implements MainInterfaces.RequiredPresenterOps,
        MainInterfaces.ProvidedPresenterOps {

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MainInterfaces.RequiredViewOps> mView;
    // Model reference
    private MainInterfaces.ProvidedModelOps mModel;

    public MainPresenter(MainInterfaces.RequiredViewOps mView) {
        this.mView = new WeakReference<>(mView);
    }

    /**
     * Called by View every time it is destroyed.
     * @param isChangingConfiguration   true: is changing configuration
     *                                  and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View show be null every time onDestroy is called
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if ( !isChangingConfiguration ) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }

    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @return
     * @throws NullPointerException when View is unavailable
     */
    private MainInterfaces.RequiredViewOps getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     * @param view  Activity instance
     */
    @Override
    public void setView(MainInterfaces.RequiredViewOps view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by Activity during MVP setup. Only called once.
     * @param model Model instance
     */
    public void setModel(MainInterfaces.ProvidedModelOps model) {
        mModel = model;
    }

    /**
     * Retrieve Application Context
     * @return  Application context
     */
    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Retrieves Activity context
     * @return  Activity context
     */
    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
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