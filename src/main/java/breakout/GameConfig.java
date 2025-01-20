package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * A collection of all the major game constant values
 *
 * @author Owen Jennings
 */
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
  public static final int NUM_LEVELS = 4;
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
  public static final double MYSTERY_BLOCK_DISPLAY_TEXT_TIME = 3;

  public static final String gameRulesString = """
      How to Play: You begin each level with a certain number of balls and lives. To start one of your lives, use the left and right arrow keys to move the ball shooter's aim. When you have your aim in the desired position, press the space bar to start your current life. The shooter will shoot balls until you run out of balls. While your balls are in motion, you can use the arrow keys to move your paddle. The paddle allows you to bounce your balls back into the game area. Everytime a ball hits a block its health will decrease by 1. A mystery block will randomly give you a power up. You can receive power ups through randomly placed mystery or power up blocks.\n 
      The power ups have the following effects:
      - Add 1 ball to your next life's shooter.
      - Remove 1 ball from your next life's shooter.
      - Temporarily multiply all points gained by 2.
      - Destroy 1 hit from all blocks in the level.\n
      You lose the game when your run out of lives or balls in a level, or the blocks reach the ground before you destroy them (in levels with falling blocks).\n
      You can using these cheat codes if you are struggling with the game:
      - 0: Return to the home screen.
      - 1-9: Switch from your current level to the level specified.
      - X: Expand the length of your paddle.
      - C: Contract the size of your paddle.
      - L: Give +1 life, allowing you to use the shooter an additional time.
      - B: Give +1 ball, which can be used if you have a life remaining.
      - V: Take a ball away from your shooter. 
      - S: Clear all the balls currently in play. If you have a life and balls remaining, you can reshoot with the shooter. This can be useful if the ball gets stuck somewhere in the level.\n
      In game, view the level number, your current score, score multiplier, lives, and balls in your shooter on the bottom of the screen.
      P.S: Be Careful! As time progresses in your life, the balls will begin to speed up. Balls will also slightly change their path as they hit walls.""";
}