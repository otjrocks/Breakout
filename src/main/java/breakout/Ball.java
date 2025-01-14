package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Ball Class, which extends the JavaFX Circle Shape
 *
 * @author Owen Jennings
 */
public class Ball extends Circle {

  private final double BALL_SIZE;
  private double BALL_SPEED;
  private double ballDirectionX;
  private double ballDirectionY;

  public Ball(double x, double y, Color color, double radius, double speed, double directionX,
      double directionY) {
    super(x, y, radius);
    BALL_SIZE = radius;
    BALL_SPEED = speed;
    ballDirectionX = directionX;
    ballDirectionY = directionY;
    this.setFill(color);
  }

  public double getSize() {
    return BALL_SIZE;
  }

  public double getDirectionX() {
    return ballDirectionX;
  }

  public double getDirectionY() {
    return ballDirectionY;
  }

  public void updateDirectionX(double directionX) {
    ballDirectionX = directionX;
  }

  public void updateDirectionY(double directionY) {
    ballDirectionY = directionY;
  }

  public void updateSpeed(double speed) {
    BALL_SPEED = speed;
  }

  private void moveX(double elapsedTime) {
    this.setCenterX(this.getCenterX() + ballDirectionX * BALL_SPEED * elapsedTime);
  }

  private void moveY(double elapsedTime) {
    this.setCenterY(this.getCenterY() + ballDirectionY * BALL_SPEED * elapsedTime);
  }

  public void move(double elapsedTime) {
    this.moveX(elapsedTime);
    this.moveY(elapsedTime);
  }

  /*
  Returns whether the Ball is current intersecting over past the game window boundary
   */
  private boolean isIntersectingBoundaryX(int windowWidth) {
    return (this.getCenterX() - this.getRadius() <= 0 ||
        this.getCenterX() + this.getRadius() >= windowWidth);
  }

  private boolean isIntersectingBoundaryY(int windowHeight) {
    return (this.getCenterY() - this.getRadius() <= 0 ||
        this.getCenterY() + this.getRadius() >= windowHeight);
  }

  public void bounceOffWall(int windowWidth, int windowHeight) {
    if (isIntersectingBoundaryX(windowWidth)) {
      updateDirectionX(getDirectionX() * -1);
    }
    if (isIntersectingBoundaryY(windowHeight)) {
      updateDirectionY(getDirectionY() * -1);
    }
  }

  public boolean isIntersectingFloor(int windowHeight) {
    return (this.getCenterY() + this.getRadius() >= windowHeight);
  }

  public boolean isIntersectingBlock(Block block) {
    return this.getBoundsInParent().intersects(block.getBoundsInParent());
  }

  // I used ChatGPT to assist with writing the logic for the intersecting TopOrBottom and LeftorRight methods
  public boolean isIntersectingTopOrBottom(Block block) {
    double ballMinY = this.getBoundsInParent().getMinY();
    double ballMaxY = this.getBoundsInParent().getMaxY();
    double blockMinY = block.getBoundsInParent().getMinY();
    double blockMaxY = block.getBoundsInParent().getMaxY();

    boolean intersectsTop = ballMaxY > blockMinY && ballMinY < blockMinY;
    boolean intersectsBottom = ballMinY < blockMaxY && ballMaxY > blockMaxY;
    return intersectsTop || intersectsBottom;
  }

  public boolean isIntersectingLeftOrRight(Block block) {
    double ballMinX = this.getBoundsInParent().getMinX();
    double ballMaxX = this.getBoundsInParent().getMaxX();
    double blockMinX = block.getBoundsInParent().getMinX();
    double blockMaxX = block.getBoundsInParent().getMaxX();

    boolean intersectsLeft = ballMinX < blockMaxX && ballMaxX > blockMaxX;
    boolean intersectsRight = ballMaxX > blockMinX && ballMinX < blockMinX;
    return intersectsLeft || intersectsRight;
  }

}
