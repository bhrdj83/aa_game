package view;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.io.FileNotFoundException;
import java.net.URL;

public class EntryMenu extends Application {
    public static Stage stage;
    public static void main(String[] args) throws FileNotFoundException {
        User.initializeUsersFromDatabase();
        launch();
    }
    @Override
    public void start(Stage stage) throws Exception {
        EntryMenu.stage = stage;
        BorderPane entryPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/EntryMenu.fxml").toExternalForm()));
        stage.getIcons().add(new Image(getClass().getResource("/images/aaIcon.jpg").toString()));
        Scene scene = new Scene(entryPane);
        stage.setScene(scene);
        stage.show();
    }

    public void register(MouseEvent mouseEvent) throws Exception {
        new RegisterMenu().start(stage);
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        new LoginMenu().start(stage);
    }

    public void skip(MouseEvent mouseEvent) throws Exception {
        User.setCurrentUser(null);
        MainMenu.setGameController(new GameMenuController());
        new MainMenu().start(stage);
    }
}
