package com.filip.safealert;

import android.content.Context;
import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {
    public static void salveazaContacte(Context ctx, String[] nrs) {
        SharedPreferences.Editor ed = ctx.getSharedPreferences("SafeAlertPrefs", Context.MODE_PRIVATE).edit();
        for (int i = 0; i < nrs.length; i++) ed.putString("nr" + (i + 1), nrs[i]);
        ed.apply();
    }

    public static List<String> incarcăContacte(Context ctx) {
        SharedPreferences p = ctx.getSharedPreferences("SafeAlertPrefs", Context.MODE_PRIVATE);
        List<String> lista = new ArrayList<>();
        for (int i = 1; i <= 5; i++) lista.add(p.getString("nr" + i, ""));
        return lista;
    }
}