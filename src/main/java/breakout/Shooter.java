package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Shooter extends Group {
  private final double SCREEN_WIDTH;
  private final double SCREEN_HEIGHT;
  private final double SHOOTER_LENGTH;
  private double SHOOTER_ANGLE;
  private final Color SHOOTER_COLOR;
  private Ball displayBall;
  private Line shooterAim;
  private boolean isEnabled;


  public Shooter(double screenWidth, double screenHeight, double shooterLength, double startingAngle, Color shooterColor) {
    SCREEN_WIDTH = screenWidth;
    SCREEN_HEIGHT = screenHeight;
    SHOOTER_LENGTH = shooterLength;
    SHOOTER_ANGLE = startingAngle;
    SHOOTER_COLOR = shooterColor;
    isEnabled = false;
  }

  private void initialize() {
    displayBall = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 10, SHOOTER_COLOR, 5, 0, 0, 0);
    shooterAim = new Line(
        SCREEN_WIDTH / 2,
        SCREEN_HEIGHT - 10,
        SCREEN_WIDTH / 2 + SHOOTER_LENGTH * Math.cos(SHOOTER_ANGLE),
        SCREEN_HEIGHT - 10 - SHOOTER_LENGTH * Math.sin(SHOOTER_ANGLE)
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
    return SHOOTER_ANGLE;
  }

  public void setAngle(double angle) {
    SHOOTER_ANGLE = angle;
    shooterAim.setEndX(SCREEN_WIDTH / 2 + SHOOTER_LENGTH * Math.cos(SHOOTER_ANGLE));
    shooterAim.setEndY(SCREEN_HEIGHT - 10 - SHOOTER_LENGTH * Math.sin(SHOOTER_ANGLE));
  }

}
