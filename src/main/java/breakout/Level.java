package breakout;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import javafx.scene.Group;
import javafx.scene.paint.Color;


public class Level extends Group {

  private final int SCREEN_WIDTH;
  private final int SCREEN_HEIGHT;
  private final int BLOCK_SIZE;
  private final Color BLOCK_COLOR;
  private final Color TEXT_COLOR;
  private final ArrayList<Block> blocks;
  public static final String LEVEL_FILE_PATH = "levels/";

  public Level(int screenWidth, int screenHeight, int blockSize, Color blockColor,
      Color textColor) {
    SCREEN_WIDTH = screenWidth;
    SCREEN_HEIGHT = screenHeight;
    BLOCK_SIZE = blockSize;
    BLOCK_COLOR = blockColor;
    TEXT_COLOR = textColor;
    blocks = new ArrayList<>();
  }

  private Scanner getScanner(int levelNumber) throws Exception {
    String levelPath = LEVEL_FILE_PATH + levelNumber + ".txt";
    URL res;
    try {
      res = getClass().getClassLoader().getResource(levelPath);
    } catch (Exception e) {
      throw new RuntimeException(e.getMessage());
    }
    File levelFile;
    try {
      levelFile = Paths.get(res.toURI()).toFile();
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    Scanner scanner;
    try {
      scanner = new Scanner(levelFile);
    } catch (FileNotFoundException e) {
      throw new Exception(e.getMessage());
    }
    return scanner;
  }

  private void createLevel(int levelNumber) throws Exception {
    Scanner scanner = getScanner(levelNumber);
    for (int j = 0; j < SCREEN_HEIGHT / BLOCK_SIZE - 1; j++) {
      for (int i = 0; i < SCREEN_WIDTH / BLOCK_SIZE; i++) {
        if (scanner.hasNextInt()) {
          int nextInt = scanner.nextInt();
          if (nextInt > 0) {
            Block block = new Block(i * BLOCK_SIZE, j * BLOCK_SIZE, "square", BLOCK_SIZE, nextInt,
                BLOCK_COLOR, TEXT_COLOR);
            blocks.add(block);
          }
        } else {
          throw new Exception("Level file is missing blocks!");
        }
      }
    }
  }

  private void initialize() {
    for (Block block : blocks) {
      this.getChildren().add(block);
    }
  }

  public void startLevel(int levelNumber) throws Exception {
    createLevel(levelNumber);
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
