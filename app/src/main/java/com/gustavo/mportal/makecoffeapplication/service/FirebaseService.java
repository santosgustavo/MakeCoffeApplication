package com.gustavo.mportal.makecoffeapplication.service;

import android.util.Log;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

    Log.i(" SERVIFIREBASECE","Passei aqui");

        super.onMessageReceived(remoteMessage);
    }

    @Override
    public void onNewToken(String s) {

        Log.i("FIREBASE SERVICE","Token: " + s);

        super.onNewToken(s);
    }
}
