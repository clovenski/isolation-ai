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
    private int quad1Spaces; // top left
    private int quad2Spaces; // top right
    private int quad3Spaces; // bottom left
    private int quad4Spaces; // bottom right
    private int xQuadUtil;
    private int oQuadUtil;
    private String winner;
    private boolean pvp;
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

    public State(boolean xTop, boolean pvp) {
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
        quad1Spaces = quad4Spaces = 15;
        quad2Spaces = quad3Spaces = 16;
        xQuadUtil = oQuadUtil = 15;
        winner = "None";
        this.pvp = pvp;
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
        quad1Spaces = otherState.quad1Spaces;
        quad2Spaces = otherState.quad2Spaces;
        quad3Spaces = otherState.quad3Spaces;
        quad4Spaces = otherState.quad4Spaces;
        xQuadUtil = otherState.xQuadUtil;
        oQuadUtil = otherState.oQuadUtil;
        winner = otherState.winner;
        pvp = otherState.pvp;
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

    public int getXQuadUtil() {
        return xQuadUtil;
    }

    public int getOQuadUtil() {
        return oQuadUtil;
    }

    private void processMove(boolean movingX, int row, int col) {
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

        if (row < 4) { // either quad 1 or 2
            if (col < 4) { // quad 1
                quad1Spaces--;
                if (movingX) {
                    xQuadUtil = quad1Spaces;
                } else {
                    oQuadUtil = quad1Spaces;
                }
            } else { // quad 2
                quad2Spaces--;
                if (movingX) {
                    xQuadUtil = quad2Spaces;
                } else {
                    oQuadUtil = quad2Spaces;
                }
            }

        } else { //  either quad 3 or 4
            if (col < 4) { // quad 3
                quad3Spaces--;
                if (movingX) {
                    xQuadUtil = quad3Spaces;
                } else {
                    oQuadUtil = quad3Spaces;
                }
            } else { // quad 4
                quad4Spaces--;
                if (movingX) {
                    xQuadUtil = quad4Spaces;
                } else {
                    oQuadUtil = quad4Spaces;
                }
            }
        }
    }

    // move into row, col
    public void move(boolean forUtilX, boolean movingX, int row, int col) {
        processMove(movingX, row, col);
        computeUtility(forUtilX, movingX);
    }

    // move into row, col; compute utility for both agents if not pvp
    public void move(boolean movingX, int row, int col) {
        processMove(movingX, row, col);

        if (!pvp) {
            computeUtility(true, movingX);
            if (agentO != null) {
                computeUtility(false, movingX);
            }
        }
    }

    private void processRevert(boolean movingX, int row, int col) {
        assert board[row][col] == '#';

        board[row][col] = movingX ? 'X' : 'O';

        if (movingX) {
            board[xRow][xCol] = '-';
            if (xRow < 4) { // either quad 1 or 2
                if (xCol < 4) { // quad 1
                    quad1Spaces++;
                } else { // quad 2
                    quad2Spaces++;
                }
    
            } else { //  either quad 3 or 4
                if (xCol < 4) { // quad 3
                    quad3Spaces++;
                } else { // quad 4
                    quad4Spaces++;
                }
            }
            xRow = row;
            xCol = col;

        } else {
            board[oRow][oCol] = '-';
            if (oRow < 4) { // either quad 1 or 2
                if (oCol < 4) { // quad 1
                    quad1Spaces++;
                } else { // quad 2
                    quad2Spaces++;
                }
    
            } else { //  either quad 3 or 4
                if (oCol < 4) { // quad 3
                    quad3Spaces++;
                } else { // quad 4
                    quad4Spaces++;
                }
            }
            oRow = row;
            oCol = col;
        }

        if (row < 4) { // either quad 1 or 2
            if (col < 4) { // quad 1
                if (movingX) {
                    xQuadUtil = quad1Spaces;
                } else {
                    oQuadUtil = quad1Spaces;
                }
            } else { // quad 2
                if (movingX) {
                    xQuadUtil = quad2Spaces;
                } else {
                    oQuadUtil = quad2Spaces;
                }
            }

        } else { //  either quad 3 or 4
            if (col < 4) { // quad 3
                if (movingX) {
                    xQuadUtil = quad3Spaces;
                } else {
                    oQuadUtil = quad3Spaces;
                }
            } else { // quad 4
                if (movingX) {
                    xQuadUtil = quad4Spaces;
                } else {
                    oQuadUtil = quad4Spaces;
                }
            }
        }
    }

    // revert back to row, col
    public void revert(boolean forUtilX, boolean movingX, int row, int col) {
        processRevert(movingX, row, col);
        computeUtility(forUtilX, movingX);
    }

    // revert back to row, col; compute utility for both agents if not pvp
    public void revert(boolean movingX, int row, int col) {
        processRevert(movingX, row, col);

        if (!pvp) {
            computeUtility(true, movingX);
            if (agentO != null) {
                computeUtility(false, movingX);
            }
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

    private void computeUtility(boolean forX, boolean xMoved) {
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
            if (xMoves != 0 && oMoves != 0) {
                utilityX = agentX.utilityFunc();
            } else if (xMoves == 0 && oMoves == 0) {
                utilityX = xMoved ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            } else if (xMoves == 0) {
                utilityX = Integer.MIN_VALUE;
            } else {
                utilityX = Integer.MAX_VALUE;
            }

        } else {
            if (agentO == null) {
                utilityO = 0;
            } else {
                if (oMoves != 0 && xMoves != 0) {
                    utilityO = agentO.utilityFunc();
                } else if (oMoves == 0 && xMoves == 0) {
                    utilityO = xMoved ? Integer.MIN_VALUE : Integer.MAX_VALUE;
                } else if (oMoves == 0) {
                    utilityO = Integer.MIN_VALUE;
                } else {
                    utilityO = Integer.MAX_VALUE;
                }
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
        quad1Spaces = quad4Spaces = 15;
        quad2Spaces = quad3Spaces = 16;
        xQuadUtil = oQuadUtil = 15;
        winner = "None";
    }

    public String toString() {
        String result = "";
        int i, j;

        for (i = 0; i < 8; i++) {
            for (j = 0; j < 8; j++) {
                result += board[i][j];
            }
        }

        return result;
    }

    public String toString(int row) {
        String result = "";

        for (int j = 0; j < 8; j++) {
            result += board[row][j] + " ";
        }

        return result.trim();
    }
}