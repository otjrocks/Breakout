package breakout;

import static breakout.GameConfig.SCORE_MULTIPLIER_TIMEOUT;

import java.util.Random;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Block extends Group {

  private static final String GAME_FONT_PATH = "/fonts/";
  private static final Font boldFont = Font.loadFont(TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), 12);
  public static final double BLOCK_IMAGE_OFFSET = 20;
  private final int INITIAL_HEALTH;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;
  private final Text healthText;
  private final GameManager gameManager;
  private int health;
  private final ScoreManager scoreManager;
  private final Level currentLevel;
  private final Random random;

  public Block(GameManager gameManager, ScoreManager scoreManager, Level currentLevel, int x, int y, String type, double size, int health, Image blockImage) {
    this(gameManager, scoreManager, currentLevel, x, y, type, size, health);
    ImageView imageView = new ImageView(blockImage);
    imageView.setFitHeight(size - BLOCK_IMAGE_OFFSET);
    imageView.setFitWidth(size - BLOCK_IMAGE_OFFSET);
    imageView.setX(BLOCK_IMAGE_OFFSET / 2);
    imageView.setY(BLOCK_IMAGE_OFFSET / 2);
    this.getChildren().add(imageView);
  }

  public Block(GameManager gameManager, ScoreManager scoreManager, Level currentLevel, int x, int y, String type, double size, int health) {
    this.gameManager = gameManager;
    this.scoreManager = scoreManager;
    this.currentLevel = currentLevel;
    this.random = new Random();
    TEXT_SIZE = size;
    BLOCK_TYPE = type;
    INITIAL_HEALTH = health;
    Rectangle rectangle = new Rectangle(size, size);

    this.health = health;
    healthText = new Text(String.valueOf(health));
    healthText.setFont(Font.font(boldFont.getFamily(), size / 6));
    healthText.setFill(getCurrentColor());

    // I asked ChatGPT to help center text within a JavaFX rectangle
    double textWidth = healthText.getBoundsInLocal().getWidth();
    double textHeight = healthText.getBoundsInLocal().getHeight();
    healthText.setX((size - textWidth) / 2);
    healthText.setY((size + textHeight) / 2); // Adjust for baseline alignment

    if (type.equals("default")) {
      rectangle.setFill(GameConfig.BLOCK_COLOR);
      rectangle.setStroke(GameConfig.BLOCK_BORDER_COLOR);
      rectangle.setStrokeWidth(3);
      this.getChildren().addAll(rectangle, healthText);
    } else {
      rectangle.setFill(Color.rgb(0, 0, 0, 0));
      this.getChildren().addAll(rectangle);
    }
    this.setLayoutX(x);
    this.setLayoutY(y);

  }

  public String getBlockType() {
    return BLOCK_TYPE;
  }

  public int getHealth() {
    return health;
  }

  private Color getCurrentColor() {
    // I asked ChatGPT how to create a color that dynamically changes based on a ratio of health remaining
    // Calculate the health ratio (between 0 and 1)
    double healthRatio = (double) health / INITIAL_HEALTH;

    // Interpolate between green (full health) and red (no health)
    int red = (int) (255 * healthRatio * 0.8 + 150);   // Red increases as health decreases
    int green = (int) (255 * (1 - healthRatio)
        + 100);       // Green decreases as health decreases
    int blue = 100;         // Blue shifts from pastel blue to light peach
    red = Math.min(red, 255);
    green = Math.min(green, 255);
    return Color.rgb(red, green, blue);
  }

  public void updateHealth(int health) {
    this.health = health;
    healthText.setText(String.valueOf(health));
    double textWidth = healthText.getBoundsInLocal().getWidth();
    double textHeight = healthText.getBoundsInLocal().getHeight();
    healthText.setX((TEXT_SIZE - textWidth) / 2);
    healthText.setY((TEXT_SIZE + textHeight) / 2);

    healthText.setFill(getCurrentColor());
  }

  public void hit() {
    updateHealth(getHealth() - 1);
    String blockType = getBlockType();
    scoreManager.incrementScore(blockType.equals("default") ? GameConfig.BLOCK_SCORE : GameConfig.POWERUP_SCORE);
    if (blockType.equals("mystery")) {
      handleMysteryBlock();
    }
    handlePowerUpEffect(blockType);
    if (getHealth() <= 0) {
      currentLevel.removeBlock(this);
    }
  }

  public void handlePowerUpEffect(String blockType) {
    switch (blockType) {
      case "addBall" -> gameManager.increaseGameBallCount();
      case "subtractBall" -> gameManager.decreaseGameBallCount();
      case "scoreMultiplier" -> handleScoreMultiplier();
      case "blockDestroyer" -> currentLevel.hitAllDefaultBlocks();
    }
  }

  private void handleMysteryBlock() {
    String[] powerUpTypes = Level.POWER_UP_TYPES;
    int powerUpIndex = random.nextInt(powerUpTypes.length);
    String powerUpType = powerUpTypes[powerUpIndex];
    handlePowerUpEffect(powerUpType);
    setMysteryBlockTextDisplay(powerUpIndex);
  }

  private void setMysteryBlockTextDisplay(int powerUpIndex) {
    Text mysteryPowerUpText = new Text(String.valueOf(Level.POWER_UP_DISPLAY_TEXT[powerUpIndex]));
    mysteryPowerUpText.setFont(Font.font(boldFont.getFamily(), TEXT_SIZE / 10));
    mysteryPowerUpText.setFill(GameConfig.MYSTERY_BLOCK_DISPLAY_TEXT_COLOR);
    // I asked ChatGPT for assistance with centering this text based on the block's rectangle dimensions.
    double textWidth = mysteryPowerUpText.getBoundsInLocal().getWidth();
    double textHeight = mysteryPowerUpText.getBoundsInLocal().getHeight();
    mysteryPowerUpText.setX(this.getLayoutX() + (TEXT_SIZE - textWidth) / 2);
    mysteryPowerUpText.setY(this.getLayoutY() + (TEXT_SIZE + textHeight) / 2);
    gameManager.addChildToGameRoot(mysteryPowerUpText);
    Timeline removeMysteryBlockDisplayText = new Timeline();
    removeMysteryBlockDisplayText.getKeyFrames().add(
        new KeyFrame(Duration.seconds(GameConfig.MYSTERY_BLOCK_DISPLAY_TIME),
            e -> gameManager.removeChildFromGameRoot(mysteryPowerUpText)));
    removeMysteryBlockDisplayText.play();
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

}
