package pl.meta.task3.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.jfree.chart.ChartUtilities;
import pl.meta.task3.backend.AntAlgorithm;
import pl.meta.task3.backend.Distances;
import pl.meta.task3.backend.Place;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Generic Algorithm";

    private final List<Place> items = new ArrayList<>();
    private Distances distances;

    @FXML
    TextField amountOfAnts;
    @FXML
    TextField randomChance;
    @FXML
    TextField feromonWeight;
    @FXML
    TextField heuristicWeight;
    @FXML
    TextField feromonEvaporation;
    @FXML
    TextField numOfIterations;
    @FXML
    TextField itemsDataFilePath;
    @FXML
    ImageView chart;
    @FXML
    ImageView chart2;
    @FXML
    TextArea consoleArea;

    public void saveLogs(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.saveChooser("Save logs to file ", actionEvent);
        if (!stringPath.isBlank()) {
            Files.write(Paths.get(stringPath), consoleArea.getText().getBytes());
            consoleArea.setText("");
            consoleArea.appendText("Logs saved saved to: %s\n".formatted(stringPath));
        }
    }

    public void loadItemsData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open items data file ", actionEvent);
        if (!stringPath.isBlank()) {
            consoleArea.appendText("Items data loaded: %s\n".formatted(stringPath));
            List<String> data = Files.readAllLines(Paths.get(stringPath));
            for (String s : data) {
                while (s.startsWith(" ")) {
                    s = s.substring(1);
                }
                String[] itemParameters = s.split(" ");
                Place place = new Place(Integer.parseInt(itemParameters[0]) - 1, Integer.parseInt(itemParameters[1]),
                        Integer.parseInt(itemParameters[2]));
                items.add(place);
                consoleArea.appendText(place + "\n");
            }
            itemsDataFilePath.setText(stringPath);
        }
    }

    public void start() {
        for (int i = 0; i < 1; i++) {
            new Thread(
                    () -> {
                        AntAlgorithm aa = new AntAlgorithm(items, Integer.parseInt(amountOfAnts.getText()),
                                Integer.parseInt(numOfIterations.getText()), Double.parseDouble(feromonEvaporation.getText()),
                                Double.parseDouble(feromonWeight.getText()), Double.parseDouble(heuristicWeight.getText()),
                                Double.parseDouble(randomChance.getText()));
                        double shortestRoute = aa.start();
                        consoleArea.appendText("Shortest route: " + shortestRoute + "\n");

                        try {
                            ChartUtilities.saveChartAsPNG(
                                    new File("chart.png"),
                                    ChartGenerator.generatePlot(aa.getIterations().toArray(new Integer[0]),
                                            aa.getAvgPopulationValues().toArray(new Double[0]), "Avg population value"),
                                    400, 220);
                            FileInputStream input = new FileInputStream("chart.png");
                            chart.setImage(new Image(input));

                            ChartUtilities.saveChartAsPNG(
                                    new File("chart2.png"),
                                    ChartGenerator.generatePlot(aa.getIterations().toArray(new Integer[0]),
                                            aa.getMinPopulationValues().toArray(new Double[0]), "Min population value"),
                                    400, 220);
                            input = new FileInputStream("chart2.png");
                            chart2.setImage(new Image(input));
                        } catch (IOException e) {
                            consoleArea.appendText("Wystapił problem przy generowaniu wykresu. \n");
                        }
                    }).start();
        }
    }
}