# CS 4200 - Project 3 - Isolation

Implement an AI using the Minimax algorithm with alpha-beta pruning to play the game called Isolation.

## Author

Joel Tengco

## Usage

Compiling and running the program is simplified with the provided bash scripts. They all essentially execute the java commands to compile, analyze and run the program.

The following commands assume you're in the project root directory (the same directory as this readme).

**Compilation**: `javac src/*.java -d bin`

**Running**: `java -cp '.;bin' Project3`

- you can pass the argument `--test` for a run with a 20 second time limit of agent X versus a random-move player