package com.gmail.julianrosser91.pacer.main;

import android.content.Context;

import com.gmail.julianrosser91.pacer.model.TrackingStatusEnum;

/**
 * Holder interface that contains all interfaces
 * responsible to maintain communication between
 * Model View Presenter layers.
 */

public class MainInterfaces {

    /**
     * Required VIEW methods available to PRESENTER
     * PRESENTER to VIEW
     */
    interface RequiredViewOps {
        void updateViewWithPace(String pace);

        void updateTrackingStatus(TrackingStatusEnum status);

        Context getAppContext();

        Context getActivityContext();
    }


    /**
     * Operations offered to VIEW to communicate with PRESENTER
     * VIEW to PRESENTER
     */
    interface ProvidedPresenterOps {
        void clickStartTrackingButton();

        void clickStopTrackingButton();

        void setView(RequiredViewOps view);

        void onDestroy(boolean isChangingConfiguration);
    }

    /**
     * Required PRESENTER methods available to MODEL
     * MODEL to PRESENTER
     */
    interface RequiredPresenterOps {
        boolean onTrackingServiceStarted();

        boolean onTrackingServiceStopped();

        void onLocationUpdated(MainModel.Split split);

        Context getAppContext();
        Context getActivityContext();

    }

    /**
     * todo <-- LOCATION SERVICE --></-->
     * Operations offered to MODEL to communicate with PRESENTER
     * PRESENTER to MODEL
     */
    interface ProvidedModelOps {
        void startTrackingService();

        void stopTrackingService();

        void onDestroy(boolean isChangingConfiguration);
    }

}
