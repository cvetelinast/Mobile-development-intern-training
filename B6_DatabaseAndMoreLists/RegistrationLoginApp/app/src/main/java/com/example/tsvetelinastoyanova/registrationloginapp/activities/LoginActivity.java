package com.example.tsvetelinastoyanova.registrationloginapp.activities;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tsvetelinastoyanova.registrationloginapp.R;
import com.example.tsvetelinastoyanova.registrationloginapp.database.AppDatabase;
import com.example.tsvetelinastoyanova.registrationloginapp.database.User;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.CustomWatcher;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserPropertiesValidator;
import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserValidator;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    UserValidator userValidator = new UserPropertiesValidator();
    User user = new User();
    ExecutorService notMainThread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initButtons();
        addEditTextChangeListeners();
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
        if (getMistakeFromUsername().isEmpty() && getMistakeFromPassword().isEmpty()) {
            loginIfUserExists();
        }
    }

    private void addEditTextChangeListeners() {
        final EditText username = findViewById(R.id.username_input);
        username.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromUsername();
                if (!result.isEmpty()) {
                    username.setError(result);
                } /*else {
                        user.setUsername(getTextFromContainer(R.id.username_container));
                    }*/
            }
        });

        final EditText password = findViewById(R.id.password_input);
        password.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromPassword();
                if (!result.isEmpty()) {
                    password.setError(result);
                }/* else {
                    user.setPassword(getTextFromContainer(R.id.password_container));
                }*/
            }
        });
    }

    private String getMistakeFromUsername() {
        String username = getTextFromContainer(R.id.username_container);
        if (userValidator.isUsernameTooShort(username)) {
            return getResources().getString(R.string.too_short_username);
        } else if (userValidator.isUsernameTooLong(username)) {
            return getResources().getString(R.string.too_long_username);
        } else if (!userValidator.isUsernameValid(username)) {
            return getResources().getString(R.string.not_valid_username);
        }
        user.setUsername(username);
        return "";
    }

    private String getMistakeFromPassword() {
        String password = getTextFromContainer(R.id.password_container);
        if (userValidator.isPasswordTooShort(password)) {
            return getResources().getString(R.string.too_short_password);
        } else if (userValidator.isPasswordTooLong(password)) {
            return getResources().getString(R.string.too_long_password);
        } else if (!userValidator.isPasswordValid(password)) {
            return getResources().getString(R.string.not_valid_password);
        }
        user.setPassword(password);
        return "";
    }

    public void loginIfUserExists() {
        notMainThread.execute(() -> {
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
        });
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
