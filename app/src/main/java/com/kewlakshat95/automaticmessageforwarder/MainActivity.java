package com.kewlakshat95.automaticmessageforwarder;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    static Switch smsForwardSwitch;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.kewlakshat95.automaticmessageforwarder.R.layout.activity_main);

        smsForwardSwitch = (Switch) findViewById(com.kewlakshat95.automaticmessageforwarder.R.id.sForwardSMS);

        sharedPreferences = getSharedPreferences("sms_forwarding", Context.MODE_PRIVATE);
        if (sharedPreferences.getBoolean("forward_sms", false)) {
            smsForwardSwitch.setChecked(true);
        } else {
            smsForwardSwitch.setChecked(false);
        }

        smsForwardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove("forward_sms");
                ComponentName receiver = new ComponentName(MainActivity.this, SMSListener.class);
                PackageManager packageManager = getPackageManager();
                if (b) {
                    packageManager.setComponentEnabledSetting(receiver, b ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    editor.putBoolean("forward_sms", true);
                    editor.apply();
                    smsForwardSwitch.setChecked(true);
                } else {
                    packageManager.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    editor.putBoolean("forward_sms", false);
                    editor.apply();
                    smsForwardSwitch.setChecked(false);
                }
            }
        });
    }
}
