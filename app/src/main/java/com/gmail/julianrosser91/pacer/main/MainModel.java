package com.gmail.julianrosser91.pacer.main;

import android.os.Handler;

import com.gmail.julianrosser91.pacer.utils.PaceUtils;

import java.util.Random;

public class MainModel implements MainInterfaces.ProvidedModelOps {

    // Presenter reference
    private MainInterfaces.RequiredPresenterOps mPresenter;
    private Handler mHandler;

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
            mPresenter = null;
            stopTrackingService();
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

    /**
     * todo - Temporary method for supplying sample location data
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


    public class Split {

        long seconds;
        long meters;

        public Split(long meters, long seconds) {
            this.seconds = seconds;
            this.meters = meters;
        }

        public long getSeconds() {
            return seconds;
        }

        public long getMeters() {
            return meters;
        }

        public String getKmPerHour() {
            return "" + PaceUtils.getKmPerHour(seconds * 1000, meters) + " kmph";
        }

        public String getPace() {
            return meters / seconds + "/mps   //  " + seconds + " secs / " + meters + " meters";
        }
    }
}
