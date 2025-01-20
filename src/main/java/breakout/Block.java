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
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * The Block class is a Group that contains features such as an Image, Rectangle, and Text,
 * depending on the block type. The Block class is used for all block types, including power ups and
 * mystery blocks. The BLOCK_TYPE indicates the block type and its subsequent behaviors.
 *
 * @author Owen Jennings
 */
public class Block extends Group {
  private static final String GAME_FONT_PATH = "/fonts/";
  private static final Font boldFont = Font.loadFont(
      TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), 12);
  public static final double BLOCK_IMAGE_OFFSET = 20;
  private final int INITIAL_HEALTH;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;
  private final Text healthText;
  private final GameManager gameManager;
  private int health;
  private final Random random;

  /**
   * A constructor to create a block object. This constructor will create a block and display it
   * based on the image file provided in the constructor.
   *
   * @param gameManager:  The game manager object, which is used to control game variables
   * @param x:            The blocks x coordinate location
   * @param y:            The blocks y coordinate location
   * @param type:         The block type string. For example: a normal block would have type
   *                      "default"
   * @param size:         The size (width and height) of a block. All blocks are represented as size
   *                      X size.
   * @param health:       The blocks health or number of hit-points
   * @param blockImage:   An Image which is used when block is displayed.
   */
  public Block(GameManager gameManager, int x, int y,
      String type, double size, int health, Image blockImage) {
    this(gameManager, x, y, type, size, health);
    ImageView imageView = new ImageView(blockImage);
    imageView.setFitHeight(size - BLOCK_IMAGE_OFFSET);
    imageView.setFitWidth(size - BLOCK_IMAGE_OFFSET);
    imageView.setX(BLOCK_IMAGE_OFFSET / 2);
    imageView.setY(BLOCK_IMAGE_OFFSET / 2);
    this.getChildren().add(imageView);
  }

  /**
   * A constructor class for a block object. This constructor will create a block with a color fill
   * instead of an image. A health text will be displayed in the center of the block if the type is
   * set to default. Otherwise, only the block will display. The blocks fill color is defined by
   * GameConfig.BLOCK_COLOR
   *
   * @param gameManager:  The game manager object, which is used to control game variables
   * @param x:            The blocks x coordinate location
   * @param y:            The blocks y coordinate location
   * @param type:         The block type string. For example: a normal block would have type
   *                      "default"
   * @param size:         The size (width and height) of a block. All blocks are represented as size
   *                      X size.
   * @param health:       The blocks health or number of hit-points
   */
  public Block(GameManager gameManager, int x, int y,
      String type, double size, int health) {
    this.gameManager = gameManager;
    this.random = new Random();
    TEXT_SIZE = size;
    BLOCK_TYPE = type;
    INITIAL_HEALTH = health;
    Rectangle rectangle = new Rectangle(size, size);
    healthText = new Text(String.valueOf(health));

    if (type.equals("default")) {
      initializeDefaultBlock(size, rectangle);
    } else {
      initializeOtherBlocks(rectangle);
    }
    updateHealth(health);
    this.setLayoutX(x);
    this.setLayoutY(y);

  }

  private void initializeOtherBlocks(Rectangle rectangle) {
    rectangle.setFill(Color.rgb(0, 0, 0, 0));
    this.getChildren().addAll(rectangle);
  }

  private void initializeDefaultBlock(double size, Rectangle rectangle) {
    healthText.setFont(Font.font(boldFont.getFamily(), size / 6));
    rectangle.setFill(GameConfig.BLOCK_COLOR);
    rectangle.setStroke(GameConfig.BLOCK_BORDER_COLOR);
    rectangle.setStrokeType(StrokeType.INSIDE);
    rectangle.setStrokeWidth(2);
    this.getChildren().addAll(rectangle, healthText);
  }

  /**
   * Get the current block type
   *
   * @return A string representation of the block type. (i.e. 'default', 'mystery', 'addBall')
   */
  public String getBlockType() {
    return BLOCK_TYPE;
  }

  /**
   * Get the current block health
   *
   * @return returns the number of hits remaining for a given block
   */
  public int getHealth() {
    return health;
  }

  /**
   * Change the current block's health and health text indicator.
   *
   * @param health: The new health of the block
   */
  public void updateHealth(int health) {
    // I asked ChatGPT to help center text within a JavaFX rectangle
    this.health = health;
    healthText.setText(String.valueOf(health));
    double textWidth = healthText.getBoundsInLocal().getWidth();
    double textHeight = healthText.getBoundsInLocal().getHeight();
    healthText.setX((TEXT_SIZE - textWidth) / 2);
    healthText.setY((TEXT_SIZE + textHeight) / 2);
    healthText.setFill(getCurrentColor());
  }

  /**
   * Handle a block hit from a ball. This will handle all the impacts of a ball hit, including
   * updating its help, removing it from a level if it is out of health, updating the game score,
   * and handling any power up effects that occur.
   */
  public void hit() {
    updateHealth(getHealth() - 1);
    String blockType = getBlockType();
    gameManager.getScoreManager().incrementScore(
        blockType.equals("default") ? GameConfig.BLOCK_SCORE : GameConfig.POWERUP_SCORE);
    if (blockType.equals("mystery")) {
      handleMysteryBlock();
    }
    handlePowerUpEffect(blockType);
    if (getHealth() <= 0) {
      gameManager.getCurrentLevel().removeBlock(this);
    }
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

  private void handlePowerUpEffect(String blockType) {
    switch (blockType) {
      case "addBall" -> gameManager.increaseGameBallCount();
      case "subtractBall" -> gameManager.decreaseGameBallCount();
      case "scoreMultiplier" -> handleScoreMultiplier();
      case "blockDestroyer" -> gameManager.getCurrentLevel().hitAllDefaultBlocks();
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
        new KeyFrame(Duration.seconds(GameConfig.MYSTERY_BLOCK_DISPLAY_TEXT_TIME),
            e -> gameManager.removeChildFromGameRoot(mysteryPowerUpText)));
    removeMysteryBlockDisplayText.play();
  }

  /**
   * The score multiplier multiplies all points received by brick collisions by 2 for 5 seconds
   */
  private void handleScoreMultiplier() {
    ScoreManager scoreManager = gameManager.getScoreManager();
    scoreManager.setScoreMultiplier(scoreManager.getScoreMultiplier() * 2);
    Timeline removeScoreMultiplierEffect = new Timeline();
    removeScoreMultiplierEffect.getKeyFrames().add(
        new KeyFrame(Duration.seconds(SCORE_MULTIPLIER_TIMEOUT),
            e -> scoreManager.setScoreMultiplier(scoreManager.getScoreMultiplier() / 2)));
    removeScoreMultiplierEffect.play();
  }

}
