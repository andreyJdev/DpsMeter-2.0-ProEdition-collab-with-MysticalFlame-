package com.jook.dpsmeter.controllers;

import com.jook.dpsmeter.MainFx;
import com.jook.dpsmeter.utils.Stages;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class OverlayController {


    @FXML
    private Group group;

    private final Label label = new Label();

    // private final AnchorPane label = new AnchorPane();
    public Rectangle rect;

    public Button calcButton = new Button();
    @FXML
    private final Button closeOverlayButton = new Button();

    @FXML
    private final Button clearButton = new Button();

    @FXML
    private final Button copyButton = new Button();


    private double initX;
    private double initY;
    //   public boolean isOn = false;

    public void initialize() {


        label.setPadding(new Insets(30, 5, 5, 5));

        label.setTextFill(Color.WHITE);
        label.setOpacity(0.9);

        group.getChildren().add(label);


        closeOverlayButton.setPrefSize(32, 30);
        closeOverlayButton.setOpacity(0.8);
        closeOverlayButton.setFocusTraversable(false);

        calcButton.setPrefSize(80, 30);
        calcButton.setText("Рассчитать");
        calcButton.setOpacity(0.8);
        calcButton.setFocusTraversable(false);

        copyButton.setPrefSize(32, 30);
        copyButton.setOpacity(0.8);
        copyButton.setFocusTraversable(false);
        copyButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/copy_white.png")).toExternalForm()));

/*
        resetButton.setPrefSize(55, 25);
        resetButton.setText("Сброс");
        resetButton.setOpacity(0.8);
        resetButton.setFocusTraversable(false);*/

        clearButton.setText("Очистить");
        clearButton.setPrefSize(70, 30);
        clearButton.setOpacity(0.8);
        clearButton.setFocusTraversable(false);

        closeOverlayButton.graphicProperty().setValue(new ImageView(Objects.requireNonNull(MainFx.class.getResource("icons/minimized_white.png")).toExternalForm()));
        closeOverlayButton.setOnAction(event -> {
            Stages.getMainStage().show();
            Stages.getMainStage().setIconified(false);
            Stages.getOverlayStage().hide();
        });

        copyButton.setOnAction(event -> Stages.getMainController().copyInString());

        calcButton.setOnAction(event -> {
            try {
                Stages.getMainController().startCalculation();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        clearButton.setOnAction(event -> Stages.getMainController().stopAndClear(true));

        HBox hbox = new HBox(0, closeOverlayButton, calcButton, clearButton, copyButton);

        group.getChildren().addAll(hbox);


// or if you only want to disable horizontal scrolling
        group.setOnMousePressed((MouseEvent event) -> {
            Stage txStage = Stages.getOverlayStage();
            initX = event.getScreenX() - txStage.getX();
            initY = event.getScreenY() - txStage.getY();


        });

        // when screen is dragged, translate it accordingly
        group.setOnMouseDragged((MouseEvent event) -> {
            Stage txStage = Stages.getOverlayStage();
            txStage.setX(event.getScreenX() - initX);
            txStage.setY(event.getScreenY() - initY);

        });
    }


    public void setText(String result) {
        Text text = new Text(result);
        text.setFill(Color.WHITE);
        label.setText(result);
       /* textFlow.getChildren().clear();
        textFlow.getChildren().add(text);*/
    }

    public void clearText() {
        //  textFlow.getChildren().clear();
        label.setText("");
    }
}
