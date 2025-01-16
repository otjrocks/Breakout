package breakout;

public class ScoreManager {

  private int score;
  private int highScore;
  private int scoreMultiplier;

  public ScoreManager() {
    this.score = 0;
    this.highScore = 0;
    this.scoreMultiplier = 1;
  }

  public int getScore() {
    return score;
  }

  public int getHighScore() {
    return highScore;
  }

  public void incrementScore() {
    this.score += scoreMultiplier;
    updateHighScore();
  }

  public void incrementScore(int amount) {
    this.score += amount * this.scoreMultiplier;
    updateHighScore();
  }

  private void updateHighScore() {
    if (this.score > this.highScore) {
      this.highScore = this.score;
    }
  }

  public int getScoreMultiplier() {
    return scoreMultiplier;
  }

  public void setScoreMultiplier(int scoreMultiplier) {
    this.scoreMultiplier = scoreMultiplier;
  }

  public void resetScore() {
    this.score = 0;
    this.scoreMultiplier = 1;
  }

}
