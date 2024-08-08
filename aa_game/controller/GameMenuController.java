package controller;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.util.Duration;
import model.Ball;
import model.User;
import view.*;

import java.util.ArrayList;

public class GameMenuController {
    private ArrayList<Ball> connectedBalls;
    private ArrayList<Transition> animations;
    private int level;
    private int ballsCount;
    private int timeRemaining;
    private Timeline timerTimeline;
    private Timeline checkerTimeline;
    private Timeline ballRemainingTimeline;
    private ArrayList<Timeline> phaseTimelines;
    private Ball shooter;
    private Ball shooter2;
    private ProgressBar freezeProgressBar;
    private Text scoreText;
    private Text angleText;
    private int map;
    private double angle;
    private Pane gamePane;
    private BorderPane pausePane;
    private Circle invisibleCircle;
    private boolean mute;
    private boolean isMultiplayer;
    private MediaPlayer mediaPlayer;

    public GameMenuController() {
        connectedBalls = new ArrayList<>();
        animations = new ArrayList<>();
        User currentUser = User.getCurrentUser();
        if(currentUser == null) {
            this.level = 1;
            this.ballsCount = 8;
            this.map = 1;
            this.mute = false;
        }
        else {
            this.level = currentUser.getLevel();
            this.ballsCount = currentUser.getBallsCount();
            this.map = currentUser.getMap();
            this.mute = currentUser.isMute();
        }
        this.angle = 0;
        this.timeRemaining = 180;
        this.phaseTimelines = new ArrayList<>();
    }

    public void setMap(int map) {
        this.map = map;
    }

    public Ball createShooter(Pane pane, Circle invisibleCircle, double x, double y, int number) {
        Ball shooter = new Ball(x, y, number, Color.BLACK);
        pane.getChildren().addAll(shooter, shooter.getNumberText());

        return shooter;
    }

    public Ball createShooter2(Pane gamePane, Circle invisibleCircle, double x, double y, int ballsCount) {
        Ball shooter2 = new Ball(x, y, ballsCount, Color.SILVER);
        gamePane.getChildren().addAll(shooter2, shooter2.getNumberText());

        return shooter2;
    }

