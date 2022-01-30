package nbu.java.utils;

import nbu.java.model.pojo.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.regex.Pattern;

public abstract class Validator {
    private static final int MIN_PASSWORD_LENGTH = 8;

    public static String validateEmail(String email) {
        String emailRegex = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";

        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        String message = "";
        if (email == null || !pattern.matcher(email).matches()) {
            message = "Invalid email!";
        }

        return message;
    }

    public static String validatePassword(String password) {
        String message = "";
        if (password == null || password.isEmpty() ||
                password.length() < MIN_PASSWORD_LENGTH || containsOnlySpaces(password)) {
            message = "Invalid password!";
        }
        return message;
    }

    public static String validateConfirmPassword(String confirmPassword, String initialPassword) {
        String message = "";
        if (!initialPassword.equals(confirmPassword)) {
            message = "Confirm password doesn't match!";
        }
        return message;
    }

    public static String validateName(String name) {
        String message = "";
        if (name == null || name.isEmpty() || containsOnlySpaces(name)) {
            message = "Invalid name!";
        }
        return message;
    }

    private static boolean containsOnlySpaces(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) != ' ') {
                return false;
            }
        }
        return true;
    }

    public static String validateEnteredAndActualPasswords(String password, User loggedUser) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        String message = "";
        if (password != null) {
            if (!encoder.matches(password, loggedUser.getPassword())) {
                message = "Wrong credentials!";
            }
        }
        return message;
    }

    public static String validateNewAndConfirmPassword(String newPassword, String enteredCurrentPassword,
                                                       String confirmPassword, User loggedUser) {
        String message = "";
        if (newPassword != null) {
            if (enteredCurrentPassword == null) {
                return "You should first enter your current password!";
            }

            message += validateEnteredAndActualPasswords(enteredCurrentPassword, loggedUser);
            if (!message.isEmpty()) {
                return message;
            }

            message += validatePassword(newPassword);
            if (!message.isEmpty()) {
                return message;
            }

            message += validateConfirmPassword(confirmPassword, newPassword);
            if (!message.isEmpty()) {
                return message;
            }

            loggedUser.setPassword(newPassword);
        }
        return message;
    }
}
