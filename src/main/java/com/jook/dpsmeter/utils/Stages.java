package com.jook.dpsmeter.utils;


import com.jook.dpsmeter.MainFx;
import com.jook.dpsmeter.controllers.*;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Objects;


public class Stages {


    private static Stage mainStage;
    private static Stage settStage;
    private static Stage skillsStage;
    private static Stage overlayStage;
    private static Stage chartsStage;

    private static FXMLLoader overlayLoader;
    private static FXMLLoader mainLoader;
    private static FXMLLoader skillsLoader;
    private static FXMLLoader settingsLoader;
    private static FXMLLoader chartsLoader;




    private final Image icon = new Image(MainFx.class.getResourceAsStream("icons/icon.png"));

    public void initRoot(Stage mainStage) throws IOException {


        mainLoader = new FXMLLoader();
        mainLoader.setLocation(MainFx.class.getResource("fxml/main.fxml"));
        Stages.mainStage = mainStage;
        mainStage.getIcons().add(icon);
        mainStage.setTitle("DpsMeter " + MainFx.ver);
        mainStage.setMinWidth(360);
        mainStage.setMinHeight(400);
        Parent root = mainLoader.load();
        Scene scene = new Scene(root);
        mainStage.setScene(scene);


        mainStage.setOnCloseRequest(t -> {

            getMainController().stopDmgCalc();
            try {
                Params.saveAll();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Platform.exit();
        });

        loadFilesAndSettings();
        mainStage.show();

    }

    public void initOverlay() throws IOException {

        overlayLoader = new FXMLLoader();
        overlayLoader.setLocation(MainFx.class.getResource("fxml/overlay.fxml"));
        Parent root = overlayLoader.load();
        overlayStage = new Stage(StageStyle.TRANSPARENT);
        //   overlayStage.getIcons().add(icon);
        overlayStage.setAlwaysOnTop(true);
        Scene scene = new Scene(root, Color.TRANSPARENT);
        overlayStage.setScene(scene);

        scene.getStylesheets().add(MainFx.class.getResource("css/Overlay.css").toExternalForm());
        //   overlayStage.initOwner(mainStage);
        overlayStage.setX(mainStage.getX());
        overlayStage.setY(mainStage.getY());

        overlayStage.setMinWidth(50);
        overlayStage.setMinHeight(50);
        overlayStage.setMaxWidth(370);
        overlayStage.setMaxHeight(397);
        Stages.getOverlayController().rect.setFill(new Color(0, 0, 0, Params.transparency));


        overlayStage.setWidth(Params.overlayWidth);
        overlayStage.setHeight(Params.overlayHeight);

        Stages.getOverlayController().rect.setWidth(Params.overlayWidth);
        Stages.getOverlayController().rect.setHeight(Params.overlayHeight);
    }

    public void initSkills() throws IOException {
        skillsLoader = new FXMLLoader();
        skillsLoader.setLocation(MainFx.class.getResource("fxml/skilllist.fxml"));
        Parent fxmlSkills = skillsLoader.load();
        skillsStage = new Stage();
        skillsStage.getIcons().add(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/icon.png"))));
        skillsStage.setTitle("Список скилов");
        skillsStage.setScene(new Scene(fxmlSkills));
        skillsStage.initOwner(mainStage);
        skillsStage.setMinWidth(280);
        skillsStage.setMinHeight(200);
        skillsStage.setHeight(getHeight());

        if (Params.darkTheme.getValue()) {
            skillsStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SkillListDark.css")).toExternalForm());
        } else {
            skillsStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SkillList.css")).toExternalForm());
        }
    }

    public void initSett() throws IOException {
        settingsLoader = new FXMLLoader();
        settingsLoader.setLocation(MainFx.class.getResource("fxml/settings.fxml"));
        Parent fxmlSettings = settingsLoader.load();
        settStage = new Stage();
        settStage.getIcons().add(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/set.png"))));
        //settStage.setTitle("build. " + Main.build + ")");
        Scene scene = new Scene(fxmlSettings);
        settStage.setScene(scene);
        //  settStage.initStyle(StageStyle.UNDECORATED);
        // settStage.initStyle(StageStyle.TRANSPARENT);
        //  settStage.setWidth(221);
        //  settStage.setHeight(232);
        //  settStage.setResizable(false);
        settStage.setOnCloseRequest(t -> {
            try {
                Params.saveAll();
                //   ToggleWindows.settingsVisible = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        settStage.initOwner(mainStage);
        settStage.toBack();
        if (Params.darkTheme.getValue()) {
            settStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/SettingsDark.css")).toExternalForm());
        } else {
            settStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/Settings.css")).toExternalForm());
        }
    }

    public void initCharts() throws IOException {
        chartsLoader = new FXMLLoader();
        chartsLoader.setLocation(MainFx.class.getResource("fxml/charts.fxml"));
        Parent fxmlCharts = chartsLoader.load();
        chartsStage = new Stage();
        chartsStage.getIcons().add(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/icon.png"))));
       // skillsStage.setTitle();
        chartsStage.setScene(new Scene(fxmlCharts));
        chartsStage.initOwner(mainStage);
        chartsStage.setMinWidth(280);
        chartsStage.setMinHeight(200);
        chartsStage.setHeight(getHeight());
        if (Params.darkTheme.getValue()) {
            chartsStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/ChartsDark.css")).toExternalForm());
        } else {
            chartsStage.getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/Charts.css")).toExternalForm());
        }
    }

    public static void setSize(double width, double height) {
        mainStage.setWidth(width);
        mainStage.setHeight(height);
    }

    public static void setCoordinate(double x, double y) {
        mainStage.setX(x);
        mainStage.setY(y);
    }


    public static double getX() {
        return mainStage.getX();
    }

    public static double getY() {
        return mainStage.getY();
    }

    public static double getWidth() {
        return mainStage.getWidth();
    }

    public static double getHeight() {
        return mainStage.getHeight();
    }

 /*   public static boolean getIconified() {
        return primaryStage.isIconified();
    }*/

/*    public static void setIconified(boolean b) {
        primaryStage.setIconified(b);
    }*/

    public static SkillListController getSkillsController() {
        return skillsLoader.getController();
    }

    public static OverlayController getOverlayController() {
        return overlayLoader.getController();
    }


    public static SettingsController getSettingsController() {
        return settingsLoader.getController();
    }

    public static MainController getMainController() {
        return mainLoader.getController();
    }

    public static ChartsController getChartsController() {
        return chartsLoader.getController();
    }



    public static Stage getMainStage() {
        return mainStage;
    }

    public static Stage getSettingsStage() {
        return settStage;
    }

    public static Stage getSkillsStage() {
        return skillsStage;
    }

    public static Stage getOverlayStage() {
        return overlayStage;
    }

    public static Stage getChartsStage() {
        return chartsStage;
    }

    private void loadFilesAndSettings() {


        if (Params.width !=0)
            Stages.setSize(Params.width, Params.height);
        if (Params.cX!=0)
            Stages.setCoordinate(Params.cX, Params.cY);

        if (Params.darkTheme.getValue()) {
            Stages.getMainStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/MainDark.css")).toExternalForm());
            Stages.getMainController().settingsButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/set_white.png")))));
            Stages.getMainController().copyButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/copy_white.png")))));
            Stages.getMainController().minimizedButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/minimized_white.png")))));
            Stages.getMainController().chartsButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/diag_white.png")))));
            Stages.getMainController().cleanButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/garbare_white.png")))));


        } else {
            Stages.getMainStage().getScene().getStylesheets().add(Objects.requireNonNull(MainFx.class.getResource("css/Main.css")).toExternalForm());
            Stages.getMainController().settingsButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/set.png")))));
            Stages.getMainController().copyButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/copy.png")))));
            Stages.getMainController().minimizedButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/minimized.png")))));
            Stages.getMainController().chartsButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/diag.png")))));
            Stages.getMainController().cleanButton.graphicProperty().setValue(new ImageView(new Image(Objects.requireNonNull(MainFx.class.getResourceAsStream("icons/garbare.png")))));
            //  Stages.getTextOutController().closeOverlayButton.graphicProperty().setValue(new ImageView("/resources/minimized.png"));
        }

       // if (Params.path == null) getMainController().setPath("Выберите файл (Chat.log в папке Aion)");
        getMainController().setPath(Objects.requireNonNullElse(Params.path, "Выберите файл (Chat.log в папке Aion)"));
    }


    public void checkExistsFiles() throws IOException {
   /*     Path dots = Paths.get("dots.txt");
        if (!Files.exists(dots)) {
            Files.createFile(dots);
            copyResource(dots);
        }
        Path skills = Paths.get("skills.txt");
        if (!Files.exists(skills)) {
            Files.createFile(skills);
            copyResource(skills);
        }
        Path ignore = Paths.get("ignore.txt");
        if (!Files.exists(ignore)) {
            Files.createFile(ignore);
            copyResource(ignore);
        }*/
    }

    public void copyResource(Path file) {
        try (InputStream in = getClass()
                .getClassLoader()
                .getResourceAsStream("resources/files/" + file.toString());
             OutputStream out = new FileOutputStream(String.valueOf(file.toAbsolutePath()))) {

            int data;
            if (in != null) {
                while ((data = in.read()) != -1) {
                    out.write(data);
                }
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}

