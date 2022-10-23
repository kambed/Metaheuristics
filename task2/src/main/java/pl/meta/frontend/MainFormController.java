package pl.meta.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import pl.meta.backend.Item;

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
    TextField backpack_weight;
    @FXML
    TextField crossMutationRate;
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
            consoleArea.appendText("Logs saved saved to: " + stringPath + "\n");
        }
    }

    public void loadItemsData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.openChooser("Open items data file ", actionEvent);
        if (!stringPath.isBlank()) {
            consoleArea.appendText("Items data loaded: " + stringPath + "\n");
            List<String> data = Files.readAllLines(Paths.get(stringPath));
            for (String s : data) {
                String[] itemParameters = s.split(";");
                Item item = new Item(itemParameters[1], Integer.parseInt(itemParameters[2]), Integer.parseInt(itemParameters[3]));
                items.add(item);
                consoleArea.appendText(item.toString() + "\n");
            }
        }
    }

    public void start(ActionEvent actionEvent) {
    }
}