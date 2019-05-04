import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Tester {
    public void run() {
        State state = new State(true, false);
        Logger logger = new Logger();
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI(scanner);
        ui.printGameState(state, logger);

        state.setAgentX(AgentInitializer.getAgentX(state));

        final int START_DEPTH = 7;
        int row, col, i, bestDepth, bestUtility, turnCount = 0;
        String compMove, bestCompMove, oppMove;
        long startTime, runTime;

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();

            Minimax.timeRemaining = 5000L;
            Minimax.resetTransTable();
            compMove = Minimax.search(true, state, START_DEPTH);
            if (compMove.equals("DNF") || compMove.equals("")) {
                compMove = getOppRandMove(false, state);
                bestDepth = 0;
                bestUtility = -4200;
            } else {
                bestDepth = START_DEPTH;
                bestUtility = Minimax.getMaxUtility();
                for (i = START_DEPTH + 1; i <= 40; i++) {
                    bestCompMove = Minimax.search(true, state, i);
                    if (!bestCompMove.equals("DNF") && !bestCompMove.equals("")) {
                        compMove = bestCompMove;
                        bestDepth = i;
                        bestUtility = Minimax.getMaxUtility();
                    } else {
                        break;
                    }
                }
            }

            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);
            System.out.println("Best depth: " + bestDepth);
            System.out.println("Best Utility: " + bestUtility);
            System.out.println("Transposition table size: " + Minimax.getTableSize());

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);
            if (turnCount < 2) {
                turnCount++;
                Minimax.random = turnCount < 2;
            }

            if (state.isTerminal()) {
                ui.printGameState(state, logger);
                break;
            }
            
            oppMove = getOppRandMove(true, state);
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            ui.printGameState(state, logger);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }

    private String getOppRandMove(boolean forX, State state) {
        Random rng = new Random();
        ArrayList<String> successors = state.getSuccessors(!forX);

        return successors.get(rng.nextInt(successors.size()));
    }
}