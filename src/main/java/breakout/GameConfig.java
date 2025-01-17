package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class GameConfig {

  public static final String TITLE = "Breakout Game";
  public static final int WIDTH = 600;
  public static final int HEIGHT = 800;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final Color BACKGROUND_COLOR = new Color(0.1328, 0.1563, 0.1914, 1);
  public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final Color BLOCK_COLOR = new Color(0.2151, 0.2422, 0.2734, 1);
  public static final Color BLOCK_BORDER_COLOR = new Color(0.1151, 0.1422, 0.1734, 1);
  public static final Color PADDLE_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final Color TEXT_COLOR = new Color(0, 0.6758, 0.7070, 1);
  public static final Paint MYSTERY_BLOCK_DISPLAY_TEXT_COLOR = Color.rgb(255, 193, 6);
  public static final double BALL_RELEASE_DELAY = 1.0 / 10;
  public static final int MIDDLE_WIDTH = WIDTH / 2;
  public static final int NUM_LEVELS = 2;
  public static final int SCORE_MULTIPLIER_TIMEOUT = 5;
  public static final int BLOCK_SCORE = 10;
  public static final int POWERUP_SCORE = 50;
  public static final int BLOCK_SIZE = 50;
  public static final int BALL_RADIUS = 5;
  public static final int BALL_SPEED = 320;
  public static final int INITIAL_NUM_LIVES = 5;
  public static final double INITIAL_PADDLE_WIDTH = 150;
  public static final int PADDLE_SPEED = 8;
  public static final int SHOOTER_LENGTH = 100;
  public static final double MYSTERY_BLOCK_DISPLAY_TIME = 3;
}