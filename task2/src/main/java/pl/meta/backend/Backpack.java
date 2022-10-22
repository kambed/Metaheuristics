package pl.meta.backend;

import java.util.List;

public class Backpack {
    private static final int MIN_POSSIBLE_VALUE = 1;
    private int max_weight;
    private List<Item> possible_items;
    private boolean[] items_occurrence;

    public Backpack(int max_weight, List<Item> possible_items) {
        this.max_weight = max_weight;
        this.possible_items = possible_items;
        this.items_occurrence = new boolean[possible_items.size()];
    }

    public int getMax_weight() {
        return max_weight;
    }

    public int get_weight() {
        int weight = 0, i = 0;
        for (Item item : possible_items) {
            if (items_occurrence[i]) {
                weight += item.getWeight();
            }
            i++;
        }
        return weight;
    }

    public int get_value() {
        if (get_weight() > getMax_weight()) {
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
}
