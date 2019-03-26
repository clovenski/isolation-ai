import java.util.ArrayList;

class State {
    private char[][] board;
    private Agent agentX;
    private Agent agentO;
    private int utilityX;
    private int utilityO;
    private boolean xTop;
    private int xRow;
    private int xCol;
    private int oRow;
    private int oCol;
    private int xMoves;
    private int oMoves;
    private int xLocalMoves;
    private int oLocalMoves;
    private String winner;
    private static final int[][][] AXIS_OFFSETS = {
        { // axis 0: up
            {-1, -2, -3, -4, -5, -6, -7},
            {0, 0, 0, 0, 0, 0, 0}
        },
        { // axis 1: up-right
            {-1, -2, -3, -4, -5, -6, -7},
            {1, 2, 3, 4, 5, 6, 7}
        },
        { // axis 2: right
            {0, 0, 0, 0, 0, 0, 0},
            {1, 2, 3, 4, 5, 6, 7}
        },
        { // axis 3: down-right
            {1, 2, 3, 4, 5, 6, 7},
            {1, 2, 3, 4, 5, 6, 7}
        },
        { // axis 4: down
            {1, 2, 3, 4, 5, 6, 7},
            {0, 0, 0, 0, 0, 0, 0}
        },
        { // axis 5: down-left
            {1, 2, 3, 4, 5, 6, 7},
            {-1, -2, -3, -4, -5, -6, -7}
        },
        { // axis 6: left
            {0, 0, 0, 0, 0, 0, 0},
            {-1, -2, -3, -4, -5, -6, -7}
        },
        { // axis 7: up-left
            {-1, -2, -3, -4, -5, -6, -7},
            {-1, -2, -3, -4, -5, -6, -7}
        }
    };

    public State(boolean xTop) {
        int i, j;

        board = new char[8][8];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        utilityX = utilityO = 0;
        this.xTop = xTop;
        agentX = null;
        agentO = null;
        board[0][0] = xTop ? 'X' : 'O';
        board[7][7] = xTop ? 'O' : 'X';
        xRow = xCol = xTop ? 0 : 7;
        oRow = oCol = xTop ? 7 : 0;
        xMoves = oMoves = 20;
        xLocalMoves = oLocalMoves = 3;
        winner = "None";
    }

