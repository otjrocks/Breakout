package breakout;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Block extends Group {

  private static final String GAME_FONT_PATH = "/fonts/";
  private static final Font boldFont = Font.loadFont(TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), 12);
  public static final double BLOCK_IMAGE_OFFSET = 20;
  private final int INITIAL_HEALTH;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;
  private final Text healthText;
  private int health;
  private ScoreManager scoreManager;
  private Level currentLevel;

  public Block(ScoreManager scoreManager, Level currentLevel, int x, int y, String type, double size, int health, Image blockImage) {
    this(x, y, type, size, health);
    this.scoreManager = scoreManager;
    this.currentLevel = currentLevel;
    ImageView imageView = new ImageView(blockImage);
    imageView.setFitHeight(size - BLOCK_IMAGE_OFFSET);
    imageView.setFitWidth(size - BLOCK_IMAGE_OFFSET);
    imageView.setX(BLOCK_IMAGE_OFFSET / 2);
    imageView.setY(BLOCK_IMAGE_OFFSET / 2);
    this.getChildren().add(imageView);
  }

  public Block(int x, int y, String type, double size, int health) {
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
      rectangle.setFill(GameManager.BLOCK_COLOR);
      rectangle.setStroke(GameManager.BLOCK_BORDER_COLOR);
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
    scoreManager.incrementScore(GameManager.BLOCK_SCORE);
    if (getHealth() <= 0) {
      currentLevel.removeBlock(this);
    }
  }


}
