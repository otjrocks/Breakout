package breakout;

import static breakout.GameConfig.HEIGHT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

/**
 * A class to handle all the global game logic and initialization, such as lives, balls in play and
 * different transitions. Additionally, contains content for the starting and ending screens.
 *
 * @author Owen Jennings
 */
public class GameManager {

  private final Group gameRoot;
  private final Scene gameScene;
  private final ArrayList<Ball> gameBalls = new ArrayList<>();
  private int gameBallCount = 1;
  private int ballsInPlay = 0;
  private Paddle gamePaddle;
  private Shooter gameShooter;
  private Level currentLevel;
  private int currentLevelNumber;
  private int livesLeft = GameConfig.INITIAL_NUM_LIVES;
  private boolean isPlaying = false;
  private boolean isFirstRound = true;
  private TextElement gameText = new TextElement();
  private final HashSet<KeyCode> activeKeys = new HashSet<>();
  private ScoreManager scoreManager;

  /**
   * Create a new Game Manager with the main scene and root objects
   *
   * @param scene: the main scene of the program
   * @param root:  the main root of the program
   */
  public GameManager(Scene scene, Group root) {
    this.gameScene = scene;
    this.gameRoot = root;
    setupScene();
  }

  /**
   * Get the current game's score manager object
   *
   * @return Current game's score manager object
   */
  public ScoreManager getScoreManager() {
    return scoreManager;
  }

  /**
   * Get the current level manager object
   *
   * @return Level object for the current level
   */
  public Level getCurrentLevel() {
    return currentLevel;
  }

  /**
   * Get the number of balls that the user has
   *
   * @return The game ball count
   */
  public int getGameBallCount() {
    return gameBallCount;
  }

  /**
   * Increase the number of balls a player has by 1. This will not spawn any balls, but will allow
   * them to have more balls for the next "point"
   */
  public void increaseGameBallCount() {
    gameBallCount++;
  }

  /**
   * Decrease the number of balls a player has by 1. This does not de-spawn any balls but removes 1
   * from a players current ball count
   */
  public void decreaseGameBallCount() {
    if (gameBallCount > 0) {
      gameBallCount--;
    }
  }

  /**
   * Get the number of balls that are currently in motion
   *
   * @return The total number of balls in play
   */
  public int getBallsInPlay() {
    return ballsInPlay;
  }

  /**
   * Update the number of balls that are current in motion/play
   *
   * @param count the amount of balls to set in play
   */
  public void setBallsInPlay(int count) {
    ballsInPlay = count;
  }

  /**
   * Decrement the number of lives remaining by 1.
   */
  public void decrementLives() {
    livesLeft--;
  }

  /**
   * Add a ball to the game ball list, which stores all balls currently in motion
   *
   * @param ball: the ball object to add
   */
  public void addGameBall(Ball ball) {
    gameBalls.add(ball);
  }

  /**
   * Add an object to the main game root
   *
   * @param child: the Node you wish to add
   */
  public void addChildToGameRoot(Node child) {
    gameRoot.getChildren().add(child);
  }

  /**
   * Remove an object from the main game root
   *
   * @param child: the Node you wish to remove from the game root.
   */
  public void removeChildFromGameRoot(Node child) {
    gameRoot.getChildren().remove(child);
  }

  /**
   * Take an animation step in the game. This should be called by the main file's animation
   *
   * @throws Exception: Any exception that occurs while the animation is running.
   */
  public void step() throws Exception {
    handleLevelTransitions();
    handleInGameLogic();
  }

  private Iterator<Ball> getGameBallIterator() {
    return gameBalls.iterator();
  }

  private void setupScene() {
    showStartScreen();
    initializeGame();
    initializeKeyEventHandlers();
  }


  private void initializeGame() {
    scoreManager = new ScoreManager();
    currentLevel = new Level(this, GameConfig.BLOCK_SIZE);
    initializeGameComponents();
  }

  private void initializeGameComponents() {
    gamePaddle = new Paddle(this, GameConfig.MIDDLE_WIDTH - GameConfig.INITIAL_PADDLE_WIDTH / 2,
        HEIGHT - GameConfig.BLOCK_SIZE * (Level.BOTTOM_OFFSET - 1),
        GameConfig.INITIAL_PADDLE_WIDTH, 5,
        GameConfig.PADDLE_SPEED, GameConfig.PADDLE_COLOR);
    gameShooter = new Shooter(this, GameConfig.SHOOTER_LENGTH, Math.PI / 2, GameConfig.BALL_COLOR);
    gameRoot.getChildren().add(gameText);
  }

