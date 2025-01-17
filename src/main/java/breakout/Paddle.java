package breakout;

import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Paddle extends Rectangle {

  private final double PADDLE_SPEED;
  private static final double MIN_PADDLE_WIDTH = 50;
  private final GameManager gameManager;

  public Paddle(GameManager gameManager, double x, double y, double width, double height,
      double speed, Color color) {
    super(x, y, width, height);
    this.gameManager = gameManager;
    PADDLE_SPEED = speed;
    this.setFill(color);
    this.setArcHeight(height);
    this.setArcWidth(height);
  }

  public void move(Set<KeyCode> activeKeys) {
    if (gameManager.getBallsInPlay() > 0 && activeKeys.contains(KeyCode.RIGHT) && canMoveRight()) {
      move(1);
    }
    if (gameManager.getBallsInPlay() > 0 && activeKeys.contains(KeyCode.LEFT) && canMoveLeft()) {
      move(-1);
    }
  }

  private void move(double direction) {
    this.setX(this.getX() + PADDLE_SPEED * direction);
  }

  private boolean canMoveLeft() {
    return (this.getX() - PADDLE_SPEED >= 0);
  }

  private boolean canMoveRight() {
    return (this.getX() + this.getWidth() + PADDLE_SPEED <= (double) GameConfig.WIDTH);
  }

  public boolean isIntersecting(Ball ball) {
    return this.getBoundsInParent().intersects(ball.getBoundsInParent());
  }

  public void handleBallCollision(Ball ball) {
    if (isIntersecting(ball)) {
      if (ball.getCenterX() - this.getX() > this.getWidth() * 2 / 3) { // Right third of paddle
        ball.updateDirectionX(Math.abs(ball.getDirectionX()));
      } else if (ball.getCenterX() - this.getX() < this.getWidth() / 3) { // Left third of paddle
        ball.updateDirectionX(Math.abs(ball.getDirectionX()) * -1);
      }
      ball.updateDirectionY(Math.abs(ball.getDirectionY())
          * -1);  // Prevent visual glitch by always sending in negative Y direction
    }
  }

  public void expand(double changeAmount) {
    if (canExpand(changeAmount)) {
      this.setWidth(this.getWidth() + changeAmount);
      this.setX(this.getX() - changeAmount / 2);
    }
  }

  public boolean canExpand(double changeAmount) {
    return (this.getWidth() + changeAmount < GameConfig.WIDTH);
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
