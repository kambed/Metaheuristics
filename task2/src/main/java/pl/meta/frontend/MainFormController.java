package pl.meta.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import pl.meta.backend.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Generic Algorithm";

    private final List<Item> items = new ArrayList<>();

    @FXML
    TextField backpackWeight;
    @FXML
    TextField crossChance;
    @FXML
    TextField mutationChance;
    @FXML
    TextField populationSize;
    @FXML
    TextField numOfIterations;
    @FXML
    TextField itemsDataFilePath;
    @FXML
    RadioButton rouletteRadioButton;
    @FXML
    RadioButton tournamentRadioButton;
    @FXML
    RadioButton singlePointRadioButton;
    @FXML
    RadioButton doublePointRadioButton;
    @FXML
    ImageView chart;
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
            consoleArea.appendText("Items data loaded: %s\n" .formatted(stringPath));
            List<String> data = Files.readAllLines(Paths.get(stringPath));
            for (String s : data) {
                String[] itemParameters = s.split(";");
                Item item = new Item(itemParameters[1], Integer.parseInt(itemParameters[2]), Integer.parseInt(itemParameters[3]));
                items.add(item);
                consoleArea.appendText(item + "\n");
            }
            itemsDataFilePath.setText(stringPath);
        }
    }

    public void start() {
        GenericAlgorithm ga = getGenericAlgorithm();

        List<Backpack> finalPopulation = ga.start(Integer.parseInt(numOfIterations.getText()));
        consoleArea.appendText(ConsoleInterface.SEPARATOR);

        int i = 1;
        finalPopulation.sort(new BackpackComparator());
        for (Backpack bp : finalPopulation) {
            consoleArea.appendText("%s. weight=%s. value=%s\n".formatted(i, bp.getWeight(), bp.getValue()));
            i++;
        }

        consoleArea.appendText(ConsoleInterface.SEPARATOR);
        consoleArea.appendText("Best backpack in final population: %s \n".formatted(finalPopulation.get(0)));
        consoleArea.appendText(ConsoleInterface.SEPARATOR);
    }

    private GenericAlgorithm getGenericAlgorithm() {
        return new GenericAlgorithm(
                items,
                Integer.parseInt(backpackWeight.getText()),
                Integer.parseInt(populationSize.getText()),
                getSelectionMethod(),
                getCrossMethod(),
                Double.parseDouble(crossChance.getText()),
                Double.parseDouble(mutationChance.getText())
        );
    }

    private CrossMethod getCrossMethod() {
        CrossMethod cm = CrossMethod.DOUBLE_POINT;
        if (singlePointRadioButton.isSelected()) {
            cm = CrossMethod.SINGLE_POINT;
        }
        return cm;
    }

    private SelectionMethod getSelectionMethod() {
        SelectionMethod sm = SelectionMethod.TOURNAMENT;
        if (rouletteRadioButton.isSelected()) {
            sm = SelectionMethod.ROULETTE;
        }
        return sm;
    }
}