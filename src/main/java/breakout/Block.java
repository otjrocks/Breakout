package breakout;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.image.Image;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Block extends Group {

  private final String GAME_FONT_PATH = "/fonts/";
  public static final double BLOCK_IMAGE_OFFSET = 20;
  private int health;
  private final int INITIAL_HEALTH;
  private final Text HEALTH_TEXT;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;

  public Block(int x, int y, String type, double size, int health, Image blockImage) {
    this(x, y, type, size, health);
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
    HEALTH_TEXT = new Text(String.valueOf(health));
    Font customFont = Font.loadFont(Block.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"),
        size / 6);
    HEALTH_TEXT.setFont(customFont);
    HEALTH_TEXT.setFill(getCurrentColor());

    // I asked ChatGPT to help center text within a JavaFX rectangle
    double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
    double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
    HEALTH_TEXT.setX((size - textWidth) / 2);
    HEALTH_TEXT.setY((size + textHeight) / 2); // Adjust for baseline alignment

    if (type.equals("default")) {
      rectangle.setFill(Main.BLOCK_COLOR);
      rectangle.setStroke(Main.BLOCK_BORDER_COLOR);
      rectangle.setStrokeWidth(3);
      this.getChildren().addAll(rectangle, HEALTH_TEXT);
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
    HEALTH_TEXT.setText(String.valueOf(health));
    double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
    double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
    HEALTH_TEXT.setX((TEXT_SIZE - textWidth) / 2);
    HEALTH_TEXT.setY((TEXT_SIZE + textHeight) / 2);

    HEALTH_TEXT.setFill(getCurrentColor());
  }


}
