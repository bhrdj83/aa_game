package view;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.net.URL;

public class SettingsMenu extends Application {
    private static GameMenuController gameController;
    @FXML
    private TextField mapInput;
    @FXML
    private TextField levelInput;
    @FXML
    private TextField ballsCountInput;
    @Override
    public void start(Stage stage) throws Exception {
        BorderPane settingsPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/SettingsMenu.fxml").toExternalForm()));

        Scene scene = new Scene(settingsPane);
        stage.setScene(scene);
        stage.show();
    }

    public void setLevel(MouseEvent mouseEvent) throws IOException {
        int level = Integer.parseInt(levelInput.getText());
        Alert alert;
        if(level < 1 || level > 3) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("level input out of bounds");
            alert.setContentText("enter a number from 1 to 3");
        }
        else {
            gameController.setLevel(level);
            if(User.getCurrentUser() != null)
                User.getCurrentUser().setLevel(level);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("level set successfully");
            alert.setContentText("you set difficulty level to " + level);
        }
        alert.showAndWait();
    }

    public void setBallsCount(MouseEvent mouseEvent) throws IOException {
        int ballsCount = Integer.parseInt(ballsCountInput.getText());
        Alert alert;
        if(ballsCount < 4 || ballsCount > 50) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("balls count input out of bounds");
            alert.setContentText("enter a number from 4 to 50");
        }
        else {
            gameController.setBallsCount(ballsCount);
            if(User.getCurrentUser() != null)
                User.getCurrentUser().setBallsCount(ballsCount);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("balls count set successfully");
            alert.setContentText("you set number of balls to " + ballsCount);
        }
        alert.showAndWait();
    }

    public static void setGameController(GameMenuController gameController) {
        SettingsMenu.gameController = gameController;
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        MainMenu mainMenu = new MainMenu();
        MainMenu.setGameController(gameController);
        mainMenu.start(EntryMenu.stage);
    }

    public void setMap(MouseEvent mouseEvent) {
        int map = Integer.parseInt(mapInput.getText());
        Alert alert;
        if(map < 1 || map > 3) {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("map input out of bounds");
            alert.setContentText("enter a number from 1 to 3");
        }
        else {
            gameController.setMap(map);
            if(User.getCurrentUser() != null)
                User.getCurrentUser().setMap(map);
            alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("map set successfully");
            alert.setContentText("you set map to " + map);
        }
        alert.showAndWait();
    }

    public void mute(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("mute/unmute");
        if(gameController.isMute()) alert.setContentText("You unmuted the sound");
        else alert.setContentText("You muted the sound");
        alert.showAndWait();

        gameController.setMute(!gameController.isMute());
        if(User.getCurrentUser() != null)
            User.getCurrentUser().setMute(!User.getCurrentUser().isMute());
    }
}
