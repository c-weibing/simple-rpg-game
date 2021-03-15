package com.wb.simplerpggame.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class InventoryObj {

    private int inventoryId;
    private String inventoryName;
    private String inventoryType;
    private int inventoryAmt;
    private boolean expanded = false;

    public InventoryObj() {
    }

    public InventoryObj(int inventoryId, String inventoryName, String inventoryType, int inventoryAmt) {
        this.inventoryId = inventoryId;
        this.inventoryName = inventoryName;
        this.inventoryType = inventoryType;
        this.inventoryAmt = inventoryAmt;
    }

    public ArrayList<Object> getInventoryList() {
        ArrayList<Object> getShopInventoryList = new ArrayList<>();

        getShopInventoryList.addAll(getShopHpPotList());

        return getShopInventoryList;
    }

    public ArrayList<InventoryObj> getShopHpPotList() {
        ArrayList<InventoryObj> hpPotList = new ArrayList<>();

        hpPotList.add(new InventoryObj(1, HpPotObj.MINOR_HEALING_POTION.getHpPotName(), HpPotObj.class.getSimpleName(), 1));
        hpPotList.add(new InventoryObj(1, HpPotObj.LESSER_HEALING_POTION.getHpPotName(), HpPotObj.class.getSimpleName(), 1));
        hpPotList.add(new InventoryObj(1, HpPotObj.GREATER_HEALING_POTION.getHpPotName(), HpPotObj.class.getSimpleName(), 1));
        hpPotList.add(new InventoryObj(1, HpPotObj.HEALING_ELIXIR.getHpPotName(), HpPotObj.class.getSimpleName(), 1));

        final List<String> definedOrder = Arrays.asList(HpPotObj.MINOR_HEALING_POTION.getHpPotName(), HpPotObj.LESSER_HEALING_POTION.getHpPotName(), HpPotObj.GREATER_HEALING_POTION.getHpPotName(), HpPotObj.HEALING_ELIXIR.getHpPotName());

        Collections.sort(hpPotList, new Comparator<InventoryObj>() {
            public int compare(InventoryObj o1, InventoryObj o2) {
                return Integer.compare(definedOrder.indexOf(o1.getInventoryName()), definedOrder.indexOf(o2.getInventoryName()));
            }
        });

        return hpPotList;
    }

    @Override
    public String toString() {
        return "InventoryObj{" +
                "inventoryId=" + inventoryId +
                ", inventoryName='" + inventoryName + '\'' +
                ", inventoryType='" + inventoryType + '\'' +
                ", inventoryAmt=" + inventoryAmt +
                '}';
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public int getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(int inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getInventoryName() {
        return inventoryName;
    }

    public void setInventoryName(String inventoryName) {
        this.inventoryName = inventoryName;
    }

    public String getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
        this.inventoryType = inventoryType;
    }

    public int getInventoryAmt() {
        return inventoryAmt;
    }

    public void setInventoryAmt(int inventoryAmt) {
        this.inventoryAmt = inventoryAmt;
    }
}
