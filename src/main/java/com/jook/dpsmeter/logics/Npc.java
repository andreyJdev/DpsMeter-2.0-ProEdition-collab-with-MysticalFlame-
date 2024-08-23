package com.jook.dpsmeter.logics;

public class Npc implements Comparable<Npc> {

    public long receivedDmg;

    public String name;

    public Npc(String name) {
        this.name = name;
        receivedDmg = 0;
    }

    public void addDamage(int dmg) {
        receivedDmg += dmg;
    }

    @Override
    public int compareTo(Npc o) {
        return (int) (this.receivedDmg - o.receivedDmg);
    }
}
