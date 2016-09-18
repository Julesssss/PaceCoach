package com.gmail.julianrosser91.pacer.model.events;

import com.gmail.julianrosser91.pacer.model.objects.TrackedRoute;

public class UpdateTrackedRouteEvent {

    TrackedRoute trackedRoute;

    public UpdateTrackedRouteEvent(TrackedRoute trackedRoute) {
        this.trackedRoute = trackedRoute;
    }

    public TrackedRoute getTrackedRoute() {
        return trackedRoute;
    }
}
