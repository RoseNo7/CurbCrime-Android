package com.roseno.curbcrime.listener;

import android.location.Location;

public interface LocationDetectListener {
    void onDetect(Location location);
    void onFailure();
}
