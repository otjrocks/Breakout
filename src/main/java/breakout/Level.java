package breakout;

import static breakout.GameManager.BLOCK_SCORE;
import static breakout.Main.HEIGHT;
import static breakout.Main.WIDTH;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import javafx.scene.Group;
import javafx.scene.image.Image;

public class Level extends Group {

  public static final String LEVEL_FILE_PATH = "levels/";
  public static final String[] POWER_UP_TYPES = new String[]{"addBall", "subtractBall", "scoreMultiplier", "blockDestroyer"};
  public static final String IMAGE_PATH = "/images/";
  public static Image[] POWER_UP_IMAGES;
  public static final int POWER_UP_PROBABILITY = 7;  // with probability 1/X place a power-up in an empty space
  private final int BOTTOM_OFFSET = 2;
  private final int BLOCK_SIZE;
  private final ArrayList<Block> blocks;
  private final Random random;
  private final ScoreManager scoreManager;

  public Level(ScoreManager scoreManager, int blockSize) {
    this.scoreManager = scoreManager;
    BLOCK_SIZE = blockSize;
    blocks = new ArrayList<>();
    random = new Random();
    POWER_UP_IMAGES = new Image[]{
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "plus.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "minus.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "star.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "tnt.png"))),
    };
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
    blocks.clear();
    Scanner scanner = getScanner(levelNumber);
    for (int j = 0; j < HEIGHT / BLOCK_SIZE - BOTTOM_OFFSET; j++) {
      for (int i = 0; i < WIDTH / BLOCK_SIZE; i++) {
        if (scanner.hasNextInt()) {
          int nextInt = scanner.nextInt();
          if (nextInt > 0) {
            Block block = new Block(scoreManager, this, i * BLOCK_SIZE, j * BLOCK_SIZE, "default", BLOCK_SIZE, nextInt);
            blocks.add(block);
          } else {
            createPowerups(i, j);
          }
        } else {
          throw new Exception("Level file is missing blocks!");
        }
      }
    }
  }

  private void createPowerups(int i, int j ) {
    boolean shouldAddPowerup = (random.nextInt(POWER_UP_PROBABILITY) == 0);
    if (shouldAddPowerup) {
      int powerUpIndex = random.nextInt(POWER_UP_TYPES.length);
      String powerUpType = POWER_UP_TYPES[powerUpIndex];
      Block block = new Block(scoreManager, this, i * BLOCK_SIZE, j * BLOCK_SIZE, powerUpType, BLOCK_SIZE, 1, POWER_UP_IMAGES[powerUpIndex]); // for now all powerups have 1 health
      blocks.add(block);
    }
  }

  private void initialize() {
    this.getChildren().clear();
    for (Block block : blocks) {
      this.getChildren().add(block);
    }
  }

  public void startLevel(int levelNumber) throws Exception {
    createLevel(levelNumber);
    initialize();
  }

  public boolean isComplete() {
    for (Block block : blocks) {
      if (block.getBlockType().equals("default")) {
        return false;
      }
    }
    return true;
  }

  public ArrayList<Block> getBlocks() {
    return blocks;
  }

  public void removeBlock(Block block) {
    this.getChildren().remove(block);
    blocks.remove(block);
  }

  public void removeAllBlocks() {
    this.getChildren().clear();
    blocks.clear();
  }

  public void hitAllDefaultBlocks() {
    Iterator<Block> iterator = blocks.iterator();
    while (iterator.hasNext()) {
      Block block = iterator.next();
      if (block.getBlockType().equals("default")) {
        block.updateHealth(block.getHealth() - 1);
        scoreManager.incrementScore(BLOCK_SCORE);
        if (block.getHealth() <= 0) {
          iterator.remove();
          removeBlock(block);
        }
      }
    }
  }


}
