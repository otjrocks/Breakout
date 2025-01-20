package breakout;

import static breakout.GameConfig.HEIGHT;
import static breakout.GameConfig.WIDTH;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * A class to handle the major text elements on the screen and ensure their proper alignment
 *
 * @author Owen Jennings
 */
public class TextElement extends StackPane {

  private static final String GAME_FONT_PATH = "/fonts/";
  private static final Font regularFont = Font.loadFont(
      TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Regular.ttf"), 12);
  private static final Font boldFont = Font.loadFont(
      TextElement.class.getResourceAsStream(GAME_FONT_PATH + "Bold.ttf"), 12);
  public static final double WRAPPING_WIDTH = 500;
  public static final int TOP_MARGIN = 20;
  public static final int BOTTOM_MARGIN = 5;
  private final Text[] texts = new Text[3];  // Store text for top, middle, and bottom

  /**
   * Create a new text element for the game, using the games current width and height
   */
  public TextElement() {
    this.setPrefSize(WIDTH, HEIGHT);
  }


  /**
   * Create text for the top middle of the text element
   *
   * @param message: Message to display
   * @param size:    Text size
   * @param color:   Color of text
   * @param bold:    Whether the text is bold
   */
  public void setTopText(String message, int size, Color color, boolean bold) {
    removeText(texts[0]);
    Text text = createText(message, size, color, bold);
    this.getChildren().add(text);
    setAlignment(text, Pos.TOP_CENTER);
    setMargin(text, new javafx.geometry.Insets(TOP_MARGIN, 0, 0, 0));
    texts[0] = text;
  }

  /**
   * Text to display in the center of the text element
   *
   * @param message: Message to display
   * @param size:    Text size
   * @param color:   Color of text
   * @param bold:    Whether the text is bold
   */

  public void setCenterText(String message, int size, Color color, boolean bold) {
    removeText(texts[1]);
    Text text = createText(message, size, color, bold);
    text.setTextAlignment(TextAlignment.LEFT);
    this.getChildren().add(text);
    setAlignment(text, Pos.CENTER);
    texts[1] = text;
  }

  /**
   * Text to display in the bottom center of the text element
   *
   * @param message: Message to display
   * @param size:    Text size
   * @param color:   Color of text
   * @param bold:    Whether the text is bold
   */
  public void setBottomText(String message, int size, Color color, boolean bold) {
    removeText(texts[2]);
    Text text = createText(message, size, color, bold);
    this.getChildren().add(text);
    setAlignment(text, Pos.BOTTOM_CENTER);
    setMargin(text, new javafx.geometry.Insets(0, 0, BOTTOM_MARGIN, 0));
    texts[2] = text;
  }

  /**
   * Clear all the text in the TextElement
   */
  public void clearText() {
    for (Text text : texts) {
      removeText(text);
    }
  }

  private void removeText(Text text) {
    if (text != null) {
      this.getChildren().remove(text);
    }
  }

  private Text createText(String message, int size, Color color, boolean bold) {
    Text text = new Text();
    Font customFont;
    if (bold) {
      customFont = Font.font(boldFont.getName(), size);
    } else {
      customFont = Font.font(regularFont.getName(), size);
    }
    text.setFont(customFont);
    text.setText(message);
    text.setFill(color);
    text.setWrappingWidth(WRAPPING_WIDTH);
    text.setTextAlignment(TextAlignment.CENTER);
    return text;
  }
}
