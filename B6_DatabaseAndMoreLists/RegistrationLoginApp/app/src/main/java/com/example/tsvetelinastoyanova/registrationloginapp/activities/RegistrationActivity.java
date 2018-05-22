package com.example.tsvetelinastoyanova.registrationloginapp.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserPropertiesValidator;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserValidator;

import java.util.List;

public class RegistrationActivity extends AppCompatActivity {

    UserValidator userValidator = new UserPropertiesValidator();
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initButtonRegister();
    }

    private void initButtonRegister() {
        Button register = findViewById(R.id.register_btn);
        register.setOnClickListener(v -> register());
    }

    private void register() {
        if (isValidUsername() && isValidPassword() && isValidRepeatedPassword() && isValidEmail() && isValidFirstName() && isValidLastName() && isValidAge()) {
            registerIfUsernameIsAvailable();
        }
    }

    private boolean isValidUsername() {
        String username = getTextFromContainer(R.id.username_container);
        if (userValidator.isUsernameTooShort(username)) {
            printToast(getResources().getString(R.string.too_short_username));
            return false;
        } else if (userValidator.isUsernameTooLong(username)) {
            printToast(getResources().getString(R.string.too_long_username));
            return false;
        } else if (!userValidator.isUsernameValid(username)) {
            printToast(getResources().getString(R.string.not_valid_username));
            return false;
        }
        user.setUsername(username);
        return true;
    }

    private boolean isValidPassword() {
        String password = getTextFromContainer(R.id.password_container);
        if (userValidator.isPasswordTooShort(password)) {
            printToast(getResources().getString(R.string.too_short_password));
            return false;
        } else if (userValidator.isPasswordTooLong(password)) {
            printToast(getResources().getString(R.string.too_long_password));
            return false;
        } else if (!userValidator.isPasswordValid(password)) {
            printToast(getResources().getString(R.string.not_valid_password));
            return false;
        }
        user.setPassword(password);
        return true;
    }

    private boolean isValidRepeatedPassword() {
        String password = getTextFromContainer(R.id.password_container);
        String repeatPassword = getTextFromContainer(R.id.verify_password_container);
        if (!userValidator.isPasswordCorrectlyRepeated(password, repeatPassword)) {
            printToast(getResources().getString(R.string.not_correctly_repeated_password));
            return false;
        }
        return true;
    }

    private boolean isValidEmail() {
        String email = getTextFromContainer(R.id.email_container);
        if (!userValidator.isEmailValid(email)) {
            printToast(getResources().getString(R.string.not_valid_email));
            return false;
        }
        user.setEmail(email);
        return true;
    }

    private boolean isValidFirstName() {
        String firstName = getTextFromContainer(R.id.first_name_container);
        if (!userValidator.isFirstNameValid(firstName)) {
            printToast(getResources().getString(R.string.not_valid_firstname));
            return false;
        } else if (!userValidator.doesFirstNameStartWithCapitalLetter(firstName)) {
            printToast(getResources().getString(R.string.not_capital_letter_firstname));
            return false;
        }
        user.setFirstName(firstName);
        return true;
    }

    private boolean isValidLastName() {
        String lastName = getTextFromContainer(R.id.last_name_container);
        if (!userValidator.isLastNameValid(lastName)) {
            printToast(getResources().getString(R.string.not_valid_lastname));
            return false;
        }
        user.setLastName(lastName);
        return true;
    }

    private boolean isValidAge() {
        String ageText = getTextFromContainer(R.id.age_container);
        int age = 0;
        if (!ageText.isEmpty()) {
            age = Integer.parseInt(ageText);
        }
        if (userValidator.isTooYoung(age)) {
            printToast(getResources().getString(R.string.too_young_age));
            return false;
        } else if (userValidator.isTooOld(age)) {
            printToast(getResources().getString(R.string.too_old_age));
            return false;
        }
        user.setAge(age);
        return true;
    }

    private void registerIfUsernameIsAvailable() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "users").build();
            List<User> userWithThisUsername = db.userDao().getUserWithThisName(user.getUsername());

            runOnUiThread(() -> {
                if (userWithThisUsername.size() != 0) {
                    printToast(getResources().getString(R.string.existing_user));
                } else {
                    printToast(getResources().getString(R.string.successful_registration));
                    saveUserInDatabase(db);
                    startIntentLoginScreen();

                }
            });
        }).start();
    }

    private void saveUserInDatabase(AppDatabase db) {
        new Thread(() -> db.userDao().insertUser(user)).start();
    }

    private void startIntentLoginScreen() {
        Intent loginIntent = new Intent(this, LoginActivity.class);
        this.startActivity(loginIntent);
    }

    private String getTextFromContainer(int id) {
        TextInputLayout usernameContainer = findViewById(id);
        return usernameContainer.getEditText().getText().toString();
    }

    private void printToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
