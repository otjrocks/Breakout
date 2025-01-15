package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Block extends Group {

  private int health;
  private final Text HEALTH_TEXT;
  private final double TEXT_SIZE;
  private final String BLOCK_TYPE;

  public Block(int x, int y, String type, double size, int health, Color color, Color textColor) {
    TEXT_SIZE = size;
    BLOCK_TYPE = type;
    Rectangle rectangle = new Rectangle(size, size);
    rectangle.setFill(color);

    this.health = health;
    HEALTH_TEXT = new Text(String.valueOf(health));
    HEALTH_TEXT.setFill(textColor);
    Font customFont = Font.loadFont(Main.GAME_FONT_BOLD, size / 10);
    HEALTH_TEXT.setFont(customFont);

    // I asked ChatGPT to help center text within a JavaFX rectangle
    double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
    double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
    HEALTH_TEXT.setX((size - textWidth) / 2);
    HEALTH_TEXT.setY((size + textHeight) / 2); // Adjust for baseline alignment

    this.getChildren().addAll(rectangle, HEALTH_TEXT);
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
