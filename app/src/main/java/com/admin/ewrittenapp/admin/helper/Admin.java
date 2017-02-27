package com.admin.ewrittenapp.admin.helper;

import android.app.Application;

import com.firebase.client.Firebase;
import com.google.firebase.FirebaseApp;

/**
 * Created by Shahrukh Mansuri on 2/27/2017.
 */

public class Admin extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Firebase.setAndroidContext(this);
    }
}

