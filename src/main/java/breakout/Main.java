package breakout;

import java.util.ArrayList;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
  public static final double BALL_RELEASE_DELAY = 1.0 / 10;
  public static final int WIDTH = 600;
  public static final int HEIGHT = 800;
  public static final int MIDDLE_WIDTH = WIDTH / 2;

  public Group root = new Group();
  public ArrayList<Ball> gameBalls = new ArrayList<>();
  public int gameBallCount = 30;
  public int ballsInPlay = 0;
  public ArrayList<Block> gameBlocks = new ArrayList<>();
  public Paddle gamePaddle;
  public double ballStartAngle = Math.PI / 2;
  public int line_length = 100;
  public Line gameAimLine = new Line(
      MIDDLE_WIDTH,
      HEIGHT - 10,
      MIDDLE_WIDTH + line_length * Math.cos(ballStartAngle),
      HEIGHT - 10 - line_length * Math.sin(ballStartAngle)
  );
  boolean line_shown = false;

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
    animation.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(SECOND_DELAY), e -> step(SECOND_DELAY)));
    animation.play();
  }

  // Create the game's "scene": what shapes will be in the game and their starting properties
  public Scene setupScene(int width, int height, Color backgroundColor) {
    for (int i = 0; i < 10; i++) {
      Block block = new Block(i * 60, 100, "square", 50, 20, BLOCK_COLOR, BALL_COLOR);
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
    if (ballsInPlay == 0) {
      if (!line_shown) {
        root.getChildren().add(gameAimLine);
        gameAimLine.setStroke(BALL_COLOR);
        line_shown = true;
      }
      Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - 10, BALL_COLOR, 5, 400, 0, 0);
      gameBalls.add(ball);
      root.getChildren().add(ball);
    }
    for (int j = 0; j < gameBalls.size(); j++) {
      Ball ball = gameBalls.get(j);
      if (ball.isIntersectingFloor(HEIGHT)) {
        gameBalls.remove(ball);
        root.getChildren().remove(ball);
        ballsInPlay--;
      }
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
          // TODO: only should update y value when you hit top or bottom of block
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
    if (ballsInPlay == 0) {
      if (code == KeyCode.SPACE) {
        // remove placeholder ball and then start play.
        root.getChildren().remove(gameBalls.getFirst());
        gameBalls.removeFirst();
        startPlay();
      }
      if (code == KeyCode.RIGHT) {
        ballStartAngle -= Math.PI / 30;
        gameAimLine.setEndX(MIDDLE_WIDTH + line_length * Math.cos(ballStartAngle));
        gameAimLine.setEndY(HEIGHT - 10 - line_length * Math.sin(ballStartAngle));
      }
      if (code == KeyCode.LEFT) {
        ballStartAngle += Math.PI / 30;
        gameAimLine.setEndX(MIDDLE_WIDTH + line_length * Math.cos(ballStartAngle));
        gameAimLine.setEndY(HEIGHT - 10 - line_length * Math.sin(ballStartAngle));
      }
    } else {
      if (code == KeyCode.RIGHT) {
        if (gamePaddle.canMoveRight(WIDTH)) {
          gamePaddle.move(1);
        }
      } else if (code == KeyCode.LEFT) {
        if (gamePaddle.canMoveLeft()) {
          gamePaddle.move(-1);
        }
      }
    }
  }

  private void startPlay() {
    Timeline addBallsTimeline = new Timeline();
    addBallsTimeline.setCycleCount(gameBallCount);
    addBallsTimeline.getKeyFrames()
        .add(new KeyFrame(Duration.seconds(BALL_RELEASE_DELAY), e -> addBall()));
    addBallsTimeline.play();
  }

  private void addBall() {
    Ball ball = new Ball(MIDDLE_WIDTH, HEIGHT - 10, BALL_COLOR, 5, 800, Math.cos(ballStartAngle),
        -Math.sin(ballStartAngle));
    gameBalls.add(ball);
    ballsInPlay++;
    root.getChildren().add(ball);
  }


  /**
   * Start the program, give complete control to JavaFX.
   */
  public static void main(String[] args) {
    launch(args);
  }

}
