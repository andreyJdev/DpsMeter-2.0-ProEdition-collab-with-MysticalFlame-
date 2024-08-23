package com.jook.dpsmeter.logics;


import java.util.*;
import java.util.stream.Collectors;


public class User implements Comparable<User> {

    public String name;

    public String characterClass;

    public long dmgSum;

    public int dotSum;

    public double countAttack;
    public double countCrit;

    private final List<Skill> detailedSkillRotation = new ArrayList<>();

    private Map<String, CommonSkillInfo> commonSkillRotation = new HashMap<>();


    User(String name) {
        this.name = name;
        dmgSum = 0;
        dotSum = 0;
        characterClass = "Неизвестно";
    }

    public void addSkill(Skill skill) {
        detailedSkillRotation.add(skill);

        if (!commonSkillRotation.containsKey(skill.name)) {

            commonSkillRotation.put(skill.name, new CommonSkillInfo());

        }
        CommonSkillInfo commonSkillInfo = commonSkillRotation.get(skill.name);
        commonSkillInfo.count++;
        commonSkillInfo.dmgSum += skill.dmg;
        dmgSum += skill.dmg;
        if (skill.isDot)
            dotSum += skill.dmg;
    }

    public void removeSkillInfo(Skill skill) {
        detailedSkillRotation.remove(detailedSkillRotation.size() - 1);
        CommonSkillInfo commonSkillInfo = commonSkillRotation.get(skill.name);
        commonSkillInfo.count--;
        commonSkillInfo.dmgSum -= skill.dmg;
        dmgSum -= skill.dmg;
        if (skill.isDot)
            dotSum -= skill.dmg;
    }


    public List<Skill> getDetailedSkillRotation() {
        return detailedSkillRotation;
    }

    public Map<String, CommonSkillInfo> getCommonSkillRotation() {
        return commonSkillRotation;
    }

    public void sortCommonSkillRotation() {
        commonSkillRotation = commonSkillRotation.entrySet().stream()
                .sorted(Map.Entry.<String, CommonSkillInfo>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    @Override
    public int compareTo(User o) {
        return (int) (this.dmgSum - o.dmgSum);
    }
}