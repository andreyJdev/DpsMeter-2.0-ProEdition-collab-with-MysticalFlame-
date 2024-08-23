package com.jook.dpsmeter.utils;


import java.awt.*;

public class Windows {


    private static final double sWidth = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
    private static final double sHeiht = Toolkit.getDefaultToolkit().getScreenSize().getHeight();

    public static void toggleSettingsWindow() {
        if (Stages.getSettingsStage().isShowing()) Stages.getSettingsStage().hide();
        else {
            double cordX = Stages.getX() + Stages.getWidth() - 5;
            double cordY = Stages.getY();
            if (cordX + 220 >= sWidth) {
                cordX = sWidth - 220;
            }

            if (cordY + 287 >= sHeiht) {
                cordY = sHeiht - 287;
            }
            Stages.getSettingsStage().toFront();
            Stages.getSettingsStage().setIconified(false);
            Stages.getSettingsStage().setX(cordX);
            Stages.getSettingsStage().setY(cordY);
            Stages.getSettingsController().setNameTextField(Params.name);
            Stages.getSettingsStage().show();
        }

    }

    public static void toggleSkillsWindow() {
        if (Stages.getSkillsStage().isShowing()) Stages.getSkillsStage().hide();
        else {

            double skillHeight = Stages.getSkillsStage().getHeight();
            double skillWidth = Stages.getSkillsStage().getWidth() - 6;
            if (Double.isNaN(skillHeight))
                skillHeight = 539;
            if (Double.isNaN(skillWidth))
                skillWidth = 310;
            double cordX = Stages.getX() - skillWidth;
            double cordY = Stages.getY();

            if (cordX <= -7) {
                cordX = -7;
            }
            if (cordY + skillHeight + 33 >= sHeiht) {
                cordY = sHeiht - skillHeight - 33;
            }
            Stages.getSkillsStage().toFront();
            Stages.getSkillsStage().setIconified(false);
            Stages.getSkillsStage().setX(cordX);
            Stages.getSkillsStage().setY(cordY);

            Stages.getSkillsStage().show();
        }
    }


    public static void toggleOverlayWindow() {

      /*  ToggleWindows.overlayVisible = true;
        if (Stages.getOverlayStage().isShowing()) Stages.getOverlayStage().hide();
        else Stages.getOverlayStage().show();*/
        Stages.getOverlayStage().show();
        Stages.getMainStage().hide();
    }

    public static void toggleChartsWindow() {
        if (Stages.getChartsStage().isShowing()) Stages.getChartsStage().hide();


            else {

            double cordX = Stages.getX() + Stages.getWidth() - 5;
            double cordY = Stages.getY();
            if (cordX + 220 >= sWidth) {
                cordX = sWidth - 220;
            }

            if (cordY + 287 >= sHeiht) {
                cordY = sHeiht - 287;
            }
                Stages.getChartsStage().toFront();
                Stages.getChartsStage().setIconified(false);
                Stages.getChartsStage().setX(cordX);
                Stages.getChartsStage().setY(cordY);

                Stages.getChartsStage().show();
        }


    }


}
