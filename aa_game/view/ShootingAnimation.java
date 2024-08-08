package view;

import controller.GameMenuController;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import model.Ball;

import java.util.Random;

public class ShootingAnimation extends Transition {
    private GameMenuController controller;
    private Circle invisibleCircle;
    private Ball shooter;
    private Ball ball;
    private Pane pane;

    public ShootingAnimation(GameMenuController controller, Ball shooter, Ball ball, Pane pane, Circle invisibleCircle) {
        this.controller = controller;
        this.invisibleCircle = invisibleCircle;
        this.ball = ball;
        this.pane = pane;
        this.shooter = shooter;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
        controller.getAnimations().add(this);
    }

    @Override
    protected void interpolate(double v) {
        boolean lost = false;
        Ball connectedBall;
        for(int i = 0 ; i < controller.getConnectedBalls().size() ; i++) {
            connectedBall = controller.getConnectedBalls().get(i);
            if(ball.getBoundsInParent().intersects(connectedBall.getBoundsInParent())) {
                lost = true;
                break;
            }
        }
        if(ball.getCenterX() + ball.getRadius() >= 600 ||
            ball.getCenterX() - ball.getRadius() <= 0 ||
            ball.getCenterY() - ball.getRadius() <= 0 ||
            ball.getCenterY() + ball.getRadius() >= 500)
            lost = true;
        if(lost) {
            pane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
            for(Transition transition : controller.getAnimations())
                transition.stop();
            controller.getTimerTimeline().stop();
            Timeline wait = new Timeline(new KeyFrame(Duration.millis(750), actionEvent -> {
                try {
                    controller.endGame(false, shooter);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }));
            wait.setCycleCount(1);
            wait.play();
        }
        else {
            Shape intersect = Shape.intersect(invisibleCircle, ball);
            if (intersect.getBoundsInLocal().getWidth() != -1) {
                this.stop();
                Line line = new Line(invisibleCircle.getCenterX(), invisibleCircle.getCenterY(), ball.getCenterX(), ball.getCenterY());
                line.setFill(Color.BLACK);
                pane.getChildren().add(line);
                ball.setLine(line);
                controller.getConnectedBalls().add(ball);
                controller.getScoreText().setText("Score: " + (controller.getConnectedBalls().size() - 5));
                controller.getFreezeProgressBar().setProgress(Math.min(1, controller.getFreezeProgressBar().getProgress() + 0.2));
                if((controller.getConnectedBalls().size() - 5) >= 3 * controller.getBallsCount() / 4 &&
                        (controller.getConnectedBalls().size() - 5) < (3 * controller.getBallsCount() / 4) + 1) controller.getPhaseTimelines().add(startPhase4());
                else if((controller.getConnectedBalls().size() - 5) >= controller.getBallsCount() / 2 &&
                        (controller.getConnectedBalls().size() - 5) < (controller.getBallsCount() / 2) + 1) controller.getPhaseTimelines().add(startPhase3());
                else if((controller.getConnectedBalls().size() - 5) >= controller.getBallsCount() / 4 &&
                        (controller.getConnectedBalls().size() - 5) < (controller.getBallsCount() / 4) + 1) {
                    controller.getPhaseTimelines().add(startPhase2Rotate());
                    controller.getPhaseTimelines().add(startPhase2Balls());
                }
                RotationAnimation2 rotationAnimation = new RotationAnimation2(controller, ball, invisibleCircle);
                rotationAnimation.play();
            }
        }
        int sign;
        if(controller.isMultiplayer() && shooter.getCenterY() < 250) sign = -1;
        else sign = 1;
        ball.setCenterY(ball.getCenterY() - 8 * sign);
        ball.setCenterX(ball.getCenterX() + 8 * Math.tan(shooter.getShootAngle()));
        ball.getNumberText().setY(ball.getNumberText().getY() - 8 * sign);
        ball.getNumberText().setX(ball.getNumberText().getX() + 8 * Math.tan(shooter.getShootAngle()));
    }

    private Timeline startPhase2Balls() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (Ball connectedBall : controller.getConnectedBalls()) {
                if(connectedBall.getRadius() < 1.1 * Ball.radius)
                    connectedBall.setRadius(1.1 * connectedBall.getRadius());
                else
                    connectedBall.setRadius(Ball.radius);
            }
            outer:
            for (Ball connectedBall : controller.getConnectedBalls()) {
                for (Ball otherConnectedBall : controller.getConnectedBalls()) {
                    if(!otherConnectedBall.equals(connectedBall)) {
                        if (connectedBall.getBoundsInParent().intersects(otherConnectedBall.getBoundsInParent())) {
                            pane.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
                            for(Transition transition : controller.getAnimations())
                                transition.stop();
                            controller.getTimerTimeline().stop();
                            Timeline wait = new Timeline(new KeyFrame(Duration.millis(750), actionEvent1 -> {
                                try {
                                    controller.endGame(false, shooter);
                                } catch (Exception e) {
                                    throw new RuntimeException(e);
                                }
                            }));
                            wait.setCycleCount(1);
                            wait.play();
                        }
                    }
                }
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        return timeline;
    }

    private Timeline startPhase2Rotate() {
        Random random = new Random();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(4000), actionEvent -> {
            RotationAnimation2.setSpeed(-RotationAnimation2.getSpeed());
            this.setCycleDuration(Duration.millis(4000 + 2000 * random.nextDouble()));
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        return timeline;
    }

    private Timeline startPhase3() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), actionEvent -> {
            for (Ball connectedBall : controller.getConnectedBalls()) {
                if(connectedBall.isVisible()) {
                    connectedBall.setVisible(false);
                    connectedBall.getNumberText().setVisible(false);
                    connectedBall.getLine().setVisible(false);
                }
                else {
                    connectedBall.setVisible(true);
                    connectedBall.getNumberText().setVisible(true);
                    connectedBall.getLine().setVisible(true);
                }
            }
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        return timeline;
    }

    private Timeline startPhase4() {
        controller.setAngle(-15);
        final int[] isIncreasing = {1};
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis((4 - controller.getLevel()) * 2000 / 300), actionEvent -> {
            if((int) controller.getAngle() == -15) isIncreasing[0] = 1;
            else if((int) controller.getAngle() == 15) isIncreasing[0] = -1;
            controller.setAngle(controller.getAngle() +  isIncreasing[0] * 0.1);
            controller.getAngleText().setText("Angle: " + String.format("%.1f", controller.getAngle()));
        }));
        timeline.setCycleCount(-1);
        timeline.play();
        return timeline;
    }
}
