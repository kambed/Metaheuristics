package pl.meta.frontend;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.jfree.chart.ChartUtilities;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class MainFormController {
    public static final String MAIN_FORM_RESOURCE = "MainForm.fxml";
    public static final String MAIN_FORM_TITLE = "Neural Network";

    private double[][] learnInputs;
    private double[][] learnOutputs;
    private double[][] testInputs;

    @FXML
    TextField numOfInputs;
    @FXML
    TextField numOfOutputs;
    @FXML
    TextField numOfHiddenLayers;
    @FXML
    TextField numOfNeuronsInHiddenLayers;
    @FXML
    CheckBox withBias;
    @FXML
    TextField learningRate;
    @FXML
    TextField momentumRate;
    @FXML
    TextField numOfEras;
    @FXML
    TextField learningDataFilePath;
    @FXML
    TextField learningOutputFilePath;
    @FXML
    TextField dataFilePath;
    @FXML
    TextArea consoleArea;
    @FXML
    RadioButton learnRadioButton;
    @FXML
    RadioButton workRadioButton;
    @FXML
    Button calculateButton;
    @FXML
    Button learnButton;
    @FXML
    Button loadTestDataButton;
    @FXML
    Button loadLearningOutputDataButton;
    @FXML
    Button loadLearningDataButton;
    @FXML
    Button saveButton;
    @FXML
    Button loadButton;
    @FXML
    ImageView chart;
    @FXML
    CheckBox generateStats;

    public void startCalculating(ActionEvent actionEvent) {

    }

    public void changeStats(ActionEvent actionEvent) {
        loadLearningOutputDataButton.setDisable(!generateStats.isSelected());
    }

    public void startLearning(ActionEvent actionEvent) throws FileNotFoundException {

    }

    public void loadLearningData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {

    }

    public void loadLearningOutput(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {

    }

    public void loadTestData(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {

    }

    public void loadNeuralNetwork(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {

    }

    public void saveNeuralNetwork(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {

    }

    public void saveLogs(ActionEvent actionEvent) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, IOException {
        String stringPath = FileChoose.saveChooser("Save logs to file ", actionEvent);
        if (!stringPath.isBlank()) {
            Files.write(Paths.get(stringPath), consoleArea.getText().getBytes());
            consoleArea.setText("");
            consoleArea.appendText("Logs saved saved to: " + stringPath + "\n");
        }
    }

    public void changeMode(ActionEvent actionEvent) {
        changeDisableMode(learnRadioButton.isSelected());
        if (learnRadioButton.isSelected()) {
            consoleArea.appendText("Learning mode selected \n");
        } else {
            consoleArea.appendText("Working mode selected \n");
        }
    }

    public void changeDisableMode(boolean learningMode) {
        calculateButton.setDisable(learningMode);
        loadTestDataButton.setDisable(learningMode);
        loadButton.setDisable(learningMode);
        generateStats.setDisable(learningMode);

        learnButton.setDisable(!learningMode);
        numOfOutputs.setDisable(!learningMode);
        numOfInputs.setDisable(!learningMode);
        numOfHiddenLayers.setDisable(!learningMode);
        numOfNeuronsInHiddenLayers.setDisable(!learningMode);
        withBias.setDisable(!learningMode);
        learningRate.setDisable(!learningMode);
        momentumRate.setDisable(!learningMode);
        numOfEras.setDisable(!learningMode);
        loadLearningOutputDataButton.setDisable(!learningMode);
        loadLearningDataButton.setDisable(!learningMode);
    }
}