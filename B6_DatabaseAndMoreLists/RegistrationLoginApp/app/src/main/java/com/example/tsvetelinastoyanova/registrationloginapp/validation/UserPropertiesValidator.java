package com.example.tsvetelinastoyanova.registrationloginapp.validation;

public class UserPropertiesValidator implements UserValidator {
    @Override
    public boolean isUsernameTooShort(String username) {
        return username.length() < 4;
    }

    @Override
    public boolean isUsernameTooLong(String username) {
        return username.length() >= 128;
    }

    @Override
    public boolean isUsernameValid(String username) {
        return doesWordMatchesRegex(username,USERNAME_REGEX);
    }

    @Override
    public boolean doesWordMatchesRegex(String word, String regex) {
        return word.matches(regex);
    }

    @Override
    public boolean isPasswordTooShort(String password) {
        return password.length() < 6;
    }

    @Override
    public boolean isPasswordTooLong(String password) {
        return password.length() >= 128;
    }

    @Override
    public boolean isPasswordValid(String password) {
        return doesWordMatchesRegex(password,PASSWORD_REGEX);
    }

    @Override
    public boolean isPasswordCorrectlyRepeated(String password, String repeatedPassword) {
        return password.equals(repeatedPassword);
    }

    @Override
    public boolean isEmailValid(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean isFirstNameValid(String firstName) {
        return !firstName.isEmpty() && doesWordMatchesRegex(firstName,FIRSTNAME_REGEX);
    }

    @Override
    public boolean doesFirstNameStartWithCapitalLetter(String firstName) {
        return Character.isUpperCase(firstName.charAt(0));
    }

    @Override
    public boolean isLastNameValid(String lastName) {
        return !lastName.isEmpty() && doesWordMatchesRegex(lastName,LASTNAME_REGEX);
    }

    @Override
    public boolean isTooYoung(int age) {
        return age < 12;
    }

    @Override
    public boolean isTooOld(int age) {
        return age > 112;
    }
}
