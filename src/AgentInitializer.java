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
                return state.getNumXMoves() - 2 * state.getNumOMoves();
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
                double dist1;
                double dist2;
                int oppLocalMoves = selfSorter ? state.getNumOLocalMoves() : state.getNumXLocalMoves();
                int oppMoves = selfSorter ? state.getNumOMoves() : state.getNumXMoves();
    
                if (oppLocalMoves == 1 || oppMoves < 8) {
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
                return state.getNumOMoves() - state.getNumXMoves();
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
                // double dist1;
                // double dist2;
                // int oppLocalMoves = selfSorter ? state.getNumXLocalMoves() : state.getNumOLocalMoves();
                // int oppMoves = selfSorter ? state.getNumXMoves() : state.getNumOMoves();

                // if (oppLocalMoves == 1 || oppMoves < 8) {
                //     dist1 = distFromOpp(state, !selfSorter, s1Row, s1Col);
                //     dist2 = distFromOpp(state, !selfSorter, s2Row, s2Col);
                // } else {
                //     dist1 = distFromCenter(s1Row, s1Col);
                //     dist2 = distFromCenter(s2Row, s2Col);
                // }

                double dist1 = distFromCenter(s1Row, s1Col);
                double dist2 = distFromCenter(s2Row, s2Col);

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