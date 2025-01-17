package breakout;

import static breakout.GameConfig.HEIGHT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

public class GameManager {

  private final Group gameRoot;
  private final Scene gameScene;
  private final ArrayList<Ball> gameBalls = new ArrayList<>();
  private int gameBallCount = 1;
  private int ballsInPlay = 0;
  private Paddle gamePaddle;
  private double paddleWidth = GameConfig.INITIAL_PADDLE_WIDTH;
  private Shooter gameShooter;
  private Level currentLevel;
  private int currentLevelNumber;
  private int livesLeft = GameConfig.INITIAL_NUM_LIVES;
  private boolean isPlaying = false;
  private boolean isFirstRound = true;
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

  private Iterator<Ball> getGameBallIterator() {
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
    handleInGameLogic();
  }

  private void setupScene() {
    showStartScreen();
    initializeGame();
    initializeKeyEventHandlers();
  }


  private void initializeGame() {
    scoreManager = new ScoreManager();
    gamePaddle = new Paddle(this, GameConfig.MIDDLE_WIDTH - paddleWidth / 2, HEIGHT - GameConfig.BLOCK_SIZE * (Level.BOTTOM_OFFSET-1),
        paddleWidth, 5,
        GameConfig.PADDLE_SPEED, GameConfig.PADDLE_COLOR);
    currentLevel = new Level(this, scoreManager, GameConfig.BLOCK_SIZE);
    gameShooter = new Shooter(this, GameConfig.SHOOTER_LENGTH, Math.PI / 2, GameConfig.BALL_COLOR, currentLevel);
    gameRoot.getChildren().add(gameText);
  }

  private void initializeKeyEventHandlers() {
    gameScene.setOnKeyPressed(e -> {
      try {
        activeKeys.add(e.getCode());  // store currently held down keys
        handleKeyInput(e.getCode());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
    gameScene.setOnKeyReleased(
        e -> activeKeys.remove(e.getCode()));  // remove keys as they are released
  }

  private void startGame() throws Exception {
    currentLevelNumber = 1;
    livesLeft = 5;
    scoreManager.resetScore();
    gameText.clearText();
    currentLevel.startLevel(currentLevelNumber);
    gameBallCount = currentLevel.getStartingBalls();
    gameRoot.getChildren().add(gamePaddle);
    gameRoot.getChildren().add(gameShooter);
    gameRoot.getChildren().add(currentLevel);
    isPlaying = true;
    isFirstRound = true;
  }

  private void handleLevelTransitions() throws Exception {
    if (isPlaying && ballsInPlay == 0
        && currentLevel.isComplete()) {  // Player has successfully completed the current level
      livesLeft = 5;
      currentLevelNumber++;
      if (currentLevelNumber > 1) {
        scoreManager.incrementScore(1000);
      }
      if (currentLevelNumber
          > GameConfig.NUM_LEVELS) {  // The player has finished the last level, show congratulations/final screen.
        showEndScreen(true, "You have won the game!\nYour final score was: " + scoreManager.getScore()
            + "\nGame's High Score: " + scoreManager.getHighScore() + "\nThanks for playing!");
        isPlaying = false;
      }
      if (currentLevelNumber <= GameConfig.NUM_LEVELS) {  // Start next level for player
        currentLevel.startLevel(currentLevelNumber);
        gameBallCount = currentLevel.getStartingBalls();
      }
    }

    if (ballsInPlay == 0 && livesLeft <= 0 || ballsInPlay == 0 && gameBallCount
        == 0) {  // Player has run out of lives and all balls have fallen OR player has run out of balls
      currentLevel.removeAllBlocks();  // clear remaining blocks off screen
      showEndScreen(false, "You ran out of lives or balls and lost!\nYour final score was: "
          + scoreManager.getScore() + "\nHigh Score: " + scoreManager.getHighScore());  // show failure screen
      isPlaying = false;
    }
  }

  private void handleInGameLogic() {
    if (isPlaying) {
      handleBallInteractions();
      gamePaddle.move(activeKeys);
      gameText.setBottomText(
          "Level: " + currentLevelNumber + " - Balls: " + gameBallCount + " - Lives Remaining: "
              + livesLeft + "\nScore Multiplier: " + scoreManager.getScoreMultiplier()
              + " - Score: " + scoreManager.getScore() + " - High Score: "
              + scoreManager.getHighScore(), 16, GameConfig.TEXT_COLOR, false);
      if (gameBallCount > 0 && ballsInPlay == 0 && !gameShooter.isEnabled()) {
        gameShooter.enable();
        if (!isFirstRound && !currentLevel.checkCanDropOneRowAndAttemptDrop()) {  // end game if the row drops below the game area
          ballsInPlay = 0;
          currentLevel.removeAllBlocks();  // clear remaining blocks off screen
          showEndScreen(false, "The blocks reached the bottom of the level!\nYour final score was: "
              + scoreManager.getScore() + "\nHigh Score: " + scoreManager.getHighScore());  // show failure screen
          isPlaying = false;
        }
      }
    }
  }

  private void handleBallInteractions() {
    Iterator<Ball> ballIterator = getGameBallIterator();
    while (ballIterator.hasNext()) {
      Ball ball = ballIterator.next();
      ball.bounceAndHandleCollisions(GameConfig.SECOND_DELAY);
      gamePaddle.handleBallCollision(ball);
      // Remove balls that have reached the floor
      if (ball.isIntersectingFloor()) {
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
      isFirstRound = false;
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
    gameText.setTopText("Brick Breaker", 30, GameConfig.TEXT_COLOR, true);
    gameText.setCenterText("RULES...\nRules continues\nTODO: add rules", 20, GameConfig.BALL_COLOR,
        false);
    gameText.setBottomText("Press SPACE to START", 25, GameConfig.TEXT_COLOR, false);
  }

  private void showEndScreen(boolean isWinner, String message) {
    if (isWinner) {
      gameText.setTopText("Congrats!", 30, GameConfig.TEXT_COLOR, true);
      gameText.setCenterText(
          message, 20,
          GameConfig.BALL_COLOR, false);
    } else {
      gameText.setTopText("Oh No!", 30, GameConfig.TEXT_COLOR, true);
      gameText.setCenterText(message, 20,
          GameConfig.BALL_COLOR, false);
    }
    gameText.setBottomText("Press (R) to play again!", 25, GameConfig.TEXT_COLOR, false);
    gameRoot.getChildren().remove(gamePaddle);
    gameRoot.getChildren().remove(gameShooter);
    gameRoot.getChildren().remove(currentLevel);
  }

}
