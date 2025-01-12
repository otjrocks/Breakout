package breakout;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
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
    public static final Color BALL_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
    public static final Color BLOCK_COLOR = new Color(0.2151, 0.2422, 0.2734, 1);
    public static final Color PADDLE_COLOR = new Color(0.9297, 0.9297, 0.9297, 1);
    public static final int FRAMES_PER_SECOND = 60;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final int WIDTH = 600;
    public static final int HEIGHT = 800;

    public Group root = new Group();
    public ArrayList<Ball> gameBalls = new ArrayList<>();
    public ArrayList<Block> gameBlocks = new ArrayList<>();
    public Paddle gamePaddle;

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
            Ball ball = new Ball((i + 1) * 20, (i + 1) * 20 + 200, BALL_COLOR, 5, 200, 1, 1);
            gameBalls.add(ball);
            Block block = new Block(i * 60, 100, "square", 50, 50, BLOCK_COLOR, BALL_COLOR);
            gameBlocks.add(block);
        }

        gamePaddle = new Paddle(200, 750, 100, 10, 50, PADDLE_COLOR);
        root.getChildren().add(gamePaddle);

        for (Ball ball : gameBalls) {
            root.getChildren().add(ball);
        }
        for (Block block : gameBlocks) {
            root.getChildren().add(block);
        }

        Scene scene = new Scene(root, width, height, backgroundColor);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void step(double elapsedTime) {
        for (Ball ball : gameBalls) {
            for (int i = 0; i < gameBlocks.size(); i++) {
                Block block = gameBlocks.get(i);
                if (ball.isIntersectingBlock(block)) {
                    block.updateHealth(block.getHealth() - 1);
                    if (block.getHealth() <= 0) {
                        gameBlocks.remove(block);
                        root.getChildren().remove(block);
                    }
                    ball.updateDirectionX(ball.getDirectionX() * -1);
                    ball.updateDirectionY(ball.getDirectionY() * -1);
                }
            }
            if (ball.isIntersectingBoundaryX(WIDTH)) {
                ball.updateDirectionX(ball.getDirectionX() * -1);
            }
            if (ball.isIntersectingBoundaryY(HEIGHT)) {
                ball.updateDirectionY(ball.getDirectionY() * -1);
            }
            ball.move(elapsedTime);
        }
    }

    // What to do each time a key is pressed
    private void handleKeyInput(KeyCode code) {
        // NOTE new Java syntax that some prefer (but watch out for the many special cases!)
        //   https://blog.jetbrains.com/idea/2019/02/java-12-and-intellij-idea/
        switch (code) {
            case RIGHT -> {
                if (gamePaddle.canMoveRight(WIDTH)) {
                    gamePaddle.move(1);
                }
            }
            case LEFT -> {
                if (gamePaddle.canMoveLeft()) {
                    gamePaddle.move(-1);
                }
            }
        }
    }


        /**
         * Start the program, give complete control to JavaFX.
         */
    public static void main(String[] args) {
        launch(args);
    }

}
