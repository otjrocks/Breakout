package breakout;

import static breakout.GameConfig.BALL_COLOR;
import static breakout.GameConfig.BALL_RADIUS;
import static breakout.GameConfig.BALL_RELEASE_DELAY;
import static breakout.GameConfig.BALL_SPEED;
import static breakout.GameConfig.MIDDLE_WIDTH;
import static breakout.GameConfig.HEIGHT;
import static breakout.GameConfig.WIDTH;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

public class Shooter extends Group {
  public static final int SHOOTER_HEIGHT_OFFSET = GameConfig.BLOCK_SIZE * (Level.BOTTOM_OFFSET - 1);

  private final GameManager gameManager;
  private final double SHOOTER_LENGTH;
  private final Color SHOOTER_COLOR;
  private double shooterAngle;
  private Ball displayBall;
  private Line shooterAim;
  private boolean isEnabled;


  public Shooter(GameManager gameManager, double shooterLength, double startingAngle, Color shooterColor) {
    this.gameManager = gameManager;
    SHOOTER_LENGTH = shooterLength;
    shooterAngle = startingAngle;
    SHOOTER_COLOR = shooterColor;
    isEnabled = false;
  }

  private void initialize() {
    displayBall = new Ball((double) WIDTH / 2, HEIGHT - SHOOTER_HEIGHT_OFFSET - 10, SHOOTER_COLOR, BALL_RADIUS, 0, 0, 0);
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

  public void enable() {
    initialize();
    isEnabled = true;
  }

  public void disable() {
    this.getChildren().removeAll(displayBall, shooterAim);
    isEnabled = false;
  }

  public boolean isEnabled() {
    return isEnabled;
  }

  public double getAngle() {
    return shooterAngle;
  }

  public void setAngle(double angle) {
    shooterAngle = angle;
    shooterAim.setEndX((double) WIDTH / 2 + SHOOTER_LENGTH * Math.cos(shooterAngle));
    shooterAim.setEndY(HEIGHT - SHOOTER_HEIGHT_OFFSET - 10 - SHOOTER_LENGTH * Math.sin(shooterAngle));
  }

  public void handleShooterActions(KeyCode code) {
    if (code == KeyCode.SPACE) {
      gameManager.decrementLives();
      launchBalls();
      gameManager.setBallsInPlay(gameManager.getGameBallCount());
      disable();
    }
    if (code == KeyCode.RIGHT) {
      setAngle(getAngle() - Math.PI / 80);
    }
    if (code == KeyCode.LEFT) {
      setAngle(getAngle() + Math.PI / 80);
    }
  }

  private void launchBalls() {
    Timeline addBallsTimeline = new Timeline();
    addBallsTimeline.setCycleCount(gameManager.getGameBallCount());
    addBallsTimeline.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(BALL_RELEASE_DELAY), e -> spawnBall()));
    addBallsTimeline.play();
  }

  private void spawnBall() {
    double startAngle = getAngle();
    Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - SHOOTER_HEIGHT_OFFSET - 10, BALL_COLOR, BALL_RADIUS, BALL_SPEED,
        Math.cos(startAngle), -Math.sin(startAngle));
    gameManager.addGameBall(ball);
    gameManager.addChildToGameRoot(ball);
  }

}
