package com.gmail.julianrosser91.pacer.main;

import android.content.Context;

import com.gmail.julianrosser91.pacer.model.objects.RouteUpdate;
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
        Context getAppContext();

        void updateTrackingStatus(MainState status);

        Context getActivityContext();

        void updateRouteInfo(RouteUpdate routeUpdate);
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

        Context getAppContext();

        Context getActivityContext();

        void onRouteUpdated(RouteUpdate routeUpdate);
    }

    /**
     * Operations offered to MODEL to communicate with PRESENTER
     * PRESENTER > MODEL
     */
    public interface ProvidedModelOps {
        void startTrackingService();

        void stopTrackingService();

        void onDestroy(boolean isChangingConfiguration);

        RouteUpdate getLastRouteUpdate();

        void updateState(MainState tracking);

        MainState getState();
    }

}
