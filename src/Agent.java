import java.util.Comparator;

class Agent {
    private final State STATE;
    private final Heuristic HEURISTIC;
    private final Comparator<String> SELF_SORTER;
    private final Comparator<String> OPP_SORTER;

    public Agent(State state, Heuristic heuristic, Comparator<String> selfSorter, Comparator<String> oppSorter) {
        STATE = state;
        HEURISTIC = heuristic;
        SELF_SORTER = selfSorter;
        OPP_SORTER = oppSorter;
    }

    public int utilityFunc() {
        return HEURISTIC.computeUtility(STATE);
    }

    public Comparator<String> getSorter(boolean ofSelf) {
        return ofSelf ? SELF_SORTER : OPP_SORTER;
    }
}