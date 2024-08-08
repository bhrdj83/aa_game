package view;

import controller.ProfileMenuController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.User;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ProfileMenu extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Pane profilePane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/ProfileMenu.fxml").toExternalForm()));
        ImageView avatar = new ImageView(
                new Image(User.getCurrentUser().getImage(), 100, 100, false, false));
        Label username = new Label();
        username.setText(User.getCurrentUser().getUsername());

        HBox hBox = new HBox();
        hBox.getChildren().addAll(avatar, username);
        hBox.setSpacing(15);

        TextField newUsername = new TextField();
        newUsername.setPromptText("new username");
        Button changeUsername = new Button();
        changeUsername.setText("change username");

        EventHandler<MouseEvent> changeUsernameEvent = mouseEvent -> {
            try {
                changeUsername(newUsername.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        changeUsername.setOnMouseClicked(changeUsernameEvent);

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("new password");
        Button changePassword = new Button();
        changePassword.setText("change password");

        EventHandler<MouseEvent> changePasswordEvent = mouseEvent -> {
            try {
                changePassword(newPassword.getText());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        changePassword.setOnMouseClicked(changePasswordEvent);

        Button deleteAccount = new Button();
        deleteAccount.setText("delete account");

        EventHandler<MouseEvent> deleteAccountEvent = mouseEvent -> {
            try {
                deleteAccount();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        deleteAccount.setOnMouseClicked(deleteAccountEvent);

        Button changeAvatar = new Button();
        changeAvatar.setText("change avatar");

        EventHandler<MouseEvent> changeAvatarEvent = mouseEvent -> {
            try {
                new AvatarMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        changeAvatar.setOnMouseClicked(changeAvatarEvent);

        Button back = new Button();
        back.setText("back");

        EventHandler<MouseEvent> backEvent = mouseEvent -> {
            try {
                new MainMenu().start(stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };

        back.setOnMouseClicked(backEvent);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(hBox, newUsername, changeUsername, newPassword, changePassword, deleteAccount, changeAvatar, back);

        profilePane.getChildren().add(vBox);
        vBox.setLayoutX(150);
        vBox.setSpacing(10);

        Scene scene = new Scene(profilePane);
        stage.setScene(scene);
        stage.show();

    }

    private void deleteAccount() throws Exception {
        ProfileMenuController.deleteAccount();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("deleted account successfully");
        alert.setContentText("user " + User.getCurrentUser().getUsername() + " account deleted");
        alert.showAndWait();
        new EntryMenu().start(EntryMenu.stage);
    }

    private void changePassword(String text) throws Exception {
        String result = ProfileMenuController.changePassword(text);
        if(result != "ok") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("change password unsuccessful");
            alert.setContentText(result);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("changed password successfully");
            alert.setContentText("change password successfully");
            alert.showAndWait();
            this.start(EntryMenu.stage);
        }
    }

    private void changeUsername(String text) throws Exception {
        String result = ProfileMenuController.changeUsername(text);
        if(result != "ok") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("change username unsuccessful");
            alert.setContentText(result);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("changed username successfully");
            alert.setContentText("change username to " + text);
            alert.showAndWait();
            this.start(EntryMenu.stage);
        }
    }
}
