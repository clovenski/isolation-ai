import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Minimax {
    private static String action;
    private static int maxDepth;
    private static Random rng = new Random();
    private static HashMap<Integer, ArrayList<String>> optimalMoves;
    private static int maxUtility;
    public static boolean random = true;

    public static String search(State state, int maxDepth) {
        Minimax.maxDepth = maxDepth;
        optimalMoves = new HashMap<Integer, ArrayList<String>>();
        action = "";
        maxUtility = Integer.MIN_VALUE;

        maxValue(state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        if (Minimax.random) {
            int size = optimalMoves.get(maxUtility).size();
            return optimalMoves.get(maxUtility).get(rng.nextInt(size));
        } else {
            return action;
        }
    }

    private static int maxValue(State state, int alpha, int beta, int depth) {
        int v, oldV, row, oldRow, col, oldCol, minResult;
        String oldAction;
        ArrayList<String> successors;

        if (state.isTerminal() || depth == maxDepth) {
            return state.getUtility();
        }

        v = Integer.MIN_VALUE;
        oldRow = state.getXRow();
        oldCol = state.getXCol();
        successors = state.getSuccessors(state.getXRow(), state.getXCol());
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));
            oldV = v;
            oldAction = action;

            state.move(true, row, col);
            minResult = minValue(state, alpha, beta, depth + 1);
            v = Math.max(v, minResult);
            if (v == oldV) {
                action = oldAction;
            } else {
                action = move;
            }
            if (depth == 0 && oldV <= minResult) {
                updateOptimals(v, move);
            }
            state.revert(true, oldRow, oldCol);
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
        }

        return v;
    }

    private static int minValue(State state, int alpha, int beta, int depth) {
        int v, oldV, row, oldRow, col, oldCol;
        String oldAction;
        ArrayList<String> successors;

        if (state.isTerminal() || depth == maxDepth) {
            return state.getUtility();
        }

        v = Integer.MAX_VALUE;
        oldRow = state.getORow();
        oldCol = state.getOCol();
        successors = state.getSuccessors(state.getORow(), state.getOCol());
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));
            oldV = v;
            oldAction = action;

            state.move(false, row, col);
            v = Math.min(v, maxValue(state, alpha, beta, depth + 1));
            if (v == oldV) {
                action = oldAction;
            } else {
                action = move;
            }
            state.revert(false, oldRow, oldCol);
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
        }

        return v;
    }

    private static void updateOptimals(int v, String move) {
        ArrayList<String> moves;
        if (v > maxUtility) {
            optimalMoves.clear();
            moves = new ArrayList<String>();
            moves.add(move);
            optimalMoves.put(v, moves);
            maxUtility = v;
        } else {
            try {
                optimalMoves.get(v).add(move);
            } catch (NullPointerException e) {
                moves = new ArrayList<String>();
                moves.add(move);
                optimalMoves.put(v, moves);
                maxUtility = v;
            }
        }
    }
}