package pl.meta.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class GenericAlgorithm {
    private final List<Item> items;
    private final int backpackMaxWeight;
    private final int populationSize;
    private final SelectionMethod selectionMethod;
    private final CrossMethod crossMethod;

    private final double crossChance;

    private final double mutationChance;

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
        for (int i = 0; i < numOfIterations; i++) {
            population = newPopulation(population);
        }
        return population;
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
            for (int i = 0; i < populationDiff; i++) {
                Backpack fromOldPopulation = population.get((int) Math.round(Math.random() * (populationSize - nextPopulation.size())));
                population.remove(fromOldPopulation);
                nextPopulation.add(fromOldPopulation);
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
        // TODO: SELECTION TOURNAMENT
        return null;
    }
}
