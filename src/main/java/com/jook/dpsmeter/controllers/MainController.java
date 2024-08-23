package com.jook.dpsmeter.controllers;

import com.jook.dpsmeter.controls.UserHBox;
import com.jook.dpsmeter.logics.*;
import com.jook.dpsmeter.utils.Params;
import com.jook.dpsmeter.utils.Stages;
import com.jook.dpsmeter.utils.Windows;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;


public class MainController {
    @FXML
    public Button chartsButton;
    @FXML
    public Button cleanButton;
    @FXML
    private ComboBox<String> classesComboBox;
    @FXML
    private Label infoLabel;
    @FXML
    private VBox vBox;
    @FXML
    private ScrollPane scroll;
    @FXML
    private Hyperlink hyperLink;
    @FXML
    private Button calculateButton;

    public Button minimizedButton;
    @FXML
    private Label copyLabel;
    @FXML
    public Button settingsButton;
    @FXML
    private ProgressIndicator pgIn;
    @FXML
    public AnchorPane node;
    @FXML
    public SplitMenuButton copyButton;
    @FXML
    private ComboBox<String> mobsComboBox;
    @FXML
    private TextField pathTextField;
    private final FileChooser fileChooser = new FileChooser();
    private Task<Void> dmgTask;
    private Task<Void> mobsTask;
    private static DpsCalculator cl;
    private static Result result;
    private Thread calcThread;
    private final NumberFormat format = NumberFormat.getInstance(Locale.US);
    private ObservableList<String> mobsList;
    private final Utilities utilities = new Utilities();

