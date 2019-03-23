import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Project3 {
    public void run() {
        Engine engine = new Engine();
        engine.startGame();
    }

    public void testrun() {
        State state = new State(true);
        Logger logger = new Logger();
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI(scanner);
        ui.printGameState(state, logger);

        int row, col, i, bestDepth;
        String compMove, bestCompMove, oppMove;
        long startTime, runTime;

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();

            Minimax.timeRemaining = 20000L;
            compMove = Minimax.search(state, 7);
            bestDepth = 7;
            for (i = 8; i <= 20; i++) {
                bestCompMove = Minimax.search(state, i);
                if (!bestCompMove.equals("DNF")) {
                    compMove = bestCompMove;
                    bestDepth = i;
                } else {
                    break;
                }
            }

            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);
            System.out.println("Best depth: " + bestDepth + "\n");

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);

            if (state.isTerminal()) {
                ui.printGameState(state, logger);
                break;
            }
            
            oppMove = getOppRandMove(state);
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            ui.printGameState(state, logger);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }

    private String getOppRandMove(State state) {
        Random rng = new Random();
        ArrayList<String> successors = state.getSuccessors(state.getORow(), state.getOCol());

        return successors.get(rng.nextInt(successors.size()));
    }

    public static void main(String[] args) {
        Project3 program = new Project3();

        if (args.length > 0 && args[0].equals("--test")) {
            program.testrun();
        } else {
            program.run();
        }
    }
}