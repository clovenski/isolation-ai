class AgentInitializer {
    public static Agent getAgentX(State state) {
        return new Agent(
            state,
            getAgentXHeuristic(),
            getAgentXSorter(state, true),
            getAgentXSorter(state, false));
    }

    private static Heuristic getAgentXHeuristic() {
        return new Heuristic() {
            public int computeUtility(State state) {
                return state.getNumXMoves() + state.getNumXLocalMoves() - state.getNumOMoves() - state.getNumOLocalMoves();
            }
        };
    }

    private static Sorter getAgentXSorter(State state, boolean selfSorter) {
        return new Sorter() {
            public int compare(String s1, String s2) {
                int s1Row = s1.charAt(0);
                int s1Col = s1.charAt(1);
                int s2Row = s2.charAt(0);
                int s2Col = s2.charAt(1);
                int dist1;
                int dist2;
                int oppMoves = selfSorter ? state.getNumOMoves() : state.getNumXMoves();
    
                if (oppMoves <= 16) {
                    dist1 = distFromOpp(state, selfSorter, s1Row, s1Col);
                    dist2 = distFromOpp(state, selfSorter, s2Row, s2Col);
                } else {
                    dist1 = distFromCenter(s1Row, s1Col);
                    dist2 = distFromCenter(s2Row, s2Col);
                }
    
                if (dist1 > dist2) {
                    return 1;
                } else if (dist1 < dist2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
    }

    public static Agent getAgentO(State state) {
        return new Agent(
            state,
            getAgentOHeuristic(),
            getAgentOSorter(state, true),
            getAgentOSorter(state, false));
    }

    private static Heuristic getAgentOHeuristic() {
        return new Heuristic() {
            public int computeUtility(State state) {
                return (state.getNumOMoves() << state.getNumOLocalMoves()) - (state.getNumXMoves() << state.getNumXLocalMoves());
            }
        };
    }

    private static Sorter getAgentOSorter(State state, boolean selfSorter) {
        return new Sorter() {
            public int compare(String s1, String s2) {
                int s1Row = s1.charAt(0);
                int s1Col = s1.charAt(1);
                int s2Row = s2.charAt(0);
                int s2Col = s2.charAt(1);
                int dist1;
                int dist2;
                int oppMoves = selfSorter ? state.getNumXMoves() : state.getNumOMoves();

                if (oppMoves <= 16) {
                    dist1 = distFromOpp(state, !selfSorter, s1Row, s1Col);
                    dist2 = distFromOpp(state, !selfSorter, s2Row, s2Col);
                } else {
                    dist1 = distFromCenter(s1Row, s1Col);
                    dist2 = distFromCenter(s2Row, s2Col);
                }

                if (dist1 > dist2) {
                    return 1;
                } else if (dist1 < dist2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        };
    }
}