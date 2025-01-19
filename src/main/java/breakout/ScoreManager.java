package breakout;

/**
 * A class to manage the score of a game.
 *
 * @author Owen Jennings
 */
public class ScoreManager {

  private int score;
  private int highScore;
  private int scoreMultiplier;

  /**
   * Create a new score manager.
   */
  public ScoreManager() {
    this.score = 0;
    this.highScore = 0;
    this.scoreMultiplier = 1;
  }

  /**
   * Get the game score
   *
   * @return integer representation of score
   */
  public int getScore() {
    return score;
  }

  /**
   * Get the current high score
   *
   * @return integer representation of the high score
   */
  public int getHighScore() {
    return highScore;
  }

  /**
   * Add to the score by amount. Multiplies by the current score multiplier amount
   *
   * @param amount: amount you which to add to the score
   */
  public void incrementScore(int amount) {
    this.score += amount * this.scoreMultiplier;
    updateHighScore();
  }

  /**
   * Get the current score multiplier value
   *
   * @return integer representation of score multiplier
   */
  public int getScoreMultiplier() {
    return scoreMultiplier;
  }

  /**
   * Set the score multiplier value. Ensures that score multiplier >= 1
   *
   * @param scoreMultiplier: new score multiplier value
   */
  public void setScoreMultiplier(int scoreMultiplier) {
    this.scoreMultiplier = Math.max(1, scoreMultiplier);
  }

  /**
   * Reset the score, but not the high score. Also resets the score multiplier
   */
  public void resetScore() {
    this.score = 0;
    this.scoreMultiplier = 1;
  }

  private void updateHighScore() {
    if (this.score > this.highScore) {
      this.highScore = this.score;
    }
  }

}