    @FXML
    public void initialize() {


        mobsList = FXCollections.observableArrayList();
        mobsList.add("Все");
        mobsComboBox.setItems(mobsList);
        mobsComboBox.getSelectionModel().select(0);
        //vBox.prefWidthProperty().bind(scroll.widthProperty().subtract(10));
        vBox.prefWidthProperty().bind(scroll.widthProperty());


        //  vBox.setPadding(new Insets(10,10,10,10));
        //  mobsComboBox.getItems().addAll("Все");
        //  mobsComboBox.getSelectionModel().select("Все");
        copyLabel.prefWidthProperty().bind(copyButton.widthProperty().subtract(22));

        copyButton.setTooltip(new Tooltip("Копировать"));
        settingsButton.setTooltip(new Tooltip("Настройки"));
        copyLabel.setTooltip(new Tooltip("Скопировать в одну строку"));
        //  scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        mobsComboBox.valueProperty().addListener((ov, t, t1) -> {
            if (t1 != null && t != null) {
                vBox.getChildren().clear();
                if (!t1.equals("Все")) {

                    stopDmgCalc();
                    mobsTask(t1.substring(0, mobsComboBox.getValue().indexOf("(") - 1));
                    new Thread(mobsTask).start();
                }
                if (t1.equals("Все")) {
                    clearMobs();
                    //       str = -1;
                    damageTask();
                    calcThread = new Thread(dmgTask);
                    calcThread.start();
                }

            }
        });
        classesComboBox.setItems(FXCollections.observableArrayList("Все", "Гладиатор", "Страж", "Убийца", "Стрелок", "Волшебник", "Заклинатель", "Чародей", "Целитель", "Бард", "Снайпер", "Пилот", "Призыв", "Неизвестно"));
        classesComboBox.getSelectionModel().select(0);

        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Log файлы (*.log*)", "*.log"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All Files (*.*)", "*"));
        fileChooser.setTitle("Выбрать файл");
    }


    public void calcButtonListener() throws IOException {
        startCalculation();
    }

    public void startCalculation() throws IOException {
        if (dmgTask==null || !dmgTask.isRunning()) {
            if(mobsTask == null || !mobsTask.isRunning()) {
                String path = pathTextField.getText();
                if (pathTextField.getText().contains("\\") && !pathTextField.getText().equals("\\")) {
                    Path dir = Paths.get(path);
                    if (Files.exists(dir)) {
                        Params.savePath(path);
                        vBox.getChildren().clear();
                        damageTask();
                        calcThread = new Thread(dmgTask);
                        calcThread.start();

                    } else {
                        vBox.getChildren().clear();
                        infoLabel.setText("Файл \"" + dir.getFileName() + "\" отсутствует\n(Проверьте включено ли логирование)");
                        infoLabel.setVisible(true);
                    }

                } else {
                    infoLabel.setText("Файл не выбран");
                    infoLabel.setVisible(true);
                }
            }
        }

    }
    private void damageTask() {
        dmgTask = new Task<>() {
            @Override
            public Void call() {
                utilities.newResult();
                hyperLink.setVisible(false);
                pgIn.setVisible(true);
                infoLabel.setVisible(false);
                if (Params.newMethod.getValue())
                    cl = new DpsCalculatorNew(Params.path, "Все", utilities);
                else
                    cl = new DpsCalculatorOld(Params.path, "Все", utilities);
                cl.clean();
                result = cl.calc();
                pgIn.setVisible(false);
               updateGui(false);
                return null;
            }
        };

    }


    private void mobsTask(String checkedNpc) {

        mobsTask = new Task<>() {
            @Override
            public Void call() {
                utilities.newResult();
                copyButton.setStyle(null);
                pgIn.setVisible(true);
                infoLabel.setVisible(false);
                if (Params.newMethod.getValue())
                    cl = new DpsCalculatorNew(Params.path, checkedNpc, utilities);
                else
                    cl = new DpsCalculatorOld(Params.path, checkedNpc, utilities);
                result = cl.calc();
                pgIn.setVisible(false);
                updateGui(true);
                return null;
            }
        };
    }

    private void clearMobs() {
        mobsComboBox.getItems().clear();
        mobsList.clear();
        mobsList.add("Все");
        mobsComboBox.setItems(mobsList);
        mobsComboBox.getSelectionModel().select(0);
        classesComboBox.getSelectionModel().select(0);
    }

    private void setResult() {

        Stages.getSkillsController().setUsers();
        Stages.getSkillsController().setUserMap(result.userMap);
        Stages.getChartsController().setPieChart(result.classes);
        SkillListController.users.clear();
        SkillListController.users.addAll(result.userMap.keySet());
        Stages.getSkillsController().setUsers();
        setResultLabels(classesComboBox.getValue());//результат

        if (!result.userMap.isEmpty()) {
            if (result.userMap.containsKey("Вы"))
                Stages.getSkillsController().setSkillList("Вы");
            else
                Stages.getSkillsController().setSkillList(SkillListController.users.get(0));
        }
        //  Stages.getSkillsController().setSkills(result.skills);
    }


    private void generateMobsList() {
        Map<String, Npc> npcMap = result.npcMap;
        mobsList.clear();
        mobsList.add("Все");
        for (Map.Entry<String, Npc> entry : npcMap.entrySet()) {
            if (entry.getValue().receivedDmg != 0) {
                mobsList.add(entry.getKey() + " (" + format.format(entry.getValue().receivedDmg) + ")");
            }
        }
        mobsComboBox.setItems(mobsList);
        mobsComboBox.getSelectionModel().select(0);
    }

    private String generateStringResult() {

        StringBuilder stringText = new StringBuilder();

        Map<String, User> userMap = result.userMap;


        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            String user = entry.getKey();
            String dmg = format.format(entry.getValue().dmgSum);
            StringBuilder preString = new StringBuilder();
            preString.append(user).append(": ").append(dmg).append(" • ");

            if (!Params.hideUnknown.getValue()) {

                if (stringText.length() + preString.length() < 255) {
                    stringText.append(preString);
                }
            }
            if (Params.hideUnknown.getValue())
                if (!entry.getValue().characterClass.equals("Неизвестно")) {
                    if (stringText.length() + preString.length() < 255) {
                        stringText.append(preString);
                    }
                }
        }
        return stringText.toString();
    }


    private String generateColumnResult() {

        StringBuilder columnText = new StringBuilder();

        Map<String, User> userMap = result.userMap;
        Map<String, Integer> extraDamageMap = result.extraDamageMap;


        for (Map.Entry<String, User> entry : userMap.entrySet()) {
            String user = entry.getKey();
            String characterClass = entry.getValue().characterClass;
            String dmg = format.format(entry.getValue().dmgSum);
            String dots = format.format(entry.getValue().dotSum);
            //  StringBuilder preString = new StringBuilder();
            //    preString.append(user).append(": ").append(dmg).append(" • ");
            double crit = (double) Math.round((entry.getValue().countCrit / entry.getValue().countAttack) * 100);

            if (!Params.hideUnknown.getValue()) {
                columnText.append(user).append(" (").append(characterClass).append(")").append(": ").append(dmg).append(" (").append(dots).append(") - ").append((int) crit).append("%").append("\n");

            }
            if (Params.hideUnknown.getValue())
                if (!entry.getValue().characterClass.equals("Неизвестно")) {
                    columnText.append(user).append(" (").append(characterClass).append(")").append(": ").append(dmg).append(" (").append(dots).append(") - ").append((int) crit).append("%").append("\n");
                }
        }
        if (result.haveExtra) {
            columnText.append("\nПрочее:\n----------------------------\n");
            for (Map.Entry<String, Integer> entry : extraDamageMap.entrySet()) {
                if (entry.getValue() != 0) {
                    columnText.append(entry.getKey()).append(": ").append(format.format(entry.getValue())).append("\n");
                }
            }
        }

        return columnText.toString();
    }


    /*static void setDetailedSkillList(String user) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        result.skills.clear();


        if (result != null && !result.userMap.isEmpty() && result.userMap.containsKey(user)) {
            for (int s = 0; s < result.userMap.get(user).getDetailedskillNameRotation().size(); s++) {
                result.skills.add(result.userMap.get(user).getDetailedskillNameRotation().get(s) + " (" + format.format(result.userMap.get(user).getDetailedSkillDmgRotation().get(s)) + ")");
            }
        }
    }*/

   /* static void setCommonSkillList(String user) {
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        result.skills.clear();
        if (result != null && !result.userMap.isEmpty() && result.userMap.containsKey(user)) {
            result.userMap.get(user).sortCommonSkillRotation();
            for (Map.Entry<String, Skill> entry : result.userMap.get(user).getCommonSkillRotation().entrySet()) {
                if (entry.getValue().dmgSum != 0) {
                    result.skills.add(entry.getKey() + " (" + format.format(entry.getValue().dmgSum) + ")" + " x" + entry.getValue().count);
                }
            }
        }
    }*/
    public void updateGui(Boolean mobTask) {

        Platform.runLater(() -> {
            setResult();
            if(!mobTask) {
                generateMobsList();
                Stages.getOverlayController().setText(generateColumnResult());
            }
        });
    }
    public void clearButtonListener() {
        stopAndClear(true);
    }

    public void stopAndClear(boolean deleteFile) {
        Stages.getSkillsController().clearSkillsAndUsers();
        stopDmgCalc();
        clearMobs();

        cl = new DpsCalculatorOld(Params.path, "Все", utilities);
        //   str = -1;
        File logfile = new File(Params.path);
        if (!Params.path.trim().isEmpty() && Params.path.substring(Params.path.lastIndexOf(".")).equals(".log")) {
            if (deleteFile) {
                if (logfile.delete()) {
                    vBox.getChildren().clear();
                    //      hBoxList.clear();
                    infoLabel.setText("Лог очищен");
                    infoLabel.setVisible(true);

                    // vBox.getChildren().add(new Label("Лог очищен"));
                    Stages.getOverlayController().clearText();
                } else {
                    infoLabel.setText("Файл не найден");
                    infoLabel.setVisible(true);
                    //   vBox.getChildren().add(new Label("Файл не найден"));
                }
            }
        }
        hyperLink.setVisible(true);
        copyButton.setStyle(null);

    }


    public void copy() {
        if (result != null && !result.userMap.isEmpty()) {

            //  StringSelection selection = new StringSelection(generateColumnResult());
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(generateColumnResult());
            clipboard.setContent(content);
        } else {
            infoLabel.setText("Нет данных для копирования");
            infoLabel.setVisible(true);
        }
    }

    public void copyInString() {
        if (result != null && !result.userMap.isEmpty()) {
            // StringSelection selection = new StringSelection(generateStringResult());
            //  Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Clipboard clipboard = Clipboard.getSystemClipboard();
            ClipboardContent content = new ClipboardContent();
            content.putString(generateStringResult());
            clipboard.setContent(content);
        } else {
            infoLabel.setText("Нет данных для копирования");
            infoLabel.setVisible(true);
        }
    }

    public void copyButtonListener() {
        copy();
    }

    public void copyStringLst() {
        copyInString();
    }

    public File chooseFile() {
        Path dir;
        if (Params.path != null && Params.path.contains("\\")) {
            dir = Paths.get(Params.path.substring(0, Params.path.lastIndexOf("\\")));

            if (Files.exists(dir)) {
                fileChooser.setInitialDirectory(new File(String.valueOf(dir)));
            }
        }
        return fileChooser.showOpenDialog(node.getScene().getWindow());
    }


    public void openButtonListener() throws IOException {


        File file = chooseFile();
        if (file != null) {
            if (!file.toString().equals(Params.path)) {
                clearMobs();
                copyButton.setStyle(null);
                pathTextField.setText(file.toString());
                if (calcThread != null)
                    stopAndClear(false);
                Stages.getSkillsController().clearSkillsAndUsers();
                vBox.getChildren().clear();
                //        hBoxList.clear();
                hyperLink.setVisible(true);
            }
            Params.savePath(file.toString());
        }
    }


    public void SetButtonListener() {
        Windows.toggleSettingsWindow();
    }

    public void skillsButtonListener() {
        Windows.toggleSkillsWindow();
    }


    public void hyperLinkListener() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Откроется окно браузера");
        alert.setTitle("Информация");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            try {

                Desktop.getDesktop().browse(URI.create("https://vk.com/club179421572"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void stopDmgCalc() {
        if (calcThread != null)
            calcThread.interrupt();
        calculateButton.setText("Рассчитать");
        Stages.getOverlayController().calcButton.setText("Рассчитать");
        //       str = -1;
    }

    public void setPath(String path) {
        pathTextField.setText(path);
    }

    public void setText(String result) {
        vBox.getChildren().add(new Label(result));
    }

    public void onMinimizedButtonListener() {
        Windows.toggleOverlayWindow();
    }


    private void setResultLabels(String className) {


        for (Map.Entry<String, User> entry : result.userMap.entrySet()) {
            String user = entry.getKey();
            String characterClass = entry.getValue().characterClass;
            if (Params.hideUnknown.getValue() && characterClass.equals("Неизвестно") && !className.equals("Неизвестно"))
                continue;
            if (!className.equals("Все")) {
                if (!characterClass.equals(className)) {
                    continue;
                }
            }
            String dmg = format.format(entry.getValue().dmgSum);
            String dots = format.format(entry.getValue().dotSum);
            double crit = (double) Math.round((entry.getValue().countCrit / entry.getValue().countAttack) * 100);
            UserHBox hBox = new UserHBox(entry.getValue().name);
            hBox.setLabelText(user + " (" + characterClass + ")" + ": " + dmg + " (" + dots + ") - " + (int) crit + "%");
            //userLabel.setText(user + " (" + characterClass + ")" + ": " + dmg + " (" + dots + ") - " + (int) crit + "%");
            vBox.getChildren().add(hBox);
        }

        if (result.haveExtra) {
            HBox hBoxExtra = new HBox();
            Label labelExtra = new Label("\nПрочее:\n----------------------------\n");
            hBoxExtra.getChildren().add(labelExtra);
            vBox.getChildren().add(hBoxExtra);
            for (Map.Entry<String, Integer> entry : result.extraDamageMap.entrySet()) {
                if (entry.getValue() != 0) {
                    HBox hBox = new HBox();
                    Label label = new Label();
                    label.setText(entry.getKey() + ": " + format.format(entry.getValue()));
                    hBox.getChildren().add(label);
                    vBox.getChildren().add(hBox);
                }
            }
        }
    }

    public void chartsButtonListener() {
        Windows.toggleChartsWindow();
    }
} //закрывает класс