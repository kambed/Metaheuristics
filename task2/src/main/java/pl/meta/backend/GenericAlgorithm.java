package pl.meta.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Comparator.comparing;

public class GenericAlgorithm {
    private final List<Item> items;
    private final int backpackMaxWeight;
    private final int populationSize;
    private final SelectionMethod selectionMethod;
    private final CrossMethod crossMethod;
    private final double crossChance;
    private final double mutationChance;
    private final List<Integer> iterations = new ArrayList<>();
    private final List<Double> avgPopulationValues = new ArrayList<>();
    private final List<Double> maxPopulationValues = new ArrayList<>();

    public GenericAlgorithm(List<Item> items, int backpackMaxWeight, int populationSize, SelectionMethod selectionMethod,
                            CrossMethod crossMethod, double crossChance, double mutationChance) {
        this.items = items;
        this.backpackMaxWeight = backpackMaxWeight;
        this.populationSize = populationSize;
        this.selectionMethod = selectionMethod;
        this.crossMethod = crossMethod;
        this.crossChance = crossChance;
        this.mutationChance = mutationChance;
    }

    public List<Backpack> start(int numOfIterations) {
        List<Backpack> population = initPopulation();
        addPointsToData(population, 0);
        for (int i = 0; i < numOfIterations; i++) {
            population = newPopulation(population);
            addPointsToData(population, i + 1);
        }
        return population;
    }

    public void addPointsToData(List<Backpack> population, int iteration) {
        iterations.add(iteration);
        maxPopulationValues.add((double) population.stream().max(comparing(Backpack::getValue)).get().getValue());
        avgPopulationValues.add(
            population.stream().mapToDouble(backpack -> (double) backpack.getValue()).average().orElse(0.0)
        );
    }

    public List<Double> getAvgPopulationValues() {
        return avgPopulationValues;
    }

    public List<Double> getMaxPopulationValues() {
        return maxPopulationValues;
    }

    public List<Integer> getIterations() {
        return iterations;
    }

    public List<Backpack> initPopulation() {
        List<Backpack> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Backpack(backpackMaxWeight, items));
        }
        return population;
    }

    public List<Backpack> newPopulation(List<Backpack> population) {
        List<Backpack> nextPopulation = new ArrayList<>();

        //Selection
        int numOfChildren = (int) (populationSize * crossChance);
        BiFunction<List<Backpack>, Integer, List<Pair>> selectPairFunction = this::selectPairsRoulette;
        if (selectionMethod == SelectionMethod.TOURNAMENT) {
            selectPairFunction = this::selectPairsTournament;
        }

        //Cross
        List<Pair> pairs = selectPairFunction.apply(population, numOfChildren);
        for (Pair pair : pairs) {
            nextPopulation.addAll(pair.cross(crossMethod));
        }

        //Move some old population elements to new one
        if (nextPopulation.size() < populationSize) {
            int populationDiff = populationSize - nextPopulation.size();
            population.sort(new BackpackComparator());
            for (int i = 0; i < populationDiff; i++) {
                nextPopulation.add(population.get(i));
            }
        }

        //Mutations
        for (int i = 0; i < populationSize; i++) {
            if (Math.random() <= mutationChance) {
                int bitToChange = (int) Math.round(Math.random() * (items.size() - 1));
                nextPopulation.get(i).setItemOccurrence(bitToChange,
                        nextPopulation.get(i).getItemsOccurrence()[bitToChange]);
            }
        }

        return nextPopulation;
    }

    public List<Pair> selectPairsRoulette(List<Backpack> population, Integer numOfChildren) {
        List<Pair> pairs = new ArrayList<>();
        if (numOfChildren % 2 == 1) {
            numOfChildren++;
        }
        for (int i = 0; i < numOfChildren / 2; i++) {
            int sumOfAdaptations = 0;
            for (Backpack bp : population) {
                sumOfAdaptations += bp.getValue();
            }

            //Choose parent1
            Backpack parent1 = chooseParentRoulette(population, sumOfAdaptations);

            //Choose parent2
            sumOfAdaptations -= parent1.getValue();
            Backpack parent2 = chooseParentRoulette(population, sumOfAdaptations);

            pairs.add(new Pair(parent1, parent2));

            population.add(parent1);
            population.add(parent2);
        }
        return pairs;
    }

    private Backpack chooseParentRoulette(List<Backpack> population, int sumOfAdaptations) {
        double parentIndex = Math.random() * sumOfAdaptations;
        for (Backpack bp : population) {
            parentIndex -= bp.getValue();
            if (parentIndex <= 0) {
                population.remove(bp);
                return bp;
            }
        }
        return population.get(populationSize - 1);
    }

    public List<Pair> selectPairsTournament(List<Backpack> population, Integer numOfChildren) {
        List<Pair> pairs = new ArrayList<>();
        if (numOfChildren % 2 == 1) {
            numOfChildren++;
        }
        for (int i = 0; i < numOfChildren / 2; i++) {
            List<List<Backpack>> groups = new ArrayList<>();
            int numOfGroups = populationSize / 10;
            if (numOfGroups % 2 == 1) {
                numOfGroups++;
            }
            //init groups
            for (int j = 0; j < numOfGroups; j++) {
                groups.add(new ArrayList<>());
            }

            //push population elements to random groups
            for (int j = 0; j < numOfGroups; j++) {
                groups.get(j).add(population.get(j));
            }
            for (int j = numOfGroups; j < populationSize; j++) {
                groups.get((int) Math.round(Math.random() * (numOfGroups - 1))).add(population.get(j));
            }

            //sort groups
            for (List<Backpack> group : groups) {
                group.sort(new BackpackComparator());
            }

            //choose parents
            int parent1GroupIndex = (int) Math.round(Math.random() * (numOfGroups - 1));
            Backpack parent1 = groups.get(parent1GroupIndex).get(0);

            int parent2GroupIndex;
            do {
                parent2GroupIndex = (int) Math.round(Math.random() * (numOfGroups - 1));
            } while (parent1GroupIndex == parent2GroupIndex);
            Backpack parent2 = groups.get(parent2GroupIndex).get(0);

            pairs.add(new Pair(parent1, parent2));
        }
        return pairs;
    }
}
