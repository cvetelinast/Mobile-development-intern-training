package com.example.tsvetelinastoyanova.registrationloginapp.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initButtonRegister();
    }

    private void initButtonRegister() {
        Button register = findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        if(initializeUserSuccessfully() && isValidUsername() && isValidPassword() && isValidRepeatedPassword() && isValidEmail() && isValidFirstName() && isValidLastName() && isValidAge()) {
            registerIfUsernameIsAvailable();
        }
    }

    private boolean isValidUsername() {
        if (user.isUsernameTooShort()) {
            final CharSequence text = getResources().getString(R.string.too_short_username);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (user.isUsernameTooLong()) {
            final CharSequence text = getResources().getString(R.string.too_long_username);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (!user.isUsernameValid()) {
            final CharSequence text = getResources().getString(R.string.not_valid_username);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidPassword() {
        if (user.isPasswordTooShort()) {
            final CharSequence text = getResources().getString(R.string.too_short_password);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (user.isPasswordTooLong()) {
            final CharSequence text = getResources().getString(R.string.too_long_password);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (!user.isPasswordValid()) {
            final CharSequence text = getResources().getString(R.string.not_valid_password);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidRepeatedPassword() {
        String repeatPassword = getTextFromContainer(R.id.verify_password_container);
        if (!user.isPasswordCorrectlyRepeated(repeatPassword)) {
            final CharSequence text = getResources().getString(R.string.not_correctly_repeated_password);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean initializeUserSuccessfully() {
        String username = getTextFromContainer(R.id.username_container);
        String password = getTextFromContainer(R.id.password_container);
        String email = getTextFromContainer(R.id.email_container);
        String firstName = getTextFromContainer(R.id.first_name_container);
        String lastName = getTextFromContainer(R.id.last_name_container);
        String age = getTextFromContainer(R.id.age_container);

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || age.isEmpty()) {
            final CharSequence text = getResources().getString(R.string.fill_all_fields_prompt);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        int ageNumber = Integer.parseInt(age);
        user = new User(username, password, email, firstName, lastName, ageNumber);
        return true;

    }

    private String getTextFromContainer(int id) {
        TextInputLayout usernameContainer = (TextInputLayout) findViewById(id);
        return usernameContainer.getEditText().getText().toString();
    }

    private boolean isValidEmail() {
        if (!user.isEmailValid()) {
            final CharSequence text = getResources().getString(R.string.not_valid_email);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    private boolean isValidFirstName() {
        if (!user.isFirstNameValid()) {
            final CharSequence text = getResources().getString(R.string.not_valid_firstname);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (!user.doesFirstNameStartWithCapitalLetter()) {
            final CharSequence text = getResources().getString(R.string.not_capital_letter_firstname);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidLastName() {
        if (!user.isLastNameValid()) {
            final CharSequence text = getResources().getString(R.string.not_valid_lastname);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean isValidAge() {
        if (user.isTooYoung()) {
            final CharSequence text = getResources().getString(R.string.too_young_age);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        } else if (user.isTooOld()) {
            final CharSequence text = getResources().getString(R.string.too_old_age);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    public void registerIfUsernameIsAvailable() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "users").build();
            List<User> userWithThisUsername = db.userDao().getUserWithThisName(user.getUsername());

            runOnUiThread(() -> {
                if (userWithThisUsername.size() != 0) {
                    final CharSequence text = getResources().getString(R.string.existing_user);
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                } else {
                    final CharSequence text = getResources().getString(R.string.successful_registration);
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                    saveUserInDatabase(db);
                    startIntentLoginScreen();

                }
            });
        }).start();
    }

    private void saveUserInDatabase(AppDatabase db) {
        new Thread(() -> {
            db.userDao().insertUser(user);
        }).start();
    }

    private void startIntentLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        this.startActivity(loginIntent);
    }
}
