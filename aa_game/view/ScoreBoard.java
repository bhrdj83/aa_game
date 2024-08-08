package view;

import controller.GameMenuController;
import controller.ScoreBoardController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;

public class ScoreBoard extends Application {
    private static GameMenuController gameMenuController;
    private int level;
    @Override
    public void start(Stage stage) throws Exception {
        Pane scoreBoardPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/ScoreBoard.fxml").toExternalForm()));

        ScoreBoardController controller = new ScoreBoardController(level);
        VBox vBox = controller.getBoard();
        scoreBoardPane.getChildren().add(vBox);

        Button back = new Button();
        back.setText("back");
        back.setLayoutX(400);
        back.setLayoutY(0);
        EventHandler<MouseEvent> backEvent = mouseEvent -> {
            try {
                MainMenu mainMenu = new MainMenu();
                MainMenu.setGameController(gameMenuController);
                mainMenu.start(EntryMenu.stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
        back.setOnMouseClicked(backEvent);
        scoreBoardPane.getChildren().add(back);

        Scene scene = new Scene(scoreBoardPane);
        stage.setScene(scene);
        stage.show();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static void setGameMenuController(GameMenuController gameMenuController) {
        ScoreBoard.gameMenuController = gameMenuController;
    }
}
