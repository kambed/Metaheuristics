package pl.meta.backend;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericAlgorithm {
    private final List<Item> items;
    private final int backpackMaxWeight;
    private final int populationSize;

    private final SelectionMethod selectionMethod;
    private final CrossMethod crossMethod;

    public GenericAlgorithm(List<Item> items, int backpackMaxWeight, int populationSize, SelectionMethod selectionMethod,
                            CrossMethod crossMethod) {
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
        List<Backpack> nextPopulation = new ArrayList<>();

        Function<List<Backpack>, Pair> selectPairFunction = this::selectPairRoulette;
        if (selectionMethod == SelectionMethod.TOURNAMENT) {
            selectPairFunction = this::selectPairTournament;
        }

        for (int i = 0; i < populationSize / 2; i++) {
            Pair pair = selectPairFunction.apply(population);
            nextPopulation.addAll(pair.cross(crossMethod));
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
