package com.aneagu.birthdaytracker.data.repository.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.aneagu.birthdaytracker.data.repository.models.Birthday;

@Database(entities = {Birthday.class}, version = AppDatabase.VERSION, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract BirthdayDao birthdayDao();
}