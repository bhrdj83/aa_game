package view;

import controller.GameMenuController;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import model.Ball;

public class RotationAnimation extends Transition {
    private GameMenuController controller;
    private int speed;
    private Ball ball;
    private Circle invisibleCircle;

    public RotationAnimation(GameMenuController controller, Ball ball, Circle invisibleCircle, int speed) {
        this.controller = controller;
        this.ball = ball;
        this.invisibleCircle = invisibleCircle;
        this.speed = speed;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
        controller.getAnimations().add(this);
    }

    @Override
    protected void interpolate(double v) {
        Rotate rotate = new Rotate(speed * 5, invisibleCircle.getCenterX(), invisibleCircle.getCenterY());
        ball.getTransforms().add(rotate);
        ball.getLine().getTransforms().add(rotate);
        ball.getNumberText().getTransforms().add(rotate);
    }
}
