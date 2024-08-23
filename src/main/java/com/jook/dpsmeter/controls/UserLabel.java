package com.jook.dpsmeter.controls;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Font;

public class UserLabel extends Label {

    public UserLabel() {
        this.setCursor(Cursor.HAND);
        this.setFont(Font.font("Arial", 13));
        this.setTooltip(new Tooltip("Нажмите чтобы увидеть список умений"));
        this.setPadding(new Insets(2, 4, 2, 5));

    }

}
