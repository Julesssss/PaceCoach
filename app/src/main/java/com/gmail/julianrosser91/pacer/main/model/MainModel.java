package com.gmail.julianrosser91.pacer.main.model;

import android.os.Handler;

import com.gmail.julianrosser91.pacer.model.objects.Route;
import com.gmail.julianrosser91.pacer.model.objects.RouteUpdate;
import com.gmail.julianrosser91.pacer.model.objects.Split;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;

import java.util.Random;

public class MainModel implements MainInterfaces.ProvidedModelOps, Route.RouteUpdateListener {

    private MainInterfaces.RequiredPresenterOps mPresenter;
    private Handler mHandler;

    private Route mRoute;

    private MainState mMainState = MainState.STOPPED;

    public MainModel(MainInterfaces.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
        mRoute = new Route(this);
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
        if (!isChangingConfiguration) {
            stopTrackingService();
            mPresenter = null;
        }
    }

    @Override
    public void startTrackingService() {
        mPresenter.onTrackingServiceStarted();
        startReturningFakeLocationData();
    }

    @Override
    public void stopTrackingService() {
        mPresenter.onTrackingServiceStopped();
        stopRepeatingTask();
    }

    @Override
    public void onRouteUpdated(RouteUpdate routeUpdate) {
        mPresenter.onRouteUpdated(routeUpdate);
    }

    /**
     * Temporary method for supplying sample location data
     */
    private void startReturningFakeLocationData() {
        if (mHandler != null) {
            mHandler.removeCallbacks(fakePaceGenerater);
        } else {
            mHandler = new Handler();
        }
        startRepeatingTask();
    }

    private void generateFakePace() {
        int r = new Random().nextInt(20);
        double v = new Random().nextDouble() * 10;

        long meters = (long) (v + r);
        Split split = new Split(meters, 3);
        mRoute.addSplit(split);
    }

    void startRepeatingTask() {
        fakePaceGenerater.run();
    }

    void stopRepeatingTask() {
        if (mHandler != null) {
            mHandler.removeCallbacks(fakePaceGenerater);
        }
    }

    Runnable fakePaceGenerater = new Runnable() {
        @Override
        public void run() {
            try {
                generateFakePace();
            } finally {
                // 100% guarantee that this always happens, even if
                // your update method throws an exception
                mHandler.postDelayed(fakePaceGenerater, 3000);
            }
        }
    };

}
