package breakout;

import java.util.ArrayList;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Level extends Group {
  private final int SCREEN_WIDTH;
  private final int SCREEN_HEIGHT;
  private final int BLOCK_SIZE;
  private final Color BLOCK_COLOR;
  private final Color TEXT_COLOR;
  private ArrayList<Block> blocks;

  public Level(int screenWidth, int screenHeight, int blockSize, Color blockColor, Color textColor) {
    SCREEN_WIDTH = screenWidth;
    SCREEN_HEIGHT = screenHeight;
    BLOCK_SIZE = blockSize;
    BLOCK_COLOR = blockColor;
    TEXT_COLOR = textColor;
    blocks = new ArrayList<>();
  }

  private void initialize() {
    for (int i = 0; i < SCREEN_WIDTH / BLOCK_SIZE; i++) {
      for (int j = 0; j < SCREEN_HEIGHT / BLOCK_SIZE - 1; j++) {
        Block block = new Block(i * BLOCK_SIZE, j * BLOCK_SIZE, "square", BLOCK_SIZE, 1, BLOCK_COLOR, TEXT_COLOR);
        this.getChildren().add(block);
        blocks.add(block);
      }
    }
  }

  public void startLevel() {
    initialize();
  }

  public boolean isComplete() {
    return blocks.isEmpty();
  }

  public ArrayList<Block> getBlocks() {
    return blocks;
  }

  public void removeBlock(Block block) {
    this.getChildren().remove(block);
    blocks.remove(block);
  }


}
