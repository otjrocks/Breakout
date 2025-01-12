package breakout;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Block extends Group {

  private int HEALTH;
  private Text HEALTH_TEXT;

  public Block(int x, int y, String type, double size, int health, Color color, Color textColor) {
    if (type.equals("square")) {
      Rectangle rectangle = new Rectangle(size, size);
      rectangle.setFill(color);

      HEALTH = health;
      HEALTH_TEXT = new Text(String.valueOf(health));
      HEALTH_TEXT.setFill(textColor);
      HEALTH_TEXT.setFont(new Font((size / 5)));

      // I asked ChatGPT to help center text within a JavaFX rectangle
      double textWidth = HEALTH_TEXT.getBoundsInLocal().getWidth();
      double textHeight = HEALTH_TEXT.getBoundsInLocal().getHeight();
      HEALTH_TEXT.setX((size - textWidth) / 2);
      HEALTH_TEXT.setY((size + textHeight) / 2); // Adjust for baseline alignment

      this.getChildren().addAll(rectangle, HEALTH_TEXT);
      this.setLayoutX(x);
      this.setLayoutY(y);

    }
  }

  public double getX() {
    return this.getLayoutX();
  }

  public double getY() {
    return this.getLayoutY();
  }

  public int getHealth() {
    return HEALTH;
  }

  public void updateHealth(int health) {
    HEALTH = health;
    HEALTH_TEXT.setText(String.valueOf(health));
  }



}
