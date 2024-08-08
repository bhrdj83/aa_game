package controller;

import model.User;

import java.io.IOException;

public class RegisterMenuController {
    public static String register(String username, String password) throws IOException {
        if(username.length() == 0)
            return "username field is empty";
        else if(password.length() == 0)
            return "password field is empty";
        else if(User.getUserByName(username) != null)
            return "this username already exists";

        User.addUser(new User(username, password));

        return "ok";
    }
}
