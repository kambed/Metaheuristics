package pl.meta.backend;

import java.util.List;
import java.util.Random;

public class Backpack {
    private static final int MIN_POSSIBLE_VALUE = 1;
    private int max_weight;
    private List<Item> possible_items;
    private boolean[] items_occurrence;

    public Backpack(int max_weight, List<Item> possible_items) {
        this.max_weight = max_weight;
        this.possible_items = possible_items;
        this.items_occurrence = new boolean[possible_items.size()];
        for (int i = 0; i < possible_items.size(); i++) {
            this.items_occurrence[i] = Math.round(Math.random()) == 0;
        }
    }

    public int getMaxWeight() {
        return max_weight;
    }

    public int getWeight() {
        int weight = 0, i = 0;
        for (Item item : possible_items) {
            if (items_occurrence[i]) {
                weight += item.getWeight();
            }
            i++;
        }
        return weight;
    }

    public int getValue() {
        if (getWeight() > getMaxWeight()) {
            return MIN_POSSIBLE_VALUE;
        }
        int value = 0, i = 0;
        for (Item item : possible_items) {
            if (items_occurrence[i]) {
                value += item.getValue();
            }
            i++;
        }
        return value;
    }

    public List<Item> getPossible_items() {
        return possible_items;
    }

    public boolean[] getItemsOccurrence() {
        return items_occurrence;
    }

    public void setItemOccurrence(int index, boolean value) {
        items_occurrence[index] = value;
    }
}
