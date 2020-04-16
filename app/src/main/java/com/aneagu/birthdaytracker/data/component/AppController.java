package com.aneagu.birthdaytracker.data.component;

import android.app.Application;

import com.aneagu.birthdaytracker.data.module.AppModule;
import com.aneagu.birthdaytracker.data.module.DbModule;

public class AppController extends Application {
    private AppComponent appComponent = DaggerAppComponent.builder()
            .appModule(new AppModule(this))
            .dbModule(new DbModule())
            .build();

    public AppComponent getAppComponent() {
        return appComponent;
    }
}