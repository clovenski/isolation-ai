import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Minimax {
    private static String action;
    private static int maxDepth;
    private static Random rng = new Random();
    private static HashMap<Integer, ArrayList<String>> optimalMoves;
    private static int maxUtility;
    private static boolean earlyStop;
    private static long startTime;
    public static boolean random = true;
    public static long timeRemaining;

    public static String search(boolean forAgentX, State state, int maxDepth) {
        Minimax.maxDepth = maxDepth;
        optimalMoves = new HashMap<Integer, ArrayList<String>>();
        action = "";
        maxUtility = Integer.MIN_VALUE;
        earlyStop = false;
        startTime = System.currentTimeMillis();

        maxValue(forAgentX, state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        if (earlyStop) {
            return "DNF";
        }

        if (Minimax.random) {
            int size;
            try {
                size = optimalMoves.get(maxUtility).size();
                return optimalMoves.get(maxUtility).get(rng.nextInt(size));
            } catch (NullPointerException e) {
                return action;
            }
        } else {
            return action;
        }
    }

    private static int maxValue(boolean forAgentX, State state, int alpha, int beta, int depth) {
        int v, oldV, row, oldRow, col, oldCol, minResult;
        String oldAction;
        ArrayList<String> successors;

        Minimax.timeRemaining -= System.currentTimeMillis() - startTime;
        Minimax.startTime = System.currentTimeMillis();

        if (Minimax.timeRemaining <= 500L) {
            earlyStop = true;
            return state.getUtility(forAgentX);
        }

        if (state.isTerminal() || depth == maxDepth) {
            return state.getUtility(forAgentX);
        }

        v = Integer.MIN_VALUE;
        oldRow = forAgentX ? state.getXRow() : state.getORow();
        oldCol = forAgentX ? state.getXCol() : state.getOCol();
        successors = state.getSuccessors(forAgentX);
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));
            oldV = v;
            oldAction = action;

            state.move(forAgentX, row, col);
            minResult = minValue(forAgentX, state, alpha, beta, depth + 1);
            v = Math.max(v, minResult);
            if (v == oldV) {
                action = oldAction;
            } else {
                action = move;
            }
            if (depth == 0 && oldV <= minResult) {
                updateOptimals(v, move);
            }
            state.revert(forAgentX, oldRow, oldCol);
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
            if (earlyStop) {
                return v;
            }
        }

        return v;
    }

    private static int minValue(boolean forAgentX, State state, int alpha, int beta, int depth) {
        int v, oldV, row, oldRow, col, oldCol;
        String oldAction;
        ArrayList<String> successors;

        Minimax.timeRemaining -= System.currentTimeMillis() - startTime;
        Minimax.startTime = System.currentTimeMillis();

        if (Minimax.timeRemaining <= 500L) {
            earlyStop = true;
            return state.getUtility(forAgentX);
        }

        if (state.isTerminal() || depth == maxDepth) {
            return state.getUtility(forAgentX);
        }

        v = Integer.MAX_VALUE;
        oldRow = forAgentX ? state.getORow() : state.getXRow();
        oldCol = forAgentX ? state.getOCol() : state.getXCol();
        successors = state.getSuccessors(!forAgentX);
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));
            oldV = v;
            oldAction = action;

            state.move(!forAgentX, row, col);
            v = Math.min(v, maxValue(forAgentX, state, alpha, beta, depth + 1));
            if (v == oldV) {
                action = oldAction;
            } else {
                action = move;
            }
            state.revert(!forAgentX, oldRow, oldCol);
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
            if (earlyStop) {
                return v;
            }
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