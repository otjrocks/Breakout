package breakout;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;
import javafx.util.Duration;


/**
 * Main file for running game
 *
 * @author Owen Jennings
 */
public class Main extends Application {

    public static final String TITLE = "Breakout Game";
    public static final Color BACKGROUND_COLOR = new Color(0.1328, 0.1563, 0.1914, 1);
    public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;

    public Ball[] gameBalls = new Ball[10];

    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start(Stage stage) {
        Scene scene = setupScene(WIDTH, HEIGHT, BACKGROUND_COLOR);
        stage.setScene(scene);
        stage.setTitle(TITLE);
        stage.show();

        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    public Scene setupScene(int width, int height, Color backgroundColor) {
        for (int i = 0; i < 10; i++) {
            Ball b = new Ball((i+1)*20, (i+1)*20, BALL_COLOR, 5, 100, 1, 1);
            gameBalls[i] = b;
        }

        Group root = new Group();
        for (int i = 0; i < 10; i++) {
            root.getChildren().add(gameBalls[i]);
        }
        Scene scene = new Scene(root, width, height, backgroundColor);
        return scene;
    }

    private void step(double elapsedTime) {
        for (int i = 0; i < 10; i++) {
            gameBalls[i].move(elapsedTime);
        }
    }

    /**
     * Start the program, give complete control to JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
