package com.aneagu.birthdaytracker.data.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.aneagu.birthdaytracker.data.repository.models.Birthday;
import com.aneagu.birthdaytracker.data.repository.remote.BirthdayRepository;
import com.aneagu.birthdaytracker.data.repository.remote.BirthdayRepositoryImpl;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.repository.local.AppDatabase;
import com.aneagu.birthdaytracker.data.repository.models.BirthdayDto;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    public AppDatabase provideRoomDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "birthday-tracker-db").allowMainThreadQueries().build();
    }

    @Provides
    @Singleton
    public BirthdayDao provideBirthdayDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.birthdayDao();
    }

    @Provides
    @Singleton
    public FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    public BirthdayRepository provideBirthdaysRemoteRepository(@NonNull Application application) {
        int playServicesStatus =
                GoogleApiAvailability
                        .getInstance()
                        .isGooglePlayServicesAvailable(application);
        if (playServicesStatus == ConnectionResult.SUCCESS) {
            DatabaseReference databaseReference =
                    FirebaseDatabase
                            .getInstance().getReference("birthdays");
            return new BirthdayRepositoryImpl(databaseReference);
        } else {
            return getFailedRepository();
        }
    }

    private BirthdayRepository getFailedRepository() {
        return (daoReference, localBirthdays, currentMail) -> {
            throw new RuntimeException("Phone version doesn't support remote database!");
        };
    }
}
