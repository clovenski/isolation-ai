import java.util.ArrayList;

class Logger {
    private ArrayList<String> compMoves;
    private ArrayList<String> oppMoves;
    private final char[] ROW_MAPPING = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'};

    public Logger() {
        compMoves = new ArrayList<String>();
        oppMoves = new ArrayList<String>();
    }

    public void log(boolean compMove, int row, int col) {
        String move = ROW_MAPPING[row] + Integer.toString(col + 1);

        if (compMove) {
            compMoves.add(move);
        } else {
            oppMoves.add(move);
        }
    }

    public String[] getMovesLog() {
        String[] movesLog = new String[Math.max(compMoves.size(), oppMoves.size())];
        int i;

        if (oppMoves.size() > compMoves.size()) {
            for (i = 0; i < oppMoves.size(); i++) {
                try {
                    movesLog[i] = compMoves.get(i) + "     ";
                    movesLog[i] += oppMoves.get(i);
                } catch (IndexOutOfBoundsException e) {
                    movesLog[i] = "       " + oppMoves.get(i);
                    continue;
                }
            }

        } else {
            for (i = 0; i < compMoves.size(); i++) {
                movesLog[i] = compMoves.get(i) + "     ";
                try {
                    movesLog[i] += oppMoves.get(i);
                } catch (IndexOutOfBoundsException e) {
                    continue;
                }
            }

        }


        return movesLog;
    }
}