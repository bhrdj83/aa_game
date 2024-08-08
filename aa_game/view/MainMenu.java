package view;

import controller.GameMenuController;
import controller.MainMenuController;
import controller.ProfileMenuController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class MainMenu extends Application {

    private static GameMenuController gameController;
    @FXML
    private Label label;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane entryPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/MainMenu.fxml").toExternalForm()));
        Scene scene = new Scene(entryPane);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize() {
        if(User.getCurrentUser() != null)
            label.setText("Welcome " + User.getCurrentUser().getUsername());
        else
            label.setText("Welcome");
    }

    public void newGame(MouseEvent mouseEvent) throws Exception {
        GameMenu gameMenu = new GameMenu();
        gameController.setMultiplayer(false);
        GameMenu.setController(gameController);
        gameMenu.start(EntryMenu.stage);
    }

    public void loadGame(MouseEvent mouseEvent) {
    }

    public void editProfile(MouseEvent mouseEvent) throws Exception {
        if(User.getCurrentUser() != null) {
            new ProfileMenu().start(EntryMenu.stage);
        }
    }

    public void showScoreBoard(MouseEvent mouseEvent) throws Exception {
        ScoreBoardMenu scoreBoardMenu = new ScoreBoardMenu();
        ScoreBoardMenu.setGameMenuController(gameController);
        scoreBoardMenu.start(EntryMenu.stage);
    }

    public void settings(MouseEvent mouseEvent) throws Exception {
        SettingsMenu settingsMenu = new SettingsMenu();
        SettingsMenu.setGameController(gameController);
        settingsMenu.start(EntryMenu.stage);
    }

    public void exit(MouseEvent mouseEvent) throws Exception {
        User.setCurrentUser(null);
        new EntryMenu().start(EntryMenu.stage);
    }

    public static void setGameController(GameMenuController gameController) {
        MainMenu.gameController = gameController;
    }

    public void multiplayerGame(MouseEvent mouseEvent) throws Exception {
        GameMenu gameMenu = new GameMenu();
        gameController.setMultiplayer(true);
        GameMenu.setController(gameController);
        gameMenu.start(EntryMenu.stage);
    }
}
