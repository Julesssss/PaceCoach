package com.gmail.julianrosser91.pacer.main.model;

import android.widget.Toast;

import com.gmail.julianrosser91.pacer.Pacer;
import com.gmail.julianrosser91.pacer.data.events.LocationEvent;
import com.gmail.julianrosser91.pacer.data.model.Route;
import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;
import com.gmail.julianrosser91.pacer.data.services.TrackingService;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;
import com.gmail.julianrosser91.pacer.main.MainInterfaces.RequiredPresenterOps;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class MainModel implements MainInterfaces.ProvidedModelOps, Route.RouteUpdateListener {

    private RequiredPresenterOps mPresenter;
    private Route mRoute;

    private MainState mMainState = MainState.STOPPED;

    public MainModel(RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mRoute = new Route(this);
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
        mRoute.reset();
        Pacer.getRoutesDatabase().deleteTable();
    }

    @Override
    public void dumpGpsCoordinateLog() {
        Pacer.getRoutesDatabase().printDatabaseData();
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
    public void onLocationEvent(LocationEvent event) {
        mRoute.addLocation(event.getLocation());
        Toast.makeText(mPresenter.getAppContext(), event.getLocation().getLatitude() + " || " + event.getLocation().getLongitude(), Toast.LENGTH_SHORT).show();
    }

    // Callback from Route object
    @Override
    public void onRouteUpdated(RouteUpdate routeUpdate) {
        mPresenter.onRouteUpdated(routeUpdate);
    }

}
