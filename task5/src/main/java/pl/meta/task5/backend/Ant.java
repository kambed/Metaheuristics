package pl.meta.task5.backend;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Ant {
    private List<Integer> visited = new ArrayList<>();
    private List<Integer> toVisit;
    private Distances distances;
    private Feromons feromons;
    private int nextPossiblePosition;
    private int currentPosition;
    private double randomMoveChance;
    private int numOfPlacesToVisit;
    private double heuristicWeight;
    private double feromonWeight;
    private double route = -1;
    private double maxDemand;
    private double currentDemand;
    private double currentTime;
    private int numOfVehicles = 0;

    public Ant(Distances distances, Feromons feromons, double randomMoveChance, int placesToVisit,
               double heuristicWeight, double feromonWeight, double maxDemand) {
        this.currentPosition = 0;
        visited.add(this.currentPosition);
        this.distances = distances;
        this.feromons = feromons;
        this.randomMoveChance = randomMoveChance;
        this.numOfPlacesToVisit = placesToVisit - 1;
        this.heuristicWeight = heuristicWeight;
        this.feromonWeight = feromonWeight;
        this.maxDemand = maxDemand;
        toVisit = IntStream.rangeClosed(1, numOfPlacesToVisit)
                .boxed().collect(Collectors.toList());
    }

    public void move() {
        //move random
        List<Integer> possibleToVisit = toVisit.stream().filter(i -> distances.getPlace(i).getDueTime() > currentTime).toList();
        if (possibleToVisit.size() != 0) {
            if (Math.random() < randomMoveChance) {
                Random random = new Random();
                nextPossiblePosition = possibleToVisit.get(random.nextInt(possibleToVisit.size()));
            } else {
                Map<Integer, Double> chance = new HashMap<>();
                double sumOfChances = 0;
                for (int i = 1; i < numOfPlacesToVisit + 1; i++) {
                    if (!possibleToVisit.contains(i)) {
                        continue;
                    }
                    double chanceValue = Math.pow(feromons.getFeromon(currentPosition, i), feromonWeight) *
                            Math.pow((1 / distances.getDistance(currentPosition, i)), heuristicWeight);
                    sumOfChances += chanceValue;
                    chance.put(i, chanceValue);
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
        } else {
            nextPossiblePosition = 0;
        }
        Place p = distances.getPlace(nextPossiblePosition);
        if (currentDemand + p.getDemand() > maxDemand || possibleToVisit.size() == 0) {
            currentDemand = 0;
            currentTime = 0;
            nextPossiblePosition = 0;
        }
        currentPosition = nextPossiblePosition;
        visited.add(currentPosition);
        toVisit.remove((Integer) currentPosition);
        currentDemand += p.getDemand();
        if (p.getReadyTime() > currentTime) {
            currentTime = p.getReadyTime();
        }
        currentTime += p.getServiceTime();
        if (currentPosition == 0) {
            numOfVehicles++;
        }
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
        if (route != -1) {
            return route;
        }
        double route = 0;
        int lastIndex = -1;
        for (Integer id : visited) {
            if (lastIndex != -1) {
                route += distances.getDistance(lastIndex, id);
            }
            lastIndex = id;
        }
        this.route = route;
        return route;
    }

    public List<List<Integer>> getRoutes(List<Integer> route) {
        List<List<Integer>> routes = new ArrayList<>();
        int lastBase = 0;
        for (int i = 1; i < route.size(); i++) {
            if (route.get(i) == 0) {
                routes.add(route.subList(lastBase, i));
                lastBase = i;
            }
        }
        return routes;
    }

    private boolean checkIfCouldBeSwapped(List<Integer> route1, List<Integer> route2, int index1, int index2) {
        List<Integer> copyRoute1 = new ArrayList<>(route1);
        List<Integer> copyRoute2 = new ArrayList<>(route2);
        copyRoute1.add(0);
        copyRoute2.add(0);
        double startDistance = calculateRoute(copyRoute1) + calculateRoute(copyRoute2);
        int placeIn1 = copyRoute1.get(index1);
        copyRoute1.set(index1, copyRoute2.get(index2));
        copyRoute2.set(index2, placeIn1);

        if (!checkIfTimeOk(copyRoute1) || !checkIfTimeOk(copyRoute2) ||
                !checkIfDemandOk(copyRoute1) || !checkIfDemandOk(copyRoute2)) {
            return false;
        }
        return (calculateRoute(copyRoute1) + calculateRoute(copyRoute2)) < startDistance;
    }

    private boolean checkIfTimeOk(List<Integer> route) {
        for (int i = 1; i < route.size(); i++) {
            Place p1 = distances.getPlace(route.get(i - 1));
            Place p2 = distances.getPlace(route.get(i));
            if (p1.getDueTime() + p1.getDueTime() > p2.getReadyTime()) {
                return false;
            }
        }
        return true;
    }

    private boolean checkIfDemandOk(List<Integer> route) {
        int demandSum = 0;
        for (Integer integer : route) {
            demandSum += distances.getPlace(integer).getDemand();
        }
        return demandSum < maxDemand;
    }

    public double calculateRoute(List<Integer> route) {
        double routeSum = 0;
        for (int i = 1; i < route.size(); i++) {
            routeSum += distances.getDistance(route.get(i - 1), route.get(i));
        }
        return routeSum;
    }

    public boolean tryEnhanceRouteWith2Opt() {
        boolean anythingChanged = false;
        List<List<Integer>> routes = getRoutes(visited);
        for (int i = 0; i < routes.size(); i++) {
            for (int j = 0; j < routes.size(); j++) {
                for (int k = 0; k < routes.get(i).size(); k++) {
                    for (int l = 0; l < routes.get(j).size(); l++) {
                        if (checkIfCouldBeSwapped(routes.get(i), routes.get(j), k, l)
                                && routes.get(i).get(k) != 0 && routes.get(j).get(k) != 0) {
                            int placeIn1 = routes.get(i).get(k);
                            routes.get(i).set(k, routes.get(j).get(l));
                            routes.get(j).set(l, placeIn1);
                            anythingChanged = true;
                        }
                    }
                }
            }
        }
        List<Integer> newVisited = new ArrayList<>();
        for (List<Integer> route : routes) {
            newVisited.addAll(route);
        }
        newVisited.add(0);
        visited = newVisited;
        return anythingChanged;
    }

    public List<Integer> getVisited() {
        return visited;
    }

    public int getToVisitSize() {
        return toVisit.size();
    }

    public List<Integer> getToVisit() {
        return toVisit;
    }

    public int getNumOfVehicles() {
        return numOfVehicles;
    }
}
