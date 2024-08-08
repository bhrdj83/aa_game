package view;

import controller.GameMenuController;
import controller.LoginMenuController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.User;

import java.net.URL;

public class LoginMenu extends Application {
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane registerPane = FXMLLoader.load(
                new URL(RegisterMenu.class.getResource("/FXML/LoginMenu.fxml").toExternalForm()));
        Scene scene = new Scene(registerPane);
        stage.setScene(scene);
        stage.show();
    }

    public void login(MouseEvent mouseEvent) throws Exception {
        String username = this.username.getText();
        String password = this.password.getText();

        String result = LoginMenuController.login(username, password);

        if(result != "ok") {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("login unsuccessful");
            alert.setContentText(result);
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("login successful");
            alert.setContentText("user \"" + username + "\" logged in successfully");
            alert.showAndWait();
            MainMenu mainMenu = new MainMenu();
            mainMenu.setGameController(new GameMenuController());
            mainMenu.start(EntryMenu.stage);
        }
    }

    public void back(MouseEvent mouseEvent) throws Exception {
        new EntryMenu().start(EntryMenu.stage);
    }
}
