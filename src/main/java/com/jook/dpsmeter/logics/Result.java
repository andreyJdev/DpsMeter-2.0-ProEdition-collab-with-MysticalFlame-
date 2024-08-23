package com.jook.dpsmeter.logics;


import java.util.HashMap;
import java.util.Map;

public class Result {

    public boolean haveExtra;

    //public ObservableList<String> skills = FXCollections.observableArrayList();

    public Map<String, User> userMap = new HashMap<>();

    public Map<String, Npc> npcMap = new HashMap<>();

    public Map<String, Integer> extraDamageMap = new HashMap<>();

   // public Map<String, Integer> classDamage = new HashMap<>();

    public Map<String, UserClass> classes = new HashMap<>();

    public Map<String, Dot> dotList = new HashMap<>();

}
