package com.jook.dpsmeter.utils;


import com.jook.dpsmeter.logics.Npc;
import com.jook.dpsmeter.logics.Skill;
import com.jook.dpsmeter.logics.User;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import org.ini4j.Ini;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Params {



    //public static boolean darkTheme;
    // public static boolean hideUnknown;
    public static BooleanProperty hideUnknown = new SimpleBooleanProperty(true);
    public static BooleanProperty darkTheme = new SimpleBooleanProperty(true);
    public static BooleanProperty newMethod = new SimpleBooleanProperty(false);

    public static File logFile;

    public static String path;
    public static String name;
    public static double transparency;
    public static double overlayWidth;
    public static double overlayHeight;

    public static double width;
    public static double height;

    public static double cX;
    public static double cY;
    // public static double mainWidth;
    //  public static double mainHeight;


    public static void saveAll() throws IOException {




        Ini ini = new Ini(new File("cfg.ini"));
        ini.put("Settings", "DARK_THEME", darkTheme.getValue());
        ini.put("Settings", "SHOW_UNKNOWN", hideUnknown.getValue());
        ini.put("Settings", "NEW_METHOD", newMethod.getValue());
        ini.put("Settings", "PATH", path);
        ini.put("Settings", "NAME", name);
        ini.put("Settings", "TRANSPARENCY", transparency);
        ini.put("Settings", "OVERLAY_WIDTH", overlayWidth);
        ini.put("Settings", "OVERLAY_HEIGHT", overlayHeight);
        ini.put("Size", "width", Stages.getMainStage().getWidth());
        ini.put("Size", "height", Stages.getMainStage().getHeight());
        if (Stages.getMainStage().isIconified())
            Stages.getMainStage().setIconified(false);
        ini.put("Coord", "cX", Stages.getMainStage().getX());
        ini.put("Coord", "cY", Stages.getMainStage().getY());

        ini.store();
    }

    public static void savePath(String path) throws IOException {
        Params.path = path;
        Ini ini = new Ini(new File("cfg.ini"));
        ini.put("Settings", "PATH", path);
        ini.store();
    }
    public static void loadAll() throws IOException {

//        Ini ini = new Ini(new File("cfg.ini"));
        Path cfgFile = Paths.get("cfg.ini");
        if (!Files.exists(cfgFile)) {

            Files.createFile(cfgFile);
        } else {
            Ini ini = new Ini();
            ini.load(cfgFile.toFile());


            try {
                darkTheme.setValue(Boolean.parseBoolean(ini.get("Settings", "DARK_THEME")));
                hideUnknown.setValue(Boolean.parseBoolean(ini.get("Settings", "SHOW_UNKNOWN")));
                newMethod.setValue(Boolean.parseBoolean(ini.get("Settings", "NEW_METHOD")));
                path = ini.get("Settings", "PATH");
                name = ini.get("Settings", "NAME");
                transparency = Double.parseDouble(ini.get("Settings", "TRANSPARENCY"));
                overlayWidth = Double.parseDouble(ini.get("Settings", "OVERLAY_WIDTH"));
                overlayHeight = Double.parseDouble(ini.get("Settings", "OVERLAY_HEIGHT"));

                if (ini.get("Size", "width") != null) {
                    width = Double.parseDouble(ini.get("Size", "width"));
                    height = Double.parseDouble(ini.get("Size", "height"));
                }

                if (ini.get("Coord", "cX") != null) {
                    cX = Double.parseDouble(ini.get("Coord", "cX"));
                    cY = Double.parseDouble(ini.get("Coord", "cY"));
                }


            } catch (NullPointerException ignored) {
            }
        }


        if (Double.isNaN(overlayWidth) || overlayWidth == 0.0) overlayWidth = 269;
        if (Double.isNaN(overlayHeight) || overlayHeight == 0.0) overlayHeight = 250;
        if (Double.isNaN(transparency) || transparency == 0.0) transparency = 0.3;
        if (Params.name == null) Params.name = "Вы";


    }


}
