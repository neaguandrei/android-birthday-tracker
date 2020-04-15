package com.aneagu.birthdaytracker.data.module;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import static com.aneagu.birthdaytracker.utils.Constants.CHANNEL_ID;

public class AppController extends Application {
    private AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .dbModule(new DbModule())
            .build();

    public AppComponent getAppComponent() {
        return appComponent;
    }
}