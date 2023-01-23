package pl.meta.task5.backend;

import pl.meta.task5.frontend.PathGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private List<Place> places;
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> minPopulationValues = new ArrayList<>();

    public AntAlgorithm(List<Place> places, int numOfAnts,
                        int numOfIterations, double feromonEvaporation, double feromonWeight,
                        double heuristicWeight, double randomChoiceChance, double vehicleMaxDemand) {
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
        for (int i = 0; i < numOfAnts; i++) {
            ants.add(new Ant(distances, feromons,
                    randomChoiceChance, numOfPlaces, heuristicWeight,
                    feromonWeight, vehicleMaxDemand));
        }
    }

    public Map<String, Double> start() {
        double shortestRoute = Double.MAX_VALUE;
        Ant bestAnt = null;
        for (int i = 0; i < numOfIterations; i++) {
            iterations.add(i);
            for (Ant a : ants) {
                while (a.getToVisitSize() > 1 || (a.getToVisitSize() == 1 && a.getToVisit().get(0) != 0)) {
                    a.move();
                }
                a.comeBackToBase();
            }
            double avgRouteInThisIteration = 0;
            for (Ant a : ants) {
                a.tryEnhanceRouteWith2Opt();
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
                        feromonWeight, vehicleMaxDemand));
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
        Map<String, Double> results = new HashMap<>();
        results.put("ShortestRoute", shortestRoute);
        results.put("NumOfVehicles", (double) bestAnt.getNumOfVehicles());
        return results;
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
