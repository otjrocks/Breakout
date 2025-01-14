package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Shooter extends Group {
  private final double SCREEN_WIDTH;
  private final double SCREEN_HEIGHT;
  private final double SHOOTER_LENGTH;
  private final Color SHOOTER_COLOR;
  private double shooterAngle;
  private Ball displayBall;
  private Line shooterAim;
  private boolean isEnabled;


  public Shooter(double screenWidth, double screenHeight, double shooterLength, double startingAngle, Color shooterColor) {
    SCREEN_WIDTH = screenWidth;
    SCREEN_HEIGHT = screenHeight;
    SHOOTER_LENGTH = shooterLength;
    shooterAngle = startingAngle;
    SHOOTER_COLOR = shooterColor;
    isEnabled = false;
  }

  private void initialize() {
    displayBall = new Ball(SCREEN_WIDTH / 2, SCREEN_HEIGHT - 10, SHOOTER_COLOR, 5, 0, 0, 0);
    shooterAim = new Line(
        SCREEN_WIDTH / 2,
        SCREEN_HEIGHT - 10,
        SCREEN_WIDTH / 2 + SHOOTER_LENGTH * Math.cos(shooterAngle),
        SCREEN_HEIGHT - 10 - SHOOTER_LENGTH * Math.sin(shooterAngle)
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
    shooterAim.setEndX(SCREEN_WIDTH / 2 + SHOOTER_LENGTH * Math.cos(shooterAngle));
    shooterAim.setEndY(SCREEN_HEIGHT - 10 - SHOOTER_LENGTH * Math.sin(shooterAngle));
  }

}
