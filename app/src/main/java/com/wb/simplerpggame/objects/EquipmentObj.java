package com.wb.simplerpggame.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EquipmentObj {
    public static final String HEAD_GEAR = "HEADGEAR";
    public static final String ARMOR = "ARMOR";
    public static final String SHIELD = "SHIELD";
    public static final String WEAPON = "WEAPON";
    public static final String FOOT_WEAR = "FOOTWEAR";

    public static final String COMMON = "COMMON";
    public static final String UNCOMMON = "UNCOMMON";
    public static final String RARE = "RARE";
    public static final String LEGENDARY = "LEGENDARY";

    public static final EquipmentObj COMMON_HAT = new EquipmentObj("Common Hat", EquipmentObj.HEAD_GEAR, EquipmentObj.COMMON, 0, 1, 10, 300, 30);
    public static final EquipmentObj COMMON_ARMOR = new EquipmentObj("Common Armor", EquipmentObj.ARMOR, EquipmentObj.COMMON, 0, 5, 15, 250, 25);
    public static final EquipmentObj COMMON_SWORD = new EquipmentObj("Common Sword", EquipmentObj.WEAPON, EquipmentObj.COMMON, 5, 0, 20, 250, 25);
    public static final EquipmentObj COMMON_SHIELD = new EquipmentObj("Common Shield", EquipmentObj.SHIELD, EquipmentObj.COMMON, 1, 1, 15, 250, 25);
    public static final EquipmentObj COMMON_SHOES = new EquipmentObj("Common Shoes", EquipmentObj.FOOT_WEAR, EquipmentObj.COMMON, 0, 1, 40, 250, 25);

    public static final EquipmentObj UNCOMMON_HAT = new EquipmentObj("Uncommon Hat", EquipmentObj.HEAD_GEAR, EquipmentObj.UNCOMMON, 0, 2, 10, 600, 60);
    public static final EquipmentObj UNCOMMON_ARMOR = new EquipmentObj("Uncommon Armor", EquipmentObj.ARMOR, EquipmentObj.UNCOMMON, 0, 10, 15, 550, 55);
    public static final EquipmentObj UNCOMMON_SWORD = new EquipmentObj("Uncommon Sword", EquipmentObj.WEAPON, EquipmentObj.UNCOMMON, 9, 0, 20, 550, 55);
    public static final EquipmentObj UNCOMMON_SHIELD = new EquipmentObj("Uncommon Shield", EquipmentObj.SHIELD, EquipmentObj.UNCOMMON, 1, 2, 15, 550, 55);
    public static final EquipmentObj UNCOMMON_SHOES = new EquipmentObj("Uncommon Shoes", EquipmentObj.FOOT_WEAR, EquipmentObj.UNCOMMON, 0, 2, 40, 550, 55);

    public static final EquipmentObj RARE_HAT = new EquipmentObj("Rare Hat", EquipmentObj.HEAD_GEAR, EquipmentObj.RARE, 0, 3, 10, 800, 80);
    public static final EquipmentObj RARE_ARMOR = new EquipmentObj("Rare Armor", EquipmentObj.ARMOR, EquipmentObj.RARE, 0, 15, 15, 750, 75);
    public static final EquipmentObj RARE_SWORD = new EquipmentObj("Rare Sword", EquipmentObj.WEAPON, EquipmentObj.RARE, 13, 0, 20, 750, 75);
    public static final EquipmentObj RARE_SHIELD = new EquipmentObj("Rare Shield", EquipmentObj.SHIELD, EquipmentObj.RARE, 2, 2, 15, 750, 75);
    public static final EquipmentObj RARE_SHOES = new EquipmentObj("Rare Shoes", EquipmentObj.FOOT_WEAR, EquipmentObj.RARE, 0, 3, 40, 750, 75);

    public static final EquipmentObj LEGENDARY_HAT = new EquipmentObj("Legendary Hat", EquipmentObj.HEAD_GEAR, EquipmentObj.LEGENDARY, 0, 4, 10, 1100, 110);
    public static final EquipmentObj LEGENDARY_ARMOR = new EquipmentObj("Legendary Armor", EquipmentObj.ARMOR, EquipmentObj.LEGENDARY, 0, 20, 15, 1050, 105);
    public static final EquipmentObj LEGENDARY_SWORD = new EquipmentObj("Legendary Sword", EquipmentObj.WEAPON, EquipmentObj.LEGENDARY, 18, 0, 20, 1050, 105);
    public static final EquipmentObj LEGENDARY_SHIELD = new EquipmentObj("Legendary Shield", EquipmentObj.SHIELD, EquipmentObj.LEGENDARY, 2, 3, 15, 1050, 105);
    public static final EquipmentObj LEGENDARY_SHOES = new EquipmentObj("Legendary Shoes", EquipmentObj.FOOT_WEAR, EquipmentObj.LEGENDARY,0, 4, 40, 1050, 105);

    public ArrayList<Object> getShopEquipmentList() {
        ArrayList<Object> equipmentList = new ArrayList<>();

        equipmentList.add(EquipmentObj.COMMON_HAT);
        equipmentList.add(EquipmentObj.COMMON_ARMOR);
        equipmentList.add(EquipmentObj.COMMON_SWORD);
        equipmentList.add(EquipmentObj.COMMON_SHIELD);
        equipmentList.add(EquipmentObj.COMMON_SHOES);

        equipmentList.add(EquipmentObj.UNCOMMON_HAT);
        equipmentList.add(EquipmentObj.UNCOMMON_ARMOR);
        equipmentList.add(EquipmentObj.UNCOMMON_SWORD);
        equipmentList.add(EquipmentObj.UNCOMMON_SHIELD);
        equipmentList.add(EquipmentObj.UNCOMMON_SHOES);

        equipmentList.add(EquipmentObj.RARE_HAT);
        equipmentList.add(EquipmentObj.RARE_ARMOR);
        equipmentList.add(EquipmentObj.RARE_SWORD);
        equipmentList.add(EquipmentObj.RARE_SHIELD);
        equipmentList.add(EquipmentObj.RARE_SHOES);

        equipmentList.add(EquipmentObj.LEGENDARY_HAT);
        equipmentList.add(EquipmentObj.LEGENDARY_ARMOR);
        equipmentList.add(EquipmentObj.LEGENDARY_SWORD);
        equipmentList.add(EquipmentObj.LEGENDARY_SHIELD);
        equipmentList.add(EquipmentObj.LEGENDARY_SHOES);

        final List<String> definedOrder = Arrays.asList(EquipmentObj.HEAD_GEAR, EquipmentObj.ARMOR, EquipmentObj.WEAPON, EquipmentObj.SHIELD, EquipmentObj.FOOT_WEAR);

        Collections.sort(equipmentList, new Comparator<Object>() {
            public int compare(Object o1, Object o2) {
                EquipmentObj compare1 = (EquipmentObj) o1;
                EquipmentObj compare2 = (EquipmentObj) o2;

                return Integer.compare(definedOrder.indexOf(compare1.getEquipmentType()), definedOrder.indexOf(compare2.getEquipmentType()));
                // int result = Integer.compare(definedOrder.indexOf(compare1.getEquipmentType()), definedOrder.indexOf(compare2.getEquipmentType()));

//                //if it's different return the correct sort
//                if (result != 0) {
//                    return result;
//                }

                //return compare1.getEquipmentName().compareToIgnoreCase(compare2.getEquipmentName());

            }
        });

        return equipmentList;
    }

    private int equipmentId;
    private String equipmentName;
    private String equipmentType;
    private String equipmentTier;
    private int equipmentAttack;
    private int equipmentDefense;
    private int equipmentDropChance;
    private boolean equipped;
    private int equipmentCount;
    private int equipmentBuyAmt;
    private int equipmentSellAmt;

    public EquipmentObj() {
    }

    public EquipmentObj(String equipmentName, String equipmentType, String equipmentTier, int equipmentAttack, int equipmentDefense, int equipmentDropChance, int equipmentBuyAmt, int equipmentSellAmt) {
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;
        this.equipmentTier = equipmentTier;
        this.equipmentAttack = equipmentAttack;
        this.equipmentDefense = equipmentDefense;
        this.equipmentDropChance = equipmentDropChance;
        this.equipmentBuyAmt = equipmentBuyAmt;
        this.equipmentSellAmt = equipmentSellAmt;
    }

    public EquipmentObj(int equipmentId, String equipmentName, String equipmentType, String equipmentTier, int equipmentAttack, int equipmentDefense, int equipmentDropChance, boolean equipped, int equipmentBuyAmt, int equipmentSellAmt) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;
        this.equipmentTier = equipmentTier;
        this.equipmentAttack = equipmentAttack;
        this.equipmentDefense = equipmentDefense;
        this.equipmentDropChance = equipmentDropChance;
        this.equipped = equipped;
        this.equipmentBuyAmt = equipmentBuyAmt;
        this.equipmentSellAmt = equipmentSellAmt;
    }

    public EquipmentObj(int equipmentId, String equipmentName, String equipmentType, String equipmentTier, int equipmentAttack, int equipmentDefense, int equipmentDropChance, boolean equipped, int equipmentCount, int equipmentBuyAmt, int equipmentSellAmt) {
        this.equipmentId = equipmentId;
        this.equipmentName = equipmentName;
        this.equipmentType = equipmentType;
        this.equipmentTier = equipmentTier;
        this.equipmentAttack = equipmentAttack;
        this.equipmentDefense = equipmentDefense;
        this.equipmentDropChance = equipmentDropChance;
        this.equipped = equipped;
        this.equipmentCount = equipmentCount;
        this.equipmentBuyAmt = equipmentBuyAmt;
        this.equipmentSellAmt = equipmentSellAmt;
    }

    @Override
    public String toString() {
        return "EquipmentObj{" +
                "equipmentId=" + equipmentId +
                ", equipmentName='" + equipmentName + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", equipmentTier='" + equipmentTier + '\'' +
                ", equipmentAttack=" + equipmentAttack +
                ", equipmentDefense=" + equipmentDefense +
                ", equipmentDropChance=" + equipmentDropChance +
                ", equipped=" + equipped +
                ", equipmentCount=" + equipmentCount +
                ", equipmentBuyAmt=" + equipmentBuyAmt +
                ", equipmentSellAmt=" + equipmentSellAmt +
                '}';
    }

    public String getEquipmentTier() {
        return equipmentTier;
    }

    public void setEquipmentTier(String equipmentTier) {
        this.equipmentTier = equipmentTier;
    }

    public int getEquipmentBuyAmt() {
        return equipmentBuyAmt;
    }

    public void setEquipmentBuyAmt(int equipmentBuyAmt) {
        this.equipmentBuyAmt = equipmentBuyAmt;
    }

    public int getEquipmentSellAmt() {
        return equipmentSellAmt;
    }

    public void setEquipmentSellAmt(int equipmentSellAmt) {
        this.equipmentSellAmt = equipmentSellAmt;
    }

    public int getEquipmentCount() {
        return equipmentCount;
    }

    public void setEquipmentCount(int equipmentCount) {
        this.equipmentCount = equipmentCount;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public int getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(int equipmentId) {
        this.equipmentId = equipmentId;
    }

    public int getEquipmentDropChance() {
        return equipmentDropChance;
    }

    public void setEquipmentDropChance(int equipmentDropChance) {
        this.equipmentDropChance = equipmentDropChance;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public int getEquipmentAttack() {
        return equipmentAttack;
    }

    public void setEquipmentAttack(int equipmentAttack) {
        this.equipmentAttack = equipmentAttack;
    }

    public int getEquipmentDefense() {
        return equipmentDefense;
    }

    public void setEquipmentDefense(int equipmentDefense) {
        this.equipmentDefense = equipmentDefense;
    }
}
