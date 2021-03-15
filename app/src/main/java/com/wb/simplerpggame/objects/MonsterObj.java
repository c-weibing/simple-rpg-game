package com.wb.simplerpggame.objects;

import java.util.ArrayList;
import java.util.Arrays;

public class MonsterObj {
    private String monsterName;
    private int[] monsterLevel;
    private int monsterHp;
    private int monsterChance;
    private ArrayList<EquipmentObj> monsterEquipmentDrop;
    private HpPotObj hpPotDrop;

    //forest
    public static final MonsterObj SLIME = new MonsterObj("Slime", new int[]{1, 5}, 5, 0, equipmentDrops(new EquipmentObj[]{EquipmentObj.COMMON_SHOES}), HpPotObj.MINOR_HEALING_POTION); //40
    public static final MonsterObj CAVE_BAT = new MonsterObj("Cave Bat", new int[]{1, 5}, 16, 40, equipmentDrops(new EquipmentObj[]{EquipmentObj.COMMON_ARMOR, EquipmentObj.COMMON_SHIELD}), null); //30
    public static final MonsterObj KOBOLD = new MonsterObj("Kobold", new int[]{6, 7}, 28, 70, equipmentDrops(new EquipmentObj[]{}), HpPotObj.MINOR_HEALING_POTION); //15
    public static final MonsterObj GOBLIN = new MonsterObj("Goblin", new int[]{8, 9}, 39, 85, equipmentDrops(new EquipmentObj[]{EquipmentObj.COMMON_SWORD}), null); //10
    public static final MonsterObj HIGH_ORC = new MonsterObj("High Orc", new int[]{10}, 50, 95, equipmentDrops(new EquipmentObj[]{EquipmentObj.COMMON_HAT}), HpPotObj.MINOR_HEALING_POTION); //5

    //ravine
    public static final MonsterObj PIXIE = new MonsterObj("Pixie", new int[]{10, 15}, 50, 0, equipmentDrops(new EquipmentObj[]{}),  HpPotObj.LESSER_HEALING_POTION);
    public static final MonsterObj SALAMANDER = new MonsterObj("Salamander", new int[]{16, 19}, 69, 40, equipmentDrops(new EquipmentObj[]{EquipmentObj.UNCOMMON_ARMOR, EquipmentObj.UNCOMMON_SHIELD}), null);
    public static final MonsterObj LAMIA = new MonsterObj("Lamia", new int[]{20, 22}, 88, 70, equipmentDrops(new EquipmentObj[]{EquipmentObj.UNCOMMON_SHOES}), null);
    public static final MonsterObj HARPY = new MonsterObj("Harpy", new int[]{23, 24}, 106, 85, equipmentDrops(new EquipmentObj[]{EquipmentObj.UNCOMMON_SWORD}), null);
    public static final MonsterObj GRIFFIN = new MonsterObj("Griffin", new int[]{25}, 125, 95, equipmentDrops(new EquipmentObj[]{EquipmentObj.UNCOMMON_HAT}), HpPotObj.LESSER_HEALING_POTION);

    //sea
    public static final MonsterObj WATER_ELEMENTAL = new MonsterObj("Water Elemental", new int[]{20, 25}, 125, 0, equipmentDrops(new EquipmentObj[]{EquipmentObj.RARE_SHOES}), HpPotObj.GREATER_HEALING_POTION);
    public static final MonsterObj SIREN = new MonsterObj("Siren", new int[]{26, 29}, 138, 40, equipmentDrops(new EquipmentObj[]{EquipmentObj.RARE_ARMOR, EquipmentObj.RARE_SHIELD}), null);
    public static final MonsterObj KELPIE = new MonsterObj("Kelpie", new int[]{30, 32}, 150, 70, equipmentDrops(new EquipmentObj[]{}), null);
    public static final MonsterObj SERPENT = new MonsterObj("Serpent", new int[]{33, 34}, 163, 85, equipmentDrops(new EquipmentObj[]{EquipmentObj.RARE_SWORD}), null);
    public static final MonsterObj KRAKEN = new MonsterObj("Kraken", new int[]{35}, 175, 95, equipmentDrops(new EquipmentObj[]{EquipmentObj.RARE_HAT}), HpPotObj.GREATER_HEALING_POTION);

