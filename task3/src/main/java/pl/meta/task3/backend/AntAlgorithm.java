package pl.meta.task3.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntAlgorithm {
    Distances distances;
    Feromons feromons;
    List<Ant> ants = new ArrayList<>();
    int numOfIterations;
    double feromonEvaporation;
    double feromonWeight;
    double heuristicWeight;
    double randomChoiceChance;
    int numOfPlaces;
    int numOfAnts;

    public AntAlgorithm(List<Place> places, int numOfAnts,
                        int numOfIterations, double feromonEvaporation, double feromonWeight,
                        double heuristicWeight, double randomChoiceChance) {
        int numOfPlaces = places.size();
        this.numOfPlaces = numOfPlaces;
        this.distances = new Distances(places);
        this.feromons = new Feromons(numOfPlaces);
        this.numOfIterations = numOfIterations;
        this.feromonEvaporation = feromonEvaporation;
        this.feromonWeight = feromonWeight;
        this.heuristicWeight = heuristicWeight;
        this.randomChoiceChance = randomChoiceChance;
        this.numOfAnts = numOfAnts;
        for (int i = 0; i < numOfAnts; i++) {
            ants.add(new Ant((new Random()).nextInt(numOfPlaces - 1), distances, feromons,
                    randomChoiceChance, numOfPlaces, heuristicWeight, feromonWeight));
        }
    }

    public double start() {
        double shortestRoute = Double.MAX_VALUE;
        for (int i = 0; i < numOfIterations; i++) {
            for (int j = 0; j < numOfPlaces - 1; j++) {
                for (Ant a : ants) {
                    a.move();
                }
            }
            for (Ant a : ants) {
                double route = a.calculateWholeRoute();
                if (route < shortestRoute) {
                    shortestRoute = route;
                }
            }
            ants.clear();
            for (int j = 0; j < numOfAnts; j++) {
                ants.add(new Ant((new Random()).nextInt(numOfPlaces - 1), distances, feromons,
                        randomChoiceChance, numOfPlaces, heuristicWeight, feromonWeight));
            }
        }
        return shortestRoute;
    }
}
