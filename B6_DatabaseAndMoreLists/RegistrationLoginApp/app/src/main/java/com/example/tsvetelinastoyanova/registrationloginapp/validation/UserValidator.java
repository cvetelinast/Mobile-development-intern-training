package com.example.tsvetelinastoyanova.registrationloginapp.validation;

public interface UserValidator {

    final String PASSWORD_REGEX = "^[!-~]*$";

    final String USERNAME_REGEX = "^[a-zA-Z0-9]*$";

    final String FIRSTNAME_REGEX = "^[a-zA-Z]*$";

    final String LASTNAME_REGEX = "^[a-zA-Z\\s]*$";

    public boolean isUsernameTooShort();

    public boolean isUsernameTooLong();

    public boolean isUsernameValid();

   // public boolean doesWordMatchesRegex(String word, String regex);

    public boolean isPasswordTooShort();

    public boolean isPasswordTooLong();

    public boolean isPasswordValid();

    public boolean isPasswordCorrectlyRepeated(String repeatedPassword);

    public boolean isEmailValid();

    public boolean isFirstNameValid();

    public boolean doesFirstNameStartWithCapitalLetter();

    public boolean isLastNameValid();

    public boolean isTooYoung();

    public boolean isTooOld();
}
