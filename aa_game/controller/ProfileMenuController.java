package controller;

import model.User;

import java.io.IOException;

public class ProfileMenuController {
    public static String changeUsername(String newUsername) throws IOException {
        if(newUsername.length() == 0)
            return "enter a new username";
        else if(User.getUserByName(newUsername) != null)
            return "this username already exists";

        User.getCurrentUser().setUsername(newUsername);

        return "ok";
    }

    public static String changePassword(String newPassword) throws IOException {
        if(newPassword.length() == 0)
            return "enter a new password";

        User.getCurrentUser().setPassword(newPassword);

        return "ok";
    }

    public static void deleteAccount() throws IOException {
        User.removeUser(User.getCurrentUser());
    }
}
