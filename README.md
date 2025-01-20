# Breakout
## By: Owen Jennings

This project implements the game of Breakout with multiple levels. This project includes various changes to the original implementation, as highlighted below.

### Timeline

 * Start Date: January 12th, 2025

 * Finish Date: January 21st, 2025

 * Hours Spent: 35 hours.



### Attributions

 * Color Scheme from [ColorHunt.](https://colorhunt.co/palette/222831393e4600adb5eeeeee)
 * Fonts via https://fonts.google.com/
   * Both the [Play](https://fonts.google.com/specimen/Play) and [Press Start 2P](https://fonts.google.com/specimen/Press+Start+2P) were used in this project.
 * Icons via https://flaticon.com/
   * All of the original power up and mystery block icons were downloaded from Flat Icon. Notably, I modified some of these SVG/PNG files from their original format to add additional information or details pertaining to this game.
 * I often referenced the [JavaFX documentation](https://docs.oracle.com/javase/8/javafx/api/) for assistance in learning the new framework.
 * I used ChatGPT for certain short code snippets (as commented in my code). 
   * Additionally, I used ChatGPT for some level inspiration and designs. For some of my levels I provided ChatGPT with a level design that I created from scratch and asked it to create a new level using the same format with a different design. I would then modify the output provided by ChatGPT to ensure the level was fair and well-balanced.

### Running the Program

 * Main class: The Main class runs the program and all of its components.

 * Data files needed: 
   * The program requires the font files Bold.ttf and Regular.ttf which are found in the resources path in the directory `/fonts/`
   * The power ups use image files found in the resources `/images/` directory. Notably, JavaFX does not display the SVG file format properly, so they must be converted to the PNG format before they are used.

 * Key/Mouse inputs:
   * This program does not utilize mouse inputs.
   * The left and right arrows are control both the ball shooter and paddle.
   * Various number and letter keys are used to control cheat keys. The space and R key are used to start the game, restart the game, and shoot balls into play from the shooter.
   * For specific instructions on how to use key/mouse inputs, view the "How to Play" section on the home screen, which is shown when the application first launches.
 * Cheat keys:
   - 0: Return to the home screen.
   - 1-9: Switch from your current level to the level specified.
   - X: Expand the length of your paddle.
   - C: Contract the size of your paddle.
   - L: Give +1 life, allowing you to use the shooter an additional time.
   - B: Give +1 ball, which can be used if you have a life remaining.
   - V: Take a ball away from your shooter.
   - S: Clear all the balls currently in play. If you have a life and balls remaining, you can reshoot with the shooter. This can be useful if the ball gets stuck somewhere in the level.


### Notes/Assumptions

 * Assumptions or Simplifications:
    * My version of breakout deviates from the original design in many ways, however I assume that most people will be able to pick up how to play quickly after reading the "How to Play" section.
    * The mechanics for how lives are handled is specifically different than the original game.
      * Unlike the original game, a life is not defined as receiving a single ball and ending when the ball reaches the floor.
      * In my game, a "life" starts with a shooter that is loaded with a predefined number of balls. You lose a life when you shoot the balls out of the  shooter and all the balls that are dispensed reach the bottom of the screen.
      * All power ups that add or remove balls do not actually impact the balls that are current in play, but rather add or decrease the number of balls that are in the shooter for your next life.
    * Some levels have blocks move down a row after each life. It is assumed that the player will know to destroy these blocks first, because they will lose if the blocks reach the floor (similar to the game Bricks n' Balls).
 * Known Bugs:
    * At higher ball speeds, the ball object collision with blocks is unstable. Occasionally, the ball collision will be registered after the ball has entered the block, causing the ball to bounce around inside the block and take away multiple health from the block in one hit.
 * Features implemented:
    * All required core features were implemented. The life mechanic feature was implemented, but varied from the original description (see assumptions).
    * For the game variations that were implemented, see the noteworthy features sections below.
 * Features unimplemented:
    * The splash screen feature extension between levels was not implemented, as I felt it was unnecessary.
 * Noteworthy Features:
   * This variation has a "Shooter" object that allows the player to aim and shoot the balls to start a life anywhere onto the screen.
     * The shooter aim is moved with the left and right arrows and shot with the space bar.
   * As time progresses in a life, the balls will gradually speed up until the reach a maximum speed to make the life get increasingly more difficult.
   * Everytime the ball hits a wall, its Y direction will slightly change randomly, making it harder for the player to track the trajectory of all the balls.
   * If a level has gravity enabled, the blocks will move down one row after each life is completed. If any default blocks reach the bottom of the screen, the player will lose the game.
   * All blocks have a specified health, can be multi-hit and display their health as a color and number in their center.
   * There are 4 power up types and blocks. Additionally, there is a mystery block type that randomly chooses one of the 4 power ups and displays on the screen which power up was selected. The 4 power ups are:
     - Add 1 ball to your next life's shooter.
     - Remove 1 ball from your next life's shooter.
     - Temporarily multiply all points gained by 2.
     - Destroy 1 hit from all blocks in the level.
   * Custom cheat keys were implemented and are described above.
   * The paddle has positional bounces, so the ball will bounce differently depending on where the ball hits.
   * The shooter that starts the game can be thought of as a custom paddle feature, as it "freezes" the paddle while the player is shooting and allows the player to aim where the balls should move in the beginning.



### Assignment Impressions

I really enjoyed this first assignment, because it allowed me to practice effective code design, while learning JavaFX and creating a cool variant of breakout. 
I was surprised how quickly my original methods grew and the immense benefits of refactoring code when I needed to create new features or update old versions of my code. 
This assignment took me longer than I expected, however a fair amount of the time I spent was devoted to additional features that I found interesting, so I was not upset with the amount of time I had to spend.

