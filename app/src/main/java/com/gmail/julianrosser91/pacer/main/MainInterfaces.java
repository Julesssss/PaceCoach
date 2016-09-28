package com.gmail.julianrosser91.pacer.main;

import android.content.Context;
import android.content.Intent;

import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;
import com.gmail.julianrosser91.pacer.main.model.MainModel;
import com.gmail.julianrosser91.pacer.main.model.MainModel.MainState;

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

        void startActivityIntent(Intent activityIntent);

        void startServiceIntent(Intent serviceIntent);

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

        void dumpGpsOptionSelected();

        void resetRouteOptionSelected();
    }

    /**
     * Required PRESENTER methods available to MODEL
     * MODEL > PRESENTER
     */
    public interface RequiredPresenterOps {
        Context getAppContext();

        Context getActivityContext();

        void onRouteUpdated(RouteUpdate routeUpdate);
    }

    /**
     * Operations offered to MODEL to communicate with PRESENTER
     * PRESENTER > MODEL
     */
    public interface ProvidedModelOps {
        void onDestroy(boolean isChangingConfiguration);

        RouteUpdate getLastRouteUpdate();

        void updateState(MainState tracking);

        MainState getState();
    }

}
