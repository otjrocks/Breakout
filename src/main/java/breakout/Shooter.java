package breakout;

import static breakout.Main.HEIGHT;
import static breakout.Main.WIDTH;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Shooter extends Group {
  public static final int SHOOTER_HEIGHT_OFFSET = 50;

  private final double SHOOTER_LENGTH;
  private final Color SHOOTER_COLOR;
  private double shooterAngle;
  private Ball displayBall;
  private Line shooterAim;
  private boolean isEnabled;


  public Shooter(double shooterLength, double startingAngle, Color shooterColor) {
    SHOOTER_LENGTH = shooterLength;
    shooterAngle = startingAngle;
    SHOOTER_COLOR = shooterColor;
    isEnabled = false;
  }

  private void initialize() {
    displayBall = new Ball((double) WIDTH / 2, HEIGHT - SHOOTER_HEIGHT_OFFSET - 10, SHOOTER_COLOR, 5, 0, 0, 0);
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

}
