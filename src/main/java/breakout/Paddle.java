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

  public void move(double direction) {
    this.setX(this.getX() + PADDLE_SPEED * direction);
  }

  public boolean canMoveLeft() {
    return (this.getX() - PADDLE_SPEED >= 0);
  }

  public boolean canMoveRight(double width) {
    return (this.getX() + this.getWidth() + PADDLE_SPEED <= width);
  }

  public boolean isIntersecting(Ball ball) {
    return this.getBoundsInParent().intersects(ball.getBoundsInParent());
  }

}
