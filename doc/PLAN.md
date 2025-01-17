  # Breakout Plan
### By: Owen Jennings

## Interesting Breakout Variants
My two favorite variants of the breakout game are **Bricks n Balls** and **Jet Ball**.
1. With Bricks n Balls, I like how the level progresses by moving one *level* down after each play. I also like how there are simple block types with the number of hits left until they are destroyed. I enjoy the game mechanic where you are able to aim and then shoot all your balls for a round. I think that this game can be improved by making an "easy" and "hard" version, where the aim path feature is disabled in the hard mode. The game can also include a speed-up button to make the balls move faster so the player can move to their next point. I like how the game has +1 ball multiplier blocks, a laser that hits all blocks in a diagonal, and a triangle and square variant.

2. With Jet Ball, I like how the power-ups are animated to come down at an arch instead of straight down. I also like the way that the paddle is able to move across the screen. Finally, I like how the blocks are often animated to move around and have multiple shapes. The visual cue for when the multiple life blocks are about to break is very fun to watch.

A think a cool combination of these two ideas would be the following: First, I would design a similar game to the Bricks n Balls, where you have square, diamond, triangle shaped blocks each with different healths.
Next, I would create the levels so that the player gets to aim and shot the balls and the bricks move down one level after each point.
During the game, a paddle appears after the balls are aimed and shot. The "paddle", or a collection basket, can be used to collect power-up that are hit by the balls and fly down to the paddle similar to the Jet Ball game.
The user only receives the power-ups if one of the balls first hits the power-up block and the the user "catches" it with their paddle. The player might also need to dodge "power-downs", that reduce the number of balls for the next round of play if hit by the paddle.


## Paddle Ideas

 * The main game paddle "catches" all the balls, at the location where the first ball hits the bottom of the screen, when the balls reach the bottom. Allow the user to release all the balls at the same time when a key is pressed. Provide an aim feature so that the player can see where the balls will go after the make their play. See Bricks n Balls game version.

 * Create a paddle that spawns after the player releases their balls at the beginning of a point, so that the user can collect falling power-ups.


## Block Ideas

 * The blocks should take multiple hits (indicated by a number in their middle) to destroy.

 * A special power-up block should drop a power up, that the user collects when it hits their paddle.

 * Different shapes of block, such as a triangle, square, or diamond.


## Power-up Ideas

 * +1/-1: Gives the player an additional ball or takes a ball away for the next round.

 * Block destroyer: When it hits the player's paddle, it automatically takes away 1, 2, 4, etc from the health of each block on the level.

 * Laser: Shot a vertical laser from where the power-up hits the players paddle, destroying part of the blocks.


## Cheat Key Ideas

 * Ball the gives a player an extra ball.

 * A cheat key that allows the user to clear the current level.

 * A cheat key that allows the player to redo a point if they make a mistake in the beginning.

 * A key that freezes a point (the blocks do not shift down one level after the point is finished)


## Level Descriptions

 * A game where the blocks are aligned in a spiral. The inside of the spiral has -1 ball power-downs, so the user must avoid them to not lose all of their balls.
Example level diagram:

```
10 10 10 10 10 10 10 10 10 10 10 10
10 0 0 0 0 0 0 0 0 0 0 10
10 0 10 10 10 10 10 10 10 10 0 10
10 0 10 0 0 0 0 0 0 10 0 10
10 0 10 0 10 10 10 10 0 10 0 10
10 0 10 0 10 00 0 10 0 10 0 10
10 0 10 0 10 100 0 10 0 10 0 10
10 0 10 0 10 10 0 10 0 10 0 10
10 0 10 0 0 0 0 10 0 10 0 10
10 0 10 10 10 10 10 10 0 10 0 10
10 0 0 0 0 0 0 0 0 10 0 10
10 10 10 10 10 10 10 10 10 10 0 10
0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0
0 0 0 0 0 0 0 0 0 0 0 0
```

 * A level with high hit-point blocks at the bottom, that need to be destroyed quickly to beat the level. All the power-ups are higher up, so the user must balance between getting power ups early or breaking the high value blocks before they reach the bottom. 
 
 * All levels should have the basic same structure. The blocks should have the same style and appearance and the user should always start with balls in the middle.

## Class Ideas

 * Block: This class will be the basic block. It can have different hit-points and shape. One method could be to get the hit-points.

 * Power-up: This class will extend upon a block. It will be a block with additional features, such as dropping the power up to the user's paddle when hit. One method could be to get/set the power-up type.

 * Ball: This is the game ball, and will have methods to get its current location and update/move locations in the game.

 * Level: This class will allow you to read a level text file and create a level with a certain number of blocks, balls, and power-ups based on the level design file.

