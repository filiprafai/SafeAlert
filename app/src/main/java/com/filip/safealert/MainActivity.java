package com.filip.safealert;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.hardware.*;
import android.location.LocationManager;
import android.net.Uri;
import android.os.*;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private boolean sosInCurs = false;
    private CountDownTimer sosCountdown;
    private BatteryHelper batteryHelper = new BatteryHelper();
    private ShortcutHelper shortcutHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CALL_PHONE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA
        }, 1);

        shortcutHelper = new ShortcutHelper(() -> declanseazaSOS(false));

        registerReceiver(batteryHelper, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_SCREEN_ON);
        registerReceiver(shortcutHelper, filter);

        TabLayout tabLayout = findViewById(R.id.TabLayout);
        final View lSOS = findViewById(R.id.layoutSOS), lMP = findViewById(R.id.layoutMP), lCON = findViewById(R.id.layoutContacte);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                lSOS.setVisibility(View.GONE); lMP.setVisibility(View.GONE); lCON.setVisibility(View.GONE);
                if (tab.getPosition() == 0) lSOS.setVisibility(View.VISIBLE);
                else if (tab.getPosition() == 1) lMP.setVisibility(View.VISIBLE);
                else if (tab.getPosition() == 2) lCON.setVisibility(View.VISIBLE);
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        final EditText e1 = findViewById(R.id.etNr1), e2 = findViewById(R.id.etNr2), e3 = findViewById(R.id.etNr3), e4 = findViewById(R.id.etNr4), e5 = findViewById(R.id.etNr5);
        List<String> nrs = ConfigManager.incarcăContacte(this);
        e1.setText(nrs.get(0)); e2.setText(nrs.get(1)); e3.setText(nrs.get(2)); e4.setText(nrs.get(3)); e5.setText(nrs.get(4));

        findViewById(R.id.btnSalveazaContacte).setOnClickListener(v -> {
            String[] numere = {e1.getText().toString(), e2.getText().toString(), e3.getText().toString(), e4.getText().toString(), e5.getText().toString()};
            ConfigManager.salveazaContacte(this, numere);
            Toast.makeText(this, "Salvat!", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.BSOS).setOnClickListener(v -> declanseazaSOS(false));
        startActivityForResult(VoiceHelper.obtineIntentAscultare(), 7);
    }

    private void declanseazaSOS(boolean inactiv) {
        if (!sosInCurs) {
            sosInCurs = true;
            LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            String link = LocationHelper.obtineLinkLocatie(lm);
            List<String> contacte = ConfigManager.incarcăContacte(this);
            SmsHelper.trimiteSmsLaToti(contacte, (inactiv ? "Inactiv! " : "SOS! ") + link);
            startActivity(new Intent(MediaStore.ACTION_VIDEO_CAPTURE));
            sosCountdown = new CountDownTimer(20000, 1000) {
                @Override public void onFinish() {
                    if (sosInCurs) {
                        List<String> nrs = ConfigManager.incarcăContacte(MainActivity.this);
                        if (!nrs.get(0).isEmpty()) {
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + nrs.get(0)));
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                startActivity(intent);
                            }
                        }
                    }
                }
                @Override public void onTick(long l) {}
            }.start();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 7 && resultCode == RESULT_OK && data != null) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && !matches.isEmpty() && matches.get(0).toLowerCase().contains("ajutor")) declanseazaSOS(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(batteryHelper);
            unregisterReceiver(shortcutHelper);
        } catch (Exception e) {}
    }
}