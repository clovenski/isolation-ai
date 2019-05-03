import java.util.Comparator;

abstract class Sorter implements Comparator<String> {
    abstract public int compare(String s1, String s2);

    protected int distFromOpp(State state, boolean forX, int row, int col) {
        int oppRow = forX ? state.getORow() : state.getXRow();
        int oppCol = forX ? state.getOCol() : state.getXCol();
        return Math.abs(row - oppRow) + Math.abs(col - oppCol);
    }

    protected int distFromCenter(int row, int col) {
        return Math.abs(row - 3) + Math.abs(col - 3);
    }
}