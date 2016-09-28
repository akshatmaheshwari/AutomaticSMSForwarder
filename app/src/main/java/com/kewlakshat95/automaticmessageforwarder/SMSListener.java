package com.kewlakshat95.automaticmessageforwarder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;

/**
 * Created by Akshat Maheshwari on 28-09-2016.
 */
public class SMSListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages;
        if (Build.VERSION.SDK_INT >= 19) {
            if (Telephony.Sms.Intents.SMS_RECEIVED_ACTION.equals(intent.getAction())) {
                messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
                for (SmsMessage message : messages) {
                    forwardSMS(context, message);
                }
            }
        } else {
            if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    try {
                        Object[] pdus = (Object[]) bundle.get("pdus");
                        if (pdus != null) {
                            messages = new SmsMessage[pdus.length];
                            for (int i = 0; i < messages.length; i++) {
                                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                forwardSMS(context, messages[i]);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private void forwardSMS(Context context, SmsMessage message) {
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);
        smsIntent.setData(Uri.parse("smsto:"));
        smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", "9972036424");
        smsIntent.putExtra("sms_body", message.getMessageBody());
        smsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(smsIntent);
    }
}
