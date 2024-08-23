package com.jook.dpsmeter.controllers;

import com.jook.dpsmeter.MainFx;
import com.jook.dpsmeter.utils.Params;
import com.jook.dpsmeter.utils.Stages;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.controlsfx.control.ToggleSwitch;

import java.io.IOException;
import java.net.URI;
import java.util.Objects;


public class SettingsController {
    @FXML
    private ToggleSwitch methodSwitch;
    @FXML
    private ToggleSwitch themeToggle;
    @FXML
    private ToggleSwitch mobsToggle;
    @FXML
    private ToggleButton toggleBtn;
    @FXML
    private Slider transparencySlider;
    @FXML
    private Slider widthSlider;
    @FXML
    private Slider heightSlider;
    // @FXML
    // private CheckBox mobsCheckBox;
    @FXML
    private TextField nameTextField;
    private boolean wasShowed = false;


    public void initialize() {



        transparencySlider.setMin(0.01);
        transparencySlider.setMax(1);
        widthSlider.setMin(269);
        widthSlider.setMax(400);
        heightSlider.setMin(250);
        heightSlider.setMax(550);





        if (Params.darkTheme.getValue()) {
            toggleBtn.setSelected(true);
            toggleBtn.setText("Светлая тема");
        } else {
            toggleBtn.setSelected(false);
            toggleBtn.setText("Темная тема");
        }
      //  mobsCheckBox.setSelected(Params.hideUnknown);
        themeToggle.setSelected(Params.darkTheme.getValue());
        Params.darkTheme.bind(themeToggle.selectedProperty());
        mobsToggle.setSelected(Params.hideUnknown.getValue());
        Params.hideUnknown.bind(mobsToggle.selectedProperty());
        methodSwitch.setSelected(Params.newMethod.getValue());
        Params.newMethod.bind(methodSwitch.selectedProperty());
        transparencySlider.setValue(Params.transparency);
        widthSlider.setValue(Params.overlayWidth);
        heightSlider.setValue(Params.overlayHeight);

        transparencySlider.valueProperty().addListener((changed, oldValue, newValue) -> Stages.getOverlayController().rect.setFill(new Color(0, 0, 0, transparencySlider.getValue())));

        nameTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            Params.name = newValue;
            if (nameTextField.getText() == null || nameTextField.getText().trim().isEmpty()) {
                Params.name = "Вы";
            }
        });


        widthSlider.valueProperty().addListener((changed, oldValue, newValue) -> {
            Stages.getOverlayStage().setWidth(widthSlider.getValue());
            Stages.getOverlayController().rect.setWidth(widthSlider.getValue());
        });

        heightSlider.valueProperty().addListener((changed, oldValue, newValue) -> {
            Stages.getOverlayStage().setHeight(heightSlider.getValue());
            Stages.getOverlayController().rect.setHeight(heightSlider.getValue());
        });

        themeToggle.selectedProperty().addListener((ob, oldValue, newValue) -> {
            if(newValue) {
                applyDarkTheme();
            }
            else {
                applyWhiteTheme();
            }
        });

    }


    public void resetListener() {
        Stages.setSize(370, 539);
    }

    public void toggleBtnLst() {

        if (toggleBtn.isSelected()) {
            applyDarkTheme();
        } else {
            applyWhiteTheme();
        }
    }

    public void hyperLinkListener() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Откроется окно браузера");
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {
                java.awt.Desktop.getDesktop().browse(URI.create("https://vk.com/club179421572"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void enteredListener() {
        Stage stage = Stages.getOverlayStage();

        wasShowed = stage.isShowing();
        if (!wasShowed)
            Stages.getOverlayStage().show();
    }

    public void releasedListener() {
        Stage stage = Stages.getOverlayStage();
        if (!wasShowed) {
            stage.hide();
        }
        Params.transparency = transparencySlider.getValue();
        Params.overlayWidth = widthSlider.getValue();
        Params.overlayHeight = heightSlider.getValue();
    }

    public void checkBoxListener() {
    //    Params.hideUnknown = mobsCheckBox.isSelected();
        //   Params.hideUnknown = mobsToggle.isSelected();
    }

    public void closeButtonListener() {
        //  ToggleWindows.settingsVisible = false;
        Stages.getSettingsStage().close();
    }

    private void applyDarkTheme() {



        Stages.getMainStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/Main.css")).toExternalForm());
        Stages.getMainStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/MainDark.css")).toExternalForm());
        Stages.getMainController().settingsButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/set_white.png")).toExternalForm()));
        Stages.getMainController().copyButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/copy_white.png")).toExternalForm()));
        Stages.getMainController().minimizedButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/minimized_white.png")).toExternalForm()));
        Stages.getMainController().chartsButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/diag_white.png")).toExternalForm()));
        Stages.getMainController().cleanButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/garbare_white.png")).toExternalForm()));
        Stages.getSettingsStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/Settings.css")).toExternalForm());
        Stages.getSettingsStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SettingsDark.css")).toExternalForm());
        Stages.getSkillsStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/SkillList.css")).toExternalForm());
        Stages.getSkillsStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SkillListDark.css")).toExternalForm());
        toggleBtn.setText("Светлая тема");
      //  Params.darkTheme = true;
    }

    private void applyWhiteTheme() {
        Stages.getMainStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/MainDark.css")).toExternalForm());
        Stages.getMainStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/Main.css")).toExternalForm());
        Stages.getMainController().settingsButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/set.png")).toExternalForm()));
        Stages.getMainController().copyButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/copy.png")).toExternalForm()));
        Stages.getMainController().minimizedButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/minimized.png")).toExternalForm()));
        Stages.getMainController().chartsButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/diag.png")).toExternalForm()));
        Stages.getMainController().cleanButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/garbare.png")).toExternalForm()));
        Stages.getSettingsStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/SettingsDark.css")).toExternalForm());
        Stages.getSettingsStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/Settings.css")).toExternalForm());
        Stages.getSkillsStage().getScene().getStylesheets().removeAll(Objects.requireNonNull(MainFx.class.getResource("css/SkillListDark.css")).toExternalForm());
        Stages.getSkillsStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SkillList.css")).toExternalForm());
        toggleBtn.setText("Темная тема");
       // Params.darkTheme = false;
    }

    public void setNameTextField(String name) {
        nameTextField.setText(name);
    }

}



