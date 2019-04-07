import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Minimax {
    private static String action;
    private static int maxDepth;
    private static Random rng = new Random();
    private static HashMap<Integer, ArrayList<String>> bestNextMoves;
    private static HashMap<String, String> transpositionTable;
    private static int maxUtility;
    private static boolean earlyStop;
    private static long startTime;
    public static int winDepth;
    public static boolean random = true;
    public static long timeRemaining;

    public static void resetTransTable() {
        if (transpositionTable == null) {
            transpositionTable = new HashMap<String, String>(4200000);
        } else {
            transpositionTable.clear();
        }
    }

    //debugging
    public static int getTableSize() {
        return transpositionTable.size();
    }

    public static String search(boolean forAgentX, State state, int maxDepth) {
        Minimax.maxDepth = maxDepth;
        bestNextMoves = new HashMap<Integer, ArrayList<String>>();
        action = "";
        maxUtility = Integer.MIN_VALUE;
        earlyStop = false;
        startTime = System.currentTimeMillis();

        maxUtility = maxValue(forAgentX, state, Integer.MIN_VALUE, Integer.MAX_VALUE, 0);

        if (earlyStop) {
            return "DNF";
        }

        if (Minimax.random) {
            int size;
            try {
                size = bestNextMoves.get(maxUtility).size();
                return bestNextMoves.get(maxUtility).get(rng.nextInt(size));
            } catch (NullPointerException e) {
                return action;
            }
        } else {
            return action;
        }
    }

    private static int maxValue(boolean forAgentX, State state, int alpha, int beta, int depth) {
        int v, oldV, bestLocalUtility, row, oldRow, col, oldCol, minResult, terminalUtil;
        String stateString, oldAction, bestSuccessor, bestLocalAction = "";
        ArrayList<String> successors;

        Minimax.timeRemaining -= System.currentTimeMillis() - startTime;
        Minimax.startTime = System.currentTimeMillis();

        if (Minimax.timeRemaining <= 210L) {
            earlyStop = true;
            return Integer.MAX_VALUE;
        }

        if (state.isTerminal() || depth == maxDepth) {
            terminalUtil = state.getUtility(forAgentX);
            if (terminalUtil == Integer.MAX_VALUE) {
                return terminalUtil - (depth - 1);
            } else if (terminalUtil == Integer.MIN_VALUE) {
                return terminalUtil + (depth - 1);
            } else {
                return terminalUtil;
            }
        }

        v = Integer.MIN_VALUE;
        oldRow = forAgentX ? state.getXRow() : state.getORow();
        oldCol = forAgentX ? state.getXCol() : state.getOCol();
        successors = state.getSuccessors(forAgentX, forAgentX);
        stateString = state.toString() + "M";
        bestSuccessor = transpositionTable.get(stateString);
        if (bestSuccessor != null) {
            putBestFirst(successors, bestSuccessor);
        }
        bestLocalUtility = v;
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));
            oldV = v;
            oldAction = action;

            state.move(forAgentX, forAgentX, row, col);
            minResult = minValue(forAgentX, state, alpha, beta, depth + 1);
            v = Math.max(v, minResult);
            if (bestLocalUtility < minResult) {
                bestLocalUtility = minResult;
                bestLocalAction = move;
            }
            if (v == oldV) {
                action = oldAction;
            } else {
                action = move;
            }
            if (depth == 0 && oldV <= minResult) {
                updateOptimals(v, move);
            }
            state.revert(forAgentX, forAgentX, oldRow, oldCol);
            if (v >= beta) {
                return v;
            }
            alpha = Math.max(alpha, v);
            if (earlyStop) {
                return v;
            }
        }

        if (!bestLocalAction.equals("")) {
            transpositionTable.put(stateString, bestLocalAction);
        }

        return v;
    }

    private static int minValue(boolean forAgentX, State state, int alpha, int beta, int depth) {
        int v, bestLocalUtility, row, oldRow, col, oldCol, terminalUtil;
        String stateString, bestSuccessor, bestLocalAction = "";
        ArrayList<String> successors;

        Minimax.timeRemaining -= System.currentTimeMillis() - startTime;
        Minimax.startTime = System.currentTimeMillis();

        if (Minimax.timeRemaining <= 210L) {
            earlyStop = true;
            return Integer.MIN_VALUE;
        }

        if (state.isTerminal() || depth == maxDepth) {
            terminalUtil = state.getUtility(forAgentX);
            if (terminalUtil == Integer.MAX_VALUE) {
                return terminalUtil - (depth - 1);
            } else if (terminalUtil == Integer.MIN_VALUE) {
                return terminalUtil + (depth - 1);
            } else {
                return terminalUtil;
            }
        }

        v = Integer.MAX_VALUE;
        oldRow = forAgentX ? state.getORow() : state.getXRow();
        oldCol = forAgentX ? state.getOCol() : state.getXCol();
        successors = state.getSuccessors(!forAgentX, forAgentX);
        stateString = state.toString() + "m";
        bestSuccessor = transpositionTable.get(stateString);
        if (bestSuccessor != null) {
            putBestFirst(successors, bestSuccessor);
        }
        bestLocalUtility = v;
        for (String move : successors) {
            row = Character.getNumericValue(move.charAt(0));
            col = Character.getNumericValue(move.charAt(1));

            state.move(forAgentX, !forAgentX, row, col);
            v = Math.min(v, maxValue(forAgentX, state, alpha, beta, depth + 1));
            if (bestLocalUtility > v) {
                bestLocalUtility = v;
                bestLocalAction = move;
            }
            state.revert(forAgentX, !forAgentX, oldRow, oldCol);
            if (v <= alpha) {
                return v;
            }
            beta = Math.min(beta, v);
            if (earlyStop) {
                return v;
            }
        }

        if (!bestLocalAction.equals("")) {
            transpositionTable.put(stateString, bestLocalAction);
        }

        return v;
    }

    private static void putBestFirst(ArrayList<String> successors, String bestMove) {
        if (!successors.remove(bestMove)) {
            throw new IllegalStateException(bestMove + " not in " + successors.toString());
        }
        successors.add(0, bestMove);
    }

    private static void updateOptimals(int v, String move) {
        ArrayList<String> moves;
        if (v > maxUtility) {
            bestNextMoves.clear();
            moves = new ArrayList<String>();
            moves.add(move);
            bestNextMoves.put(v, moves);
            maxUtility = v;
        } else {
            try {
                bestNextMoves.get(v).add(move);
            } catch (NullPointerException e) {
                moves = new ArrayList<String>();
                moves.add(move);
                bestNextMoves.put(v, moves);
                maxUtility = v;
            }
        }
    }

    public static int getMaxUtility() {
        return maxUtility;
    }
}