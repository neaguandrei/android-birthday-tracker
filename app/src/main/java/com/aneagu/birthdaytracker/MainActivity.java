package com.aneagu.birthdaytracker;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.aneagu.birthdaytracker.data.component.AppController;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.utils.Constants;
import com.aneagu.birthdaytracker.utils.DateUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.aneagu.birthdaytracker.utils.Constants.CHANNEL_ID;

public class MainActivity extends AppCompatActivity {

    @Inject
    BirthdayDao birthdayDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_birthdays, R.id.navigation_settings)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        ButterKnife.bind(this);
        ((AppController) getApplicationContext()).getAppComponent().inject(this);
        createNotificationChannel();
        showNotifications();
    }


    private void showNotifications() {
        if (isReminderStatusEnabled()) {
            CompletableFuture.supplyAsync(() -> birthdayDao.findAllExistingBirthdays())
                    .thenApply(birthdays -> {
                        List<String> namesSet = new ArrayList<>();
                        birthdays.forEach(birthday -> {
                            if (birthday.getDate().equals(DateUtils.fromDateToString(new Date()))) {
                                namesSet.add(birthday.getFullName());
                            }
                        });

                        return namesSet;
                    })
                    .thenAccept(strings -> {
                        if (strings.size() < 1) {
                            return;
                        }

                        StringBuilder stringBuilder = new StringBuilder();
                        strings.forEach(s -> {
                            stringBuilder.append(s);
                            if (strings.size() > 1 && strings.size() - 1 != strings.indexOf(s)) {
                                stringBuilder.append(", ");
                            }
                        });

                        String bigTextSecondPart = strings.size() == 1 ? " is celebrated today" : " are celebrated today!";
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                                .setContentTitle("Birthdays today")
                                .setContentText("Don't forget to say 'Happy Birthday!'")
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(stringBuilder.toString() + bigTextSecondPart))
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                .setAutoCancel(true);

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(new Random(1000).nextInt(), builder.build());
                    });
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private boolean isReminderStatusEnabled() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(Constants.PREFERENCES_REMINDER_STATUS_KEY, false);
    }

}