    public State(State otherState) {
        int i, j;

        board = new char[8][8];
        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                board[i][j] = otherState.board[i][j];
            }
        }

        utilityX = otherState.utilityX;
        utilityO = otherState.utilityO;
        xTop = otherState.xTop;
        agentX = otherState.agentX;
        agentO = otherState.agentO;
        xRow = otherState.xRow;
        xCol = otherState.xCol;
        oRow = otherState.oRow;
        oCol = otherState.oCol;
        xMoves = otherState.xMoves;
        oMoves = otherState.oMoves;
        xLocalMoves = otherState.xLocalMoves;
        oLocalMoves = otherState.oLocalMoves;
        winner = otherState.winner;
    }

    public void setAgentX(Agent agent) {
        agentX = agent;
    }

    public void setAgentO(Agent agent) {
        agentO = agent;
    }

    private ArrayList<String> successors(boolean ofX) {
        ArrayList<String> successors = new ArrayList<String>(27);
        int i, axis, row, col, rowOffset, colOffset;

        row = ofX ? xRow : oRow;
        col = ofX ? xCol : oCol;

        for (axis = 0; axis < 8; axis++) {
            for (i = 0; i < 8; i++) {
                try {
                    rowOffset = State.AXIS_OFFSETS[axis][0][i];
                    colOffset = State.AXIS_OFFSETS[axis][1][i];
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

    public ArrayList<String> getSuccessors(boolean ofX) {
        return successors(ofX);
    }

    public ArrayList<String> getSuccessors(boolean ofX, boolean usingXSorter) {
        ArrayList<String> successors = successors(ofX);

        if (usingXSorter) {
            successors.sort(agentX.getSorter(ofX));
        } else if (agentO != null) {
            successors.sort(agentO.getSorter(!ofX));
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

    public int getNumXMoves() {
        return xMoves;
    }

    public int getNumOMoves() {
        return oMoves;
    }

    public int getNumXLocalMoves() {
        return xLocalMoves;
    }

    public int getNumOLocalMoves() {
        return oLocalMoves;
    }

    // move into row, col
    public void move(boolean forUtilX, boolean movingX, int row, int col) {
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

        computeUtility(forUtilX);
    }

    // move into row, col; compute utility for both agents
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

        computeUtility(true);
        if (agentO != null) {
            computeUtility(false);
        }
    }

    // revert back to row, col
    public void revert(boolean forUtilX, boolean movingX, int row, int col) {
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

        computeUtility(forUtilX);
    }

    // revert back to row, col; compute utility for both agents
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

        computeUtility(true);
        if (agentO != null) {
            computeUtility(false);
        }
    }

    private int numLocalMoves(boolean ofX) {
        int row, col, rowOffset, colOffset, count = 0;

        row = ofX ? xRow : oRow;
        col = ofX ? xCol : oCol;
        for (int i = 0; i < 8; i++) {
            try {
                rowOffset = State.AXIS_OFFSETS[i][0][0];
                colOffset = State.AXIS_OFFSETS[i][1][0];
                if (board[row + rowOffset][col + colOffset] == '-') {
                    count++;
                }
            } catch (IndexOutOfBoundsException e) {
                break;
            }
        }

        return count;
    }

    private void computeUtility(boolean forX) {
        int i, j, rowOffset, colOffset;
        xMoves = oMoves = 0;

        xLocalMoves = numLocalMoves(true);
        oLocalMoves = numLocalMoves(false);

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                try {
                    rowOffset = State.AXIS_OFFSETS[i][0][j];
                    colOffset = State.AXIS_OFFSETS[i][1][j];
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
                    rowOffset = State.AXIS_OFFSETS[i][0][j];
                    colOffset = State.AXIS_OFFSETS[i][1][j];
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

        if (forX) {
            if (xMoves == 0) {
                utilityX = Integer.MIN_VALUE;
            } else if (oMoves == 0) {
                utilityX = Integer.MAX_VALUE;
            } else {
                utilityX = agentX.utilityFunc();
            }

        } else {
            if (oMoves == 0) {
                utilityO = Integer.MIN_VALUE;
            } else if (xMoves == 0) {
                utilityO = Integer.MAX_VALUE;
            } else if (agentO != null) {
                utilityO = agentO.utilityFunc();
            } else {
                utilityO = 0;
            }
        }
    }

    public boolean isTerminal() {
        int i, rowOffset, colOffset;
        boolean xFree = false;

        for (i = 0; i < 8; i++) {
            try {
                rowOffset = State.AXIS_OFFSETS[i][0][0];
                colOffset = State.AXIS_OFFSETS[i][1][0];
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
                rowOffset = State.AXIS_OFFSETS[i][0][0];
                colOffset = State.AXIS_OFFSETS[i][1][0];
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

    public int getUtility(boolean ofX) {
        return ofX ? utilityX : utilityO;
    }

    public String getWinner() {
        return winner;
    }

    public State getCopy() {
        return new State(this);
    }

    public void reset() {
        int i, j;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                board[i][j] = '-';
            }
        }

        utilityX = utilityO = 0;
        board[0][0] = xTop ? 'X' : 'O';
        board[7][7] = xTop ? 'O' : 'X';
        xRow = xCol = xTop ? 0 : 7;
        oRow = oCol = xTop ? 7 : 0;
        xMoves = oMoves = 20;
        xLocalMoves = oLocalMoves = 3;
        winner = "None";
    }

    public char[] toCharArray() {
        char[] array = new char[64];
        int index = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                array[index++] = board[i][j];
            }
        }

        return array;
    }

    public String toString(int row) {
        String result = "";

        for (int j = 0; j < 8; j++) {
            result += board[row][j] + " ";
        }

        return result.trim();
    }
}