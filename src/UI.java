import java.util.ArrayList;
import java.util.Scanner;

class UI {
    private static final String COL_HEADER = "  1 2 3 4 5 6 7 8";
    private static final String[] ROW_HEADER = {"A ", "B ", "C ", "D ", "E ", "F ", "G ", "H "};
    private static final char[] ROW_MAPPING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private Scanner scanner;
    private boolean pvpTimed;

    public UI(Scanner scanner) {
        this.scanner = scanner;
        pvpTimed = false;
    }

    public UI(Scanner scanner, boolean pvpTimed) {
        this.scanner = scanner;
        this.pvpTimed = pvpTimed;
    }

    private void displayGame(State state, Logger logger) {
        String[] movesLog = logger.getMovesLog();

        for (int i = 0; i < 8; i++) {
            System.out.print(ROW_HEADER[i] + state.toString(i));
            if (i < movesLog.length) {
                System.out.printf("%6s%2d. %s\n", "", (i + 1), movesLog[i]);
            } else {
                System.out.println();
            }
        }

        if (movesLog.length > 8) {
            for (int i = 8; i < movesLog.length; i++) {
                System.out.printf("%23s%2d. %s\n", "", (i + 1), movesLog[i]);
            }
        }
    }

    public void printGameState(State state, Logger logger) {
        System.out.println("\n" + COL_HEADER + "    Computer vs. Opponent");
        displayGame(state, logger);
    }

    public void printGameStatePVP(State state, Logger logger) {
        System.out.println("\n" + COL_HEADER + "    PLAYER X vs. PLAYER O");
        displayGame(state, logger);
        if (pvpTimed) {
            System.out.println("\n Time Remaining:");
            System.out.printf("X: %-5s O: %-5s\n", logger.getXTimeLeft(), logger.getOTimeLeft());
        }
    }

    private String getUserInput(String prompt, ArrayList<String> choices) {
        String userChoice;

        while (true) {
            System.out.print("\n" + prompt);

            userChoice = scanner.nextLine().toUpperCase();

            if (!userChoice.matches("[A-H][1-8]")) {
                System.err.print("INVALID INPUT");
                continue;
            }

            userChoice = Integer.valueOf(userChoice.charAt(0) - 'A').toString()
                        + (Character.getNumericValue(userChoice.charAt(1)) - 1);

            if (!choices.contains(userChoice)) {
                System.err.print("INVALID CHOICE");
            } else {
                break;
            }
        }

        return userChoice;
    }

    public String getOppMove(ArrayList<String> choices) {
        String prompt = "Enter opponent's move: ";
        return getUserInput(prompt, choices);
    }

    public String getNextMove(boolean forX, ArrayList<String> choices) {
        String prompt = "Enter move for PLAYER " + (forX ? "X: " : "O: ");
        return getUserInput(prompt, choices);
    }

    public void printCompMove(int row, int col) {
        String move = ROW_MAPPING[row] + Integer.toString(col + 1);

        System.out.println("\nComputer's move is: " + move);
    }

    public void printRunTime(long runTime) {
        System.out.printf("\nMinimax Run Time: %.3f seconds\n", runTime / 1000.0);
    }

    public boolean getWhoFirst() {
        boolean xFirst;
        String input;

        while (true) {
            System.out.print("Who goes first, C for computer, O for opponent: ");

            input = scanner.nextLine().toUpperCase();

            if (input.matches("[CO]")) {
                xFirst = input.equals("C");
                break;
            } else {
                System.err.println("INVALID INPUT");
            }
        }

        return xFirst;
    }

    public boolean getWhoFirstPVP() {
        boolean xFirst;
        String input;

        while (true) {
            System.out.print("Who goes first, X or O: ");

            input = scanner.nextLine().toUpperCase();

            if (input.matches("[XO]")) {
                xFirst = input.equals("X");
                break;
            } else {
                System.err.println("INVALID INPUT");
            }
        }

        return xFirst;
    }

    public long getTimeLimit() {
        long timeLimit;

        while (true) {
            System.out.print("Enter time limit (in seconds): ");

            try {
                timeLimit = Long.parseLong(scanner.nextLine()) * 1000L;
                break;
            } catch (Exception e) {
                System.err.println("INVALID INPUT");
            }
        }

        return timeLimit;
    }

    public long getTimeLimitPVP() {
        long timeLimit;

        while (true) {
            System.out.print("Enter time limit of each player (in minutes, [5...20]): ");

            try {
                timeLimit = Long.parseLong(scanner.nextLine());
                if (timeLimit < 5 || timeLimit > 20) {
                    throw new IllegalArgumentException();
                }
                break;
            } catch (Exception e) {
                System.err.println("INVALID INPUT");
            }
        }

        return timeLimit * 60 * 1000;
    }

    public void println(String s) {
        System.out.println(s);
    }

    public void printWinner(String winner) {
        assert winner.matches("[XO]");

        System.out.println(winner + " is winner!");
    }

    public void printStatistics(Logger logger) {
        System.out.printf("X won: %2d games\n", logger.getXWinCount());
        System.out.printf("O won: %2d games\n", logger.getOWinCount());
        System.out.printf("Total: %2d games\n", logger.getTotalGames());
    }
}