package breakout;

import static breakout.Main.HEIGHT;
import static breakout.Main.WIDTH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class GameManager {
  public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final Color BLOCK_COLOR = new Color(0.2151, 0.2422, 0.2734, 1);
  public static final Color BLOCK_BORDER_COLOR = new Color(0.1151, 0.1422, 0.1734, 1);
  public static final Color PADDLE_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
  public static final Color TEXT_COLOR = new Color(0, 0.6758, 0.7070, 1);

  public static final double BALL_RELEASE_DELAY = 1.0 / 10;
  public static final int MIDDLE_WIDTH = WIDTH / 2;
  public static final int NUM_LEVELS = 2;
  public static final int SCORE_MULTIPLIER_TIMEOUT = 5;
  public static final int BLOCK_SCORE = 10;
  public static final int POWERUP_SCORE = 50;
  public static final int BLOCK_SIZE = 50;
  public static final int BALL_RADIUS = 5;
  public static final int BALL_SPEED = 300;
  public static final int INITIAL_NUM_BALLS = 3;
  public static final int PADDLE_SPEED = 5;
  public static final int SHOOTER_LENGTH = 100;

  private final Group gameRoot;
  private final Scene gameScene;
  private final ArrayList<Ball> gameBalls = new ArrayList<>();
  private int gameBallCount = INITIAL_NUM_BALLS;
  private int ballsInPlay = 0;
  private Paddle gamePaddle;
  private double paddleWidth = 120;
  private Shooter gameShooter;
  private Level currentLevel;
  private int currentLevelNumber;
  private int livesLeft = 5;
  private boolean isPlaying = false;
  private final TextElement gameText = new TextElement();
  private final HashSet<KeyCode> activeKeys = new HashSet<>();
  private ScoreManager scoreManager;

  public GameManager(Scene scene, Group root) {
    this.gameScene = scene;
    this.gameRoot = root;
    setupScene();
  }

  public int getGameBallCount() {
    return gameBallCount;
  }

  public void increaseGameBallCount() {
    gameBallCount++;
  }

  public void decreaseGameBallCount() {
    gameBallCount--;
  }

  public int getBallsInPlay() {
    return ballsInPlay;
  }

  public void setBallsInPlay(int count) {
    ballsInPlay = count;
  }

  public int getLives() {
    return livesLeft;
  }

  public void setLives(int newLives) {
    livesLeft = newLives;
  }

  public void decrementLives() {
    livesLeft--;
  }

  public Iterator<Ball> getGameBallIterator() {
    return gameBalls.iterator();
  }

  public void removeGameBall(Ball ball) {
    gameBalls.remove(ball);
  }

  public void addGameBall(Ball ball) {
    gameBalls.add(ball);
  }

  public void addChildToGameRoot(Node child) {
    gameRoot.getChildren().add(child);
  }

  public void removeChildFromGameRoot(Node child) {
    gameRoot.getChildren().remove(child);
  }

  public void step() throws Exception {
    handleLevelTransitions();
    handleGameLogic();
  }

  private void setupScene() {
    initializeGame();
    showStartScreen();
    gameScene.setOnKeyPressed(e -> {
      try {
        activeKeys.add(e.getCode());
        handleKeyInput(e.getCode());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    gameScene.setOnKeyReleased(e -> activeKeys.remove(e.getCode()));
  }


  private void initializeGame() {
    scoreManager = new ScoreManager();
    gamePaddle = new Paddle(this, MIDDLE_WIDTH - paddleWidth / 2, HEIGHT - 50, paddleWidth, 5,
        PADDLE_SPEED, PADDLE_COLOR);
    gameShooter = new Shooter( this, SHOOTER_LENGTH, Math.PI / 2, BALL_COLOR);
    currentLevel = new Level(this, scoreManager, BLOCK_SIZE);
    gameRoot.getChildren().add(gameText);
  }

  private void startGame() throws Exception {
    currentLevelNumber = 1;
    livesLeft = 5;
    gameBallCount = INITIAL_NUM_BALLS;
    scoreManager.resetScore();
    gameText.clearText();
    currentLevel.startLevel(currentLevelNumber);
    gameRoot.getChildren().add(gamePaddle);
    gameRoot.getChildren().add(gameShooter);
    gameRoot.getChildren().add(currentLevel);
    isPlaying = true;
  }

  private void handleLevelTransitions() throws Exception {
    if (isPlaying && ballsInPlay == 0
        && currentLevel.isComplete()) {  // Player has successfully completed the current level
      livesLeft = 5;
      currentLevelNumber++;
      if (currentLevelNumber > 1) {
        scoreManager.incrementScore(1000);
      }
      if (currentLevelNumber > NUM_LEVELS) {  // The player has finished the last level, show congratulations/final screen.
        showEndScreen(true);
        isPlaying = false;
      }
      if (currentLevelNumber <= NUM_LEVELS) {  // Start next level for player
        currentLevel.startLevel(currentLevelNumber);
      }
    }

    if (ballsInPlay == 0 && livesLeft <= 0 || ballsInPlay == 0 && gameBallCount
        == 0) {  // Player has run out of lives and all balls have fallen OR player has run out of balls
      currentLevel.removeAllBlocks();  // clear remaining blocks off screen
      showEndScreen(false);  // show failure screen
      isPlaying = false;
    }
  }

  private void handleGameLogic() {
    if (isPlaying) {
      handleBallInteractions();
      gamePaddle.move(activeKeys);
      gameText.setBottomText(
          "Level: " + currentLevelNumber + " - Balls: " + gameBallCount + " - Lives Remaining: "
              + livesLeft + "\nScore Multiplier: " + scoreManager.getScoreMultiplier()
              + " - Score: " + scoreManager.getScore() + " - High Score: "
              + scoreManager.getHighScore(), 16, TEXT_COLOR, false);
      if (gameBallCount > 0 && ballsInPlay == 0 && !gameShooter.isEnabled()) {
        gameShooter.enable();
      }
    }
  }

  private void handleBallInteractions() {
    Iterator<Ball> ballIterator = getGameBallIterator();
    while (ballIterator.hasNext()) {
      Ball ball = ballIterator.next();
      ball.handleBlockCollisions(currentLevel);
      ball.bounceOffWall(WIDTH, HEIGHT);
      ball.move(Main.SECOND_DELAY);
      gamePaddle.handleBallCollision(ball);
      // Remove balls that have reached the floor
      if (ball.isIntersectingFloor(HEIGHT)) {
        ballIterator.remove();
        removeChildFromGameRoot(ball);
        setBallsInPlay(getBallsInPlay() - 1);
      }
    }
  }


  // What to do each time a key is pressed
  private void handleKeyInput(KeyCode code) throws Exception {
    handleCheatCodes(code);
    if (!isPlaying && code == KeyCode.SPACE || !isPlaying && code == KeyCode.R) {
      startGame();
    } else if (isPlaying && ballsInPlay == 0) {
      gameShooter.handleShooterActions(code);
    }
  }

  private void handleCheatCodes(KeyCode code) {
    if (code == KeyCode.L) {
      livesLeft++;
    }
    if (code == KeyCode.B) {
      gameBallCount++;
    }
    if (code == KeyCode.X) {
      if (gamePaddle.canExpand(10)) {
        paddleWidth += 10;
        gamePaddle.expand(10);
      }
    }
    if (code == KeyCode.C) {
      paddleWidth -= 10;
      gamePaddle.collapse(10);
    }
  }

  private void showStartScreen() {
    gameText.setTopText("Brick Breaker", 30, TEXT_COLOR, true);
    gameText.setCenterText("RULES...\nRules continues\nTODO: add rules", 20, BALL_COLOR, false);
    gameText.setBottomText("Press SPACE to START", 25, TEXT_COLOR, false);
  }

  private void showEndScreen(boolean isWinner) {
    if (isWinner) {
      gameText.setTopText("Congrats!", 30, TEXT_COLOR, true);
      gameText.setCenterText("You have won the game!\nYour final score was: " + scoreManager.getScore()
              + "\nGame's High Score: " + scoreManager.getHighScore() + "\nThanks for playing!", 20,
          BALL_COLOR, false);
    } else {
      gameText.setTopText("Oh No!", 30, TEXT_COLOR, true);
      gameText.setCenterText("You ran out of lives or balls and lost!\nYour final score was: "
              + scoreManager.getScore() + "\nHigh Score: " + scoreManager.getHighScore(), 20,
          BALL_COLOR, false);
    }
    gameText.setBottomText("Press (R) to play again!", 25, TEXT_COLOR, false);
    gameRoot.getChildren().remove(gamePaddle);
    gameRoot.getChildren().remove(gameShooter);
    gameRoot.getChildren().remove(currentLevel);
  }

}
