package breakout;

import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

  private final double PADDLE_SPEED;
  private static final double MIN_PADDLE_WIDTH = 50;

  public Paddle(double x, double y, double width, double height, double speed, Color color) {
    super(x, y, width, height);
    PADDLE_SPEED = speed;
    this.setFill(color);
    this.setArcHeight(height);
    this.setArcWidth(height);
  }

  public void move(Set<KeyCode> activeKeys) {
    if (activeKeys.contains(KeyCode.RIGHT)) {
      if (canMoveRight(Main.WIDTH)) {
        move(1);
      }
    }
    if (activeKeys.contains(KeyCode.LEFT)) {
      if (canMoveLeft()) {
        move(-1);
      }
    }
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

  public void handleBallCollision(Ball ball) {
    if (isIntersecting(ball)) {
      if (ball.getCenterX() - this.getX() > this.getWidth()*2 / 3) { // Right third of paddle
        ball.updateDirectionY(ball.getDirectionY() * -1);
        ball.updateDirectionX(Math.abs(ball.getDirectionX()));
      } else if (ball.getCenterX() - this.getX() < this.getWidth() / 3) { // Left third of paddle
        ball.updateDirectionY(ball.getDirectionY() * -1);
        ball.updateDirectionX(Math.abs(ball.getDirectionX()) * -1);
      } else {  // Center third of paddle
        ball.updateDirectionY(ball.getDirectionY() * -1);
      }
    }
  }

  public void expand(double x) {
    double expandAmount = Math.min(x, Main.WIDTH);
    this.setWidth(this.getWidth() + expandAmount);
    this.setX(this.getX() - expandAmount / 2);
  }

  public void collapse(double x) {
    if (this.getWidth() - x < MIN_PADDLE_WIDTH) {
      this.setWidth(MIN_PADDLE_WIDTH);
    } else {
      this.setX(this.getX() + x / 2);
      this.setWidth(this.getWidth() - x);
    }
  }

}
