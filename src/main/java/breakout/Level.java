package breakout;

import static breakout.GameConfig.BLOCK_SCORE;
import static breakout.GameConfig.HEIGHT;
import static breakout.GameConfig.WIDTH;

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
  public static final String[] POWER_UP_TYPES = new String[]{"addBall", "subtractBall",
      "scoreMultiplier", "blockDestroyer"};
  public static final String[] POWER_UP_DISPLAY_TEXT = new String[]{"+1 Ball!", "-1 Ball!",
      "x2 Score!", "Boom!"};
  public static final String IMAGE_PATH = "/images/";
  public static final int POWER_UP_PROBABILITY = 7;  // with probability 1/X place a power-up in an empty space
  public static final int BOTTOM_OFFSET = 2; // number of block heights on the bottom of screen that are not part of the level
  public static Image[] SPECIAL_BLOCK_IMAGES;
  private final int BLOCK_SIZE;
  private final ArrayList<Block> blocks;
  private final Random random;
  private final ScoreManager scoreManager;
  private final GameManager gameManager;
  private int currentLevelStartingBalls;
  private boolean currentLevelHasGravity;

  public Level(GameManager gameManager, ScoreManager scoreManager, int blockSize) {
    this.gameManager = gameManager;
    this.scoreManager = scoreManager;
    BLOCK_SIZE = blockSize;
    blocks = new ArrayList<>();
    random = new Random();
    currentLevelStartingBalls = 0;
    currentLevelHasGravity = false;
    SPECIAL_BLOCK_IMAGES = new Image[]{
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "plus.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "minus.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "star.png"))),
        new Image(Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "tnt.png"))),
        new Image(
            Objects.requireNonNull(getClass().getResourceAsStream(IMAGE_PATH + "mystery.png"))),
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
    setStartingBalls(scanner.nextInt());
    setHasGravity(scanner.nextBoolean());
    for (int j = 0; j < (HEIGHT / BLOCK_SIZE) - BOTTOM_OFFSET; j++) {
      for (int i = 0; i < WIDTH / BLOCK_SIZE; i++) {
        if (scanner.hasNextInt()) {
          int nextInt = scanner.nextInt();
          if (nextInt > 0) {
            Block block = new Block(gameManager, i * BLOCK_SIZE, j * BLOCK_SIZE,
                "default", BLOCK_SIZE, nextInt);
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

  private void createPowerups(int i, int j) {
    boolean shouldAddPowerup = (random.nextInt(POWER_UP_PROBABILITY) == 0);
    if (shouldAddPowerup) {
      int powerUpIndex = random.nextInt(POWER_UP_TYPES.length + 1);
      String specialBlockType;
      if (powerUpIndex == POWER_UP_TYPES.length) {
        specialBlockType = "mystery";
      } else {
        specialBlockType = POWER_UP_TYPES[powerUpIndex];
      }
      Block block = new Block(gameManager, i * BLOCK_SIZE, j * BLOCK_SIZE,
          specialBlockType, BLOCK_SIZE, 1,
          SPECIAL_BLOCK_IMAGES[powerUpIndex]); // for now all powerups have 1 health
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
    removeAllBlocks();
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

  public int getStartingBalls() {
    return currentLevelStartingBalls;
  }

  private void setStartingBalls(int count) {
    currentLevelStartingBalls = count;
  }

  public boolean hasGravity() {
    return currentLevelHasGravity;
  }

  private void setHasGravity(boolean hasGravity) {
    currentLevelHasGravity = hasGravity;
  }

  private boolean canDrop(Block block) {
    return block.getLayoutY() + GameConfig.BLOCK_SIZE + BOTTOM_OFFSET * GameConfig.BLOCK_SIZE
        <= GameConfig.HEIGHT;
  }

  /**
   * Check to see if the current block configuration is able to drop down one row. Power Up block
   * types do not count as part of the calculation, because they will simply be removed if the row
   * can drop
   *
   * @return true if the level can drop down one row, otherwise return false.
   */
  public boolean canDropOneLevel() {
    for (Block block : blocks) {
      if (block.getBlockType().equals("default") && !canDrop(block)) {
        return false;
      }
    }
    return true;
  }

  /**
   * First checks if the level can drop one row and gravity is enabled in the level. If gravity is
   * enabled and the blocks can drop, their y-values will be updated.
   *
   * @return true if the drop occurred, false if the drop cannot happen
   */
  public boolean checkCanDropOneRowAndAttemptDrop() {
    if (!hasGravity()) { // if the level does not have gravity, we will just return true, so that the player can continue
      return true;
    }
    if (!canDropOneLevel()) {
      return false;
    }
    Iterator<Block> iterator = blocks.iterator();
    while (iterator.hasNext()) {
      Block block = iterator.next();
      if (canDrop(block)) {
        block.setLayoutY(block.getLayoutY() + GameConfig.BLOCK_SIZE);
      } else {  // A power up is on the last row, so it cannot drop further, but it should be removed
        iterator.remove();
        this.removeBlock(block);
      }
    }
    return true;
  }


}
