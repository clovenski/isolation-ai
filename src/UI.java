import java.util.ArrayList;
import java.util.Scanner;

class UI {
    private static final String COL_HEADER = "  1 2 3 4 5 6 7 8";
    private static final String[] ROW_HEADER = {"A ", "B ", "C ", "D ", "E ", "F ", "G ", "H "};
    private static final char[] ROW_MAPPING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private Scanner scanner;

    public UI(Scanner scanner) {
        this.scanner = scanner;
    }

    public void printGameState(State state, Logger logger) {
        // print current game board state along with moves log
        String[] movesLog = logger.getMovesLog();

        System.out.println(COL_HEADER + "    Computer vs. Opponent");
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

    public String getOppMove(ArrayList<String> choices) {
        String oppChoice;

        while (true) {
            System.out.print("Enter opponent's move: ");

            oppChoice = scanner.nextLine().toUpperCase();

            if (!oppChoice.matches("[A-H][1-8]")) {
                System.err.println("INVALID INPUT");
                continue;
            }

            oppChoice = Integer.valueOf(oppChoice.charAt(0) - 'A').toString()
                        + (Character.getNumericValue(oppChoice.charAt(1)) - 1);

            if (!choices.contains(oppChoice)) {
                System.err.println("INVALID CHOICE");
            } else {
                System.out.println();
                break;
            }
        }

        return oppChoice;
    }

    public void printCompMove(int row, int col) {
        String move = ROW_MAPPING[row] + Integer.toString(col + 1);

        System.out.println("\nComputer's move is: " + move + "\n");
    }

    public void printRunTime(long runTime) {
        System.out.printf("\nMinimax Run Time: %.3f seconds\n\n", runTime / 1000.0);
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

    public void printWinner(String winner) {
        assert winner.matches("[XO]");

        System.out.println(winner + " is winner!");
    }
}