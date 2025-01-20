# Breakout Design
### By: Owen Jennings
### January 20th, 2025



## Design Goals
My main goals were to write clean code by having small methods that focus on solving a single issue. Additionally, I hope to properly name and use constants, so that the game setup and features could be quickly updated by simply changing constants in a config file. 
I hoped to provide meaningful comments and understandable code, so that someone who had not viewed the code before would have a good understanding of what was going on in my design.
I also hoped to properly divide features in proper classes and objects. My hope was to create an enjoyable game with more lively gameplay. Instead of having a single ball with one hit blocks, I wanted to create a game similar to "Bricks n' Balls" that has a lot of balls and large hit point blocks, so that the game always felt exciting and fast-paced.

## High-Level Design
- The Main class of my program focuses on initializing the application window and creating an instance of the GameManager.
- The GameConfig class stores all the constant values used by multiple classes.
- The GameManager handles creating all the required objects for the game and the logic required when objects interact.
- The Level class handles creating and loading levels from files. It also handles placing power up blocks and mystery blocks randomly in a level. The level class can clear all blocks, drop blocks down a row if gravity is enabled, and check if a level is complete.
- The Paddle handles the creation, movement, and interactions that occur with the paddle. 
- The ScoreManager maintains and updates the score accounting for the current score multiplier. It also maintains and updates the current high score.
- The Shooter object when enabled allows the user to move the shooter aim and launch balls when the space bar is clicked.
- The TextElement allows the GameManager class to quickly and easily add text to various parts of the screen.
- The Ball class handles the ball's appearance, size, movement, direction, speed, and collisions with other blocks.
- The Block class is used to create all block types. The block has a type field to denote which type of block is created. Inside the block class is logic for handling block hits, mystery block effects, and power up effects.

Most objects have a reference to the GameManager, to allow them to read and update global game variables. Additionally, the ScoreManager allows multiple classes to interact with and read the current score. 

## Assumptions or Simplifications
The game and design for the most part are self-explanatory. Most of the gameplay is summarized in the "How to Play" section on the home screen, which can be accessed at any time by pressing the zero key.
Most methods are clearly named. Any methods or variables that are not self-explanatory are annotated with JavaDoc comments to allow someone new to the code base to understand what is happening.


## Changes from the Plan
I stuck with my original plan to create a game inspired by the original breakout game and the "Brinks n' Balls" variation. My plan document included many variations that I could implement, but I realistically had time only to implement the features that I found most interesting. 
Like my original plan, I created a shooter that allows the player to aim and release all the balls at the beginning of a point. I also created a paddle that is not usable until after the shooter is finished.
Initially, I thought it would be fun to have the power up blocks drop power up balls that needed to be caught by the paddle. Ultimately, I did not implement this feature, as falling power up balls would have added unnecessary complexity to my game which already has a lot of moving features.

Like my original design, I created multi-hit blocks and power up blocks. Initially, I proposed creating blocks with different shapes (i.e. Triangle), but while implementing I realized that handling the collision logic for these more complex shapes would be prohibitively complex.
I added many of the power ups that I initially proposed. I also added a 2x score multiplier which was not initially in my plan document. My level designs were highly inspired by my initial plan document, but are not one-to-one replicas of what I first designed.

Initially, I planned to create a separate Powerup class that would extend the default block type. My final design instead only used one block class and used a type string to differentiate between normal and power up blocks. In the future, I may separate the power ups into their own class, but I did not find it strictly necessary to complete my game. 
I ended up adding a feature that speeds up the ball and changes its direction slightly as it hits walls which was not in my original plan, because I thought it made the game more interesting and enjoyable to play.

## How to Add New Levels
Levels should be added into the resources path in the folder `/levels/`. The level file should follow the format X.txt, where X is the level number. 
The NUM_LEVELS constant in the GameConfig class should be updated whenever you add a new level. For specifics on how to format the level file, view the FORMAT.txt in the `/levels/` directory.