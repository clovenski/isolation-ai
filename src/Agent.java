class Agent {
    private final State STATE;
    private final Heuristic HEURISTIC;

    public Agent(State state, Heuristic heuristic) {
        STATE = state;
        HEURISTIC = heuristic;
    }

    public int utilityFunc() {
        return HEURISTIC.computeUtility(STATE);
    }
}