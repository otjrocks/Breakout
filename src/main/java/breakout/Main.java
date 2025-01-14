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
  public static final Color TEXT_COLOR = new Color(0, 0.6758, 0.7070, 1);
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
  public static final double BALL_RELEASE_DELAY = 1.0 / 10;
  public static final int WIDTH = 600;
  public static final int HEIGHT = 800;
  public static final int MIDDLE_WIDTH = WIDTH / 2;
  public static final int NUM_LEVELS = 2;

  public Group root = new Group();
  public ArrayList<Ball> gameBalls = new ArrayList<>();
  public int gameBallCount = 100;
  public int ballsInPlay = 0;
  public Paddle gamePaddle;
  public Shooter gameShooter;
  public Level currentLevel;
  public int currentLevelNumber = 0;
  public int livesLeft = 5;
  public boolean isPlaying = true;
  public boolean isStarted = false;
  private final TextElement gameText = new TextElement(WIDTH, HEIGHT);
  public int score = 0;

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) throws Exception {
    Scene scene = setupScene(WIDTH, HEIGHT, BACKGROUND_COLOR);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
          try {
            step(SECOND_DELAY);
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }));
    animation.play();
  }

  // Create the game's "scene": what shapes will be in the game and their starting properties
  public Scene setupScene(int width, int height, Color backgroundColor) throws Exception {
    initializeGame();
    displayStartScreen();
    Scene scene = new Scene(root, width, height, backgroundColor);
    scene.setOnKeyPressed(e -> {
      try {
        handleKeyInput(e.getCode());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    return scene;
  }

  private void initializeGame() {
    gamePaddle = new Paddle(0, 750, 100, 5, 20, PADDLE_COLOR);
    gameShooter = new Shooter(WIDTH, HEIGHT - 50, 100, Math.PI / 2, BALL_COLOR);
    currentLevel = new Level(WIDTH, HEIGHT, 50, BLOCK_COLOR, BALL_COLOR);
    root.getChildren().add(gameText);
  }

  private void startGame() throws Exception {
    gameText.clearText();
    root.getChildren().add(gamePaddle);
    root.getChildren().add(gameShooter);
    root.getChildren().add(currentLevel);
    isStarted = true;
  }

  private void step(double elapsedTime) throws Exception {
    if (ballsInPlay == 0 && livesLeft <= 0) {
      currentLevel.removeAllBlocks();
      displayEndScreen(false);
      isPlaying = false;
    } else if (ballsInPlay == 0 && currentLevel.isComplete()) {
      livesLeft = 5;
      currentLevelNumber++;
      if (currentLevelNumber > 1) {
        score += 1000;
      }
      if (currentLevelNumber > NUM_LEVELS && isPlaying) {
        displayEndScreen(true);
        isPlaying = false;
      }
      if (currentLevelNumber <= NUM_LEVELS) {
        currentLevel.startLevel(currentLevelNumber);
      }
    }

    if (isStarted && isPlaying) {
      handleIntersections(elapsedTime);
      gameText.setBottomText(
          "Level " + currentLevelNumber +
              "\nBalls: " + gameBallCount +
              " - Lives Remaining: " + livesLeft +
              " - Score: " + score, 14, TEXT_COLOR);
      if (ballsInPlay == 0 && !gameShooter.isEnabled()) {
        gameShooter.enable();
      }
    }
  }

  // Handle all intersection logic
  private void handleIntersections(double elapsedTime) {
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
          score += 10;
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
  private void handleKeyInput(KeyCode code) throws Exception {
    if (!isStarted && code == KeyCode.SPACE) {
      startGame();
    } else if (isStarted && ballsInPlay == 0) {
      if (code == KeyCode.SPACE) {
        livesLeft--;
        startPlay();
        ballsInPlay = gameBallCount;
        gameShooter.disable();
      }
      if (code == KeyCode.RIGHT) {
        gameShooter.setAngle(gameShooter.getAngle() - Math.PI / 40);
      }
      if (code == KeyCode.LEFT) {
        gameShooter.setAngle(gameShooter.getAngle() + Math.PI / 40);
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
    Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - 60, BALL_COLOR, 5, 500, Math.cos(startAngle),
        -Math.sin(startAngle));
    gameBalls.add(ball);
    root.getChildren().add(ball);
  }

  private void displayStartScreen() {
    gameText.setTopText("Brick Breaker", 40, TEXT_COLOR);
    gameText.setCenterText("RULES...\nRules continues\nTODO: add rules", 20, TEXT_COLOR);
    gameText.setBottomText("Press SPACE to START", 20, TEXT_COLOR);
  }

  private void displayEndScreen(boolean isWinner) {
    if (isWinner) {
      gameText.setTopText("Congratulations!", 40, TEXT_COLOR);
      gameText.setCenterText("You have won the game!\nYour final score was: " + score + "\nThanks for playing!", 20, BALL_COLOR);
    } else {
      gameText.setTopText("Oh No!", 40, TEXT_COLOR);
      gameText.setCenterText("You ran out of lives!\nYour final score was: " + score, 20, BALL_COLOR);
    }
    gameText.setBottomText("Press (R) to play again!", 20, TEXT_COLOR);
    root.getChildren().remove(gamePaddle);
    root.getChildren().remove(gameShooter);
  }

  /**
   * Start the program, give complete control to JavaFX.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
