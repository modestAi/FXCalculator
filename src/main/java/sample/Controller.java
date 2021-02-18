package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public class Controller implements Initializable{

    @FXML
    private BorderPane borderPane;
    @FXML
    private Label label;
    @FXML
    private Button close;
    @FXML
    private Button minimize;
    @FXML
    public Button equal;


    List<String> strings = new ArrayList<>();
    String toShow;
    String text;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setFocusTraversable(true);
        borderPane.setOnKeyReleased(this::onKeyPressed);
        borderPane.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.ENTER) {
                onEquals();
                System.out.println("Equals");
            }
            borderPane.requestFocus();
        });

        Font font = getFont();

        Tooltip tooltip = new Tooltip();
        Tooltip tooltip1 = new Tooltip();

        tooltip1.setFont(font);
        tooltip.setFont(font);

        Image image;
        try {
            String path = "src/main/resources/";
            image = new Image(new FileInputStream(path + "Close.png"));
            BackgroundImage bImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(close.getWidth(), close.getHeight(), true, true, true, false));
            Background view = new Background(bImage);
            close.setBackground(view);
            tooltip1.setText("Closes the calculator");

            close.setTooltip(tooltip1);
            image = new Image(new FileInputStream(path + "Minimize.png"));
            BackgroundImage b2Image = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(minimize.getWidth(), minimize.getHeight(), true, true, true, false));
            Background view2 = new Background(b2Image);
            minimize.setBackground(view2);
            tooltip.setText("Minimizes the calculator");
            minimize.setTooltip(tooltip);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void clicked(ActionEvent e) {

        text = ((Button) e.getSource()).getText();
        strings.add(text);
        if (strings.size() > 1) {
            settingText();
        } else {
            label.setText(text);
        }
    }

    void onKeyPressed(KeyEvent e) {

        if (e.getCode() == KeyCode.ESCAPE || e.getCode() == KeyCode.DELETE) {
            clearAll();
        }
        if (e.getCode() == KeyCode.LEFT_PARENTHESIS || e.getCode() == KeyCode.RIGHT_PARENTHESIS) {
            strings.add(e.getText());
            settingText();
        }
        if (e.getCode() == KeyCode.EQUALS) {
            onEquals();
            System.out.println("Equals");
        }

        if (e.getCode() == KeyCode.BACK_SPACE) {
            clearLast();
        }
        if (e.getCode().isDigitKey()) {
            strings.add(e.getText());
            settingText();
        }
        var keyCodes = new ArrayList<KeyCode>();
        keyCodes.add(KeyCode.ADD);
        keyCodes.add(KeyCode.SUBTRACT);
        keyCodes.add(KeyCode.DIVIDE);
        keyCodes.add(KeyCode.MULTIPLY);
        for (var c : keyCodes) {
            if (e.getCode() == c) {
                strings.add(e.getText());
                settingText();
            }
        }
    }

    Predicate<Double> isInteger = e -> {
        String foo = String.valueOf(e);
        return Pattern.matches("^[1-9]+[0-9]*\\.?0*$", foo);
    };

    @FXML
    public void clearAll() {
        strings.clear();
        label.setText("0");
        System.out.println("clear all called");
    }

    @FXML
    public void clearLast() {

        int index = strings.size() - 1;
        if (index > 0) {
            strings.remove(index);
            settingText();
        }
        if (index == 0) {
            strings.clear();
            label.setText("0");
        }
        System.out.println("Clear Last");
    }

    @FXML
    public void onClose(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();

    }

    @FXML
    public void onMinimize(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setIconified(true);
    }

    private Font getFont() {
        String fontFamily = "Arial";
        double fontSize = 15;
        FontWeight fontWeight = FontWeight.BOLD;
        FontPosture fontPosture = FontPosture.ITALIC;
        return Font.font(fontFamily, fontWeight, fontPosture, fontSize);
    }

    private void settingText() {  //set text when continuously being clicked
        toShow = strings.toString().replace("[", "").replace("]", "").replace(", ", "");
        label.setText(toShow);
    }

    @FXML
    public void onEquals() {
        try {
            String test = String.join("", strings);
            double c = EvalString.eval(test);
            strings.clear();
            String nums = String.valueOf(c);
            String result = nums.substring(0, nums.indexOf("."));
            if (isInteger.test(c)) { //zero trailing
                label.setText(result);
                strings.add(result);
            } else {
                strings.add(nums);
                label.setText(result);
            }
        } catch (IllegalArgumentException e) {
            label.setText("Syntax Error");
            e.printStackTrace();
        }
    }
}