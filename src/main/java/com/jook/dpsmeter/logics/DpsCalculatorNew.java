package com.jook.dpsmeter.logics;

import com.jook.dpsmeter.utils.Params;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DpsCalculatorNew implements DpsCalculator {

    private final Map<String, UserClass> classes;
    private final String[] classArray;
    private final String[] summonArray = new String[]{"Элементаль огня", "Элементаль воды", "Элементаль земли", "Элементаль воздуха", "Святая энергия", "Святая мощь"};

    private final String name = Params.name;
    private final String[] ignoreReplace = new String[]{"Рапсодия мушки", "Резкий свист", "Отсроченный взрыв", "Невероятное извержение вулкана", "Мощная вулканическая вспышка", "Великое извержение вулкана", "Призыв: Мощь ветра", "Бомбы с краской", "Узы шторма", "Часовая бомба", "Узы жизни", "Узы гнева", "Узы времени"};
    private final String checkedNpc;
    private final String path;
    private final Utilities ut;

    public DpsCalculatorNew(String path, String checkedNpc, Utilities ut) {
        this.ut = ut;
        this.path = path;
        this.checkedNpc = checkedNpc;

        classArray = ut.getClassArray();
        classes = ut.getClasses();

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
                if (strLine.contains("использует:") && (strLine.contains("постоянно получает урон") || strLine.contains("неотвратимой вспышки") || strLine.contains("неотвратимых уз")) && !strLine.contains("используете:")) {
                    String username = strLine.substring(strLine.indexOf(" : ") + 3, strLine.indexOf("использует") - 1);
                    if (username.contains("Критический удар!  "))
                        username = username.replaceAll("Критический удар! ", "");
                    if (username.contains("Критический удар!") && !username.contains("Критический удар! "))
                        username = username.replaceAll("Критический удар!", "");
                    if ((username.contains(" ") || username.contains("-")) && !username.contains("Элементаль"))
                        continue;
                    addUser(username);
                    String skillName = strLine.substring(strLine.indexOf("использует: ") + 12, strLine.indexOf(". "));
                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");
                    ut.getUserMap().get(username).countAttack++;
                    if (strLine.contains("Критический удар!"))
                        ut.getUserMap().get(username).countCrit++;
                    addDot(username, skillName);
                    replaceDot(username, skillName);
                    addRotation(username, new Skill(skillName, 0, true));


                }
                if (strLine.contains("используете") && (strLine.contains("постоянно получает урон") || strLine.contains("неотвратимой вспышки"))) {
                    String username = name;
                    addUser(username);

                    String skillName = strLine.substring(strLine.indexOf("используете: ") + 13, strLine.indexOf(". "));
                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");
                    addRotation(username, new Skill(skillName, 0, true));

                    addDot(name, skillName);
                    replaceDot(name, skillName);


                    ut.getUserMap().get(username).countAttack++;
                    if (strLine.contains("Критический удар!"))
                        ut.getUserMap().get(username).countCrit++;

                }

                if (strLine.contains("Вы использовали:") && strLine.contains("неотвратимых уз")) {
                    String username = name;
                    addUser(username);

                    String skillName = strLine.substring(strLine.indexOf("использовали: ") + 14, strLine.indexOf(". "));
                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");
                    addRotation(username, new Skill(skillName, 0, true));
                    addDot(name, skillName);
                    replaceDot(name, skillName);

                    ut.getUserMap().get(username).countAttack++;
                    if (strLine.contains("Критический удар!"))
                        ut.getUserMap().get(username).countCrit++;

                }
                if (strLine.contains("получает продолжительный урон")) {

                    String skillName = strLine.substring(strLine.indexOf(" : ") + 3, strLine.lastIndexOf(": "));
                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");
                    String username = name;
                    addUser(username);
                    addRotation(username, new Skill(skillName, 0, true));
                    addDot(name, skillName);
                    replaceDot(name, skillName);
                    ut.getUserMap().get(username).countAttack++;
                    if (strLine.contains("Критический удар!"))
                        ut.getUserMap().get(username).countCrit++;
                }
                if (strLine.contains("получает") && strLine.contains(" ед. урона") && (!strLine.contains("использует") && !strLine.contains("наносит") && !strLine.contains("нанесли"))) {
                    String skillName = strLine.substring(strLine.indexOf(" : ") + 3, strLine.lastIndexOf(": "));

                    if (skillName.contains("Критический удар!"))
                        skillName = skillName.replaceAll("Критический удар! ", "");
                    if (skillName.contains("Критический удар!") && !skillName.contains("Критический удар! "))
                        skillName = skillName.replaceAll("Критический удар!", "");
                    Pattern pattern = Pattern.compile(" [IVX]+");
                    Matcher matcher = pattern.matcher(skillName);
                    skillName = matcher.replaceAll("");

                    String mobName = strLine.substring(strLine.lastIndexOf(": ") + 2, strLine.indexOf(" получает "));
                    addNpc(mobName);
                    String dmgStr = strLine.substring(strLine.indexOf("получает") + 9, strLine.indexOf("ед. ") - 1);
                    dmgStr = dmgStr.replaceAll(" ", "");
                    int dmgNum = Integer.parseInt(dmgStr);

                    boolean ignore = false;
                    for (String s : ut.getIgnoredSkills()) {
                        if (skillName.equals(s)) {
                            addDamageToNpc(mobName, dmgNum);
                            ignore = true;
                            break;
                        }
                    }

                    //        if (skillName.contains("Магический урон ")) ignore = true;
                    if (ignore) continue;


                    boolean other = false;

                    for (Map.Entry<String, Integer> entry : ut.getExtraDamageMap().entrySet()) {
                        if (skillName.equals(entry.getKey())) {
                            ut.getExtraDamageMap().put(skillName, entry.getValue() + dmgNum);
                            ut.getResult().haveExtra = true;
                            addDamageToNpc(mobName, dmgNum);
                            other = true;
                            break;
                        }
                    }
                    if (other) continue;

                    boolean inDotsActive = false;


                    for (Map.Entry<String, Dot> entry : ut.getDotList().entrySet()) {
                        if (skillName.equals(entry.getKey())) {
                            String persdotdmg = ut.getDotList().get(skillName).personUsedDot;
                            addRotation(persdotdmg, new Skill(skillName, dmgNum, true));
                            addDamageToNpc(mobName, dmgNum);
                            ut.getDotList().get(skillName).lastDotsDamage= dmgNum;
                            inDotsActive = true;
                            break;
                        }
                    }


                    if (!inDotsActive) {

                        String username = name;
                        addUser(username);
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

            } //закрывает цикл

        } //блок try


        catch (IOException e) {
            System.out.println("Ошибка4");
        }


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

        ut.sortMaps();


        //ut.getUserMap() = ut.getUserMap();
        // result.ut.getNpcMap() = ut.getNpcMap();
        // result.ut.getExtraDamageMap() = ut.getExtraDamageMap();

        return ut.getResult();
    }  //закрывает метод

    private void replaceDot(String username, String skillName) {
        boolean contain = false;
        for (String s : ignoreReplace) {
            if (skillName.equals(s)) {
                contain = true;
                break;
            }
        }


        if (username.equals(name) && !ut.getDotList().get(skillName).lastPersonUsedDot.equals(username) && !contain) {
            Skill skill = new Skill(skillName, ut.getDotList().get(skillName).lastDotsDamage, true);
            addRotation(name, skill);
            removeRotation(ut.getDotList().get(skillName).lastPersonUsedDot, skill);
        }
        ut.getDotList().get(skillName).lastPersonUsedDot=username;

    }


    private void addDot(String username, String skillName) {


        if(!ut.getDotList().containsKey(skillName)) {
            ut.addDot(skillName, new Dot(username, username, 0));
        }
        else {
            ut.getDotList().get(skillName).personUsedDot = username;
        }

       /* if (!activeDotsList.contains(skillName)) {
            activeDotsList.add(skillName);
            personUsedDot.add(username);
            lastPersonUsedDot.add(username);
            lastDotsDamage.add(0);
        } else
            personUsedDot.set(activeDotsList.indexOf(skillName), username);

        */
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

    private void addRotation(String username, Skill skill) {
        ut.getUserMap().get(username).addSkill(skill);
    }

    private void addDamageToNpc(String npcName, int dmg) {
        ut.getNpcMap().get(npcName).addDamage(dmg);
    }

    private void removeRotation(String username, Skill skill) {
        ut.getUserMap().get(username).removeSkillInfo(skill);
    }
}
