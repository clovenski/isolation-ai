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
            compMove = Minimax.search(state, 7);
            for (i = 8; i <= 20; i++) {
                bestCompMove = Minimax.search(state, i);
                if (!bestCompMove.equals("DNF")) {
                    compMove = bestCompMove;
                } else {
                    break;
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
                ui.printGameState(state, logger);
                break;
            }

            ui.printCompMove(row, col);
            oppMove = ui.getOppMove(state.getSuccessors(false));
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);
            logger.log(false, row, col);
        }

        ui.printWinner(state.getWinner());

        scanner.close();
    }
}