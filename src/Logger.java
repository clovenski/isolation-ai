import java.util.ArrayList;

class Logger {
    private ArrayList<String> xMoves;
    private ArrayList<String> oMoves;
    private final char[] ROW_MAPPING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};
    private long xTimeLeft;
    private long oTimeLeft;
    private int xWinCount;
    private int totalGames;

    public Logger() {
        xMoves = new ArrayList<String>();
        oMoves = new ArrayList<String>();
        xWinCount = totalGames = 0;
    }

    public Logger(long startingTimeLimit) {
        xMoves = new ArrayList<String>();
        oMoves = new ArrayList<String>();
        xWinCount = totalGames = 0;
        xTimeLeft = oTimeLeft = startingTimeLimit;
    }

    public void log(boolean xMove, int row, int col) {
        String move = ROW_MAPPING[row] + Integer.toString(col + 1);

        if (xMove) {
            xMoves.add(move);
        } else {
            oMoves.add(move);
        }
    }

    public void logTime(boolean forX, long timeUsed) {
        if (forX) {
            xTimeLeft -= timeUsed;
        } else {
            oTimeLeft -= timeUsed;
        }
    }

    public void logGame(boolean xWon) {
        if (xWon) {
            xWinCount++;
        }

        totalGames++;
    }

    public int getXWinCount() {
        return xWinCount;
    }

    public int getOWinCount() {
        return totalGames - xWinCount;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public String[] getMovesLog() {
        String[] movesLog = new String[Math.max(xMoves.size(), oMoves.size())];
        int i;

        if (oMoves.size() > xMoves.size()) {
            for (i = 0; i < oMoves.size(); i++) {
                try {
                    movesLog[i] = xMoves.get(i) + "     ";
                    movesLog[i] += oMoves.get(i);
                } catch (IndexOutOfBoundsException e) {
                    movesLog[i] = "       " + oMoves.get(i);
                    continue;
                }
            }

        } else {
            for (i = 0; i < xMoves.size(); i++) {
                movesLog[i] = xMoves.get(i) + "     ";
                try {
                    movesLog[i] += oMoves.get(i);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }

        }

        return movesLog;
    }

    public boolean exceededTimeLimit(boolean playerX) {
        return playerX ? xTimeLeft <= 0 : oTimeLeft <= 0;
    }

    public String getXTimeLeft() {
        long seconds = Math.round(xTimeLeft / 1000.0);
        return String.format("%2d:%02d", seconds / 60, seconds % 60);
    }

    public String getOTimeLeft() {
        long seconds = Math.round(oTimeLeft / 1000.0);
        return String.format("%2d:%02d", seconds / 60, seconds % 60);
    }

    public void resetMovesLog() {
        xMoves.clear();
        oMoves.clear();
    }
}