package com.filip.safealert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShortcutHelper extends BroadcastReceiver {
    private int shortcutCount = 0;
    private long lastShortcutTime = 0;
    private final ShortcutListener listener;

    public interface ShortcutListener {
        void onShortcutTriggered();
    }

    public ShortcutHelper(ShortcutListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        long now = System.currentTimeMillis();
        if (now - lastShortcutTime < 1500) {
            shortcutCount++;
        } else {
            shortcutCount = 1;
        }
        lastShortcutTime = now;

        if (shortcutCount == 2) {
            shortcutCount = 0;
            if (listener != null) listener.onShortcutTriggered();
        }
    }
}