package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

  private final double PADDLE_SPEED;

  public Paddle(double x, double y, double width, double height, double speed, Color color) {
    super(x, y, width, height);
    PADDLE_SPEED = speed;
    this.setFill(color);
  }

  public void move(double direction, double elapsedTime) {
    this.setX(this.getX() + PADDLE_SPEED * direction * elapsedTime);
  }

  public boolean canMoveLeft(double elapsedTime) {
    return (this.getX() - PADDLE_SPEED * elapsedTime >= 0);
  }

  public boolean canMoveRight(double width, double elapsedTime) {
    return (this.getX() + this.getWidth() + PADDLE_SPEED * elapsedTime <= width);
  }

  public boolean isIntersecting(Ball ball) {
    return this.getBoundsInParent().intersects(ball.getBoundsInParent());
  }

}
