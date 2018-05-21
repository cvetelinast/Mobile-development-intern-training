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

public class LoginActivity extends AppCompatActivity {

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initButtons();
    }

    private void initButtons() {
        Button register = findViewById(R.id.register_btn);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterScreen();
            }
        });

        Button login = findViewById(R.id.login_btn);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeUser();
                openLoginScreen();
            }
        });
    }

    private void openRegisterScreen() {
        Intent registerIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
        LoginActivity.this.startActivity(registerIntent);
    }

    private void openLoginScreen() {
        if (validUsername() && validPassword()){
            loginIfUsernameExists();
        }
    }

    private boolean validUsername() {
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

    private boolean validPassword() {
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

    private void initializeUser() {
        TextInputLayout usernameContainer = (TextInputLayout) findViewById(R.id.username_container);
        String username = usernameContainer.getEditText().getText().toString();
        Log.d("tag", username);
        TextInputLayout passwordContainer = (TextInputLayout) findViewById(R.id.password_container);
        String password = passwordContainer.getEditText().getText().toString();
        Log.d("tag", password);
        user = new User(username, password);
    }

    public void loginIfUsernameExists() {
        new Thread(() -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "users").build();
            List<User> userWithThisUsername = db.userDao().getUserWithThisName(user.getUsername());

            runOnUiThread(() -> {
                if (userWithThisUsername.size() == 0) {
                    final CharSequence text = getResources().getString(R.string.not_existing_user);
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                } else if (!userWithThisUsername.get(0).getPassword().equals(user.getPassword())) {
                    final CharSequence text = getResources().getString(R.string.wrong_password);
                    Toast.makeText(this, text, Toast.LENGTH_LONG).show();
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
}
