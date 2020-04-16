package com.aneagu.birthdaytracker.ui.settings;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.component.AppController;
import com.aneagu.birthdaytracker.data.repository.remote.BirthdayRepository;
import com.aneagu.birthdaytracker.data.repository.local.BirthdayDao;
import com.aneagu.birthdaytracker.data.repository.models.BirthdayDto;
import com.aneagu.birthdaytracker.data.repository.models.Mapper;
import com.aneagu.birthdaytracker.ui.auth.LoginActivity;
import com.aneagu.birthdaytracker.utils.ConnectionStatus;
import com.aneagu.birthdaytracker.utils.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {

    private static final int REQUEST_CODE_ADD = 0;

    @BindView(R.id.sign_in)
    TextView tvSignIn;

    @BindView(R.id.sync)
    TextView tvSync;

    @BindView(R.id.switch_enable_reminders)
    Switch switchReminder;

    @BindView(R.id.cake)
    ImageView imageCake;

    @Inject
    BirthdayDao birthdayDao;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    BirthdayRepository birthdaysRemoteRepository;

    private FirebaseUser firebaseUser;

    private ObjectAnimator objectAnimator;

    @OnCheckedChanged(R.id.switch_enable_reminders)
    void onSwitchChanged(Switch view, boolean checked) {
        view.setChecked(checked);
        setEnabledRemindersStatus(checked);
    }

    @OnClick(R.id.sign_in)
    void onSignIn() {
        if (!ConnectionStatus.verifyConnection(getContext())) {
            Toast.makeText(getContext(), "You need internet to authenticate!", Toast.LENGTH_SHORT).show();
            return;
        }

        signIn();
    }

    @OnClick(R.id.sync)
    void onSync() {
        if (firebaseAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "Authenticate to enable birthdays synchronization", Toast.LENGTH_SHORT).show();
        } else {
            if (!ConnectionStatus.verifyConnection(getContext())) {
                Toast.makeText(getContext(), "Synchronization can't take place right now!", Toast.LENGTH_SHORT).show();
                return;
            }

            imageCake.setVisibility(View.VISIBLE);
            objectAnimator.setDuration(1000);
            objectAnimator.start();
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                imageCake.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "Birthdays synchronized successfully!", Toast.LENGTH_SHORT).show();
            }, 1000);

            sync();
        }
    }

    private void sync() {
        CompletableFuture.supplyAsync(() -> birthdayDao.findAll())
                .thenAccept(birthdays -> {
                    birthdaysRemoteRepository.synchronizeData(birthdayDao, birthdays, firebaseUser.getEmail());
                });
    }

    @OnClick(R.id.share)
    void onShareBirthdays() {
        getBirthdaysList().thenAccept(sharedText -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, sharedText);
            startActivity(Intent.createChooser(intent, "Share"));
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, root);
        getEnabledReminderStatus();
        setUpAnimation();
        return root;
    }

    @Override
    public void onAttach(@NotNull Context context) {
        ((AppController) context.getApplicationContext()).getAppComponent().inject(this);
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMuttableSignInButton();
    }

    private void setUpMuttableSignInButton() {
        firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser != null) {
            tvSignIn.setText("Sign Out");
            tvSignIn.setOnClickListener(view -> {
                firebaseAuth.signOut();
                tvSignIn.setText("Sign In");
                tvSignIn.setOnClickListener(viewSignIn -> {
                    signIn();
                });
            });
        }
    }

    private void setUpAnimation() {
        objectAnimator = ObjectAnimator.ofFloat(imageCake, "rotation", 360);
    }

    private void signIn() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, REQUEST_CODE_ADD);
    }

    private CompletableFuture<String> getBirthdaysList() {
        StringBuilder stringBuilder = new StringBuilder();
        return CompletableFuture.supplyAsync(() -> birthdayDao.findAll())
                .thenApply(birthdays -> {
                    birthdays.forEach(birthday -> stringBuilder.append(birthday.toString()).append("\n"));
                    return stringBuilder.toString();
                });
    }

    private void setEnabledRemindersStatus(boolean checked) {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(Constants.PREFERENCES_REMINDER_STATUS_KEY, checked);
        editor.apply();
    }

    private void getEnabledReminderStatus() {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getPreferences(Context.MODE_PRIVATE);
        switchReminder.setChecked(sharedPreferences.getBoolean(Constants.PREFERENCES_REMINDER_STATUS_KEY, false));
    }
}