    public void freeze() {
        if(freezeProgressBar.getProgress() == 1) {
            freezeProgressBar.setProgress(0);
            RotationAnimation2.setSpeed((double) level / 3);
            Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000 * (9 - 2 * level)), actionEvent -> {
                RotationAnimation2.setSpeed(level);
            }));
            timeline.setCycleCount(1);
            timeline.play();
        }
    }

    public Text timerShow() {
        Text text = new Text();
        text.setY(30);
        text.setX(30);
        text.setText("Timer: " + String.format("%02d", timeRemaining / 60) + " : " + String.format("%02d", timeRemaining % 60));
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            timeRemaining--;
            int minute = timeRemaining / 60;
            int second = timeRemaining % 60;
            text.setText("Timer: " + String.format("%02d", minute) + " : " + String.format("%02d", second));
        }))  ;
        this.timerTimeline = timeline;
        timeline.setCycleCount(180);
        timeline.play();
        return text;
    }

    public Timeline winChecker(Pane pane, Ball shooter) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            if(timeRemaining == 0 ||
                connectedBalls.size() - 5 == ballsCount) {
                pane.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
                for(Transition transition : animations)
                    transition.stop();
                timerTimeline.stop();
                Timeline wait = new Timeline(new KeyFrame(Duration.millis(750), actionEvent2 -> {
                    try {
                        endGame(true, shooter);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }));
                wait.setCycleCount(1);
                wait.play();
            }
        }));
        this.checkerTimeline = timeline;
        timeline.setCycleCount(-1);
        timeline.play();
        return timeline;
    }

    public void shoot(Ball shooter, Pane pane, Circle invisibleCircle) {
        Ball ball;
        if(shooter.equals(this.shooter))
            ball = new Ball(shooter.getCenterX(), shooter.getCenterY(), shooter.getNumber(), Color.BLACK);
        else
            ball = new Ball(shooter.getCenterX(), shooter.getCenterY(), shooter.getNumber(), Color.SILVER);
        shooter.shoot(angle);
        pane.getChildren().addAll(ball, ball.getNumberText());
        ShootingAnimation shootingAnimation = new ShootingAnimation(this, shooter, ball, pane, invisibleCircle);
        if(!mute) {
            Media media = new Media(getClass().getResource("/media/SPRTField_Balloon against wall 1 (ID 1825)_BSB.wav").toString());
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaPlayer.play();
        }
        shootingAnimation.play();
    }

    public static void moveRight(Ball shooter) {
        if(shooter.getCenterX() < 390) {
            shooter.setCenterX(shooter.getCenterX() + 5);
            shooter.getNumberText().setLayoutX(shooter.getNumberText().getLayoutX() + 5);
        }
    }

    public static void moveLeft(Ball shooter) {
        if(shooter.getCenterX() > 210) {
            shooter.setCenterX(shooter.getCenterX() - 5);
            shooter.getNumberText().setLayoutX(shooter.getNumberText().getLayoutX() - 5);
        }
    }

    public ArrayList<Ball> getConnectedBalls() {
        return connectedBalls;
    }

    public ArrayList<Transition> getAnimations() {
        return animations;
    }

    public ProgressBar getFreezeProgressBar() {
        return freezeProgressBar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getBallsCount() {
        return ballsCount;
    }

    public void setBallsCount(int ballsCount) {
        this.ballsCount = ballsCount;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(int timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Timeline getTimerTimeline() {
        return timerTimeline;
    }

    public void setTimerTimeline(Timeline timerTimeline) {
        this.timerTimeline = timerTimeline;
    }

    public Ball getShooter() {
        return shooter;
    }

    public void setShooter(Ball shooter) {
        this.shooter = shooter;
    }

    public Ball getShooter2() {
        return shooter2;
    }

    public void setShooter2(Ball shooter2) {
        this.shooter2 = shooter2;
    }

    public void endGame(boolean win, Ball shooter) throws Exception {
        this.checkerTimeline.stop();
        ballRemainingTimeline.stop();
        if(!mute)
            mediaPlayer.stop();
        int time = 180 - timeRemaining;
        int minute = time / 60;
        int second = time % 60;
        if(!isMultiplayer) {
            int score;
            if (win) score = this.ballsCount - shooter.getNumber();
            else score = this.ballsCount - shooter.getNumber() - 1;
            User currentUser = User.getCurrentUser();
            if (currentUser != null) {
                if (score > currentUser.getScore(level)) {
                    currentUser.setScore(level, score);
                    currentUser.setTime(level, time);
                } else if (score == currentUser.getScore(level) &&
                        currentUser.getTime(level) > time)
                    currentUser.setTime(level, time);
            }

            for (int i = 0; i < phaseTimelines.size(); i++)
                phaseTimelines.get(i).stop();

            this.getAnimations().clear();
            this.getConnectedBalls().clear();
            this.shooter = null;
            this.timeRemaining = 180;
            GameOverMenu gameOverMenu = new GameOverMenu();
            gameOverMenu.setScore(score);
            gameOverMenu.setMinute(minute);
            gameOverMenu.setSecond(second);
            gameOverMenu.setWon(win);
            GameOverMenu.setGameMenuController(this);
            gameOverMenu.start(EntryMenu.stage);
        }
        else {
            int score1 = ballsCount - shooter.getNumber();
            int score2 = ballsCount - shooter2.getNumber();
            for (int i = 0; i < phaseTimelines.size(); i++)
                phaseTimelines.get(i).stop();

            this.getAnimations().clear();
            this.getConnectedBalls().clear();
            this.shooter = null;
            this.timeRemaining = 180;
            MultiGameOverMenu multiGameOverMenu = new MultiGameOverMenu();
            multiGameOverMenu.setScore1(score1);
            multiGameOverMenu.setScore2(score2);
            multiGameOverMenu.setMinute(minute);
            multiGameOverMenu.setSecond(second);
            multiGameOverMenu.setWon(win);
            MultiGameOverMenu.setGameMenuController(this);
            multiGameOverMenu.start(EntryMenu.stage);
        }
    }

    public ProgressBar showBallRemaining() {
        ProgressBar progressBar = new ProgressBar();
        progressBar.setProgress(1);
        progressBar.setLayoutX(30);
        progressBar.setLayoutY(50);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(10), actionEvent -> {
            progressBar.setProgress((double) shooter.getNumber() / ballsCount);
            if(shooter.getNumber() <= 5) progressBar.setStyle("-fx-accent: green");
            else if(shooter.getNumber() <= ballsCount / 2) progressBar.setStyle("-fx-accent: yellow");
            else progressBar.setStyle("-fx-accent: red");
        }));
        this.ballRemainingTimeline = timeline;
        timeline.setCycleCount(-1);
        timeline.play();
        return progressBar;
    }

    public ProgressBar createFreezeProgressBar() {
        ProgressBar progressBar = new ProgressBar();
        this.freezeProgressBar = progressBar;
        progressBar.setProgress(0);
        progressBar.setLayoutX(30);
        progressBar.setLayoutY(450);
        progressBar.setStyle("-fx-accent: cyan");
        return progressBar;
    }

    public void initiateMap(Circle invisibleCircle, Pane pane) {
        RotationAnimation2.setSpeed(level);
        if(map < 4) {
            double angle;
            if(map == 3)
                angle = ((map + 1) * Math.PI / 2) / 5;
            else
                angle = ((map + 1) * Math.PI / 2) / 4;

            for(int i = 0 ; i < 5 ; i++) {
                Ball ball = new Ball(invisibleCircle.getCenterX() + invisibleCircle.getRadius() * Math.sin(i * angle), invisibleCircle.getCenterY() + invisibleCircle.getRadius() * Math.cos(i * angle), 0, Color.BLACK);
                pane.getChildren().add(ball);
                connectedBalls.add(ball);
                Line line = new Line(invisibleCircle.getCenterX(), invisibleCircle.getCenterY(), ball.getCenterX(), ball.getCenterY());
                line.setFill(Color.BLACK);
                pane.getChildren().add(line);
                ball.setLine(line);
                RotationAnimation2 rotationAnimation = new RotationAnimation2(this, ball, invisibleCircle);
                rotationAnimation.play();
            }
        }
    }

    public Text scoreText() {
        Text text = new Text();
        this.scoreText = text;
        text.setText("Score: 0");
        text.setX(30);
        text.setY(80);
        return text;
    }

    public Text getScoreText() {
        return scoreText;
    }

    public Text angleText() {
        Text text = new Text();
        this.angleText = text;
        text.setText("Angle: 0");
        text.setX(30);
        text.setY(110);
        return text;
    }

    public Text getAngleText() {
        return angleText;
    }

    public ArrayList<Timeline> getPhaseTimelines() {
        return phaseTimelines;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Pane getGamePane() {
        return gamePane;
    }

    public void setGamePane(Pane gamePane) {
        this.gamePane = gamePane;
    }

    public BorderPane getPausePane() {
        return pausePane;
    }

    public void setPausePane(BorderPane pausePane) {
        this.pausePane = pausePane;
    }

    public Circle getInvisibleCircle() {
        return invisibleCircle;
    }

    public void setInvisibleCircle(Circle invisibleCircle) {
        this.invisibleCircle = invisibleCircle;
    }

    public void setBallRemainingTimeline(Timeline ballRemainingTimeline) {
        this.ballRemainingTimeline = ballRemainingTimeline;
    }

    public int getMap() {
        return map;
    }

    public Timeline getCheckerTimeline() {
        return checkerTimeline;
    }

    public Timeline getBallRemainingTimeline() {
        return ballRemainingTimeline;
    }

    public boolean isMute() {
        return mute;
    }

    public void setMute(boolean mute) {
        this.mute = mute;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }


}
