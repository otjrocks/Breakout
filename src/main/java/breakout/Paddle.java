package breakout;

import java.util.Set;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * A class that extends the Rectangle object and handles paddle interactions
 *
 * @author Owen Jennings
 */
public class Paddle extends Rectangle {

  public static final int PADDLE_WIDTH_MODIFIER_AMOUNT = 10;
  private final double PADDLE_SPEED;
  private static final double MIN_PADDLE_WIDTH = 50;
  private final GameManager gameManager;

  /**
   * Create a new paddle object
   *
   * @param gameManager: The game manager
   * @param x:           The starting x coordinate
   * @param y:           The starting y coordinate
   * @param width:       The starting width
   * @param height:      The height of a paddle
   * @param speed:       The speed of a paddle
   * @param color:       The fill color of the paddle
   */
  public Paddle(GameManager gameManager, double x, double y, double width, double height,
      double speed, Color color) {
    super(x, y, width, height);
    this.gameManager = gameManager;
    PADDLE_SPEED = speed;
    this.setFill(color);
    this.setArcHeight(height);
    this.setArcWidth(height);
  }

  /**
   * Move the paddle on the screen if movement is valid This method should be called within the
   * game's main step function, so that the paddle can move smoothly as the screen updates.
   *
   * @param activeKeys: A set of all currently pressed keys. This set should be updated whenever a
   *                    key is pressed or released
   */
  public void moveAndHandleExpandAndCollapse(Set<KeyCode> activeKeys) {
    if (gameManager.getBallsInPlay() > 0 && activeKeys.contains(KeyCode.RIGHT) && canMoveRight()) {
      move(1);
    }
    if (gameManager.getBallsInPlay() > 0 && activeKeys.contains(KeyCode.LEFT) && canMoveLeft()) {
      move(-1);
    }
    handleExpandAndCollapse(activeKeys);
  }

  /**
   * Handle all interactions with a ball
   *
   * @param ball: The ball that is potentially intersecting the paddle
   */
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

  private void expand() {
    if (canExpand()) {
      this.setWidth(this.getWidth() + (double) PADDLE_WIDTH_MODIFIER_AMOUNT);
      this.setX(this.getX() - (double) PADDLE_WIDTH_MODIFIER_AMOUNT / 2);
    }
  }

  private boolean canExpand() {
    return (this.getWidth() + (double) PADDLE_WIDTH_MODIFIER_AMOUNT < GameConfig.WIDTH);
  }

  private void collapse() {
    if (this.getWidth() - (double) PADDLE_WIDTH_MODIFIER_AMOUNT < MIN_PADDLE_WIDTH) {
      this.setWidth(MIN_PADDLE_WIDTH);
    } else {
      this.setX(this.getX() + (double) PADDLE_WIDTH_MODIFIER_AMOUNT / 2);
      this.setWidth(this.getWidth() - (double) PADDLE_WIDTH_MODIFIER_AMOUNT);
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

  private boolean isIntersecting(Ball ball) {
    return this.getBoundsInParent().intersects(ball.getBoundsInParent());
  }

  private void handleExpandAndCollapse(Set<KeyCode> activeKeys) {
    if (activeKeys.contains(KeyCode.X)) {
      if (canExpand()) {
        expand();
      }
    }
    if (activeKeys.contains(KeyCode.C)) {
      collapse();
    }
  }

}
