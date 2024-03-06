package com.roseno.curbcrime.util;

import android.telephony.SmsManager;

public class MessageSender {
    private final String TAG = "MessageSender";

    /**
     * 메시지 전송
     * @param target        전송 대상
     * @param message       메시지
     */
    public static void sendMessage(String target, String message) {
        SmsManager smsManager = SmsManager.getDefault();

        smsManager.sendTextMessage(target, null, message, null, null);
    }
}
