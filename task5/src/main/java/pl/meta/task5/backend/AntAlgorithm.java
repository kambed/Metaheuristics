package pl.meta.task5.backend;

import pl.meta.task5.frontend.PathGenerator;

import java.util.ArrayList;
import java.util.List;

public class AntAlgorithm {
    private Distances distances;
    private Feromons feromons;
    private List<Ant> ants = new ArrayList<>();
    private int numOfIterations;
    private double feromonWeight;
    private double heuristicWeight;
    private double randomChoiceChance;
    private int numOfPlaces;
    private int numOfAnts;
    private double vehicleMaxDemand;
    private int numOfVehicles;
    private List<Place> places;
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> minPopulationValues = new ArrayList<>();

    public AntAlgorithm(List<Place> places, int numOfAnts,
                        int numOfIterations, double feromonEvaporation, double feromonWeight,
                        double heuristicWeight, double randomChoiceChance,
                        int numOfVehicles, double vehicleMaxDemand) {
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
        this.vehicleMaxDemand = vehicleMaxDemand;
        this.numOfVehicles = numOfVehicles;
        for (int i = 0; i < numOfAnts; i++) {
            ants.add(new Ant(distances, feromons,
                    randomChoiceChance, numOfPlaces, heuristicWeight,
                    feromonWeight, vehicleMaxDemand, numOfVehicles));
        }
    }

    public double start() {
        double shortestRoute = Double.MAX_VALUE;
        Ant bestAnt = null;
        for (int i = 0; i < numOfIterations; i++) {
            iterations.add(i);
            for (int j = 0; j < numOfPlaces - 1; j++) {
                for (Ant a : ants) {
                    a.move();
                }
            }
            double avgRouteInThisIteration = 0;
            for (Ant a : ants) {
                double route = a.calculateWholeRoute();
                avgRouteInThisIteration += route;
                if (route < shortestRoute) {
                    shortestRoute = route;
                    bestAnt = a;
                }
            }
            avgPopulationValues.add(avgRouteInThisIteration / ants.size());
            minPopulationValues.add(shortestRoute);
            feromons.evaporate();
            for (Ant a : ants) {
                a.addFeromons();
            }
            ants.clear();
            for (int j = 0; j < numOfAnts; j++) {
                ants.add(new Ant(distances, feromons,
                        randomChoiceChance, numOfPlaces, heuristicWeight,
                        feromonWeight, vehicleMaxDemand, numOfVehicles));
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

    public List<Integer> getIterations() {
        return iterations;
    }

    public List<Double> getAvgPopulationValues() {
        return avgPopulationValues;
    }

    public List<Double> getMinPopulationValues() {
        return minPopulationValues;
    }
}
