package controller;

import model.User;

public class LoginMenuController {
    public static String login(String username, String password) {
        User user;
        if(username.length() == 0)
            return "username field is empty";
        else if(password.length() == 0)
            return "password field is empty";
        else if((user = User.getUserByName(username)) == null)
            return "username doesn't exist";
        else if(!user.isPasswordCorrect(password))
            return "wrong password";

        User.setCurrentUser(user);

        return "ok";
    }
}
