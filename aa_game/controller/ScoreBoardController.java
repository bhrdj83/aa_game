package controller;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import model.User;

import java.util.ArrayList;

public class ScoreBoardController {
    private int level;

    public ScoreBoardController(int level) {
        this.level = level;
    }

    public VBox getBoard() {
        ArrayList<User> sortedUsers = User.sortUsers(level);

        VBox vBox = new VBox();
        Label title = new Label("Level " + level);
        title.setMinWidth(400);
        vBox.getChildren().add(title);

        HBox header = new HBox();
        Text userHeader = new Text("User");
        userHeader.setWrappingWidth(150);
        Text avatarHeader = new Text("  ");
        avatarHeader.setWrappingWidth(150);
        Text scoreHeader = new Text("Score");
        scoreHeader.setWrappingWidth(150);
        Text timeHeader = new Text("Time");
        timeHeader.setWrappingWidth(150);
        header.getChildren().addAll(userHeader, avatarHeader, scoreHeader, timeHeader);
        vBox.getChildren().add(header);
        for(int i = 1 ; i <= Math.min(10, sortedUsers.size()) ; i++) {
            HBox userContent = new HBox();
            User user = sortedUsers.get(i - 1);
            userContent.setPrefHeight(40);

            Text usernameField = new Text(i + ". " + user.getUsername());
            usernameField.setWrappingWidth(150);
            userContent.getChildren().add(usernameField);

            ImageView avatar = new ImageView(
                    new Image(user.getImage(), 40, 40, false, false));
            userContent.getChildren().add(avatar);
            Text empty = new Text("");
            empty.setWrappingWidth(110);
            userContent.getChildren().add(empty);

            Text scoreField = new Text(String.format("%5d", user.getScore(level)));
            scoreField.setWrappingWidth(150);
            userContent.getChildren().add(scoreField);

            int minute = user.getTime(level) / 60;
            int second = user.getTime(level) % 60;
            Text timeField = new Text(String.format("%02d", minute) + " : " + String.format("%02d", second));
            timeField.setWrappingWidth(150);
            userContent.getChildren().add(timeField);

            if(i == 1)
                userContent.setBackground(new Background(new BackgroundFill(Color.GOLD, CornerRadii.EMPTY, Insets.EMPTY)));
            else if(i == 2)
                userContent.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));
            else if(i == 3)
                userContent.setBackground(new Background(new BackgroundFill(Color.YELLOW, CornerRadii.EMPTY, Insets.EMPTY)));
            else
                userContent.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));

            vBox.getChildren().add(userContent);
        }
        return vBox;
    }
}
