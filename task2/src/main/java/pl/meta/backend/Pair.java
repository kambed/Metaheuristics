package pl.meta.backend;

import java.util.ArrayList;
import java.util.List;

public class Pair {
    public Backpack backpack1;
    public Backpack backpack2;

    int numOfItems;

    public Pair(Backpack backpack1, Backpack backpack2) {
        this.backpack1 = backpack1;
        this.backpack2 = backpack2;
        numOfItems = backpack1.getPossibleItems().size();
    }

    public List<Backpack> initChildren() {
        List<Backpack> children = new ArrayList<>();
        children.add(new Backpack(backpack1.getMaxWeight(), backpack1.getPossibleItems()));
        children.add(new Backpack(backpack2.getMaxWeight(), backpack2.getPossibleItems()));
        return children;
    }

    public List<Backpack> crossSinglePoint() {
        List<Backpack> children = initChildren();
        int crossPoint = (int) Math.round(Math.random() * (numOfItems - 2) + 1);
        cross(children, crossPoint, numOfItems);
        return children;
    }

    public List<Backpack> crossDoublePoint() {
        List<Backpack> children = initChildren();
        int crossPoint1 = (int) Math.round(Math.random() * (numOfItems - 2) + 1);
        int crossPoint2;
        do {
            crossPoint2 = (int) Math.round(Math.random() * (numOfItems - 2) + 1);
        } while (crossPoint1 == crossPoint2);
        if (crossPoint1 > crossPoint2) {
            int temp = crossPoint1;
            crossPoint1 = crossPoint2;
            crossPoint2 = temp;
        }
        cross(children, crossPoint1, crossPoint2);
        for (int i = crossPoint2; i < numOfItems; i++) {
            children.get(0).setItemOccurrence(i, backpack1.getItemsOccurrence()[i]);
            children.get(1).setItemOccurrence(i, backpack2.getItemsOccurrence()[i]);
        }
        return children;
    }

    private void cross(List<Backpack> children, int crossPoint1, int crossPoint2) {
        for (int i = 0; i < crossPoint1; i++) {
            children.get(0).setItemOccurrence(i, backpack1.getItemsOccurrence()[i]);
            children.get(1).setItemOccurrence(i, backpack2.getItemsOccurrence()[i]);
        }
        for (int i = crossPoint1; i < crossPoint2; i++) {
            children.get(0).setItemOccurrence(i, backpack2.getItemsOccurrence()[i]);
            children.get(1).setItemOccurrence(i, backpack1.getItemsOccurrence()[i]);
        }
    }
}
