package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Main file for running game
 *
 * @author Owen Jennings
 */
public class Main extends Application {

  public static final String TITLE = "Breakout Game";
  public static final Color BACKGROUND_COLOR = new Color(0.1328, 0.1563, 0.1914, 1);
  public static final int WIDTH = 600;
  public static final int HEIGHT = 800;
  public static final int FRAMES_PER_SECOND = 60;
  public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;

  private final Group root = new Group();

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND_COLOR);
    stage.setScene(scene);
    stage.setTitle(TITLE);
    stage.show();

    GameManager gameManager = new GameManager(scene, root);
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> {
          try {
            gameManager.step();
          } catch (Exception ex) {
            throw new RuntimeException(ex);
          }
        }));
    animation.play();
  }

  /**
   * Start the program, give complete control to JavaFX.
   */
  public static void main(String[] args) {
    launch(args);
  }
}
