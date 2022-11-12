package pl.meta.task3.backend;

import java.util.Arrays;

public class Feromons {
    private static double[][] weights;

    public Feromons(int numOfElements) {
        weights = new double[numOfElements][numOfElements];
        Arrays.stream(weights).forEach(row -> Arrays.fill(row, 1));
    }

    public double getFeromon(int id1, int id2) {
        return weights[id1][id2];
    }

    public void setFeromon(int id1, int id2, double value) {
        weights[id1][id2] = value;
        weights[id2][id1] = value;
    }
}
