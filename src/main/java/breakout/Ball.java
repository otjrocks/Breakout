package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Ball Class, which extends the JavaFX Circle Shape
 *
 * @author Owen Jennings
 */
public class Ball extends Circle {
  public double BOUNCER_SIZE;
  public double BOUNCER_SPEED;
  public double BOUNCER_DIRECTION_X;
  public double BOUNCER_DIRECTION_Y;
  public Color BOUNCER_COLOR;

  public Ball(double x, double y, Color color, double radius, double speed, double directionX, double directionY) {
    super(x, y, radius);
    BOUNCER_SIZE = radius;
    BOUNCER_SPEED = speed;
    BOUNCER_DIRECTION_X = directionX;
    BOUNCER_DIRECTION_Y = directionY;
    BOUNCER_COLOR = color;
    this.setFill(BOUNCER_COLOR);
  }

  public double getSize() {
    return BOUNCER_SIZE;
  }

  public void updateDirectionX(double directionX) {
    BOUNCER_DIRECTION_X = directionX;
  }

  public void updateDirectionY(double directionY) {
    BOUNCER_DIRECTION_Y = directionY;
  }

  public void updateDirection(double directionX, double directionY) {
    updateDirectionX(directionX);
    updateDirectionY(directionY);
  }

  public void updateSpeed(double speed) {
    BOUNCER_SPEED = speed;
  }

  private void moveX(double elapsedTime) {
    this.setCenterX(this.getCenterX() + BOUNCER_DIRECTION_X * BOUNCER_SPEED * elapsedTime);
  }

  private void moveY(double elapsedTime) {
    this.setCenterY(this.getCenterY() + BOUNCER_DIRECTION_Y * BOUNCER_SPEED * elapsedTime);
  }

  public void move(double elapsedTime) {
    this.moveX(elapsedTime);
    this.moveY(elapsedTime);
  }

}
