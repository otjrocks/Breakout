package breakout;

import java.util.Random;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Ball Class, which extends the JavaFX Circle Shape
 *
 * @author Owen Jennings
 */
public class Ball extends Circle {

  public static final double BALL_COLLISION_ENTROPY_STRENGTH = 0.01;
  public static final double BALL_SPEED_UP_CONSTANT = 0.1;
  public static final double BALL_MAX_SPEED = GameConfig.BALL_SPEED * 3;
  private double BALL_SPEED;
  private double ballDirectionX;
  private double ballDirectionY;
  private final Random random = new Random();
  private final GameManager gameManager;

  /**
   * Creates a game ball.
   *
   * @param x:          The ball's initial x coordinate.
   * @param y:          The ball's initial y coordinate.
   * @param color:      The ball's color
   * @param radius:     The ball's radius
   * @param speed:      The ball's initial speed
   * @param directionX: The ball's initial X direction
   * @param directionY: The ball's initial Y direction
   */
  public Ball(GameManager gameManager, double x, double y, Color color, double radius, double speed,
      double directionX,
      double directionY) {
    super(x, y, radius);
    BALL_SPEED = speed;
    ballDirectionX = directionX;
    ballDirectionY = directionY;
    this.setFill(color);
    this.gameManager = gameManager;
  }

  /**
   * Get current X direction
   *
   * @return X direction
   */
  public double getDirectionX() {
    return ballDirectionX;
  }

  /**
   * Get current Y direction
   *
   * @return Y direction
   */
  public double getDirectionY() {
    return ballDirectionY;
  }

  /**
   * Set new ball X direction
   *
   * @param directionX: new x direction
   */
  public void updateDirectionX(double directionX) {
    ballDirectionX = directionX;
  }

  /**
   * Set new ball Y direction
   *
   * @param directionY: new y direction
   */
  public void updateDirectionY(double directionY) {
    ballDirectionY = directionY;
  }

  /**
   * Move the ball according to its current direction, speed, and the time that has elapsed. This
   * should be called every step in the animation of the ball. This also handles the ball bouncing
   * off walls and block collisions
   *
   * @param elapsedTime: The amount of time that has elapsed since last bounce
   */
  public void bounceAndHandleCollisions(double elapsedTime) {
    bounceOffWall();
    handleBlockCollisions();
    this.moveX(elapsedTime);
    this.moveY(elapsedTime);
    increaseSpeed(); // Increase speed as time elapses to make game move quicker
  }

  /**
   * Check if the ball is intersecting the floor/bottom of the game area.
   *
   * @return true if the ball is at or below the game area floor, false otherwise
   */
  public boolean isIntersectingFloor() {
    return (this.getCenterY() + this.getRadius() >= GameConfig.HEIGHT);
  }

  private void increaseSpeed() {
    BALL_SPEED = Math.min(BALL_MAX_SPEED, BALL_SPEED + BALL_SPEED_UP_CONSTANT);
  }

  private void moveX(double elapsedTime) {
    this.setCenterX(this.getCenterX() + ballDirectionX * BALL_SPEED * elapsedTime);
  }

  private void moveY(double elapsedTime) {
    this.setCenterY(this.getCenterY() + ballDirectionY * BALL_SPEED * elapsedTime);
  }

  private boolean isIntersectingBoundaryX() {
    return (this.getCenterX() - this.getRadius() <= 0 ||
        this.getCenterX() + this.getRadius() >= GameConfig.WIDTH);
  }

  private boolean isIntersectingBoundaryY() {
    return (this.getCenterY() - this.getRadius() <= 0 ||
        this.getCenterY() + this.getRadius() >= GameConfig.HEIGHT);
  }

  private void bounceOffWall() {
    if (isIntersectingBoundaryX()) {
      updateDirectionX(getDirectionX() * -1);
      addEntropy();
    }
    if (isIntersectingBoundaryY()) {
      updateDirectionY(getDirectionY() * -1);
      addEntropy();
    }
  }

  private void handleBlockCollisions() {
    Level currentLevel = gameManager.getCurrentLevel();
    for (Block block : currentLevel.getBlocks()) {
      if (isIntersectingBlock(block)) {
        block.hit();
        // only update ball direction if a default block is hit and not any powerups
        if (block.getBlockType().equals("default") && isIntersectingLeftOrRight(block)) {
          updateDirectionX(getDirectionX() * -1);
        }
        if (block.getBlockType().equals("default") && isIntersectingTopOrBottom(block)) {
          updateDirectionY(getDirectionY() * -1);
        }
        break; // break so that ball can't hit two blocks at once
      }
    }
  }

  private boolean isIntersectingBlock(Block block) {
    return this.getBoundsInParent().intersects(block.getBoundsInParent());
  }

  // I used ChatGPT to assist with writing the logic for the intersecting TopOrBottom and LeftorRight methods
  private boolean isIntersectingTopOrBottom(Block block) {
    double ballMinY = this.getBoundsInParent().getMinY();
    double ballMaxY = this.getBoundsInParent().getMaxY();
    double blockMinY = block.getBoundsInParent().getMinY();
    double blockMaxY = block.getBoundsInParent().getMaxY();

    boolean intersectsTop = ballMaxY > blockMinY && ballMinY < blockMinY;
    boolean intersectsBottom = ballMinY < blockMaxY && ballMaxY > blockMaxY;
    return intersectsTop || intersectsBottom;
  }

  private boolean isIntersectingLeftOrRight(Block block) {
    double ballMinX = this.getBoundsInParent().getMinX();
    double ballMaxX = this.getBoundsInParent().getMaxX();
    double blockMinX = block.getBoundsInParent().getMinX();
    double blockMaxX = block.getBoundsInParent().getMaxX();

    boolean intersectsLeft = ballMinX < blockMaxX && ballMaxX > blockMaxX;
    boolean intersectsRight = ballMaxX > blockMinX && ballMinX < blockMinX;
    return intersectsLeft || intersectsRight;
  }

  /**
   * Add entropy to the balls Y direction whenever called, to make ball have more interesting
   * movement.
   */
  private void addEntropy() {
    // I asked ChatGPT for assistance in writing this code, which adds entropy to the balls movement, whenever called
    double randomDeltaY =
        (random.nextDouble()) * BALL_COLLISION_ENTROPY_STRENGTH; // only add entropy to Y value
    ballDirectionY += randomDeltaY;
  }

}

