package com.jook.dpsmeter.logics;

import com.jook.dpsmeter.MainFx;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Utilities {

    private final String[] classArray = new String[]{"Неизвестно", "Гладиатор", "Страж", "Убийца", "Стрелок", "Волшебник", "Заклинатель", "Чародей", "Целитель", "Бард", "Снайпер", "Пилот", "Призыв"};
    private final List<String> ignoredSkills;
    private static final Logger logger = LogManager.getLogger(MainFx.class);
    private Result result;
    public Utilities () {
        ignoredSkills = new ArrayList<>();
    }

    public Map<String, Npc> getNpcMap() {
        return result.npcMap;
    }

    public Map<String, Integer> getExtraDamageMap() {
        return result.extraDamageMap;
    }

    public List<String> getIgnoredSkills() {
        return ignoredSkills;
    }

    public Result getResult() {
        return result;
    }

    public Map<String, User> getUserMap() {
        return result.userMap;
    }




  /*  private void addGeneralRotation(String username, String skillName) {
        userMap.get(username).addGeneralSkillInfo(skillName);
    }*/


    private void readFiles() {
        try (BufferedReader brs = new BufferedReader(new InputStreamReader(new FileInputStream("ignore.txt"), "windows-1251"))) {
            String line;
            while ((line = brs.readLine()) != null) {

                if (!line.isEmpty()) {
                    ignoredSkills.add(line.trim());
                }
            }
        } catch (IOException e) {
            logger.warn("Отсутствует файл ignore.txt");
        }

        try (BufferedReader brs = new BufferedReader(new InputStreamReader(new FileInputStream("skills.txt"), "windows-1251"))) {
            String line;
            String classname = null;
            while ((line = brs.readLine()) != null) {
                if (line.contains("[")) {
                    classname = line.substring(1, line.indexOf("]"));
                } else {
                    if (result.classes.containsKey(classname)) {
                        if (!line.trim().isEmpty())
                            result.classes.get(classname).addSkill(line.trim());
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Отсутствует файл skills.txt");
        }
        try (BufferedReader brs = new BufferedReader(new InputStreamReader(new FileInputStream("dots.txt"), "windows-1251"))) {
            String line;
            String classname = null;
            while ((line = brs.readLine()) != null) {
                if (line.contains("[")) {
                    classname = line.substring(1, line.indexOf("]"));
                } else {
                    if (result.classes.containsKey(classname)) {
                        if (!line.trim().isEmpty())
                            result.classes.get(classname).addDot(line.trim());
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("Отсутствует файл dots.txt");
        }
    }

    

    private void addExtraDamageMap() {
        String[] extraDamageArray = new String[]{"Порох", "Клятва земли", "Клятва кинжала", "Готовность к стрельбе", "Обжигающее пламя", "Усиление магического взрыва", "Эффект возмущения", "Обещание земли", "Благословение ветра", "Благословение огня", "Магический урон огнем", "Магический урон воздухом", "Магический урон водой", "Магический урон землей", "Сильный отпор", "Песчаная ловушка", "Магический обет"};

        for (String s : extraDamageArray) {
            result.extraDamageMap.put(s, 0);
        }
    }
    private void addClassesMap() {
        for (int i = 1; i < classArray.length; i++) {
            result.classes.put(classArray[i], new UserClass());
        }
    }
    public void sortMaps () {

        result.userMap = result.userMap.entrySet().stream()
                .sorted(Map.Entry.<String, User>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        result.npcMap = result.npcMap.entrySet().stream()
                .sorted(Map.Entry.<String, Npc>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));
        result.extraDamageMap = result.extraDamageMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    }

    public Map<String, UserClass> getClasses() {
        return result.classes;
    }

    public String[] getClassArray () {
        return classArray;
    }

    public Map<String, Dot> getDotList () {
        return result.dotList;
    }
    public void addDot(String dotName, Dot dot) {
        result.dotList.put(dotName, dot);
    }

    public void newResult() {
        result = new Result();
        addExtraDamageMap();
        addClassesMap();
        readFiles();
    }
}
