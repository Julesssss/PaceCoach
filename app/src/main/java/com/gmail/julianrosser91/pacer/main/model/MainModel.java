package com.gmail.julianrosser91.pacer.main.model;

import android.os.Handler;

import com.gmail.julianrosser91.pacer.model.objects.Split;
import com.gmail.julianrosser91.pacer.main.MainInterfaces;

import java.util.Random;

public class MainModel implements MainInterfaces.ProvidedModelOps {

    // Presenter reference
    private MainInterfaces.RequiredPresenterOps mPresenter;
    private Handler mHandler;
    private Split lastSplit;

    private MainState mMainState = MainState.STOPPED;

    public MainModel(MainInterfaces.RequiredPresenterOps presenter) {
        this.mPresenter = presenter;
    }

    /**
     * Called by Presenter when View is destroyed
     * @param isChangingConfiguration   true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            stopTrackingService();
            mPresenter = null;
        }
    }

    public String getLastSplitPace() {
        if (lastSplit != null) {
            return lastSplit.getKmPerHour();
        } else {
            return new Split().getKmPerHour();
        }
    }

    public void updateState(MainState state) {
        mMainState = state;
    }

    public MainState getState() {
        return mMainState;
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
        this.lastSplit = split;
        mPresenter.onLocationUpdated(split);
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
