package com.aneagu.birthdaytracker.data.module;


import android.app.Application;

import com.aneagu.birthdaytracker.data.repository.local.AppDatabase;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.ui.birthdays.NewBirthdayActivity;
import com.aneagu.birthdaytracker.ui.birthdays.BirthdaysFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, DbModule.class})
public interface AppComponent {

    void inject(NewBirthdayActivity target);

    void inject(BirthdaysFragment target);

    Application application();

    AppDatabase appDatabase();

    BirthdayDao birthdayDao();

}
