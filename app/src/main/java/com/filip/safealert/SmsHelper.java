package com.filip.safealert;

import android.telephony.SmsManager;
import java.util.List;

public class SmsHelper {
    public static void trimiteSmsLaToti(List<String> numere, String mesaj) {
        SmsManager sms = SmsManager.getDefault();
        for (String nr : numere) {
            if (nr != null && !nr.isEmpty()) {
                sms.sendTextMessage(nr, null, mesaj, null, null);
            }
        }
    }
}