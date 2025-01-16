package breakout;

import java.io.InputStream;
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
  private final Text HEALTH_TEXT;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;

  public Block(int x, int y, String type, double size, int health, Image blockImage) {
    this(x, y, type, size, health, Color.rgb(0,0,0,0), Color.rgb(0,0,0,0));
    ImageView imageView = new ImageView(blockImage);
    imageView.setFitHeight(size - BLOCK_IMAGE_OFFSET);
    imageView.setFitWidth(size - BLOCK_IMAGE_OFFSET);
    imageView.setX(BLOCK_IMAGE_OFFSET / 2);
    imageView.setY(BLOCK_IMAGE_OFFSET / 2);
    this.getChildren().add(imageView);
  }

  public Block(int x, int y, String type, double size, int health, Color color, Color textColor) {
    TEXT_SIZE = size;
    BLOCK_TYPE = type;
    Rectangle rectangle = new Rectangle(size, size);
    rectangle.setFill(color);

    this.health = health;
    HEALTH_TEXT = new Text(String.valueOf(health));
    Font customFont = Font.loadFont(Block.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), size / 5);
    HEALTH_TEXT.setFont(customFont);
    HEALTH_TEXT.setFill(textColor);

    // I asked ChatGPT to help center text within a JavaFX rectangle
    double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
    double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
    HEALTH_TEXT.setX((size - textWidth) / 2);
    HEALTH_TEXT.setY((size + textHeight) / 2); // Adjust for baseline alignment

    if (type.equals("default")) {
      this.getChildren().addAll(rectangle, HEALTH_TEXT);
    } else {
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

  public void updateHealth(int health) {
    this.health = health;
    HEALTH_TEXT.setText(String.valueOf(health));
    double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
    double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
    HEALTH_TEXT.setX((TEXT_SIZE - textWidth) / 2);
    HEALTH_TEXT.setY((TEXT_SIZE + textHeight) / 2);
  }


}
