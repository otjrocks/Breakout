package breakout;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Main file for running game
 *
 * @author Owen Jennings
 */
public class Main extends Application {

  public static final String TITLE = "Breakout Game";
  public static final Color BACKGROUND_COLOR = new Color(0.1328, 0.1563, 0.1914, 1);
  public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final Color BLOCK_COLOR = new Color(0.2151, 0.2422, 0.2734, 1);
  public static final Color PADDLE_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final double BALL_RELEASE_DELAY = 1.0 / 10;
  public static final int WIDTH = 600;
  public static final int HEIGHT = 800;
  public static final int MIDDLE_WIDTH = WIDTH / 2;

  public Group root = new Group();
  public ArrayList<Ball> gameBalls = new ArrayList<>();
  public int gameBallCount = 100;
  public int ballsInPlay = 0;
  public Paddle gamePaddle;
  public Shooter gameShooter;
  public Level currentLevel;

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    Scene scene = setupScene(WIDTH, HEIGHT, BACKGROUND_COLOR);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
    animation.play();
  }

  // Create the game's "scene": what shapes will be in the game and their starting properties
  public Scene setupScene(int width, int height, Color backgroundColor) {
    gamePaddle = new Paddle(0, 750, 100, 10, 20, PADDLE_COLOR);
    root.getChildren().add(gamePaddle);

    gameShooter = new Shooter(WIDTH, HEIGHT, 100, Math.PI / 2, BALL_COLOR);
    root.getChildren().add(gameShooter);

    currentLevel = new Level(WIDTH, HEIGHT, 50, BLOCK_COLOR, BALL_COLOR);
    currentLevel.startLevel();
    root.getChildren().add(currentLevel);

    Scene scene = new Scene(root, width, height, backgroundColor);
    scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
    return scene;
  }

  private void step(double elapsedTime) {
    if (ballsInPlay == 0 && !gameShooter.isEnabled()) {
      gameShooter.enable();
    }
    for (int j = 0; j < gameBalls.size(); j++) {
      Ball ball = gameBalls.get(j);
      if (gamePaddle.isIntersecting(ball)) {
        ball.updateDirectionY(ball.getDirectionY() * -1);
      }
      if (ball.isIntersectingFloor(HEIGHT)) {
        gameBalls.remove(ball);
        root.getChildren().remove(ball);
        ballsInPlay--;
      }
      ArrayList<Block> gameBlocks = currentLevel.getBlocks();
      for (Block block : gameBlocks) {
        if (ball.isIntersectingBlock(block)) {
          block.updateHealth(block.getHealth() - 1);
          if (block.getHealth() <= 0) {
            currentLevel.removeBlock(block);
          }
          if (ball.isIntersectingLeftOrRight(block)) {
            ball.updateDirectionX(ball.getDirectionX() * -1);
          }
          if (ball.isIntersectingTopOrBottom(block)) {
            ball.updateDirectionY(ball.getDirectionY() * -1);
          }
          break; // break so that ball can't hit two blocks at once
          // TODO: fix interaction logic
        }
      }
      if (ball.isIntersectingBoundaryX(WIDTH)) {
        ball.updateDirectionX(ball.getDirectionX() * -1);
      }
      if (ball.isIntersectingBoundaryY(HEIGHT)) {
        ball.updateDirectionY(ball.getDirectionY() * -1);
      }
      ball.move(elapsedTime);
    }
  }

  // What to do each time a key is pressed
  private void handleKeyInput(KeyCode code) {
    if (ballsInPlay == 0) {
      if (code == KeyCode.SPACE) {
        startPlay();
        ballsInPlay = gameBallCount;
        gameShooter.disable();
      }
      if (code == KeyCode.RIGHT) {
        gameShooter.setAngle(gameShooter.getAngle() - Math.PI / 20);
      }
      if (code == KeyCode.LEFT) {
        gameShooter.setAngle(gameShooter.getAngle() + Math.PI / 20);
      }
    } else {
      if (code == KeyCode.RIGHT) {
        if (gamePaddle.canMoveRight(WIDTH)) {
          gamePaddle.move(1);
        }
      } else if (code == KeyCode.LEFT) {
        if (gamePaddle.canMoveLeft()) {
          gamePaddle.move(-1);
        }
      }
    }
  }

  private void startPlay() {
    Timeline addBallsTimeline = new Timeline();
    addBallsTimeline.setCycleCount(gameBallCount);
    addBallsTimeline.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(BALL_RELEASE_DELAY), e -> addBall()));
    addBallsTimeline.play();
  }

  private void addBall() {
    double startAngle = gameShooter.getAngle();
    Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - 10, BALL_COLOR, 5, 500, Math.cos(startAngle),
        -Math.sin(startAngle));
    gameBalls.add(ball);
    root.getChildren().add(ball);
  }

  /**
   * Start the program, give complete control to JavaFX.
   */
  public static void main(String[] args) {
    launch(args);
  }

}
