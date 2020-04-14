package com.aneagu.birthdaytracker.data.repository.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Birthday.class}, version = AppDatabase.VERSION)
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract BirthdayDao birthdayDao();
}