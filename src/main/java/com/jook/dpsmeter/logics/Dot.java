package com.jook.dpsmeter.logics;

import java.util.ArrayList;
import java.util.List;

public class Dot {

    public String personUsedDot;
    public String lastPersonUsedDot;
    public int lastDotsDamage = 0;

    public Dot(String personUsedDot, String lastPersonUsedDot, int lastDotsDamage) {

        this.personUsedDot = personUsedDot;
        this.lastPersonUsedDot = lastPersonUsedDot;
        this.lastDotsDamage = lastDotsDamage;
    }
}
