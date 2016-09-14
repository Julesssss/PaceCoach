package com.gmail.julianrosser91.pacer.events;

import android.content.Intent;

import java.io.Serializable;

public interface IntentCallback {
    void onIntentReceived(Intent chatIntent);

    void onSplitAdded();
}