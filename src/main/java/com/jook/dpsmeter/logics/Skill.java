package com.jook.dpsmeter.logics;

import java.util.ArrayList;
import java.util.List;

public class Skill {

    public String name;
    public int dmg;
    Boolean isDot = false;


    public Skill(String name, int dmg) {
        this.name = name;
        this.dmg = dmg;
    }

    public Skill(String name, int dmg, Boolean isDot) {
        this.name = name;
        this.dmg = dmg;
        this.isDot = isDot;
    }


}



