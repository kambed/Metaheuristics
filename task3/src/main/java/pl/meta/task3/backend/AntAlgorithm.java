package pl.meta.task3.backend;

import pl.meta.task3.frontend.PathGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AntAlgorithm {
    Distances distances;
    Feromons feromons;
    List<Ant> ants = new ArrayList<>();
    int numOfIterations;
    double feromonWeight;
    double heuristicWeight;
    double randomChoiceChance;
    int numOfPlaces;
    int numOfAnts;
    List<Place> places;

    public AntAlgorithm(List<Place> places, int numOfAnts,
                        int numOfIterations, double feromonEvaporation, double feromonWeight,
                        double heuristicWeight, double randomChoiceChance) {
        int numOfPlaces = places.size();
        this.numOfPlaces = numOfPlaces;
        this.distances = new Distances(places);
        this.feromons = new Feromons(numOfPlaces, feromonEvaporation);
        this.numOfIterations = numOfIterations;
        this.feromonWeight = feromonWeight;
        this.heuristicWeight = heuristicWeight;
        this.randomChoiceChance = randomChoiceChance;
        this.numOfAnts = numOfAnts;
        this.places = places;
        for (int i = 0; i < numOfAnts; i++) {
            ants.add(new Ant((new Random()).nextInt(numOfPlaces - 1), distances, feromons,
                    randomChoiceChance, numOfPlaces, heuristicWeight, feromonWeight));
        }
    }

    public double start() {
        double shortestRoute = Double.MAX_VALUE;
        Ant bestAnt = null;
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
                    bestAnt = a;
                }
            }
            feromons.evaporate();
            for (Ant a : ants) {
                a.addFeromons();
            }
            ants.clear();
            for (int j = 0; j < numOfAnts; j++) {
                ants.add(new Ant((new Random()).nextInt(numOfPlaces - 1), distances, feromons,
                        randomChoiceChance, numOfPlaces, heuristicWeight, feromonWeight));
            }
        }
        List<Double> x = new ArrayList<>();
        List<Double> y = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (Integer locId : bestAnt.getVisited()) {
            for (Place p : places) {
                if(p.getId() == locId) {
                    x.add((double) p.getX() * 6 + 50);
                    y.add((double) p.getY() * 6 + 50);
                    labels.add(String.valueOf(p.getId()));
                    break;
                }
            }
        }
        PathGenerator.generateRoad(x, y, labels);
        return shortestRoute;
    }
}
