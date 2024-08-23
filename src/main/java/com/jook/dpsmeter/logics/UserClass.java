package com.jook.dpsmeter.logics;

import java.util.ArrayList;
import java.util.List;

public class UserClass {
    private final List<String> skills = new ArrayList<>();
    private final List<String> dots = new ArrayList<>();

    public int dmgSum = 0;
    public int dotDmg = 0;
    public int countClasses = 0;


    public void addSkill(String skill) {
        skills.add(skill);
    }

    public void addDot(String dot) {
        dots.add(dot);
    }

    public List<String> getSKillList() {
        return skills;
    }

    public List<String> getDotList() {
        return dots;
    }


}
