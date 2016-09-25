package com.gmail.julianrosser91.pacer.main.model;

import android.widget.Toast;

import com.gmail.julianrosser91.pacer.Pacer;
import com.gmail.julianrosser91.pacer.data.events.RouteUpdateEvent;
import com.gmail.julianrosser91.pacer.data.model.Route;
import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;
import com.gmail.julianrosser91.pacer.data.services.TrackingService;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;
import com.gmail.julianrosser91.pacer.main.MainInterfaces.RequiredPresenterOps;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainModel implements MainInterfaces.ProvidedModelOps{

    public enum MainState {
        TRACKING,
        STOPPED
    }

    private RequiredPresenterOps mPresenter;
    private RouteUpdate lastRouteUpdate = RouteUpdate.getEmptyRouteUpdate();

    private MainState mMainState = MainState.STOPPED;

    public MainModel(RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        checkTrackingStatus();
        EventBus.getDefault().register(this);
    }

    /**
     * todo - If Service is still running, we should re-load current route data. ((DATABASE))
     */
    private void checkTrackingStatus() {
        if (TrackingService.getIsTracking()) {
            mMainState = MainState.TRACKING;
        }
    }

    public RouteUpdate getLastRouteUpdate() {
        return lastRouteUpdate;
    }

    public void updateState(MainState state) {
        mMainState = state;
    }

    public MainState getState() {
        return mMainState;
    }

    /**
     * Called by Presenter when View is destroyed
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe
    public void onLocationEvent(RouteUpdateEvent event) {
        lastRouteUpdate = event.getRouteUpdate();
        mPresenter.onRouteUpdated(lastRouteUpdate);
        Toast.makeText(mPresenter.getAppContext(), event.getRouteUpdate().getSpeed() + "km/s || " + event.getRouteUpdate().getDistanceKms() + "km", Toast.LENGTH_SHORT).show();
    }

}
