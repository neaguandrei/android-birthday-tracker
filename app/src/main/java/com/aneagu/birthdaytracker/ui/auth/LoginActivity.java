package com.aneagu.birthdaytracker.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.aneagu.birthdaytracker.R;
import com.aneagu.birthdaytracker.data.module.AppController;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SIGN_IN_GOOGLE = 97;

    @BindView(R.id.login_tie_email)
    TextInputEditText tieEmail;

    @BindView(R.id.login_tie_password)
    TextInputEditText tiePassword;

    @BindView(R.id.progressbar)
    ProgressBar progressBar;

    @Inject
    FirebaseAuth firebaseAuth;

    @OnClick(R.id.buttonLogin)
    void onMailLogin() {
        if (isValid()) {
            authMail();
        }
    }

    @OnClick(R.id.butttonLoginGmail)
    void onGoogleLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        Intent signInIntent = GoogleSignIn.getClient(getApplicationContext(), gso).getSignInIntent();
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN_GOOGLE);
    }

    @OnClick(R.id.textViewSignup)
    void onSignUp() {
        finish();
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        ((AppController) getApplicationContext()).getAppComponent().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN_GOOGLE) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                authGoogle(account);
            } catch (ApiException e) {
                Log.e(LoginActivity.class.getName(), e.getLocalizedMessage());
            }
        }
    }

    private void authGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(),
                                firebaseUser.getEmail() + " authenticated!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }


    private void authMail() {
        String email = Objects.requireNonNull(tieEmail.getText()).toString();
        String password = Objects.requireNonNull(tiePassword.getText()).toString();

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                        Toast.makeText(getApplicationContext(),
                                firebaseUser.getEmail() + " authenticated!", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK, getIntent());
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                    progressBar.setVisibility(View.GONE);
                });
    }

    private boolean isValid() {
        if (tieEmail.getText() == null || tieEmail.getText().toString().trim().isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(tieEmail.getText().toString()).matches()) {
            Toast.makeText(getApplicationContext(), R.string.error_email_login, Toast.LENGTH_SHORT).show();
            tieEmail.setError(getString(R.string.profile_activity_email_error));
            tieEmail.requestFocus();
            return false;
        } else if (tiePassword.getText() == null || tiePassword.getText().toString().trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), R.string.date_format_error, Toast.LENGTH_SHORT).show();
            tiePassword.setError(getString(R.string.error_password_login));
            tiePassword.requestFocus();
            return false;
        }
        return true;
    }
}
