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
        state = new State(xFirst);

        int row, col, turnCount = 0;
        String compMove, oppMove;
        long startTime, runTime;

        if (!xFirst) {
            ui.printGameState(state, logger);
            oppMove = ui.getOppMove(state.getSuccessors(state.getORow(), state.getOCol()));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);

            ui.printGameState(state, logger);
        }

        while (!state.isTerminal()) {
            startTime = System.currentTimeMillis();
            compMove = Minimax.search(state, 8); // implement iter deepening instead of static 8 depth
            runTime = System.currentTimeMillis() - startTime;
            ui.printRunTime(runTime);

            row = Character.getNumericValue(compMove.charAt(0));
            col = Character.getNumericValue(compMove.charAt(1));
            state.move(true, row, col);
            logger.log(true, row, col);
            if (turnCount < 2) {
                turnCount++;
                Minimax.random = turnCount < 5;
            }

            ui.printGameState(state, logger);

            if (state.isTerminal()) {
                ui.printGameState(state, logger);
                break;
            }

            ui.printCompMove(row, col);
            oppMove = ui.getOppMove(state.getSuccessors(state.getORow(), state.getOCol()));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }
}