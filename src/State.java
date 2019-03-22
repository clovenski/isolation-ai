import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

class State {
    private char[][] board;
    private int utility;
    private int xRow;
    private int xCol;
    private int oRow;
    private int oCol;
    private String winner;
    private HashMap<Integer, int[][]> axisOffsets;

    public State(boolean xTop) {
        int i, j;

        board = new char[8][8];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        utility = 0;

        board[0][0] = xTop ? 'X' : 'O';
        board[7][7] = xTop ? 'O' : 'X';
        xRow = xCol = xTop ? 0 : 7;
        oRow = oCol = xTop ? 7 : 0;

        winner = "None";

        initOffsets();
    }

    public State(State otherState) {
        int i, j;

        board = new char[8][8];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                board[i][j] = otherState.board[i][j];
            }
        }

        utility = otherState.utility;

        xRow = otherState.xRow;
        xCol = otherState.xCol;
        oRow = otherState.oRow;
        oCol = otherState.oCol;
        winner = otherState.winner;

        initOffsets();
    }

    private void initOffsets() {
        int[][] offsets;
        axisOffsets = new HashMap<Integer, int[][]>();

        // axis 0: up
        offsets = new int[][] {
            {-1, -2, -3, -4, -5, -6, -7},
            {0, 0, 0, 0, 0, 0, 0}
        };
        axisOffsets.put(0, offsets);

        // axis 1: up-right
        offsets = new int[][] {
            {-1, -2, -3, -4, -5, -6, -7},
            {1, 2, 3, 4, 5, 6, 7}
        };
        axisOffsets.put(1, offsets);

        // axis 2: right
        offsets = new int[][] {
            {0, 0, 0, 0, 0, 0, 0},
            {1, 2, 3, 4, 5, 6, 7}
        };
        axisOffsets.put(2, offsets);

        // axis 3: down-right
        offsets = new int[][] {
            {1, 2, 3, 4, 5, 6, 7},
            {1, 2, 3, 4, 5, 6, 7}
        };
        axisOffsets.put(3, offsets);

        // axis 4: down
        offsets = new int[][] {
            {1, 2, 3, 4, 5, 6, 7},
            {0, 0, 0, 0, 0, 0, 0}
        };
        axisOffsets.put(4, offsets);

        // axis 5: down-left
        offsets = new int[][] {
            {1, 2, 3, 4, 5, 6, 7},
            {-1, -2, -3, -4, -5, -6, -7}
        };
        axisOffsets.put(5, offsets);

        // axis 6: left
        offsets = new int[][] {
            {0, 0, 0, 0, 0, 0, 0},
            {-1, -2, -3, -4, -5, -6, -7}
        };
        axisOffsets.put(6, offsets);

        // axis 7: up-left
        offsets = new int[][] {
            {-1, -2, -3, -4, -5, -6, -7},
            {-1, -2, -3, -4, -5, -6, -7}
        };
        axisOffsets.put(7, offsets);

        // end initOffsets
    }

    private LinkedList<Integer> getAxisOrder(int row, int col) {
        // get order in which to check axes

        // dummy code, simplified axis ordering: clockwise from up
        int[] temp = new int[] {0, 1, 2, 3, 4, 5, 6, 7};
        LinkedList<Integer> order = new LinkedList<Integer>();
        for (int i : temp) {
            order.add(i);
        }
        return order;
    }

    public ArrayList<String> getSuccessors(int row, int col) {
        ArrayList<String> successors = new ArrayList<String>(27);
        int i, rowOffset, colOffset;
        LinkedList<Integer> axisOrder = getAxisOrder(row, col);

        for (Integer axis : axisOrder) {
            for (i = 0; i < 8; i++) {
                try {
                    rowOffset = axisOffsets.get(axis)[0][i];
                    colOffset = axisOffsets.get(axis)[1][i];
                    if (board[row + rowOffset][col + colOffset] == '-') {
                        successors.add(Integer.toString(row + rowOffset) + Integer.toString(col + colOffset));
                    } else {
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }

        return successors;
    }

    public int getXRow() {
        return xRow;
    }

    public int getXCol() {
        return xCol;
    }

    public int getORow() {
        return oRow;
    }

    public int getOCol() {
        return oCol;
    }

    // move into row, col
    public void move(boolean movingX, int row, int col) {
        assert board[row][col] == '-';

        board[row][col] = movingX ? 'X' : 'O';

        if (movingX) {
            board[xRow][xCol] = '#';
            xRow = row;
            xCol = col;
        } else {
            board[oRow][oCol] = '#';
            oRow = row;
            oCol = col;
        }

        computeUtility();
    }

    // revert back to row, col
    public void revert(boolean movingX, int row, int col) {
        assert board[row][col] == '#';

        board[row][col] = movingX ? 'X' : 'O';

        if (movingX) {
            board[xRow][xCol] = '-';
            xRow = row;
            xCol = col;
        } else {
            board[oRow][oCol] = '-';
            oRow = row;
            oCol = col;
        }

        computeUtility();
    }

    private void computeUtility() {
        int i, j, rowOffset, colOffset, xMoves, oMoves;
        xMoves = oMoves = 0;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                try {
                    rowOffset = axisOffsets.get(i)[0][j];
                    colOffset = axisOffsets.get(i)[1][j];
                    if (board[xRow + rowOffset][xCol + colOffset] == '-') {
                        xMoves++;
                    } else {
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                try {
                    rowOffset = axisOffsets.get(i)[0][j];
                    colOffset = axisOffsets.get(i)[1][j];
                    if (board[oRow + rowOffset][oCol + colOffset] == '-') {
                        oMoves++;
                    } else {
                        break;
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
        }
        // improve this utility evaluation
        utility = xMoves - oMoves;
    }

    public boolean isTerminal() {
        int i, rowOffset, colOffset;
        boolean xFree;

        xFree = false;

        for (i = 0; i < 8; i++) {
            try {
                rowOffset = axisOffsets.get(i)[0][0];
                colOffset = axisOffsets.get(i)[1][0];
                if (board[xRow + rowOffset][xCol + colOffset] == '-') {
                    xFree = true;
                    break;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }

        if (!xFree) {
            winner = "O";
            return true;
        }

        for (i = 0; i < 8; i++) {
            try {
                rowOffset = axisOffsets.get(i)[0][0];
                colOffset = axisOffsets.get(i)[1][0];
                if (board[oRow + rowOffset][oCol + colOffset] == '-') {
                    return false;
                }
            } catch (IndexOutOfBoundsException e) {
                continue;
            }
        }

        winner = "X";
        return true;
    }

    public int getUtility() {
        return utility;
    }

    public String getWinner() {
        return winner;
    }

    public State getCopy() {
        return new State(this);
    }

    public String toString(int row) {
        String result = "";

        for (int j = 0; j < 8; j++) {
            result += board[row][j] + " ";
        }

        return result.trim();
    }
}