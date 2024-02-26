package com.roseno.curbcrime.manager;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;

public class ServiceManager {
    private static final String TAG = "ServiceManager";

    /**
     * 서비스 실행 여부 확인
     * @param context
     * @param service
     * @return
     */
    public static boolean isRunning(final Context context, Class<? extends Service> service) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String serviceName = service.getName();

        for (ActivityManager.RunningServiceInfo serviceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceInfo.service.getClassName().equals(serviceName)) {
                return true;
            }
        }

        return false;
    }
}
