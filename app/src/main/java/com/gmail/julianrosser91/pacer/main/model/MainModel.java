package com.gmail.julianrosser91.pacer.main.model;

import android.os.Handler;
import android.widget.Toast;

import com.gmail.julianrosser91.pacer.model.events.LocationEvent;
import com.gmail.julianrosser91.pacer.model.objects.Route;
import com.gmail.julianrosser91.pacer.model.objects.RouteUpdate;
import com.gmail.julianrosser91.pacer.model.objects.Split;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Random;

public class MainModel implements MainInterfaces.ProvidedModelOps, Route.RouteUpdateListener {

    private MainInterfaces.RequiredPresenterOps mPresenter;
    private Handler mHandler;

    private Route mRoute;

    private MainState mMainState = MainState.STOPPED;

    public MainModel(MainInterfaces.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mRoute = new Route(this);
        EventBus.getDefault().register(this);
    }

    public RouteUpdate getLastRouteUpdate() {
        return mRoute.getLastRouteUpdate();
    }

    public void updateState(MainState state) {
        mMainState = state;
    }

    public MainState getState() {
        return mMainState;
    }

    @Override
    public void resetRoute() {
        stopTrackingService();
        mRoute.reset();
    }

    /**
     * Called by Presenter when View is destroyed
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        EventBus.getDefault().unregister(this);
        if (!isChangingConfiguration) {
            stopTrackingService();
            mPresenter = null;
        }
    }

    @Override
    public void startTrackingService() {
        mPresenter.onTrackingServiceStarted();
//        startReturningFakeLocationData();
    }

    @Override
    public void stopTrackingService() {
        mPresenter.onTrackingServiceStopped();
//        stopRepeatingTask();
    }

    @Subscribe
    public void onLocationEvent(LocationEvent event) {
        mRoute.addLocation(event.getLocation());
        Toast.makeText(mPresenter.getAppContext(), "Location: " + event.getLocation().getLongitude(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRouteUpdated(RouteUpdate routeUpdate) {
        mPresenter.onRouteUpdated(routeUpdate);
    }

}
