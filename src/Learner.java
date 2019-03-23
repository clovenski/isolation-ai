import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class Learner {
    // char -> ascii
    // - -> 45
    // # -> 35
    // O -> 79
    // X -> 88

    // normalized
    // - -> 0.18867924528301888
    // # -> 0.0
    // O -> 0.8301886792452831
    // X -> 1.0

    private final HashMap<Character, Double> CHAR_VALUES;
    private final String FEAT_FILENAME = "dataset" + File.separator + "features.txt";
    private final String LAB_FILENAME = "dataset" + File.separator + "labels.txt";
    private State state;
    private PrintWriter featuresWriter;
    private PrintWriter labelsWriter;

    public Learner() {
        CHAR_VALUES = new HashMap<Character, Double>();
        CHAR_VALUES.put('-', 0.18867924528301888);
        CHAR_VALUES.put('#', 0.0);
        CHAR_VALUES.put('O', 0.8301886792452831);
        CHAR_VALUES.put('X', 1.0);

        try {
            featuresWriter = new PrintWriter(new FileWriter(new File(FEAT_FILENAME)), true);
            labelsWriter = new PrintWriter(new FileWriter(new File(LAB_FILENAME)), true);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void learn() {
        int row, col, turnCount;
        String oppMove;

        for (int i = 0; i < 10; i++) {
            turnCount = 0;
            state = new State(true);
            while (!state.isTerminal()) {
                Minimax.timeRemaining = 400000L;
                Minimax.searchForLearning(this, state, 10);

                if (turnCount < 2) {
                    turnCount++;
                    Minimax.random = turnCount < 2;
                }

                if (state.isTerminal()) {
                    break;
                }

                oppMove = getOppRandMove();
                row = Character.getNumericValue(oppMove.charAt(0));
                col = Character.getNumericValue(oppMove.charAt(1));
                state.move(false, row, col);
            }
        }

        for (int i = 0; i < 10; i++) {
            turnCount = 0;
            state = new State(false);

            oppMove = getOppRandMove();
            row = Character.getNumericValue(oppMove.charAt(0));
            col = Character.getNumericValue(oppMove.charAt(1));
            state.move(false, row, col);

            while (!state.isTerminal()) {
                Minimax.timeRemaining = 400000L;
                Minimax.searchForLearning(this, state, 10);

                if (turnCount < 2) {
                    turnCount++;
                    Minimax.random = turnCount < 2;
                }

                if (state.isTerminal()) {
                    break;
                }

                oppMove = getOppRandMove();
                row = Character.getNumericValue(oppMove.charAt(0));
                col = Character.getNumericValue(oppMove.charAt(1));
                state.move(false, row, col);
            }
        }

        featuresWriter.close();
        labelsWriter.close();
    }

    public void applyAction(String action) {
        int row = Character.getNumericValue(action.charAt(0));
        int col = Character.getNumericValue(action.charAt(1));
        state.move(true, row, col);
    }

    private String getOppRandMove() {
        Random rng = new Random();
        ArrayList<String> successors = state.getSuccessors(state.getORow(), state.getOCol());

        return successors.get(rng.nextInt(successors.size()));
    }

    private double[] getStateFeatures(State state) {
        double[] features = new double[64];
        int index = 0;

        for (char c : state.toCharArray()) {
            features[index++] = CHAR_VALUES.get(c);
        }

        return features;
    }

    public void writeToFile(State state, int label) {
        double[] features = getStateFeatures(state);
        String row = "";

        for (double feature : features) {
            row += feature + " ";
        }
        row = row.trim();
        featuresWriter.println(row);

        labelsWriter.println(label);
    }

    public static void main(String[] args) {
        Learner learner = new Learner();

        learner.learn();
    }
}