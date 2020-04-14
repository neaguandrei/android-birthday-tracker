package com.aneagu.birthdaytracker.data.module;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.room.Room;

import com.aneagu.birthdaytracker.data.models.Birthday;
import com.aneagu.birthdaytracker.data.repository.BirthdaysRemoteRepository;
import com.aneagu.birthdaytracker.data.repository.IBirthdaysRemoteRepository;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.repository.local.AppDatabase;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.CompletableFuture;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    public AppDatabase provideRoomDatabase(@NonNull Application application) {
        return Room.databaseBuilder(application, AppDatabase.class, "birthday-tracker-db").build();
    }

    @Provides
    @Singleton
    public BirthdayDao provideBirthdayDao(@NonNull AppDatabase appDatabase) {
        return appDatabase.birthdayDao();
    }

    @Provides
    @Singleton
    public IBirthdaysRemoteRepository provideBirthdaysRemoteRepository(Application application) {
        int playServicesStatus =
                GoogleApiAvailability
                        .getInstance()
                        .isGooglePlayServicesAvailable(application);
        if (playServicesStatus == ConnectionResult.SUCCESS) {
            DatabaseReference databaseReference =
                    FirebaseDatabase
                            .getInstance()
                            .getReference();
            return new BirthdaysRemoteRepository(databaseReference);
        } else {
            return getFailedRepository();
        }
    }

    private IBirthdaysRemoteRepository getFailedRepository() {
        return new IBirthdaysRemoteRepository() {
            @Override
            public void createBirthday(Birthday birthday, String currentMail) {
                throw new RuntimeException("FireBase not supported");
            }

            @Override
            public CompletableFuture getBirthdays() {
                throw new RuntimeException("FireBase not supported");
            }

            @Override
            public CompletableFuture updateBirthday(Birthday birthday) {
                throw new RuntimeException("FireBase not supported");
            }

            @Override
            public CompletableFuture deleteBirthday(Birthday birthday) {
                throw new RuntimeException("FireBase not supported");
            }
        };
    }
}
