package com.gmail.julianrosser91.pacer.mvp;

import com.tinmegali.mvp.mvp.GenericModel;

public class MainModel extends GenericModel<MVPInterfaces.RequiredPresenterOps>
        implements MVPInterfaces.ProvidedModelOps {

    /**
     * Method that recovers a reference to the PRESENTER
     * - You must ALWAYS call {@link super#onCreate(Object)} here
     *
     * @param presenterOps Presenter interface
     */
    @Override
    public void onCreate(MVPInterfaces.RequiredPresenterOps presenterOps) {
        super.onCreate(presenterOps);
        // initialize objects
    }

    /**
     * Called by layer PRESENTER when VIEW pass for a reconstruction/destruction.
     * Usefull for kill/stop activities that could be running on the background
     * Threads
     *
     * @param isChangingConfiguration Informs that a change is occurring on configuration
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // kill or stop actions
    }

    @Override
    public boolean startTrackingService() {
        return false;
    }

    @Override
    public boolean stopTrackingService() {
        return false;
    }
}
