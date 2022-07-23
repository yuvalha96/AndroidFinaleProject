package com.example.finaleProject;

import android.app.Activity;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private MainViewModel myViewModel;

    @Override
    public void onReceive(Context context, Intent intent) {
        SmsMessage[] messages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        SmsMessage message = messages[0];
        if(message != null)
        {
            String sender = message.getDisplayOriginatingAddress();
            String body = message.getDisplayMessageBody();
            myViewModel = MainViewModel.getInstance((Application)context.getApplicationContext(), context, null);
            myViewModel.addNewSMSNote(sender, body, new Date(System.currentTimeMillis()));
        }
    }
}