    //abyss
    public static final MonsterObj UNDEAD = new MonsterObj("Undead", new int[]{35, 40}, 175, 0, equipmentDrops(new EquipmentObj[]{}), HpPotObj.MINOR_HEALING_POTION);
    public static final MonsterObj SKELETON = new MonsterObj("Skeleton", new int[]{41, 44}, 194,  40, equipmentDrops(new EquipmentObj[]{EquipmentObj.LEGENDARY_ARMOR, EquipmentObj.LEGENDARY_SHIELD}), HpPotObj.LESSER_HEALING_POTION);
    public static final MonsterObj GHOUL = new MonsterObj("Ghoul", new int[]{45, 47}, 213, 70, equipmentDrops(new EquipmentObj[]{EquipmentObj.LEGENDARY_SHOES}), HpPotObj.GREATER_HEALING_POTION);
    public static final MonsterObj MINOTAUR = new MonsterObj("Minotaur", new int[]{48, 49}, 231, 85, equipmentDrops(new EquipmentObj[]{EquipmentObj.LEGENDARY_SWORD}), null);
    public static final MonsterObj MANTICORE = new MonsterObj("Manticore", new int[]{50}, 250, 95, equipmentDrops(new EquipmentObj[]{EquipmentObj.LEGENDARY_HAT}), HpPotObj.HEALING_ELIXIR);

    private ArrayList<MonsterObj> listOfMonsters = new ArrayList<MonsterObj>() {{
        add(MonsterObj.SLIME);
        add(MonsterObj.CAVE_BAT);
        add(MonsterObj.KOBOLD);
        add(MonsterObj.GOBLIN);
        add(MonsterObj.HIGH_ORC);

        add(MonsterObj.PIXIE);
        add(MonsterObj.SALAMANDER);
        add(MonsterObj.LAMIA);
        add(MonsterObj.HARPY);
        add(MonsterObj.GRIFFIN);

        add(MonsterObj.WATER_ELEMENTAL);
        add(MonsterObj.SIREN);
        add(MonsterObj.KELPIE);
        add(MonsterObj.SERPENT);
        add(MonsterObj.KRAKEN);

        add(MonsterObj.UNDEAD);
        add(MonsterObj.SKELETON);
        add(MonsterObj.GHOUL);
        add(MonsterObj.MINOTAUR);
        add(MonsterObj.MANTICORE);
    }};

    public static ArrayList<EquipmentObj> equipmentDrops(EquipmentObj[] listOfEquipment) {
        ArrayList<EquipmentObj> equipmentDropsArray = new ArrayList<>();

        equipmentDropsArray.addAll(Arrays.asList(listOfEquipment));
        return equipmentDropsArray;
    }

    public MonsterObj getMonsterObjectByName(String monsterName) {
        MonsterObj getMonsterObj = MonsterObj.SLIME;

        for (MonsterObj m : listOfMonsters) {
            if (m.getMonsterName().equals(monsterName)) {
                getMonsterObj = m;
            }
        }

        return getMonsterObj;
    }

    public MonsterObj() {
    }

    public MonsterObj(String monsterName, int[] monsterLevel, int monsterHp, int monsterChance, ArrayList<EquipmentObj> monsterEquipmentDrop, HpPotObj hpPotDrop) {
        this.monsterName = monsterName;
        this.monsterLevel = monsterLevel;
        this.monsterHp = monsterHp;
        this.monsterChance = monsterChance;
        this.monsterEquipmentDrop = monsterEquipmentDrop;
        this.hpPotDrop = hpPotDrop;
    }

    @Override
    public String toString() {
        return "MonsterObj{" +
                "monsterName='" + monsterName + '\'' +
                ", monsterLevel=" + Arrays.toString(monsterLevel) +
                ", monsterHp=" + monsterHp +
                ", monsterChance=" + monsterChance +
                ", monsterEquipmentDrop=" + monsterEquipmentDrop +
                ", hpPotDrop=" + hpPotDrop +
                '}';
    }

    public void setMonsterLevel(int[] monsterLevel) {
        this.monsterLevel = monsterLevel;
    }

    public int[] getMonsterLevel() {
        return monsterLevel;
    }

    public HpPotObj getHpPotDrop() {
        return hpPotDrop;
    }

    public void setHpPotDrop(HpPotObj hpPotDrop) {
        this.hpPotDrop = hpPotDrop;
    }

    public ArrayList<EquipmentObj> getMonsterEquipmentDrop() {
        return monsterEquipmentDrop;
    }

    public void setMonsterEquipmentDrop(ArrayList<EquipmentObj> monsterEquipmentDrop) {
        this.monsterEquipmentDrop = monsterEquipmentDrop;
    }

    public String getMonsterName() {
        return monsterName;
    }

    public void setMonsterName(String monsterName) {
        this.monsterName = monsterName;
    }

    public int getMonsterHp() {
        return monsterHp;
    }

    public void setMonsterHp(int monsterHp) {
        this.monsterHp = monsterHp;
    }

    public int getMonsterChance() {
        return monsterChance;
    }

    public void setMonsterChance(int monsterChance) {
        this.monsterChance = monsterChance;
    }
}
