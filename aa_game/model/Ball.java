package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Ball extends Circle {
    public static final int radius = 8;
    private int number;
    private Text numberText;
    private Line line;
    private double shootAngle;
    public Ball (double x, double y, int number, Color color) {
        super(x, y, radius);
        this.number = number;
        this.setFill(color);
        Text text = new Text(String.valueOf(number));
        if(number < 1) text.setText("");
        else {
            text.setFill(Color.WHITE);
            text.setWrappingWidth(Ball.radius * 2);
            text.prefHeight(Ball.radius * 2);
            text.setX(x - Ball.radius);
            text.setY(y + 4);
            text.setTextAlignment(TextAlignment.CENTER);
        }
        this.numberText = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Text getNumberText() {
        return numberText;
    }

    public void setNumberText(Text numberText) {
        this.numberText = numberText;
    }

    public void shoot(double shootAngle) {
        this.shootAngle = shootAngle;
        number--;
        numberText.setText(String.valueOf(number));
        if(number < 1) {
            this.setVisible(false);
            numberText.setVisible(false);
        }
    }

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public double getShootAngle() {
        return shootAngle;
    }

    public void setShootAngle(double shootAngle) {
        this.shootAngle = shootAngle;
    }
}
