package breakout;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.shape.Circle;


/**
 * Main file for running game
 *
 * @author Owen Jennings
 */
public class Main extends Application {
    public static final String TITLE = "Breakout Game";
    public static final Color BACKGROUND_COLOR = new Color(0.1328, 0.1563, 0.1914, 1);

    public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;


    /**
     * Initialize what will be displayed.
     */
    @Override
    public void start (Stage stage) {

        Circle shape = new Circle(200, 200, 5);
        shape.setFill(BALL_COLOR);

        Group root = new Group();
        root.getChildren().add(shape);

        Scene scene = new Scene(root, WIDTH, HEIGHT, BACKGROUND_COLOR);
        stage.setScene(scene);

        stage.setTitle(TITLE);
        stage.show();
    }
}
