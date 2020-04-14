package com.aneagu.birthdaytracker.data.module;

import android.app.Application;

public class AppController extends Application {
    private AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .dbModule(new DbModule())
            .build();

    public AppComponent getAppComponent() {
        return appComponent;
    }
}