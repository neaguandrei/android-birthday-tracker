package com.aneagu.birthdaytracker.ui.auth;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aneagu.birthdaytracker.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextInputEditText tieEmail, tiePassword;
    private CheckBox cbRememberMe;

//    private Session session;
//    private FirebaseHelper firebaseHelper;

    private boolean isUserRemembered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        initializeComponents();
    }

//    private void initializeComponents() {
//        session = new Session(this);
//        dbHelper = new DatabaseHelper(this);
//        firebaseHelper = FirebaseHelper.getInstance();
//
////        tieEmail = findViewById(R.id.login_tie_email);
////        tiePassword = findViewById(R.id.login_tie_password);
////        progressBar = findViewById(R.id.progressbar);
////        TextView tvRegister = findViewById(R.id.textViewSignup);
////        Button btnLogin = findViewById(R.id.buttonLogin);
////        cbRememberMe = findViewById(R.id.login_cb_remember_me);
//
//        tvRegister.setOnClickListener(toRegisterEvent());
//        btnLogin.setOnClickListener(loginEvent());
//
//        checkIfUserIsAlreadyLoggedIn();
//    }

//    private void checkIfUserIsAlreadyLoggedIn() {
//        if (session.isUserLoggedIn()) {
//            startActivity(new Intent(LoginActivity.this, MainActivity.class));
//            finish();
//        }
//    }
//
//    @NonNull
//    private View.OnClickListener loginEvent() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (cbRememberMe.isChecked())
//                    isUserRemembered = true;
//                else
//                    isUserRemembered = false;
//                loginUser();
//            }
//        };
//    }
//
//    @NonNull
//    private View.OnClickListener toRegisterEvent() {
//        return new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
//            }
//        };
//    }

//    private void loginUser() {
//        if (isValid()) {
//            String email = tieEmail.getText().toString();
//            String password = tiePassword.getText().toString();
//
//            User loggedUser = new User(email, password);
//
//            if (verifyConnection()) {
//                firebaseLogin(loggedUser);
//            } else {
//                offlineLogin(loggedUser);
//            }
//        }
//    }

//    private void offlineLogin(User user) {
//        if (dbHelper.getUser(user)) {
//            if (isUserRemembered) {
//                session.createUserLoginSessionWithRemember(user);
//            } else {
//                session.createUserLoginSessionWithoutRemember(user);
//            }
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            intent.putExtra("rememberMe", isUserRemembered);
//            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            Toast.makeText(getApplicationContext(), "Authenticated in offline mode!", Toast.LENGTH_SHORT).show();
//            finish();
//        } else {
//            Toast.makeText(getApplicationContext(), "Failed to log in! You either never logged in on this phone before or information is wrong! Get connected to the internet and try again!", Toast.LENGTH_SHORT).show();
//        }
//    }

//    private void firebaseLogin(final User user) {
//        firebaseHelper.openConnection();
//        progressBar.setVisibility(View.VISIBLE);
//        firebaseHelper.getAuth().signInWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @SuppressLint("StaticFieldLeak")
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    if (dbHelper.getUser(user)) {
//                        if (isUserRemembered) {
//                            session.createUserLoginSessionWithRemember(user);
//                        } else {
//                            session.createUserLoginSessionWithoutRemember(user);
//                        }
//                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                        intent.putExtra("rememberMe", isUserRemembered);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                        Toast.makeText(getApplicationContext(), "Successfully logged in! Welcome!", Toast.LENGTH_SHORT).show();
//                        finish();
//                    } else if (!dbHelper.getUser(user)) {
//                        Log.d("Test", "Introducing data into local database " + firebaseHelper.getAuth().getCurrentUser().getUid());
//                        newDeviceLogin();
//                    }
//                } else {
//                    Toast.makeText(getApplicationContext(), "Wrong account information!", Toast.LENGTH_SHORT).show();
//                    progressBar.setVisibility(View.GONE);
//                }
//            }
//        });
//    }

//    // Functie apelata in cazul in care userul se logheaza pentru prima oara pe respectivul device.
//    private void newDeviceLogin() {
//        firebaseHelper.openConnection();
//        firebaseHelper.getUsersReference().child(firebaseHelper.getAuth().getCurrentUser().getUid())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) {
//                            User importedUser = dataSnapshot.getValue(User.class);
//                            //Preiau userul din Firebase
//                            dbHelper.insertUser(importedUser);
//                            // Il inserez. Daca reusesc sa inserez atunci il loghez
//                            if (dbHelper.getUser(importedUser)) {
//                                session.createUserLoginSessionWithRemember(importedUser);
//                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                                finish();
//                            } else {
//                                Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_SHORT).show();
//                            }
//                            syncDownUserDataFromFirebase();
//                        }
//                        progressBar.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//                        Log.e("ReportActivity", "Data is not available");
//                    }
//                });
//    }

//    private void syncDownUserDataFromFirebase() {
//        firebaseHelper.openConnection();
//        firebaseHelper.getUserNotesReference().orderByChild("idNote").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    Note note = data.getValue(Note.class);
//                    if (note != null && note.getUserToken().equals(dbHelper.getUserToken(session))) {
//                        dbHelper.insertNote(note, dbHelper.getUserToken(session));
//                        Log.d("Note sync: ", note.toString());
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("Note sync error: ", databaseError.toString());
//            }
//        });
//
//        firebaseHelper.getGooglePlacesHotelsReference().orderByChild("id").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                    final GoogleHotel googleHotel = data.getValue(GoogleHotel.class);
//                    if (googleHotel != null && googleHotel.getUserToken().equals(dbHelper.getUserToken(session))) {
//                        dbHelper.insertHotels(googleHotel, dbHelper.getUserToken(session));
//                        Log.d("GoogleHotel sync: ", googleHotel.toString());
//                        firebaseHelper.getGooglePlacesReviewsReference().orderByChild("id").addListenerForSingleValueEvent(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                for (DataSnapshot data : dataSnapshot.getChildren()) {
//                                    GoogleReview review = data.getValue(GoogleReview.class);
//                                    if (review != null && review.getHotelToken().equals(googleHotel.getHotelToken())) {
//                                        dbHelper.insertReview(review, googleHotel);
//                                        Log.d("Review sync: ", review.toString());
//                                    }
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError databaseError) {
//                                Log.e("Review sync error: ", databaseError.toString());
//                            }
//                        });
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Log.e("GoogleHotel error: ", databaseError.toString());
//            }
//        });
//    }

    public boolean isValid() {
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
//
//    public boolean verifyConnection() {
//        return ConnectionStatus.getInstance(getApplicationContext()).isOnline();
//    }

}
