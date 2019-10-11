# Conway's Game of Life
A simulation of the popular Conway's Game of Life cellular automaton algorithm allowing users to populate and eradicate lifeforms, built with Java.

## Background
The algorithm must follow these rules:
1. A live cell with fewer than two surrounding live neighbours dies, due to underpopulation.
2. A live cell with two or three surrounding live neighbours lives to the next generation.
3. A live cell with more than three surrounding live neighbours dies, due to overpopulation.
4. A dead cell with exactly three surrounding live neighbours becomes a live cell, due to reproduction.


## Demo

Upon initialization, the game is pre-populated with a randomly generated pattern where a black pixel signifies a present lifeform. Users are able to eradicate and populate lifeforms on designated locations in square blocks.
![alt text](https://github.com/nasark/GameofLife/blob/master/demos/demo1.gif "First Demo")



Pre-made patterns can be uploaded from text files where 0's indicate a lifeform and X's indicate a blank space
![alt text](https://github.com/nasark/GameofLife/blob/master/demos/demo2.gif "Second Demo")
