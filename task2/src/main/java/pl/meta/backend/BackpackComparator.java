package pl.meta.backend;

import java.util.Comparator;

public class BackpackComparator implements Comparator<Backpack> {
    @Override
    public int compare(Backpack o1, Backpack o2) {
        return Integer.compare(o2.getValue(), o1.getValue());
    }
}
