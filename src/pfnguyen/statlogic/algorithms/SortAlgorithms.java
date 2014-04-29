package pfnguyen.statlogic.algorithms;

import java.util.ArrayList;

public class SortAlgorithms {

    private SortAlgorithms() {
    }

    /**
     * Inefficient sort for large lists
     */
    public static void BubbleSort(ArrayList<Double> values) {
        for (int i = 0; i < values.size(); i++) {
            for (int j = 0; j < values.size(); j++) {

                if (j != values.size() - 1)
                    if (values.get(j).compareTo(values.get(j + 1)) > 0) {
                        Double temp = values.get(j);
                        values.set(j, values.get(j + 1));
                        values.set(j + 1, temp);
                    }
            }
        }
    }
}
