package com.example.tsvetelinastoyanova.registrationloginapp.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.tsvetelinastoyanova.registrationloginapp.validation.UserValidator;

@Entity(tableName = "users")
public class User implements UserValidator {

    @ColumnInfo(name = "username")
    @PrimaryKey
    @NonNull
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo(name = "firstName")
    private String firstName;

    @ColumnInfo(name = "lastName")
    private String lastName;

    @ColumnInfo(name = "age")
    private int age;

    @Ignore
    public User(String username, String password){
        this.username = username;
        this.password = password;
    }

    public User(@NonNull String username, String password, String email, String firstName, String lastName, int age) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    @NonNull
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public void setUsername(@NonNull String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public boolean isUsernameTooShort() {
        return username.length() < 4;
    }

    @Override
    public boolean isUsernameTooLong() {
        return username.length() >= 128;
    }

    @Override
    public boolean isUsernameValid() {
        return doesWordMatchesRegex(username,USERNAME_REGEX);
    }

   // @Override
    public boolean doesWordMatchesRegex(String word, String regex) {
        return word.matches(regex);
    }

    @Override
    public boolean isPasswordTooShort() {
        return password.length() < 6;
    }

    @Override
    public boolean isPasswordTooLong() {
        return password.length() >= 128;
    }

    @Override
    public boolean isPasswordValid() {
        return doesWordMatchesRegex(password,PASSWORD_REGEX);
    }

    @Override
    public boolean isPasswordCorrectlyRepeated(String repeatedPassword) {
        return password.equals(repeatedPassword);
    }

    @Override
    public boolean isEmailValid() {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean isFirstNameValid() {
        return !firstName.isEmpty() && doesWordMatchesRegex(firstName,FIRSTNAME_REGEX);
    }

    @Override
    public boolean doesFirstNameStartWithCapitalLetter() {
        return Character.isUpperCase(firstName.charAt(0));
    }

    @Override
    public boolean isLastNameValid() {
        return !lastName.isEmpty() && doesWordMatchesRegex(lastName,LASTNAME_REGEX);
    }

    @Override
    public boolean isTooYoung() {
        return age < 12;
    }

    @Override
    public boolean isTooOld() {
        return age > 112;
    }
}

