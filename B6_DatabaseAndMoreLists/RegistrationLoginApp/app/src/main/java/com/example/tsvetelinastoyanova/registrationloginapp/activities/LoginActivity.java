package com.example.tsvetelinastoyanova.registrationloginapp.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserPropertiesValidator;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserValidator;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    UserValidator userValidator = new UserPropertiesValidator();
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initButtons();
    }

    private void initButtons() {
        Button register = findViewById(R.id.register_btn);
        register.setOnClickListener(v -> openRegisterScreen());

        Button login = findViewById(R.id.login_btn);
        login.setOnClickListener(v -> openLoginScreen());
    }

    private void openRegisterScreen() {
        Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    private void openLoginScreen() {
        if (setUsernameIfValid() && setPasswordIfValid()) {
            loginIfUsernameExists();
        }
    }

    private boolean setUsernameIfValid() {
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

    private boolean setPasswordIfValid() {
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

    public void loginIfUsernameExists() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "users").build();
            List<User> userWithThisUsername = db.userDao().getUserWithThisName(user.getUsername());

            runOnUiThread(() -> {
                if (userWithThisUsername.size() == 0) {
                    printToast(getResources().getString(R.string.not_existing_user));
                } else if (!userWithThisUsername.get(0).getPassword().equals(user.getPassword())) {
                    printToast(getResources().getString(R.string.wrong_password));
                } else {
                    startIntentUsersListScreen();
                }
            });
        }).start();
    }

    private void startIntentUsersListScreen() {
        Intent usersListIntent = new Intent(this, UsersListActivity.class);
        this.startActivity(usersListIntent);
    }

    private void printToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }

    private String getTextFromContainer(int id) {
        TextInputLayout usernameContainer = findViewById(id);
        return usernameContainer.getEditText().getText().toString();
    }
}
