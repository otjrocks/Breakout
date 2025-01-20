package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;


/**
 * Main file for running game
 *
 * @author Owen Jennings
 */
public class Main extends Application {

  private final Group root = new Group();

  /**
   * Initialize what will be displayed.
   */
  @Override
  public void start(Stage stage) {
    Scene scene = new Scene(root, GameConfig.WIDTH, GameConfig.HEIGHT, GameConfig.BACKGROUND_COLOR);
    stage.setScene(scene);
    stage.setTitle(GameConfig.TITLE);
    stage.show();

    GameManager gameManager = new GameManager(scene, root);
    Timeline animation = new Timeline();
    animation.setCycleCount(Timeline.INDEFINITE);
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(GameConfig.SECOND_DELAY), e -> {
          try {
            gameManager.step();  // The main step function for the game, from the game manager
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
