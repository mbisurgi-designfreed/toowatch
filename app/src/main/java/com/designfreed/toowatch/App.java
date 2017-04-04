package com.designfreed.toowatch;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
