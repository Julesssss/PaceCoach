package com.gmail.julianrosser91.pacer.data.events;

import com.gmail.julianrosser91.pacer.data.model.RouteUpdate;

public class RouteUpdateEvent {

    private RouteUpdate routeUpdate;

    public RouteUpdateEvent(RouteUpdate routeUpdate) {
        this.routeUpdate = routeUpdate;
    }

    public RouteUpdate getRouteUpdate() {
        return routeUpdate;
    }
}
