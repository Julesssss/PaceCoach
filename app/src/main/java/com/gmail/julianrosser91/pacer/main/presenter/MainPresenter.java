package com.gmail.julianrosser91.pacer.main.presenter;

import android.content.Context;
import android.content.Intent;

import com.gmail.julianrosser91.pacer.main.MainInterfaces;
import com.gmail.julianrosser91.pacer.main.model.MainState;
import com.gmail.julianrosser91.pacer.data.events.StopServiceEvent;
import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;
import com.gmail.julianrosser91.pacer.data.services.TrackingService;
import com.gmail.julianrosser91.pacer.settings.SettingsActivity;
import com.gmail.julianrosser91.pacer.utils.PermissionHelper;

import org.greenrobot.eventbus.EventBus;

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
        updateViewState();
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
    public void onRouteUpdated(RouteUpdate routeUpdate) {
        if (mView != null && mView.get() != null) {
            mView.get().updateRouteInfo(routeUpdate);
        }
    }

    @Override
    public void clickStartTrackingButton() {
        if (PermissionHelper.isLocationPermissionEnabled(getAppContext())) {
            mView.get().startServiceIntent(new Intent(mView.get().getActivityContext(), TrackingService.class));
            mModel.updateState(MainState.TRACKING);
            updateViewState();
        } else {
            PermissionHelper.askForLocationPermission(getActivityContext());
        }
    }

    @Override
    public void clickStopTrackingButton() {
        EventBus.getDefault().post(new StopServiceEvent());
        mModel.updateState(MainState.STOPPED);
        updateViewState();
    }

    @Override
    public void dumpGpsOptionSelected() {
        mModel.dumpGpsCoordinateLog();
    }

    @Override
    public void resetRouteOptionSelected() {
        EventBus.getDefault().post(new StopServiceEvent());
        mModel.updateState(MainState.STOPPED);
        mModel.resetRoute();
        updateViewState();
    }

    public void updateViewState() {
        // Press back and re-open. Service still running. FInd instance? Or stop and re-start????
        if (mView != null && mView.get() != null && mModel != null) {
            mView.get().updateTrackingStatus(mModel.getState());
            mView.get().updateRouteInfo(mModel.getLastRouteUpdate());
        }
    }

    public void settingsButtonPressed() {
        Intent settingsIntent = new Intent(getActivityContext(), SettingsActivity.class);
        mView.get().startActivityIntent(settingsIntent);
    }
}
