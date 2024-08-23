package com.jook.dpsmeter.controls;

import com.jook.dpsmeter.utils.Stages;
import com.jook.dpsmeter.utils.Windows;
import javafx.scene.layout.HBox;

public class UserHBox extends HBox {

    //private final String name;
    private final UserLabel userLabel;

    public UserHBox(String name) {

        this.setId("hBox");
        this.setOnMouseClicked(event -> {
            Stages.getSkillsController().setSkillList(name);
            if (!Stages.getSkillsStage().isShowing()) {
                Windows.toggleSkillsWindow();
            }
        });

        userLabel = new UserLabel();
        this.getChildren().add(userLabel);
    }

    public void setLabelText(String text) {
        userLabel.setText(text);
    }

    public String getName() {
        return null;
    }
}
