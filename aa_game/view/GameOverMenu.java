package view;

import controller.GameMenuController;
import controller.ScoreBoardController;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class GameOverMenu extends Application {
    private boolean won;
    private int minute;
    private int second;
    private int score;
    private static GameMenuController gameMenuController;
    @Override
    public void start(Stage stage) throws Exception {
        Pane gameOverPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/GameOverMenu.fxml").toExternalForm()));

        VBox main = new VBox();

        HBox header = new HBox();
        Label winStateLabel = new Label();
        if(won) winStateLabel.setText("You Won!");
        else winStateLabel.setText("You Lost!");
        header.getChildren().add(winStateLabel);

        Button ok = new Button();
        ok.setText("OK");
        ok.setOnMouseClicked(mouseEvent -> {
            MainMenu mainMenu = new MainMenu();
            try {
                mainMenu.start(EntryMenu.stage);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        header.getChildren().add(ok);
        header.setSpacing(250);

        main.getChildren().add(header);

        Label scoreLabel = new Label();
        scoreLabel.setText("Your Score: " + score);
        main.getChildren().add(scoreLabel);

        Label timeLabel = new Label();
        timeLabel.setText("Your Time: " + minute + " : " + second);
        main.getChildren().add(timeLabel);


        ScoreBoardController scoreBoardController = new ScoreBoardController(gameMenuController.getLevel());
        VBox scoreBoard = scoreBoardController.getBoard();
        //scoreBoard.setLayoutY(150);
        //gameOverPane.getChildren().add(scoreBoard);
        main.getChildren().add(scoreBoard);

        gameOverPane.getChildren().add(main);

        Scene scene = new Scene(gameOverPane);
        stage.setScene(scene);
        stage.show();
    }


    public static void setGameMenuController(GameMenuController gameMenuController) {
        GameOverMenu.gameMenuController = gameMenuController;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
