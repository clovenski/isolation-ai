class Agent {
    private final State STATE;
    private final Heuristic HEURISTIC;

    public Agent(State state, Heuristic heuristic) {
        STATE = state;
        HEURISTIC = heuristic;
    }

    public int utilityFunc(int xMoves, int oMoves) {
        return HEURISTIC.computeUtility(STATE, xMoves, oMoves);
    }
}