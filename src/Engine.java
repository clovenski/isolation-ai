import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Engine {
    private State state;
    private long timeLimit;
    private Logger logger;
    private Scanner scanner;
    private UI ui;

    public void startGame() {
        logger = new Logger();
        scanner = new Scanner(System.in);
        ui = new UI(scanner);
        timeLimit = ui.getTimeLimit();

        boolean xFirst = ui.getWhoFirst();
        state = new State(xFirst, false);

        state.setAgentX(AgentInitializer.getAgentX(state));

        final int START_DEPTH;
        if (timeLimit >= 5000) {
            START_DEPTH = 7;
        } else if (timeLimit >= 3000) {
            START_DEPTH = 6;
        } else {
            START_DEPTH = 5;
        }
        int row, col, i, turnCount = 0;
        String compMove, bestCompMove, oppMove;
        long startTime, runTime;

        ui.printGameState(state, logger);
        if (!xFirst) {
            oppMove = ui.getOppMove(state.getSuccessors(false));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            ui.printGameState(state, logger);
        }

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();

            Minimax.timeRemaining = timeLimit;
            Minimax.resetTransTable();
            compMove = Minimax.search(true, state, START_DEPTH);
            if (compMove.equals("DNF") || compMove.equals("")) {
                compMove = getRandMove();
            } else {
                for (i = START_DEPTH + 1; i <= 40; i++) {
                    bestCompMove = Minimax.search(true, state, i);
                    if (!bestCompMove.equals("DNF") && !bestCompMove.equals("")) {
                        compMove = bestCompMove;
                    } else {
                        break;
                    }
                }
            }

            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);
            if (turnCount < 2) {
                turnCount++;
                Minimax.random = turnCount < 2;
            }

            ui.printGameState(state, logger);

            if (state.isTerminal()) {
                break;
            }

            ui.printCompMove(row, col);
            oppMove = ui.getOppMove(state.getSuccessors(false));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);
            ui.printGameState(state, logger);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }

    private String getRandMove() {
        Random rng = new Random();
        ArrayList<String> successors = state.getSuccessors(true);

        return successors.get(rng.nextInt(successors.size()));
    }

    public void startPVPGame(boolean timed) {
        scanner = new Scanner(System.in);
        if (timed) {
            ui = new UI(scanner, true);
            timeLimit = ui.getTimeLimitPVP();
            logger = new Logger(timeLimit);
        } else {
            ui = new UI(scanner, false);
            logger = new Logger();
        }

        boolean xFirst = ui.getWhoFirstPVP();
        state = new State(xFirst, true);

        int row, col;
        String xMove, oMove;
        long startTime, runTime;
        startTime = runTime = 0L;

        ui.printGameStatePVP(state, logger);
        if (!xFirst) {
            if (timed) {
                startTime = System.currentTimeMillis();
            }
            oMove = ui.getNextMove(false, state.getSuccessors(false));
            if (timed) {
                runTime = System.currentTimeMillis() - startTime;
            }
            row = Character.getNumericValue(oMove.charAt(0));
            col = Character.getNumericValue(oMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            if (timed) {
                ui.println(String.format("Player O took %d seconds", runTime / 1000));
                logger.logTime(false, runTime);
                if (logger.exceededTimeLimit(false)) {
                    ui.println("O ran out of time!");
                    ui.printWinner("X");
                    scanner.close();
                    return;
                }
            }

            ui.printGameStatePVP(state, logger);
        }

        while (!state.isTerminal()) {
            if (timed) {
                startTime = System.currentTimeMillis();
            }
            xMove = ui.getNextMove(true, state.getSuccessors(true));
            if (timed) {
                runTime = System.currentTimeMillis() - startTime;
            }
            row = Character.getNumericValue(xMove.charAt(0));
            col = Character.getNumericValue(xMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);

            if (timed) {
                ui.println(String.format("Player X took %d seconds", runTime / 1000));
                logger.logTime(true, runTime);
                if (logger.exceededTimeLimit(true)) {
                    ui.println("X ran out of time!");
                    ui.printWinner("O");
                    scanner.close();
                    return;
                }
            }

            ui.printGameStatePVP(state, logger);

            if (state.isTerminal()) {
                break;
            }

            if (timed) {
                startTime = System.currentTimeMillis();
            }
            oMove = ui.getNextMove(false, state.getSuccessors(false));
            if (timed) {
                runTime = System.currentTimeMillis() - startTime;
            }
            row = Character.getNumericValue(oMove.charAt(0));
            col = Character.getNumericValue(oMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            if (timed) {
                ui.println(String.format("Player O took %d seconds", runTime / 1000));
                logger.logTime(false, runTime);
                if (logger.exceededTimeLimit(false)) {
                    ui.println("O ran out of time!");
                    ui.printWinner("X");
                    scanner.close();
                    return;
                }
            }

            ui.printGameStatePVP(state, logger);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }
}