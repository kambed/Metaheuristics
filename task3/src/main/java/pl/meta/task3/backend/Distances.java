package pl.meta.task3.backend;

import java.util.List;

public class Distances {
    private final double[][] distances;

    public Distances(List<Place> places) {
        int numOfPlaces = places.size();
        distances = new double[numOfPlaces][numOfPlaces];
        for (int i = 0; i < numOfPlaces; i++) {
            for (int j = 0; j < numOfPlaces; j++) {
                distances[i][j] = Math.sqrt(
                        ((places.get(i).getX() - places.get(j).getX()) * (places.get(i).getX() - places.get(j).getX())) +
                                ((places.get(i).getY() - places.get(j).getY()) * (places.get(i).getY() - places.get(j).getY()))
                );
            }
        }
    }

    public double getDistance(int id1, int id2) {
        return distances[id1][id2];
    }
}
