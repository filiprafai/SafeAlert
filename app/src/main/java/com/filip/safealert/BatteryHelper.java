package com.filip.safealert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.BatteryManager;
import android.location.LocationManager;
import androidx.appcompat.app.AlertDialog;

public class BatteryHelper extends BroadcastReceiver {
    private boolean alerta10Trimisa = false;
    private boolean alerta1Trimisa = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        int pct = (level * 100) / scale;

        if (pct <= 10 && !alerta10Trimisa) {
            alerta10Trimisa = true;
            SmsHelper.trimiteSmsLaToti(ConfigManager.incarcăContacte(context), "Baterie 10%.");
            new AlertDialog.Builder(context)
                    .setTitle("Baterie Scazuta")
                    .setMessage("Activezi modul de economisire?")
                    .setPositiveButton("DA", (d, w) -> {
                        Intent i = new Intent(android.provider.Settings.ACTION_BATTERY_SAVER_SETTINGS);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }).show();
        }

        if (pct <= 1 && !alerta1Trimisa) {
            alerta1Trimisa = true;
            String loc = LocationHelper.obtineLinkLocatie((LocationManager) context.getSystemService(Context.LOCATION_SERVICE));
            SmsHelper.trimiteSmsLaToti(ConfigManager.incarcăContacte(context), "Baterie 1%! Locatie: " + loc);
        }
    }
}