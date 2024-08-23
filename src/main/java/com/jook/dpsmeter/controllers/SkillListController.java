package com.jook.dpsmeter.controllers;


import com.jook.dpsmeter.logics.CommonSkillInfo;
import com.jook.dpsmeter.logics.Skill;
import com.jook.dpsmeter.logics.User;
import com.jook.dpsmeter.utils.Params;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.input.ClipboardContent;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class SkillListController {
    @FXML
    private ToggleButton skillsToggle;
    @FXML
    private ComboBox<String> usersComboBox;
    @FXML
    private ListView<String> skillListView;


    Map<String, User> userMap = new HashMap<>();

    private final ObservableList<String> skills = FXCollections.observableArrayList();

    static ObservableList<String> users = FXCollections.observableArrayList();

    public void initialize() {
        // usersComboBox.getSelectionModel().select("");
        //  usersComboBox.getItems().addAll(Params.name);
        //  usersComboBox.getSelectionModel().select(Params.name);
        skillListView.setItems(skills);
        usersComboBox.valueProperty().addListener((ov, t, t1) -> {
            if (t1 != null && t != null) {
                setSkillList(t1);
            }
        });
    }


    public void setUserMap(Map<String, User> userMap) {
        this.userMap = userMap;
    }


    void clearSkillsAndUsers() {

        skillListView.getItems().clear();
        usersComboBox.getItems().clear();
        users.remove(Params.name);
        usersComboBox.getItems().setAll(Params.name);
        usersComboBox.getSelectionModel().select(Params.name);

    }


    public void copyListener() {
        //  MainController cm = new MainController();
        javafx.scene.input.Clipboard clipboard = javafx.scene.input.Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(getSkills());
        clipboard.setContent(content);
    }


    private String getSkills() {
        StringBuilder skillsStringBuilder = new StringBuilder();
        for (String skill : skills) {
            skillsStringBuilder.append(skill).append("\n");
        }
        return skillsStringBuilder.toString();
    }

    public void toggleListener() {
        setSkillList(usersComboBox.getValue());
        //  setSkills();
    }

    public void setSkillList(String userName) {


        if (skillsToggle.isSelected()) {
            setDetailedSkillList(userName);
        } else {
            setCommonSkillList(userName);
        }

    }

    void setUsers() {
        usersComboBox.getItems().clear();
     /*   users.remove(Params.name);
        usersComboBox.getItems().setAll(Params.name);
        usersComboBox.getSelectionModel().select(Params.name);*/
        usersComboBox.getItems().addAll(users);
    }

   /* void selectUser(String name) {
        usersComboBox.getSelectionModel().select(name);
    }*/




    private void setCommonSkillList(String userName) {
        usersComboBox.getSelectionModel().select(userName);
        User user = userMap.get(userName);
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        skills.clear();
        user.sortCommonSkillRotation();

        for (Map.Entry<String, CommonSkillInfo> entry : user.getCommonSkillRotation().entrySet()) {
            if (entry.getValue().dmgSum != 0) {

                skills.add(entry.getKey() + " (" + format.format(entry.getValue().dmgSum) + ")" + " x" + entry.getValue().count);
            }
        }
    }

    private void setDetailedSkillList(String userName) {
        usersComboBox.getSelectionModel().select(userName);
        NumberFormat format = NumberFormat.getInstance(Locale.US);
        skills.clear();
        for (int s = 0; s < userMap.get(userName).getDetailedSkillRotation().size(); s++) {
            Skill skill = userMap.get(userName).getDetailedSkillRotation().get(s);
            skills.add(skill.name + " (" + format.format(skill.dmg) + ")");
        }

    }

   /* public void sortCommonSkillRotation(User ) {
        System.out.println(commonSkillRotation);
        commonSkillRotation = commonSkillRotation.entrySet().stream()
                .sorted(Map.Entry.<String, Skill>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }
*/
}
