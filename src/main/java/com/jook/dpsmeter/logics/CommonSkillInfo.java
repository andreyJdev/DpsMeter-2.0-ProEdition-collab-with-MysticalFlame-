package com.jook.dpsmeter.logics;

public class CommonSkillInfo implements Comparable<CommonSkillInfo> {
    public int dmgSum;
    public int count;


    @Override
    public int compareTo(CommonSkillInfo o) {
        return this.dmgSum - o.dmgSum;
    }
}

