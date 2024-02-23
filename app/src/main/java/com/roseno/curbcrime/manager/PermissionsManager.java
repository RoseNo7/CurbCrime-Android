package com.roseno.curbcrime.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManager {
    private final String TAG = "PermissionManager";

    public static final int PERMISSIONS_REQUEST_CODE = 1;

    /**
     * 권한 목록 가져오기
     * TODO: 필요한 권한에 따라 추가
     * @return      권한 목록
     */
    private static String[] getPermissions() {
        return new String[]{};
    }

    /**
     * 권한 요청
     * @param activity
     */
    public static void requestPermissions(Activity activity) {
        String[] permissions = getPermissions();

        ActivityCompat.requestPermissions(activity, permissions, PERMISSIONS_REQUEST_CODE);
    }

    /**
     * 권한 여부 확인
     * @param context
     * @return      권한 여부
     */
    public static boolean hasPermissions(Context context) {
        boolean isPermissionsGranted = true;

        String[] permissions = getPermissions() ;

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_DENIED) {
                isPermissionsGranted = false;
                break;
            }
        }

        return isPermissionsGranted;
    }

    /**
     * 권한 요청을 위해, 권한 설정 화면으로 이동
     * @param context
     */
    public static void requestPermissionsThroughSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);

        Toast.makeText(context, "권한을 허용해주세요.", Toast.LENGTH_SHORT).show();

        context.startActivity(intent);
    }
}
