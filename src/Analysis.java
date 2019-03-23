import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Analysis {
    public void analyze() {
        State state = new State(true);
        Logger logger = new Logger();
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI(scanner);
        ui.printGameState(state, logger);

        state.setAgentX(new Agent(state, new Heuristic(){
            public int computeUtility(State state, int xMoves, int oMoves) {
                return xMoves - oMoves - oMoves;
            }
        }));

        state.setAgentO(new Agent(state, new Heuristic(){
            public int computeUtility(State state, int xMoves, int oMoves) {
                return oMoves - xMoves;
            }
        }));

        int row, col, i, bestDepth, turnCount = 0;
        String compMove, bestCompMove, oppMove, bestOppMove;
        long startTime, runTime;

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();

            Minimax.timeRemaining = 20000L;
            compMove = Minimax.search(true, state, 7);
            if (compMove.equals("DNF") || compMove.equals("")) {
                compMove = getOppRandMove(false, state);
                bestDepth = 0;
            } else {
                bestDepth = 7;
                for (i = 8; i <= 40; i++) {
                    bestCompMove = Minimax.search(true, state, i);
                    if (!bestCompMove.equals("DNF") && !bestCompMove.equals("")) {
                        compMove = bestCompMove;
                        bestDepth = i;
                    } else {
                        break;
                    }
                }
            }

            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);
            System.out.println("Best depth: " + bestDepth);

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);

            ui.printGameState(state, logger);

            if (state.isTerminal()) {
                break;
            }
            
            startTime = System.currentTimeMillis();

            Minimax.timeRemaining = 20000L;
            oppMove = Minimax.search(false, state, 7);
            if (oppMove.equals("DNF") || oppMove.equals("")) {
                oppMove = getOppRandMove(true, state);
                bestDepth = 0;
            } else {
                bestDepth = 7;
                for (i = 8; i <= 40; i++) {
                    bestOppMove = Minimax.search(false, state, i);
                    if (!bestOppMove.equals("DNF") && !bestOppMove.equals("")) {
                        oppMove = bestOppMove;
                        bestDepth = i;
                    } else {
                        break;
                    }
                }
            }

            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);
            System.out.println("Best depth: " + bestDepth);

            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);
            if (turnCount < 2) {
                turnCount++;
                Minimax.random = turnCount < 2;
            }

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

    public static void main(String[] args) {
        Analysis analysis = new Analysis();
        analysis.analyze();
    }
}