  private void initializeKeyEventHandlers() {
    initializeOnKeyPressedHandler();
    initializeOnKeyReleasedHandler();
  }

  private void initializeOnKeyPressedHandler() {
    gameScene.setOnKeyPressed(e -> {
      try {
        activeKeys.add(e.getCode());  // store currently held down keys
        handleKeyInput(e.getCode());
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
    });
  }

  private void initializeOnKeyReleasedHandler() {
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
    checkPlayerHasCompletedLevel();
    checkPlayersOutOfLivesOrBalls();
  }

  private void checkPlayersOutOfLivesOrBalls() {
    if (ballsInPlay == 0 && livesLeft <= 0 || ballsInPlay == 0 && gameBallCount
        == 0) {  // Player has run out of lives and all balls have fallen OR player has run out of balls
      endGameAndShowEndScreen(false, "You ran out of lives or balls and lost!");
    }
  }

  private void checkPlayerHasCompletedLevel() throws Exception {
    if (isPlaying
        && currentLevel.isComplete()) { // Player has successfully completed the current level
      removeAllBallsFromPlay(getGameBallIterator()); // remove all remaining balls from previous level
      livesLeft = 5;
      currentLevelNumber++;
      if (currentLevelNumber > 1) {
        scoreManager.incrementScore(1000);
      }
      if (currentLevelNumber
          > GameConfig.NUM_LEVELS) {  // The player has finished the last level, show congratulations/final screen.
        endGameAndShowEndScreen(true, "You have won the game!");
      }
      if (currentLevelNumber <= GameConfig.NUM_LEVELS) {  // Start next level for player
        currentLevel.startLevel(currentLevelNumber);
        gameBallCount = currentLevel.getStartingBalls();
      }
    }
  }

  private void handleInGameLogic() {
    if (isPlaying) {
      handleBallInteractions();
      gamePaddle.moveAndHandleExpandAndCollapse(activeKeys);
      gameText.setBottomText(
          "Level: " + currentLevelNumber + " - Balls: " + gameBallCount + " - Lives Remaining: "
              + livesLeft + "\nScore Multiplier: " + scoreManager.getScoreMultiplier()
              + " - Score: " + scoreManager.getScore() + " - High Score: "
              + scoreManager.getHighScore(), 16, GameConfig.TEXT_COLOR, false);
      if (gameBallCount > 0 && ballsInPlay == 0 && !gameShooter.isEnabled()) {
        gameShooter.enable();
        attemptLevelDropAndCheckLossCondition();
      }
    }
  }

  private void attemptLevelDropAndCheckLossCondition() {
    if (!isFirstRound
        && !currentLevel.checkCanDropOneRowAndAttemptDrop()) {  // end game if the row drops below the game area
      ballsInPlay = 0;
      endGameAndShowEndScreen(false, "The blocks reached the bottom of the level!");
    }
  }

  private void handleBallInteractions() {
    Iterator<Ball> ballIterator = getGameBallIterator();
    while (ballIterator.hasNext()) {
      Ball ball = ballIterator.next();
      ball.bounceAndHandleCollisions(GameConfig.SECOND_DELAY);
      gamePaddle.handleBallCollision(ball);
      removeBallIfIntersectingFloor(ball, ballIterator);
    }
  }

  private void removeBallIfIntersectingFloor(Ball ball, Iterator<Ball> ballIterator) {
    // Remove balls that have reached the floor
    if (ball.isIntersectingFloor()) {
      ballIterator.remove();
      removeChildFromGameRoot(ball);
      setBallsInPlay(getBallsInPlay() - 1);
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
    if (code == KeyCode.S) {
      removeAllBallsFromPlay(getGameBallIterator());
    }
  }

  private void removeAllBallsFromPlay(Iterator<Ball> ballIterator) {
    ballsInPlay = 0;
    while (ballIterator.hasNext()) {
      removeChildFromGameRoot(ballIterator.next());
      ballIterator.remove();
    }
  }

  private void endGameAndShowEndScreen(boolean isWinner, String message) {
    currentLevel.removeAllBlocks();
    showEndScreen(isWinner, message +
        "\nYour final score was: "
        + scoreManager.getScore() + "\nHigh Score: "
        + scoreManager.getHighScore());
    isPlaying = false;
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
