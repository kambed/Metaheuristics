package pl.meta.task3.backend;

import java.util.Arrays;

public class Feromons {
    private double[][] weights;
    private double evaporationRate;

    public Feromons(int numOfElements, double evaporationRate) {
        weights = new double[numOfElements][numOfElements];
        Arrays.stream(weights).forEach(row -> Arrays.fill(row, 1));
        this.evaporationRate = evaporationRate;
    }

    public double getFeromon(int id1, int id2) {
        return weights[id1][id2];
    }

    public void setFeromon(int id1, int id2, double value) {
        weights[id1][id2] = value;
        weights[id2][id1] = value;
    }

    public void evaporate() {
        for (int i = 0; i < weights.length; i++) {
            for (int j = 0; j < weights.length; j++) {
                weights[i][j] = weights[i][j] * (1 - evaporationRate);
            }
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Feromons{");
        sb.append("weights=").append(Arrays.deepToString(weights));
        sb.append('}');
        return sb.toString();
    }
}
