package view;

import controller.GameMenuController;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Ball;
import model.User;

import java.net.URL;

public class GameMenu extends Application {
    private static GameMenuController controller;
    private Pane gamePane;
    private BorderPane pausePane;
    private Text timerText;
    @FXML
    private Button soundtrack1;
    @FXML
    private Button soundtrack2;
    @FXML
    private Button soundtrack3;

    @Override
    public void start(Stage stage) throws Exception {
        Pane gamePane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/GameMenu.fxml").toExternalForm()));
        controller.setGamePane(gamePane);

        Circle mainCircle = new Circle(300, 250, 40);
        mainCircle.setFill(Color.BLACK);
        gamePane.getChildren().add(mainCircle);

        Circle invisibleCircle = new Circle(300, 250, 130);
        invisibleCircle.setVisible(false);
        gamePane.getChildren().add(invisibleCircle);
        controller.setInvisibleCircle(invisibleCircle);

        controller.initiateMap(invisibleCircle, gamePane);

        Ball shooter = controller.createShooter(gamePane, invisibleCircle, 300, 470, controller.getBallsCount());
        controller.setShooter(shooter);

        Ball shooter2 = null;
        if(controller.isMultiplayer()) {
            shooter2 = controller.createShooter2(gamePane, invisibleCircle, 300, 30, controller.getBallsCount());
            controller.setShooter2(shooter2);
        }

        Text timer = controller.timerShow();
        gamePane.getChildren().add(timer);

        Media media = new Media(getClass().getResource("/media/soundtrack1.mp3").toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        controller.setMediaPlayer(mediaPlayer);
        if(!controller.isMute())
            mediaPlayer.play();

        controller.winChecker(gamePane, shooter);

        ProgressBar ballRemainProgress = controller.showBallRemaining();
        gamePane.getChildren().add(ballRemainProgress);

        Text scoreText = controller.scoreText();
        gamePane.getChildren().add(scoreText);

        Text angleText = controller.angleText();
        gamePane.getChildren().add(angleText);

        ProgressBar freezeProgressBar = controller.createFreezeProgressBar();
        gamePane.getChildren().add(freezeProgressBar);

        BorderPane pausePane = FXMLLoader.load(
                new URL(EntryMenu.class.getResource("/FXML/PauseMenu.fxml").toExternalForm()));
        pausePane.setVisible(false);
        gamePane.getChildren().add(pausePane);
        controller.setPausePane(pausePane);

        Button pauseButton = new Button();
        pauseButton.setText("Pause");
        pauseButton.setLayoutX(520);
        pauseButton.setLayoutY(30);
        pauseButton.setMaxWidth(50);
        pauseButton.setOnMouseClicked(mouseEvent -> {
            showPauseMenu(pausePane, gamePane);
        });
        gamePane.getChildren().add(pauseButton);

        Ball finalShooter = shooter2;
        Ball finalShooter1 = shooter2;
        gamePane.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode().equals(KeyCode.SPACE)) controller.shoot(shooter, gamePane, invisibleCircle);
            else if(keyEvent.getCode().equals(KeyCode.LEFT)) GameMenuController.moveLeft(shooter);
            else if(keyEvent.getCode().equals(KeyCode.RIGHT)) GameMenuController.moveRight(shooter);
            else if(keyEvent.getCode().equals(KeyCode.TAB)) controller.freeze();
            else if(controller.isMultiplayer() && keyEvent.getCode().equals(KeyCode.ENTER)) controller.shoot(finalShooter1, gamePane, invisibleCircle);
            else if(controller.isMultiplayer() && keyEvent.getCode().equals(KeyCode.A)) GameMenuController.moveLeft(finalShooter);
            else if(controller.isMultiplayer() && keyEvent.getCode().equals(KeyCode.D)) GameMenuController.moveRight(finalShooter);
        });

        Scene scene = new Scene(gamePane);
        stage.setScene(scene);
        gamePane.requestFocus();
        stage.show();
    }

    private void showPauseMenu(BorderPane pausePane, Pane gamePane) {
        controller.getTimerTimeline().stop();
        for(int i = 0 ; i < controller.getPhaseTimelines().size() ; i++) {
            controller.getPhaseTimelines().get(i).stop();
        }
        for (Node child : gamePane.getChildren()) {
            if(!child.equals(pausePane)) child.setVisible(false);
        }
        pausePane.setVisible(true);
        pausePane.requestFocus();
    }

    public static void setController(GameMenuController controller) {
        GameMenu.controller = controller;
    }

    public void resume(MouseEvent mouseEvent) {
        controller.getTimerTimeline().play();
        for(int i = 0 ; i < controller.getPhaseTimelines().size() ; i++) {
            controller.getPhaseTimelines().get(i).play();
        }
        for (Node child : controller.getGamePane().getChildren()) {
            if(!child.equals(controller.getPausePane()) &&
                !child.equals(controller.getInvisibleCircle())) child.setVisible(true);
        }
        controller.getPausePane().setVisible(false);
        controller.getGamePane().requestFocus();
    }

    public void restart(MouseEvent mouseEvent) throws Exception {
       GameMenuController newGameMenuController = new GameMenuController();
       newGameMenuController.setBallsCount(controller.getBallsCount());
       newGameMenuController.setMap(controller.getMap());
       newGameMenuController.setLevel(controller.getLevel());
       MainMenu.setGameController(newGameMenuController);
       GameMenu.setController(newGameMenuController);
       new GameMenu().start(EntryMenu.stage);
    }

    public void controllerGuid(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText("Control Guid");
        String content = "Single Player (Player1 in Multiplayer): \n";
        content += "Shoot: Space\n" + "Move Right: Right Arrow\n" + "Move Left: Left Arrow\n";
        content += "Player2 in Multiplayer: \n";
        content += "Shoot: Enter\n" + "Move Right: D\n" + "Move Left: A";
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void exit(MouseEvent mouseEvent) throws Exception {
        for(Transition transition : controller.getAnimations())
            transition.stop();
        if(!controller.isMute())
            controller.getMediaPlayer().stop();

        controller.getAnimations().clear();
        controller.getTimerTimeline().stop();
        controller.getBallRemainingTimeline().stop();
        controller.getConnectedBalls().clear();
        controller.setShooter(null);
        controller.setTimeRemaining(180);
        new MainMenu().start(EntryMenu.stage);
    }

    public void mute(MouseEvent mouseEvent) {
        if(controller.isMute())
            controller.getMediaPlayer().play();
        else
            controller.getMediaPlayer().stop();
        controller.setMute(!controller.isMute());
        if(User.getCurrentUser() != null)
            User.getCurrentUser().setMute(!User.getCurrentUser().isMute());
    }

    public void changeTrack(MouseEvent mouseEvent) {
        Media media;
        Button clickedButton = (Button) mouseEvent.getSource();
        if(clickedButton.equals(soundtrack1))
            media = new Media(getClass().getResource(User.getSoundtracks().get(0)).toString());
        else if(clickedButton.equals(soundtrack2))
            media = new Media(getClass().getResource(User.getSoundtracks().get(1)).toString());
        else
            media = new Media(getClass().getResource(User.getSoundtracks().get(2)).toString());

        if(!controller.isMute()) controller.getMediaPlayer().stop();
        controller.setMediaPlayer(new MediaPlayer(media));
        if(!controller.isMute()) controller.getMediaPlayer().play();
    }
}
