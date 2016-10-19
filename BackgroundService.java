package com.oney.gcm;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.os.Build;

import io.neson.react.notification.NotificationAttributes;
import android.os.Bundle;
import io.neson.react.notification.Notification;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.*;
import android.widget.Toast;
import android.content.Context;


public class BackgroundService extends Service {
    private static final String TAG = "BackgroundService";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Context context = getApplicationContext();
        Bundle bundle = intent.getBundleExtra("bundle");
        Object result = bundle.get("info");
        String data   = result.toString();
        String id     = bundle.get("google.message_id").toString();
        try{
            JSONObject jsonObj = new JSONObject(data);
            String message = jsonObj.getString("message");
            String subject = jsonObj.getString("subject");
            Integer notificationId = Integer.parseInt(id.substring(0, 10).replaceAll("[\\D]", ""));

            NotificationAttributes attributes = new NotificationAttributes();
            attributes.delayed = false;
            attributes.scheduled = false;
            attributes.autoClear = true;
            attributes.inboxStyle = false;
            attributes.priority = 2;
            attributes.sound = "default";
            attributes.smallIcon = "ic_launcher";
            attributes.message = message;
            attributes.subject = subject;
            attributes.tickerText = message;
            Notification notification = new Notification(this, notificationId, attributes);
            notification.create();
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();

        }catch (JSONException e) {
            Log.d(TAG, "Failed to get notificaction => :"+e);
        }  
        return START_NOT_STICKY;
    }
}