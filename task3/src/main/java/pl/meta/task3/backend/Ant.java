package pl.meta.task3.backend;

import java.util.ArrayList;
import java.util.List;

public class Ant {
    List<Integer> visited = new ArrayList<>();
    Distances distances;
    int currentPosition;

    public Ant(int startPosition, Distances distances) {
        this.currentPosition = startPosition;
        visited.add(startPosition);
        this.distances = distances;
    }

    public void move(int moveTo) {
        visited.add(moveTo);
        currentPosition = moveTo;
    }

    public double calculateWholeRoute() {
        double route = 0;
        int lastIndex = -1;
        for (Integer id: visited) {
            if (lastIndex != -1) {
                route += distances.getDistance(lastIndex, id);
            }
            lastIndex = id;
        }
        return route;
    }
}
