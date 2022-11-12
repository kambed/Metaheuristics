package pl.meta.task3.backend;

import java.util.ArrayList;
import java.util.List;

public class AntAlgorithm {
    Distances distances;
    Feromons feromons;
    List<Ant> ants = new ArrayList<>();
    int numOfIterations;
    double feromonEvaporation;
    double feromonWeight;
    double heuristicWeight;
    double randomChoiceChance;

    public AntAlgorithm(List<Place> places, int numOfAnts,
                        int numOfIterations, double feromonEvaporation, double feromonWeight,
                        double heuristicWeight, double randomChoiceChance) {
        int numOfPlaces = places.size();
        this.distances = new Distances(places);
        this.feromons = new Feromons(numOfPlaces);
        this.numOfIterations = numOfIterations;
        this.feromonEvaporation = feromonEvaporation;
        this.feromonWeight = feromonWeight;
        this.heuristicWeight = heuristicWeight;
        this.randomChoiceChance = randomChoiceChance;
        for (int i = 0; i < numOfAnts; i++) {
            ants.add(new Ant((int) Math.round(Math.random() * (numOfPlaces - 1) + 1),distances));
        }
    }
}
