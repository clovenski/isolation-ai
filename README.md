# CS 4200 - Project 3 - Isolation

Implement an AI using the Minimax algorithm with alpha-beta pruning to play the game called Isolation - two players on an 8x8 board, each player can move like a queen in chess, spaces that each player moves to become occupied and become a sort of barrier, goal is to isolate your opponent to the point that they have no more available moves.

## Author

Joel Tengco

## Usage

Compiling and running the program is simplified with the provided bash scripts. They all essentially execute the java commands to compile, analyze and run the program.

The following commands assume you're in the project root directory (the same directory as this readme).

Note: These commands work for me on a Windows machine using Git Bash.

**Compilation**: `javac src/*.java -d bin`

**Running**: `java -cp '.;bin' Project3`

- you can pass the argument `--test` for a run with a 20 second time limit of agent X versus a random-move player

- you can also pass `--pvp` for player versus player

  - by default pvp is not timed, but passing `--timed` will prompt for a time limit; no player can exceed this amount of total, accumulated thinking time