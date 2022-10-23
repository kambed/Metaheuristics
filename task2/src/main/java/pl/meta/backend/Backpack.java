package pl.meta.backend;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Backpack {
    private static final int MIN_POSSIBLE_VALUE = 1;
    private int maxWeight;
    private List<Item> possibleItems;
    private boolean[] itemsOccurrence;

    public Backpack(int maxWeight, List<Item> possibleItems) {
        this.maxWeight = maxWeight;
        this.possibleItems = possibleItems;
        this.itemsOccurrence = new boolean[possibleItems.size()];
        for (int i = 0; i < possibleItems.size(); i++) {
            this.itemsOccurrence[i] = Math.round(Math.random()) == 0;
        }
    }

    public int getMaxWeight() {
        return maxWeight;
    }

    public int getWeight() {
        int weight = 0, i = 0;
        for (Item item : possibleItems) {
            if (itemsOccurrence[i]) {
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
        for (Item item : possibleItems) {
            if (itemsOccurrence[i]) {
                value += item.getValue();
            }
            i++;
        }
        return value;
    }

    public List<Item> getPossibleItems() {
        return possibleItems;
    }

    public boolean[] getItemsOccurrence() {
        return itemsOccurrence;
    }

    public void setItemOccurrence(int index, boolean value) {
        itemsOccurrence[index] = value;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Backpack{");
        sb.append(", itemsOccurrence=").append(Arrays.toString(itemsOccurrence));
        sb.append(", weight=").append(getWeight());
        sb.append(", value=").append(getValue());
        sb.append('}');
        return sb.toString();
    }
}
