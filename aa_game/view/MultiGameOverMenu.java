package view;

import controller.GameMenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;

public class MultiGameOverMenu extends Application {
    private boolean won;
    private int minute;
    private int second;
    private int score1;
    private int score2;
    private static GameMenuController gameMenuController;

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane multiGameOverPane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/MultiGameOverMenu.fxml").toExternalForm()));

        VBox main = new VBox();

        HBox header = new HBox();
        Label winStateLabel = new Label();
        winStateLabel.setMinWidth(300);
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
        header.setSpacing(100);

        main.getChildren().add(header);

        Label timeLabel = new Label();
        timeLabel.setText("Time: " + minute + " : " + second);
        timeLabel.setMinWidth(300);
        main.getChildren().add(timeLabel);

        Label score1Label = new Label();
        score1Label.setText("Player1 Score: " + score1);
        score1Label.setMinWidth(300);
        main.getChildren().add(score1Label);

        Label score2Label = new Label();
        score2Label.setText("Player2 Score: " + score2);
        score2Label.setMinWidth(300);
        main.getChildren().add(score2Label);

        multiGameOverPane.getChildren().add(main);

        Scene scene = new Scene(multiGameOverPane);
        stage.setScene(scene);
        stage.show();
    }

    public boolean isWon() {
        return won;
    }

    public void setWon(boolean won) {
        this.won = won;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getScore1() {
        return score1;
    }

    public void setScore1(int score1) {
        this.score1 = score1;
    }

    public int getScore2() {
        return score2;
    }

    public void setScore2(int score2) {
        this.score2 = score2;
    }

    public static GameMenuController getGameMenuController() {
        return gameMenuController;
    }

    public static void setGameMenuController(GameMenuController gameMenuController) {
        MultiGameOverMenu.gameMenuController = gameMenuController;
    }
}
