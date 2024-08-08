package view;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;

public class ScoreBoardMenu extends Application {
    private static GameMenuController gameMenuController;
    @FXML
    private Button level1;
    @FXML
    private Button level2;
    @FXML
    private Button level3;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane scoreBoardMenuPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/ScoreBoardMenu.fxml").toExternalForm()));

        Scene scene = new Scene(scoreBoardMenuPane);
        stage.setScene(scene);
        stage.show();
    }

    public void showScoreBoard(MouseEvent mouseEvent) throws Exception {
        Button clickedButton = (Button) mouseEvent.getSource();
        ScoreBoard scoreBoard = new ScoreBoard();
        if(clickedButton.equals(level1)) scoreBoard.setLevel(1);
        else if(clickedButton.equals(level2)) scoreBoard.setLevel(2);
        else if(clickedButton.equals(level3)) scoreBoard.setLevel(3);
        ScoreBoard.setGameMenuController(gameMenuController);
        scoreBoard.start(EntryMenu.stage);
    }

    public static void setGameMenuController(GameMenuController gameMenuController) {
        ScoreBoardMenu.gameMenuController = gameMenuController;
    }
}
