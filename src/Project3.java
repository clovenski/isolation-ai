import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

class Project3 {
    public void run() {
        State state = new State(true);
        Logger logger = new Logger();
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI(scanner);
        int row, col;
        String compMove, oppMove;

        long startTime, runTime;

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();
            compMove = Minimax.search(state, 7);
            runTime = System.currentTimeMillis() - startTime;
            System.out.printf("\nMinimax Run Time: %.3f seconds\n\n", runTime / 1000.0);
            
            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);
            
            ui.printGameState(state, logger);
            
            if (state.isTerminal()) {
                break;
            }
            
            ui.printCompMove(row, col);
            oppMove = ui.getOppMove(state.getSuccessors(state.getORow(), state.getOCol()));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);
        }

        scanner.close();
    }

    public void testrun() {
        State state = new State(true);
        Logger logger = new Logger();
        Scanner scanner = new Scanner(System.in);
        UI ui = new UI(scanner);
        ui.printGameState(state, logger);

        int row, col;
        String compMove, oppMove;
        long startTime, runTime;

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();
            compMove = Minimax.search(state, 8);
            runTime = System.currentTimeMillis() - startTime;
            System.out.printf("Minimax Run Time: %.3f seconds\n", runTime / 1000.0);

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);

            if (state.isTerminal()) {
                break;
            }
            
            oppMove = getOppRandMove(state);
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            ui.printGameState(state, logger);
        }

        scanner.close();
    }

    private String getOppRandMove(State state) {
        Random rng = new Random();
        ArrayList<String> successors = state.getSuccessors(state.getORow(), state.getOCol());

        return successors.get(rng.nextInt(successors.size()));
    }

    public static void main(String[] args) {
        Project3 program = new Project3();

        program.testrun();
    }
}