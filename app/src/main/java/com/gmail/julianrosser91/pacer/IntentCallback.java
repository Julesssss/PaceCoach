package com.gmail.julianrosser91.pacer;

import android.content.Intent;

import java.io.Serializable;

public interface IntentCallback {
    void onIntentReceived(Intent chatIntent);
}