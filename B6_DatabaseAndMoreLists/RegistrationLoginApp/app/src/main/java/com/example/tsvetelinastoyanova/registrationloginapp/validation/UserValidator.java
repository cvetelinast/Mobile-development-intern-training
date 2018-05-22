package com.example.tsvetelinastoyanova.registrationloginapp.validation;

public interface UserValidator {

    final String PASSWORD_REGEX = "^[!-~]*$";

    final String USERNAME_REGEX = "^[a-zA-Z0-9]*$";

    final String FIRSTNAME_REGEX = "^[a-zA-Z]*$";

    final String LASTNAME_REGEX = "^[a-zA-Z\\s]*$";

    public boolean isUsernameTooShort(String username);

    public boolean isUsernameTooLong(String username);

    public boolean isUsernameValid(String username);

    public boolean doesWordMatchesRegex(String word, String regex);

    public boolean isPasswordTooShort(String password);

    public boolean isPasswordTooLong(String password);

    public boolean isPasswordValid(String password);

    public boolean isPasswordCorrectlyRepeated(String password, String repeatedPassword);

    public boolean isEmailValid(String email);

    public boolean isFirstNameValid(String firstName);

    public boolean doesFirstNameStartWithCapitalLetter(String firstName);

    public boolean isLastNameValid(String lastName);

    public boolean isTooYoung(int age);

    public boolean isTooOld(int age);
}
