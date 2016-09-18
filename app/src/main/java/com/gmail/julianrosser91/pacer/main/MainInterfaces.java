package com.gmail.julianrosser91.pacer.main;

import android.content.Context;

import com.gmail.julianrosser91.pacer.model.objects.Split;
import com.gmail.julianrosser91.pacer.main.model.MainState;

/**
 * Holder interface that contains all interfaces
 * responsible to maintain communication between
 * Model View Presenter layers.
 */

public class MainInterfaces {

    /**
     * Required VIEW methods available to PRESENTER
     * PRESENTER > VIEW
     */
    public interface RequiredViewOps {
        void updateViewWithPace(String pace);

        Context getAppContext();

        void updateTrackingStatus(MainState status);

        Context getActivityContext();
    }


    /**
     * Operations offered to VIEW to communicate with PRESENTER
     * VIEW > PRESENTER
     */
    public interface ProvidedPresenterOps {
        void clickStartTrackingButton();

        void clickStopTrackingButton();

        void setView(RequiredViewOps view);

        void onDestroy(boolean isChangingConfiguration);
    }

    /**
     * Required PRESENTER methods available to MODEL
     * MODEL > PRESENTER
     */
    public interface RequiredPresenterOps {
        void onTrackingServiceStarted();

        void onTrackingServiceStopped();

        void onLocationUpdated(Split split);

        Context getAppContext();

        Context getActivityContext();

    }

    /**
     * Operations offered to MODEL to communicate with PRESENTER
     * PRESENTER > MODEL
     */
    public interface ProvidedModelOps {
        void startTrackingService();

        void stopTrackingService();

        void onDestroy(boolean isChangingConfiguration);

        String getLastSplitPace();

        void updateState(MainState tracking);

        MainState getState();
    }

}
