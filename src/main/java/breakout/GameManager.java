package breakout;

import static breakout.Main.HEIGHT;
import static breakout.Main.WIDTH;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;

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
    gamePaddle = new Paddle(MIDDLE_WIDTH - paddleWidth / 2, HEIGHT - 50, paddleWidth, 5,
        PADDLE_SPEED, PADDLE_COLOR);
    gameShooter = new Shooter( SHOOTER_LENGTH, Math.PI / 2, BALL_COLOR);
    currentLevel = new Level(scoreManager, BLOCK_SIZE);
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
        && currentLevel.isComplete()) {  // Current level completed with lives remaining
      livesLeft = 5;
      currentLevelNumber++;
      if (currentLevelNumber > 1) {
        scoreManager.incrementScore(1000);
      }
      if (currentLevelNumber > NUM_LEVELS && isPlaying) {
        showEndScreen(true);
        isPlaying = false;
      }
      if (currentLevelNumber <= NUM_LEVELS) {
        currentLevel.startLevel(currentLevelNumber);
      }
    }

    if (ballsInPlay == 0 && livesLeft <= 0 || ballsInPlay == 0 && gameBallCount
        == 0) {  // Player ran out of lives and all balls have fallen or player has run out of balls
      currentLevel.removeAllBlocks();
      showEndScreen(false);
      isPlaying = false;
    }
  }

  private void handleGameLogic() {
    if (isPlaying) {
      handleInteractions();
      handlePaddleMovement();
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

  private void handlePaddleMovement() {
    if (ballsInPlay > 0) {
      gamePaddle.move(activeKeys);
    }
  }

  private void handleInteractions() {
    removeFallenBalls();
    updateBallPositions();
    handlePaddleInteractions();
    handlePowerupEffects();
    for (Ball ball: gameBalls) {
      ball.handleBlockCollisions(currentLevel);
    }
  }

  private void updateBallPositions() {
    for (Ball ball : gameBalls) {
      ball.bounceOffWall(WIDTH, HEIGHT);
      ball.move(Main.SECOND_DELAY);
    }
  }

  private void handlePowerupEffects() {
    for (Ball ball : gameBalls) {
      ArrayList<Block> gameBlocks = currentLevel.getBlocks();
      for (Block block : gameBlocks) {
        if (ball.isIntersectingBlock(block)) {
          String blockType = block.getBlockType();
          if (!blockType.equals("default")) {
            switch (blockType) {
              case "addBall" -> gameBallCount++;
              case "subtractBall" -> gameBallCount--;
              case "scoreMultiplier" -> handleScoreMultiplier();
              case "blockDestroyer" -> handleBlockDestroyer();
            }
            scoreManager.incrementScore(POWERUP_SCORE);
            currentLevel.removeBlock(block);
            break;
          }
        }
      }
    }
  }

  /*
  The score multiplier multiplies all points received by brick collisions by 2 for 5 seconds
   */
  private void handleScoreMultiplier() {
    scoreManager.setScoreMultiplier(scoreManager.getScoreMultiplier() * 2);
    Timeline removeScoreMultiplierEffect = new Timeline();
    removeScoreMultiplierEffect.getKeyFrames().add(
        new KeyFrame(Duration.seconds(SCORE_MULTIPLIER_TIMEOUT),
            e -> scoreManager.setScoreMultiplier(scoreManager.getScoreMultiplier() / 2)));
    removeScoreMultiplierEffect.play();
  }

  private void handleBlockDestroyer() {
    Iterator<Block> iterator = currentLevel.getBlocks().iterator();
    while (iterator.hasNext()) {
      Block block = iterator.next();
      if (block.getBlockType().equals("default")) {
        block.updateHealth(block.getHealth() - 1);
        scoreManager.incrementScore(BLOCK_SCORE);
        if (block.getHealth() <= 0) {
          iterator.remove();
          currentLevel.removeBlock(block);
        }
      }
    }
  }

  private void handlePaddleInteractions() {
    for (Ball ball : gameBalls) {
      gamePaddle.handleBallCollision(ball);
    }
  }

  private void removeFallenBalls() {
    for (int i = 0; i < gameBalls.size(); i++) {
      Ball ball = gameBalls.get(i);
      if (ball.isIntersectingFloor(HEIGHT)) {
        gameBalls.remove(ball);
        gameRoot.getChildren().remove(ball);
        ballsInPlay--;
      }
    }
  }

  // What to do each time a key is pressed
  private void handleKeyInput(KeyCode code) throws Exception {
    handleCheatCodes(code);
    if (!isPlaying && code == KeyCode.SPACE || !isPlaying && code == KeyCode.R) {
      startGame();
    } else if (isPlaying && ballsInPlay == 0) {
      handleShooterActions(code);
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
      paddleWidth += 5;
      gamePaddle.expand(5);
    }
    if (code == KeyCode.C) {
      paddleWidth -= 5;
      gamePaddle.collapse(5);
    }
  }

  private void handleShooterActions(KeyCode code) {
    if (code == KeyCode.SPACE) {
      livesLeft--;
      launchBalls();
      ballsInPlay = gameBallCount;
      gameShooter.disable();
    }
    if (code == KeyCode.RIGHT) {
      gameShooter.setAngle(gameShooter.getAngle() - Math.PI / 80);
    }
    if (code == KeyCode.LEFT) {
      gameShooter.setAngle(gameShooter.getAngle() + Math.PI / 80);
    }
  }

  private void launchBalls() {
    Timeline addBallsTimeline = new Timeline();
    addBallsTimeline.setCycleCount(gameBallCount);
    addBallsTimeline.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(BALL_RELEASE_DELAY), e -> spawnBall()));
    addBallsTimeline.play();
  }

  private void spawnBall() {
    double startAngle = gameShooter.getAngle();
    Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - 60, BALL_COLOR, BALL_RADIUS, BALL_SPEED,
        Math.cos(startAngle), -Math.sin(startAngle));
    gameBalls.add(ball);
    gameRoot.getChildren().add(ball);
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
