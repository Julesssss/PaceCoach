package com.gmail.julianrosser91.pacer.model.events;

import android.content.Intent;

public interface IntentCallback {
    void onIntentReceived(Intent chatIntent);

    void onSplitAdded();
}