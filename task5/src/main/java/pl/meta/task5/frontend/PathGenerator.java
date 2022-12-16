package pl.meta.task5.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.util.Collections;

public class PathGenerator extends JFrame {

    public static void generateRoad(java.util.List<Double> x, java.util.List<Double> y, java.util.List<String> labels) {
        JFrame f = new JFrame("Ant Road");
        f.setSize((int) Math.round(Collections.max(x)) + 100, (int) Math.round(Collections.max(y)) + 100);

        f.add(new JComponent() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D graphics = (Graphics2D) g.create();
                Path2D polyline = new Path2D.Double();
                polyline.moveTo(x.get(0), y.get(0));
                JLabel label = new JLabel(labels.get(0) + " [" + (int) Math.round(x.get(0)) + "," + (int) Math.round(y.get(0)) + "]");
                Dimension size = label.getPreferredSize();
                label.setBounds((int) Math.round(x.get(0)), (int) Math.round(y.get(0)), size.width, size.height);
                //f.add(label);
                for (int i = 1; i < x.size(); i++) {
                    polyline.lineTo(x.get(i), y.get(i));
                    label = new JLabel(labels.get(i) + " [" + (int) Math.round(x.get(i)) + "," + (int) Math.round(y.get(i)) + "]");
                    size = label.getPreferredSize();
                    label.setBounds((int) Math.round(x.get(i)), (int) Math.round(y.get(i)), size.width, size.height);
                    //f.add(label);
                }
                graphics.draw(polyline);
                graphics.dispose();
            }
        });
        f.setVisible(true);
    }
}
