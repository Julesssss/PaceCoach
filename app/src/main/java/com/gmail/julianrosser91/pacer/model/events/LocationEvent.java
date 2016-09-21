package com.gmail.julianrosser91.pacer.model.events;

import android.location.Location;

public class LocationEvent {

    Location location;

    public LocationEvent(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
