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

  public double getDirectionX() {
    return BOUNCER_DIRECTION_X;
  }

  public double getDirectionY() {
    return BOUNCER_DIRECTION_Y;
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

  /*
  Returns whether the Ball is current intersecting over past the game window boundary
   */
  public boolean isIntersectingBoundaryX(int windowWidth) {
    return (this.getCenterX() - this.getRadius() <= 0 ||
        this.getCenterX() + this.getRadius() >= windowWidth);
  }

  public boolean isIntersectingBoundaryY(int windowHeight) {
    return (this.getCenterY() - this.getRadius() <= 0 ||
        this.getCenterY() + this.getRadius() >= windowHeight);
  }

  public boolean isIntersectingBlock(Block block) {
    return this.getBoundsInParent().intersects(block.getBoundsInParent());
  }

}
