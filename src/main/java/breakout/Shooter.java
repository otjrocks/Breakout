package breakout;

import static breakout.GameConfig.BALL_COLOR;
import static breakout.GameConfig.BALL_RADIUS;
import static breakout.GameConfig.BALL_RELEASE_DELAY;
import static breakout.GameConfig.BALL_SPEED;
import static breakout.GameConfig.MIDDLE_WIDTH;
import static breakout.GameConfig.HEIGHT;
import static breakout.GameConfig.WIDTH;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

/**
 * A class that extends Group and acts as a visualization for where the player will shoot balls on
 * the screen to start a round in the game
 */
public class Shooter extends Group {

  public static final int SHOOTER_HEIGHT_OFFSET = GameConfig.BLOCK_SIZE * (Level.BOTTOM_OFFSET - 1);
  public static final double SHOOTER_MOVEMENT_INTERVAL = Math.PI / 60;
  private final GameManager gameManager;
  private final double SHOOTER_LENGTH;
  private final Color SHOOTER_COLOR;
  private double shooterAngle;
  private Ball displayBall;
  private Line shooterAim;
  private boolean isEnabled;
  private final List<Ball> balls = new ArrayList<>();
  private final Timeline launchBallsTimeline = new Timeline();

  /**
   * Create a new shooter
   *
   * @param gameManager:   the current game manager
   * @param shooterLength: the length of the shooter's aim
   * @param startingAngle: the starting angle for the shooter's aim
   * @param shooterColor:  the color of the shooter's aim and display ball
   */
  public Shooter(GameManager gameManager, double shooterLength, double startingAngle,
      Color shooterColor) {
    this.gameManager = gameManager;
    SHOOTER_LENGTH = shooterLength;
    shooterAngle = startingAngle;
    SHOOTER_COLOR = shooterColor;
    isEnabled = false;
  }

  /**
   * Enables the shooter, allowing it to be seen and allowing the player to shoot balls
   */
  public void enable() {
    initialize();
    isEnabled = true;
  }

  /**
   * Disables the shooter, removing it from the view and preventing shooting
   */
  public void disable() {
    this.getChildren().removeAll(displayBall, shooterAim);
    isEnabled = false;
  }

  /**
   * Returns if the shooter is enabled
   *
   * @return true if enabled, false if disabled
   */
  public boolean isEnabled() {
    return isEnabled;
  }

  /**
   * Handle all the shooter actions: shoot, move aim left and move aim right
   *
   * @param code: key that is currently pressed
   */
  public void handleShooterActions(KeyCode code) {
    if (isEnabled) {
      if (code == KeyCode.SPACE) {
        gameManager.decrementLives();
        spawnBallsAndStartLaunchTimeline();
        gameManager.setBallsInPlay(gameManager.getGameBallCount());
        disable();
      }
      moveShooterAim(code);
    }
  }

  private void moveShooterAim(KeyCode code) {
    if (code == KeyCode.RIGHT) {
      setAngle(getAngle() - SHOOTER_MOVEMENT_INTERVAL);
    }
    if (code == KeyCode.LEFT) {
      setAngle(getAngle() + SHOOTER_MOVEMENT_INTERVAL);
    }
  }

  private double getAngle() {
    return shooterAngle;
  }

  private void setAngle(double angle) {
    if (angle < Math.PI / 20) {
      shooterAngle = Math.PI / 20;
    } else if (angle > Math.PI - Math.PI / 20) {
      shooterAngle = Math.PI - Math.PI / 20;
    } else {
      shooterAngle = angle;
    }
    shooterAim.setEndX((double) WIDTH / 2 + SHOOTER_LENGTH * Math.cos(shooterAngle));
    shooterAim.setEndY(
        HEIGHT - SHOOTER_HEIGHT_OFFSET - 10 - SHOOTER_LENGTH * Math.sin(shooterAngle));
  }

  private void initialize() {
    displayBall = new Ball(gameManager, (double) WIDTH / 2, HEIGHT - SHOOTER_HEIGHT_OFFSET - 10,
        SHOOTER_COLOR, BALL_RADIUS, 0, 0, 0);
    shooterAim = new Line(
        (double) WIDTH / 2,
        HEIGHT - SHOOTER_HEIGHT_OFFSET - 10,
        (double) WIDTH / 2 + SHOOTER_LENGTH * Math.cos(shooterAngle),
        HEIGHT - SHOOTER_HEIGHT_OFFSET - 10 - SHOOTER_LENGTH * Math.sin(shooterAngle)
    );
    displayBall.setFill(SHOOTER_COLOR);
    shooterAim.setStroke(SHOOTER_COLOR);
    this.getChildren().addAll(displayBall, shooterAim);
  }


  private void spawnBallsAndStartLaunchTimeline() {
    clearBallLaunchingTimeline();
    addBallsForNextRound();
    // Timeline to set the direction balls based on the cannon angle when shot.
    launchBallsTimeline.setCycleCount(gameManager.getGameBallCount());
    launchBallsTimeline.getKeyFrames().add(
        new KeyFrame(Duration.seconds(BALL_RELEASE_DELAY), e -> setCurrentBallIntoMotion()));
    launchBallsTimeline.play();
  }

  private void clearBallLaunchingTimeline() {
    // ensure removal of any previous balls and keyframes that
    // did not fire in case shooter was stopped in the middle of ball launching.
    balls.clear();
    launchBallsTimeline.getKeyFrames().clear();  // clear all existing keyframes
    launchBallsTimeline.stop();
  }

  private void addBallsForNextRound() {
    balls.clear();
    for (int i = 0; i < gameManager.getGameBallCount(); i++) {
      Ball ball = new Ball(gameManager, MIDDLE_WIDTH, HEIGHT - SHOOTER_HEIGHT_OFFSET - 10,
          BALL_COLOR,
          BALL_RADIUS, BALL_SPEED, 0, 0);
      gameManager.addGameBall(ball);
      gameManager.addChildToGameRoot(ball);
      balls.add(ball); // Add the ball to the list
    }
  }

  private void setCurrentBallIntoMotion() {
    if (!balls.isEmpty()) {
      Ball ball = balls.removeFirst();
      double startAngle = getAngle();
      ball.updateDirectionX(Math.cos(startAngle));
      ball.updateDirectionY(-Math.sin(startAngle));
    }
  }

}
