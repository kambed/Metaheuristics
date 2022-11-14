package pl.meta.task3.backend;

import java.util.*;

public class Ant {
    List<Integer> visited = new ArrayList<>();
    Distances distances;
    Feromons feromons;
    int currentPosition;
    double randomMoveChance;
    int numOfPlacesToVisit;
    double heuristicWeight;
    double feromonWeight;
    double route = -1;

    public Ant(int startPosition, Distances distances, Feromons feromons, double randomMoveChance, int placesToVisit,
               double heuristicWeight, double feromonWeight) {
        this.currentPosition = startPosition;
        visited.add(startPosition);
        this.distances = distances;
        this.feromons = feromons;
        this.randomMoveChance = randomMoveChance;
        this.numOfPlacesToVisit = placesToVisit;
        this.heuristicWeight = heuristicWeight;
        this.feromonWeight = feromonWeight;
    }

    public void move() {
        //move random
        if (Math.random() < randomMoveChance) {
            int nextRandomPlaceIndex;
            Random random = new Random();
            do {
                nextRandomPlaceIndex = random.nextInt(numOfPlacesToVisit);
            } while (visited.contains(nextRandomPlaceIndex));
            visited.add(nextRandomPlaceIndex);
            currentPosition = nextRandomPlaceIndex;
        } else {
            Map<Integer, Double> chance = new HashMap<>();
            double sumOfChances = 0;
            for (int i = 0; i < numOfPlacesToVisit; i++) {
                if (!visited.contains(i)) {
                    double chanceValue = Math.pow(feromons.getFeromon(currentPosition, i), feromonWeight) *
                            Math.pow((1 / distances.getDistance(currentPosition, i)), heuristicWeight);
                    sumOfChances += chanceValue;
                    chance.put(i, chanceValue);
                }
            }
            double rouletteIndex = Math.random() * sumOfChances;
            int rouletteChosenIndex = 0;
            for (Map.Entry<Integer, Double> entry : chance.entrySet()) {
                rouletteIndex -= entry.getValue();
                if (rouletteIndex < 0) {
                    rouletteChosenIndex = entry.getKey();
                    break;
                }
            }
            visited.add(rouletteChosenIndex);
            currentPosition = rouletteChosenIndex;
        }
    }

    public void addFeromons() {
        int previousIndex = -1;
        for (Integer v : visited) {
            if (previousIndex != -1) {
                feromons.setFeromon(previousIndex, v, feromons.getFeromon(previousIndex, v) +
                        (1 / calculateWholeRoute()));
            }
            previousIndex = v;
        }
    }

    public double calculateWholeRoute() {
        if (route == -1) {
            double route = 0;
            int lastIndex = -1;
            for (Integer id : visited) {
                if (lastIndex != -1) {
                    route += distances.getDistance(lastIndex, id);
                }
                lastIndex = id;
            }
            this.route = route;
        }
        return route;
    }
}
