package com.gmail.julianrosser91.pacer.mvp;

import com.tinmegali.mvp.mvp.ActivityView;
import com.tinmegali.mvp.mvp.ModelOps;
import com.tinmegali.mvp.mvp.PresenterOps;

public class MVPInterfaces {

    /**
     * Required VIEW methods available to PRESENTER
     *      PRESENTER to VIEW
     */
    interface RequiredViewOps extends ActivityView {
        void updateViewWithPace();
    }


    /**
     * Operations offered to VIEW to communicate with PRESENTER
     *      VIEW to PRESENTER
     */
    interface ProvidedPresenterOps extends PresenterOps<RequiredViewOps> {
        void clickStartTrackingButton();
        void clickStopTrackingButton();
    }

    /**
     * Required PRESENTER methods available to MODEL
     *      MODEL to PRESENTER
     */
    interface RequiredPresenterOps {
        boolean onTrackingServiceStarted();
        boolean onTrackingServiceStopped();
        boolean onLocationUpdated();

    }

    /**
     * todo <-- LOCATION SERVICE --></-->
     * Operations offered to MODEL to communicate with PRESENTER
     *      PRESENTER to MODEL
     */
    interface ProvidedModelOps extends ModelOps<RequiredPresenterOps> {
        boolean startTrackingService();
        boolean stopTrackingService();
    }

}
