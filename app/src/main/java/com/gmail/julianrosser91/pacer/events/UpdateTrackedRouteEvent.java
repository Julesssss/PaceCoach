package com.gmail.julianrosser91.pacer.events;

import com.gmail.julianrosser91.pacer.model.TrackedRoute;

public class UpdateTrackedRouteEvent {

    TrackedRoute trackedRoute;

    public UpdateTrackedRouteEvent(TrackedRoute trackedRoute) {
        this.trackedRoute = trackedRoute;
    }

    public TrackedRoute getTrackedRoute() {
        return trackedRoute;
    }
}
