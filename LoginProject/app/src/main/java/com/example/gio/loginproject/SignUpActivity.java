package com.example.gio.loginproject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import model.SQLiteDB;
import model.User;

public class SignUpActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String FULL_NAME = "fullName";

    private final AppCompatActivity activity = SignUpActivity.this;

    private EditText userFirstName;
    private EditText userName;
    private EditText userEmail;
    private EditText userPassword;
    private View sProgressView;
    private View singUpFormView;
    private Intent intent = getIntent();
    private Snackbar mySnackbar;

    private SQLiteDB myDatabase = new SQLiteDB(activity);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        userFirstName = findViewById(R.id.userFirstName);
        userName = findViewById(R.id.userName);
        userEmail = findViewById(R.id.userEmail);
        userPassword = findViewById(R.id.userPassword);


        sProgressView = findViewById(R.id.sing_up_progress);
        singUpFormView = findViewById(R.id.form_sign_up_form);

        if (intent != null) {
            userEmail.setText(intent.getStringExtra(EMAIL));
        }


        Button registerButton = (Button) findViewById(R.id.register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptSignUp();

            }
        });

        Button cancel_button = (Button) findViewById(R.id.cancel_button);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void attemptSignUp() {

        // Reset errors.
        userFirstName.setError(null);
        userName.setError(null);
        userEmail.setError(null);
        userPassword.setError(null);

        // Store values at the time of the login attempt.
        String firstName = userFirstName.getText().toString();
        String name = userName.getText().toString();
        String email = userEmail.getText().toString();
        String password = userPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for first name field is not null
        if (TextUtils.isEmpty(firstName)) {
            userFirstName.setError(getString(R.string.error_field_required));
            focusView = userFirstName;
            cancel = true;
        }

        // Check for name field is not null
        if (TextUtils.isEmpty(name)) {
            userName.setError(getString(R.string.error_field_required));
            focusView = userName;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            userPassword.setError(getString(R.string.error_invalid_password));
            focusView = userPassword;
            cancel = true;

        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            userEmail.setError(getString(R.string.error_field_required));
            focusView = userEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            userEmail.setError(getString(R.string.error_invalid_email));
            focusView = userEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {

            //request of confirmation of add new user
            new AlertDialog.Builder(activity)
                    .setTitle("Are you sure ?")
                    .setMessage("Pressing yes will add user on database")
                    .setPositiveButton("yes",
                            new DialogInterface.OnClickListener() {
                                // when user confirms by clicking on yes we add user if does not exist
                                public void onClick(DialogInterface dialog, int which) {
                                    // Show a progress spinner, and kick off a background task to
                                    // perform the user login attempt.
                                    showProgress(true);
                                    User user = new User(firstName, name, email, password);
                                    if (!myDatabase.addUser(user)) {
                                        mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                                                R.string.error_email_allready_exist, Snackbar.LENGTH_SHORT);
                                        showProgress(false);
                                        mySnackbar.show();
                                    } else {
                                        mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout),
                                                R.string.success_message, Snackbar.LENGTH_SHORT);
                                        showProgress(false);
                                        mySnackbar.show();
                                    }
                                }
                            })
                    .setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(activity,
                                            SignUpActivity.class);
                                    startActivity(intent);
                                }
                            })
                    .create().show();

            Intent intent = new Intent(activity, WelcomeActivity.class);
            intent.putExtra(FULL_NAME, userFirstName.getText().toString() + " " + userName.getText().toString());
            startActivity(intent);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            singUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            singUpFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    singUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            sProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            sProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            singUpFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
