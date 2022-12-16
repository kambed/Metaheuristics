package pl.meta.task5.backend;

import java.util.*;

public class Ant {
    private List<Integer> visited = new ArrayList<>();
    private Distances distances;
    private Feromons feromons;
    private int nextPossiblePosition;
    private int currentPosition;
    private double randomMoveChance;
    private int numOfPlacesToVisit;
    private int numOfPlacesVisited;
    private double heuristicWeight;
    private double feromonWeight;
    private double route = -1;
    private double maxDemand;
    private double currentDemand;
    private double maxTime;
    private double currentTime;
    private int numOfVehicles;

    public Ant(Distances distances, Feromons feromons, double randomMoveChance, int placesToVisit,
               double heuristicWeight, double feromonWeight, double maxDemand, int numOfVehicles) {
        this.currentPosition = 0;
        visited.add(this.currentPosition);
        this.distances = distances;
        this.feromons = feromons;
        this.randomMoveChance = randomMoveChance;
        this.numOfPlacesToVisit = placesToVisit - 1;
        this.heuristicWeight = heuristicWeight;
        this.feromonWeight = feromonWeight;
        this.maxDemand = maxDemand;
        this.numOfVehicles = numOfVehicles;
    }

    public void move() {
        //move random
        if (Math.random() < randomMoveChance) {
            int nextRandomPlaceIndex;
            Random random = new Random();
            do {
                nextRandomPlaceIndex = random.nextInt(numOfPlacesToVisit);
            } while (visited.contains(nextRandomPlaceIndex) || nextRandomPlaceIndex == 0);
            nextPossiblePosition = nextRandomPlaceIndex;
        } else {
            Map<Integer, Double> chance = new HashMap<>();
            double sumOfChances = 0;
            for (int i = 0; i < numOfPlacesToVisit; i++) {
                if (!visited.contains(i) && i != 0) {
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
            nextPossiblePosition = rouletteChosenIndex;
        }
        Place p = distances.getPlace(nextPossiblePosition);
        if (currentDemand + p.getDemand() > maxDemand) {
            currentPosition = 0;
            currentDemand = 0;
        } else {
            currentPosition = nextPossiblePosition;
        }
        visited.add(currentPosition);
        currentDemand += p.getDemand();
    }

    public void comeBackToBase() {
        visited.add(0);
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

    public List<Integer> getVisited() {
        return visited;
    }
}
