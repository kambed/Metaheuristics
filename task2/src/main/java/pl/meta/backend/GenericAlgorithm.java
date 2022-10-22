package pl.meta.backend;

import java.util.ArrayList;
import java.util.List;

public class GenericAlgorithm {
    private final List<Item> items;
    private final int backpackMaxWeight;
    private final int populationSize;

    private final int selectionMethod;

    private final int crossMethod;

    public GenericAlgorithm(List<Item> items, int backpackMaxWeight, int populationSize, int selectionMethod,
                            int crossMethod) {
        this.items = items;
        this.backpackMaxWeight = backpackMaxWeight;
        this.populationSize = populationSize;
        this.selectionMethod = selectionMethod;
        this.crossMethod = crossMethod;
    }

    public List<Backpack> initPopulation() {
        List<Backpack> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++) {
            population.add(new Backpack(backpackMaxWeight, items));
        }
        return population;
    }

    public List<Backpack> newPopulation(List<Backpack> population) {
        List<Pair> pairsToCross = new ArrayList<>();
        if (selectionMethod == 0) {
            for (int i = 0; i < populationSize / 2; i++) {
                pairsToCross.add(selectPairRoulette(population));
            }
        } else if (selectionMethod == 1) {
            for (int i = 0; i < populationSize / 2; i++) {
                pairsToCross.add(selectPairTournament(population));
            }
        }
        List<Backpack> nextPopulation = new ArrayList<>();
        if (crossMethod == 0) {
            for (Pair pair : pairsToCross) {
                nextPopulation.addAll(pair.crossSinglePoint());
            }
        } else if (crossMethod == 1) {
            for (Pair pair : pairsToCross) {
                nextPopulation.addAll(pair.crossDoublePoint());
            }
        }
        return nextPopulation;
    }

    public Pair selectPairRoulette(List<Backpack> population) {
        // TODO: SELECTION ROULETTE
        return null;
    }

    public Pair selectPairTournament(List<Backpack> population) {
        // TODO: SELECTION TOURNAMENT
        return null;
    }
}
