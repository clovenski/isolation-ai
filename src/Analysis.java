class Analysis {
    public static void main(String[] args) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%2d ", getSpaceWeight(i, j));
            }
            System.out.println();
        }

        System.out.println();

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                System.out.printf("%2s ", Integer.toString(i) + Integer.toString(j));
            }
            System.out.println();
        }
    }

    public static int getSpaceWeight(int i, int j) {
        int weight;

        weight = 14;
        for (int k = 0; k < 4; k++) {
            weight += countDiag(i, j, k);
        }

        return weight;
    }

    public static int countDiag(int i, int j, int axis) {
        int count = 0;
        switch (axis) {
            case 0:
            while (i + 1 < 8 && j + 1 < 8) {
                count++;
                i++;
                j++;
            }
            break;
            case 1:
            while (i + 1 < 8 && j - 1 >= 0) {
                count++;
                i++;
                j--;
            }
            break;
            case 2:
            while (i - 1 >= 0 && j - 1 >= 0) {
                count++;
                i--;
                j--;
            }
            break;
            case 3:
            while (i - 1 >= 0 && j + 1 < 8) {
                count++;
                i--;
                j++;
            }
            break;
        }
        return count;
    }
}