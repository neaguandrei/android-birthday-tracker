package com.aneagu.birthdaytracker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.component.AppController;
import com.aneagu.birthdaytracker.utils.ConnectionStatus;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends AppCompatActivity {
    @BindView(R.id.register_tie_email)
    TextInputEditText tieEmail;

    @BindView(R.id.register_tie_password)
    TextInputEditText tiePassword;

    @BindView(R.id.register_tie_confirm_password)
    TextInputEditText tieConfirmPassword;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Inject
    FirebaseAuth firebaseAuth;

    @OnClick(R.id.textViewLogin)
    void onReturnToLogin() {
        finish();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }

    @OnClick(R.id.buttonSignUp)
    void onSignUp() {
        registerUser();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        ((AppController) getApplicationContext()).getAppComponent().inject(this);
    }

    private void registerUser() {
        if (!ConnectionStatus.verifyConnection(getApplicationContext())) {
            Toast.makeText(getApplicationContext(), "You need internet to authenticate!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isValid()) {
            String email = Objects.requireNonNull(tieEmail.getText()).toString();
            String password = Objects.requireNonNull(tiePassword.getText()).toString();
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),
                                    firebaseUser.getEmail() + " authenticated!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        progressBar.setVisibility(View.GONE);
                    });
        }
    }

    public boolean isValid() {
        if (tieEmail.getText() == null || tieEmail.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(tieEmail.getText().toString()).matches()) {
            Toast.makeText(getApplicationContext(), R.string.error_email_login, Toast.LENGTH_SHORT).show();
            tieEmail.setError(getString(R.string.profile_activity_email_error));
            tieEmail.requestFocus();
            return false;
        } else if (tiePassword.getText() == null || tiePassword.getText().toString().trim().isEmpty() || tiePassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.signup_error_password, Toast.LENGTH_SHORT).show();
            tiePassword.setError(getString(R.string.signup_error_password));
            tiePassword.requestFocus();
            return false;
        } else if (!tiePassword.getText().toString().equals(tieConfirmPassword.getText().toString())) {
            Toast.makeText(getApplicationContext(), R.string.signup_error_password_match, Toast.LENGTH_SHORT).show();
            tieConfirmPassword.setError(getString(R.string.signup_error_password_match));
            tieConfirmPassword.requestFocus();
            return false;
        } else if (tieConfirmPassword.getText() == null || tieConfirmPassword.getText().toString().trim().isEmpty() || tieConfirmPassword.getText().toString().length() < 6) {
            Toast.makeText(getApplicationContext(), R.string.signup_error_password, Toast.LENGTH_SHORT).show();
            tieConfirmPassword.setError(getString(R.string.signup_error_password));
            tieConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }
}
