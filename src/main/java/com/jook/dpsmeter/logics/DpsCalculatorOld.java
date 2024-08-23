package com.jook.dpsmeter.logics;


import com.jook.dpsmeter.MainFx;
import com.jook.dpsmeter.utils.Params;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DpsCalculatorOld implements DpsCalculator {
    private static final Logger logger = LogManager.getLogger(MainFx.class);
    private final Map<String, UserClass> classes;
    private final String name = Params.name;
    private final String checkedNpc;
    private final String path;
    private final String[] classArray;
    private final String[] summonArray = new String[]{"Элементаль огня", "Элементаль воды", "Элементаль земли", "Элементаль воздуха", "Святая энергия", "Святая мощь"};
    private final Utilities ut;

    public DpsCalculatorOld(String path, String checkedNpc, Utilities ut) {
        this.ut = ut;
        this.path = path;
        this.checkedNpc = checkedNpc;
        classes = ut.getClasses();
        classArray = ut.getClassArray();
    }


    public void clean() {
        //     str = 0;
        for (Map.Entry<String, UserClass> entry : classes.entrySet()) {
            if (entry.getValue().countClasses != 0)
                entry.getValue().dotDmg = 0;
        }
    }

    public Result calc() {

        try (BufferedReader brlog = new BufferedReader(new InputStreamReader(new FileInputStream(path), "windows-1251"))) {
            String strLine;

            while ((strLine = brlog.readLine()) != null) {
                if (strLine.contains("[charname:"))
                    continue;
                if (!checkedNpc.equals("Все") && !strLine.contains(checkedNpc))
                    continue;
                if (!strLine.contains("использует") && !strLine.contains("использовали") && !strLine.contains("ед. урона") && !strLine.contains("получает") && !strLine.contains("ед. критического урона"))
                    continue;
                if (strLine.contains("получаете") || strLine.contains("вам") || strLine.contains("персонажу") || strLine.contains("получили") || strLine.contains("вместо ") || strLine.contains("вместо цели") || strLine.contains("вместо RND") || strLine.contains("отражает"))
                    continue;
                if (strLine.contains("от кровотечения") || strLine.contains("Эхо паралича"))
                    continue;

                //Автоатака считающего
                if (strLine.contains("Вы нанесли") && strLine.contains("ед.") && strLine.contains("урона")) {
                    String username = name;
                    addUser(username);
                    String mobName = strLine.substring(strLine.indexOf("цели ") + 5, strLine.lastIndexOf(". "));
                    addNpc(mobName);
                    String dmgStr = strLine.substring(strLine.indexOf("нанесли") + 8, strLine.indexOf("ед. ") - 1);
                    dmgStr = dmgStr.replaceAll(" ", "");
                    int dmgNum = Integer.parseInt(dmgStr);
                    String skillName = "Автоатака";
                    addRotation(username, new Skill(skillName, dmgNum));
                    addDamageToNpc(mobName, dmgNum);
                    continue;
                }

                //Автоатака персонажей
                if (strLine.contains(" наносит ") && strLine.contains("ед. урона") && !strLine.contains(" персонажу ") && !strLine.contains(" вам ")) {
                    String username = strLine.substring(strLine.indexOf(" : ") + 3, strLine.indexOf("наносит") - 1);
                    if ((username.contains(" ") || username.contains("-")) && !username.contains("Элементаль"))
                        if (!username.contains("Энергия"))
                            continue;
                    addUser(username);
                    String mobName = strLine.substring(strLine.indexOf("цели ") + 5, strLine.lastIndexOf(". "));
                    addNpc(mobName);
                    String dmgStr = strLine.substring(strLine.indexOf("наносит ") + 8, strLine.indexOf("ед. ") - 1);
                    dmgStr = dmgStr.replaceAll(" ", "");
                    int dmgNum = Integer.parseInt(dmgStr);
                    String skillName = "Автоатака";
                    if (ut.getUserMap().get(username).characterClass.equals(classArray[0])) {
                        for (String s : summonArray) {
                            if (username.equals(s)) {
                                ut.getUserMap().get(username).characterClass = classArray[12];
                                break;
                            }
                        }
                    }
                    addRotation(username, new Skill(skillName, dmgNum));
                    addDamageToNpc(mobName, dmgNum);
                    continue;
                }
                //Скилы персонажей
                if (strLine.contains("использует") && strLine.contains(" ед. урона")) {
                    String username = strLine.substring(strLine.indexOf(" : ") + 3, strLine.indexOf("использует") - 1);
                    if (username.contains("Критический удар! "))
                        username = username.replaceAll("Критический удар! ", "");
                    if (username.contains("Критический удар!") && !username.contains("Критический удар! "))
                        username = username.replaceAll("Критический удар!", "");
                    if ((username.contains(" ") || username.contains("-")) && !username.contains("Элементаль")) {
                        if (!username.contains("Вестник кары") && !username.contains("Святая энергия") && !username.contains("Святая мощь"))
                            continue;
                    }
                    addUser(username);
                    String skillName = strLine.substring(strLine.indexOf("использует") + 11, strLine.indexOf(". "));
                    if (skillName.substring(0, 1).contains(" "))
                        skillName = skillName.substring(1);
                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern1 = Pattern.compile(" [IVX]+");
                    Matcher matcher1 = pattern1.matcher(skillName);
                    skillName = matcher1.replaceAll("");

                    //Определение класса
                    if (ut.getUserMap().get(username).characterClass.equals(classArray[0])) {
                        for (String s : summonArray) {
                            if (username.equals(s)) {
                                ut.getUserMap().get(username).characterClass = classArray[12];
                                break;
                            }
                        }
                    }

                    if (ut.getUserMap().get(username).characterClass.equals(classArray[0])) {

                        if ((username.contains("Элементаль ") || skillName.contains("Призыв") && !(skillName.contains("Призыв холода") || skillName.contains("Призыв скалы") || skillName.contains("Призыв тепла")))) {
                            ut.getUserMap().get(username).characterClass = classArray[12];
                        } else {

                            for (Map.Entry<String, UserClass> pair : classes.entrySet()) {
                                UserClass characterClass = pair.getValue();
                                for (String s : characterClass.getSKillList()) {
                                    if (s.equals(skillName)) {
                                        ut.getUserMap().get(username).characterClass = pair.getKey();

                                        characterClass.countClasses++;
                                        //System.out.println(pair.getKey() + " : " + characterClass.countClases + " : " + username + " : " + skill);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    String mobName = strLine.substring(strLine.indexOf(". ") + 2, strLine.indexOf(" получает "));
                    addNpc(mobName);
                    String dmgStr = strLine.substring(strLine.indexOf("получает ") + 9, strLine.indexOf("ед. ") - 1);
                    dmgStr = dmgStr.replaceAll(" ", "");
                    int dmgNum = Integer.parseInt(dmgStr);
                    ut.getUserMap().get(username).countAttack++;
                    if (strLine.contains("Критический удар!"))
                        ut.getUserMap().get(username).countCrit++;
                    addRotation(username, new Skill(skillName, dmgNum));
                    addDamageToNpc(mobName, dmgNum);
                    continue;
                }

                if (strLine.contains("получает") && strLine.contains(" ед. урона") && (!strLine.contains("использует") && !strLine.contains(" наносит ") && !strLine.contains("нанесли"))) {
                    String skillName = strLine.substring(strLine.indexOf(" : ") + 3, strLine.lastIndexOf(": "));

                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");
                    boolean ignore = false;
                    for (String s : ut.getIgnoredSkills()) {
                        if (skillName.equals(s)) {
                            ignore = true;
                            break;
                        }
                    }

                    //        if (skillName.contains("Магический урон ")) ignore = true;
                    if (ignore) continue;

                    String mobName = strLine.substring(strLine.lastIndexOf(": ") + 2, strLine.indexOf(" получает "));
                    addNpc(mobName);
                    String dmgStr = strLine.substring(strLine.indexOf("получает") + 9, strLine.indexOf("ед. ") - 1);
                    dmgStr = dmgStr.replaceAll(" ", "");
                    int dmgNum = Integer.parseInt(dmgStr);

                    boolean other = false;

                    for (Map.Entry<String, Integer> entry : ut.getExtraDamageMap().entrySet()) {
                        if (skillName.equals(entry.getKey())) {
                            ut.getExtraDamageMap().put(skillName, entry.getValue() + dmgNum);
                            ut.getResult().haveExtra = true;
                            other = true;
                            break;
                        }
                    }
                    if (other) continue;
                    boolean inListDots = false;
                    if (skillName.contains("Команда:")) {
                        skillName = skillName.substring(0, skillName.lastIndexOf(" "));
                    }


                    for (Map.Entry<String, UserClass> pair : classes.entrySet()) {
                        UserClass characterClass = pair.getValue();
                        for (String s : characterClass.getDotList()) {
                            if (s.equals(skillName)) {
                                inListDots = true;

                                //        System.out.println(characterClass.countClases);


                                if (characterClass.countClasses == 1) {
                                    for (Map.Entry<String, User> entry : ut.getUserMap().entrySet()) {
                                        if (!entry.getValue().characterClass.equals("Неизвестно") && classes.get(entry.getValue().characterClass).countClasses != 0) {
                                            if (entry.getValue().characterClass.equals(pair.getKey())) {
                                                addRotation(entry.getKey(), new Skill(skillName, dmgNum, true));
                                                addDamageToNpc(mobName, dmgNum);
                                            }
                                        }
                                    }
                                } else {
                                    characterClass.dotDmg += dmgNum;
                                }
                                //  characterClass.dotDmg += dmgNum;

                                //    if (pair.getKey().equals("Заклинатель"))
                                //       System.out.println(skillName);
                                break;
                            }
                        }
                    }

                    if (!inListDots) {
                        String username = name;
                        addUser(username);
                        //Определение класса

                        if (ut.getUserMap().get(username).characterClass.equals(classArray[0])) {
                            for (String s : summonArray) {
                                if (username.equals(s)) {
                                    ut.getUserMap().get(username).characterClass = classArray[12];
                                    break;
                                }
                            }
                        }

                        if (ut.getUserMap().get(username).characterClass.equals(classArray[0])) {
                            for (Map.Entry<String, UserClass> pair : classes.entrySet()) {
                                UserClass characterClass = pair.getValue();
                                for (String s : characterClass.getSKillList()) {
                                    if (s.equals(skillName)) {
                                        ut.getUserMap().get(username).characterClass = pair.getKey();

                                        characterClass.countClasses++;
                                        break;
                                    }
                                }
                            }
                        }


                        ut.getUserMap().get(username).countAttack++;
                        if (strLine.contains("Критический удар!"))
                            ut.getUserMap().get(username).countCrit++;
                        addRotation(username, new Skill(skillName, dmgNum));
                        addDamageToNpc(mobName, dmgNum);
                    }
                }

            }//закрывает цикл
        }

        //   }
        // блок try
        catch (IOException e) {
            logger.error(e.getStackTrace());
        }

        //   MainController.str = str;
        // System.out.println("Уже прочитано: " + MainController.str);
        //System.out.println("Текущая: " + str);

        for (Map.Entry<String, User> entry : ut.getUserMap().entrySet()) {
            if (!entry.getValue().characterClass.equals(classArray[0]))
                classes.get(entry.getValue().characterClass).dmgSum += entry.getValue().dmgSum;
        }


        for (Map.Entry<String, User> entry : ut.getUserMap().entrySet()) {
            if (!entry.getValue().characterClass.equals("Неизвестно") && classes.get(entry.getValue().characterClass).countClasses != 0) {
                int dotDmg = classes.get(entry.getValue().characterClass).dotDmg / classes.get(entry.getValue().characterClass).countClasses;
                entry.getValue().dmgSum += dotDmg;
                entry.getValue().dotSum += dotDmg;
            }
        }

        if (ut.getUserMap().size() > 0) {
            for (Map.Entry<String, User> entry : ut.getUserMap().entrySet()) {
                if (!entry.getValue().characterClass.equals(classArray[0]) && ut.getNpcMap().containsKey(entry.getKey())) {
                    ut.getNpcMap().remove(entry.getValue().name);
                }
            }
        }
        //Сортировка мап
        ut.sortMaps();





        return ut.getResult();
    }  //закрывает метод

    private void addRotation(String username, Skill skill) {
        ut.getUserMap().get(username).addSkill(skill);
    }

    private void addDamageToNpc(String npcName, int dmg) {
        ut.getNpcMap().get(npcName).addDamage(dmg);
    }

    private void addUser(String name) {
        if (!ut.getUserMap().containsKey(name)) {
            ut.getUserMap().put(name, new User(name));
        }
    }

    private void addNpc(String mobName) {
        if (!ut.getNpcMap().containsKey(mobName)) {
            ut.getNpcMap().put(mobName, new Npc(mobName));
        }
    }
}
