package view;

import controller.GameMenuController;
import javafx.animation.Transition;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import model.Ball;

public class RotationAnimation2 extends Transition {
    private GameMenuController controller;
    private static double speed;
    private Ball ball;
    private Circle invisibleCircle;
    private double angle;

    public RotationAnimation2(GameMenuController controller, Ball ball, Circle invisibleCircle) {
        this.controller = controller;
        this.ball = ball;
        this.invisibleCircle = invisibleCircle;
        this.setCycleDuration(Duration.millis(1000));
        this.setCycleCount(-1);
        controller.getAnimations().add(this);
        this.angle = Math.atan2(ball.getCenterX() - invisibleCircle.getCenterX(), ball.getCenterY() - invisibleCircle.getCenterY());
    }

    public static double getSpeed() {
        return RotationAnimation2.speed;
    }

    @Override
    protected void interpolate(double v) {
        angle += 0.05 * speed;
        ball.setCenterX(invisibleCircle.getCenterX() + invisibleCircle.getRadius() * Math.sin(angle));
        ball.setCenterY(invisibleCircle.getCenterY() + invisibleCircle.getRadius() * Math.cos(angle));
        ball.getNumberText().setX(invisibleCircle.getCenterX() + invisibleCircle.getRadius() * Math.sin(angle) - Ball.radius);
        ball.getNumberText().setY(invisibleCircle.getCenterY() + invisibleCircle.getRadius() * Math.cos(angle) + 4);
        ball.getLine().setEndX(invisibleCircle.getCenterX() + invisibleCircle.getRadius() * Math.sin(angle));
        ball.getLine().setEndY(invisibleCircle.getCenterY() + invisibleCircle.getRadius() * Math.cos(angle));
    }

    public static void setSpeed(double speed) {
        RotationAnimation2.speed = speed;
    }
}
