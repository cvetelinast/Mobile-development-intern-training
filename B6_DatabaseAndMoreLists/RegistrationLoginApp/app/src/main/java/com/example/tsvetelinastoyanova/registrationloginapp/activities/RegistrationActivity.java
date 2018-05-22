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

public class RegistrationActivity extends AppCompatActivity {

    UserValidator userValidator = new UserPropertiesValidator();
    User user = new User();
    ExecutorService notMainThread = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initButtonRegister();
        addEditTextChangeListeners();
    }

    private void initButtonRegister() {
        Button register = findViewById(R.id.register_btn);
        register.setOnClickListener(v -> register());
    }

    private void register() {
        if (getMistakeFromUsername().isEmpty() && getMistakeFromPassword().isEmpty() && getMistakeFromValidatedPassword().isEmpty()
                && getMistakeFromEmail().isEmpty() && getMistakeFromFirstName().isEmpty() && getMistakeFromLastName().isEmpty() && getMistakeFromAge().isEmpty()) {
            registerIfUsernameIsAvailable();
        }
    }

    private void addEditTextChangeListeners() {
       /* final EditText username = findViewById(R.id.username_input);
        username.addTextChangedListener(new CustomWatcher(username, getMistakeFromUsername()));

        final EditText password = findViewById(R.id.password_input);
        password.addTextChangedListener(new CustomWatcher(password, getMistakeFromPassword()));*/

        final EditText username = findViewById(R.id.username_input);
        username.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromUsername();
                if (!result.isEmpty()) {
                    username.setError(result);
                }
            }
        });

        final EditText password = findViewById(R.id.password_input);
        password.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromPassword();
                if (!result.isEmpty()) {
                    password.setError(result);
                }
            }
        });

        final EditText verifyPassword = findViewById(R.id.verify_password_input);
        verifyPassword.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromValidatedPassword();
                if (!result.isEmpty()) {
                    verifyPassword.setError(result);
                }
            }
        });

        final EditText email = findViewById(R.id.email_input);
        email.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromEmail();
                if (!result.isEmpty()) {
                    email.setError(result);
                }
            }
        });
        final EditText firstName = findViewById(R.id.first_name_input);
        firstName.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromFirstName();
                if (!result.isEmpty()) {
                    firstName.setError(result);
                }
            }
        });
        final EditText lastName = findViewById(R.id.last_name_input);
        lastName.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromLastName();
                if (!result.isEmpty()) {
                    lastName.setError(result);
                }
            }
        });
        final EditText age = findViewById(R.id.age_input);
        age.addTextChangedListener(new CustomWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                String result = getMistakeFromAge();
                if (!result.isEmpty()) {
                    age.setError(result);
                }
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

    private String getMistakeFromValidatedPassword() {
        String password = getTextFromContainer(R.id.password_container);
        String repeatPassword = getTextFromContainer(R.id.verify_password_container);
        if (!userValidator.isPasswordCorrectlyRepeated(password, repeatPassword)) {
            return getResources().getString(R.string.not_correctly_repeated_password);
        }
        return "";
    }

    private String getMistakeFromEmail() {
        String email = getTextFromContainer(R.id.email_container);
        if (!userValidator.isEmailValid(email)) {
            return getResources().getString(R.string.not_valid_email);
        }
        user.setEmail(email);
        return "";
    }

    private String getMistakeFromFirstName() {
        String firstName = getTextFromContainer(R.id.first_name_container);
        if (!userValidator.isFirstNameValid(firstName)) {
            return getResources().getString(R.string.not_valid_firstname);
        } else if (!userValidator.doesFirstNameStartWithCapitalLetter(firstName)) {
            return getResources().getString(R.string.not_capital_letter_firstname);
        }
        user.setFirstName(firstName);
        return "";
    }

    private String getMistakeFromLastName() {
        String lastName = getTextFromContainer(R.id.last_name_container);
        if (!userValidator.isLastNameValid(lastName)) {
            return getResources().getString(R.string.not_valid_lastname);
        }
        user.setLastName(lastName);
        return "";
    }

    private String getMistakeFromAge() {
        String ageText = getTextFromContainer(R.id.age_container);
        int age = 0;
        if (!ageText.isEmpty()) {
            age = Integer.parseInt(ageText);
        }
        if (userValidator.isTooYoung(age)) {
            return getResources().getString(R.string.too_young_age);
        } else if (userValidator.isTooOld(age)) {
            return getResources().getString(R.string.too_old_age);
        }
        user.setAge(age);
        return "";
    }

    private void registerIfUsernameIsAvailable() {
        notMainThread.execute(() -> {
            AppDatabase db = Room.databaseBuilder(this, AppDatabase.class, "users").build();
            List<User> userWithThisUsername = db.userDao().getUserWithThisName(user.getUsername());

            runOnUiThread(() -> {
                if (userWithThisUsername.size() != 0) {
                    printToast(getResources().getString(R.string.existing_user));
                } else {
                    printToast(getResources().getString(R.string.successful_registration));
                    startIntentLoginScreen();
                    saveUserInDatabase(db);
                }
            });
        });
    }

    private void saveUserInDatabase(AppDatabase db) {
        notMainThread.execute(() -> db.userDao().insertUser(user));
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
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

}
