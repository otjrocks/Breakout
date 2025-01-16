package breakout;

import java.io.InputStream;
import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class TextElement extends StackPane {
  private static final String GAME_FONT_PATH = "/fonts/";
  public static final double WRAPPING_WIDTH = 400;
  public static final int TOP_MARGIN = 20;
  public static final int BOTTOM_MARGIN = 5;
  private final Text[] texts = new Text[3];  // Store text for top, middle, and bottom

  public TextElement(double screenWidth, double screenHeight) {
    this.setPrefSize(screenWidth, screenHeight);
  }

  private Text createText(String message, int size, Color color, boolean bold) {
    Text text = new Text();
    Font customFont;
    if (bold) {
      customFont = Font.loadFont(TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), size);
    } else {
      customFont = Font.loadFont(TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Regular.ttf"), size);
    }
    text.setFont(customFont);
    text.setText(message);
    text.setFill(color);
    text.setWrappingWidth(WRAPPING_WIDTH);
    text.setTextAlignment(TextAlignment.CENTER);
    return text;
  }

  public void setTopText(String message, int size, Color color, boolean bold) {
    if (texts[0] != null) {
      this.getChildren().remove(texts[0]);
    }
    Text text = createText(message, size, color, bold);
    this.getChildren().add(text);
    setAlignment(text, Pos.TOP_CENTER);
    setMargin(text, new javafx.geometry.Insets(TOP_MARGIN, 0, 0, 0));
    texts[0] = text;
  }

  public void setCenterText(String message, int size, Color color, boolean bold) {
    if (texts[1] != null) {
      this.getChildren().remove(texts[1]);
    }
    Text text = createText(message, size, color, bold);
    this.getChildren().add(text);
    setAlignment(text, Pos.CENTER);
    texts[1] = text;
  }

  public void setBottomText(String message, int size, Color color, boolean bold) {
    if (texts[2] != null) {
      this.getChildren().remove(texts[2]);
    }
    Text text = createText(message, size, color, bold);
    this.getChildren().add(text);
    setAlignment(text, Pos.BOTTOM_CENTER);
    setMargin(text, new javafx.geometry.Insets(0, 0, BOTTOM_MARGIN, 0));
    texts[2] = text;
  }

  public void clearText() {
    if (texts[0] != null) {
      this.getChildren().remove(texts[0]);
    }
    if (texts[1] != null) {
      this.getChildren().remove(texts[1]);
    }
    if (texts[2] != null) {
      this.getChildren().remove(texts[2]);
    }
  }
}
