package com.wb.simplerpggame.objects;

public class HpPotObj {
    private String hpPotName;
    private int recoverAmt;
    private int hpPotDropChance;
    private int maxAmtToDrop;
    private int buyAmt;
    private int sellAmt;

    public static final HpPotObj MINOR_HEALING_POTION = new HpPotObj("Minor Healing Potion", 5, 30, 5, 10, 1);
    public static final HpPotObj LESSER_HEALING_POTION = new HpPotObj("Lesser Healing Potion", 15, 30, 2, 50, 5);
    public static final HpPotObj GREATER_HEALING_POTION = new HpPotObj("Greater Healing Potion", 40, 30, 2, 150, 15);
    public static final HpPotObj HEALING_ELIXIR = new HpPotObj("Healing Elixir", 100, 50, 2, 500, 50);

    public HpPotObj(String hpPotName, int recoverAmt, int hpPotDropChance, int maxAmtToDrop, int buyAmt, int sellAmt) {
        this.hpPotName = hpPotName;
        this.recoverAmt = recoverAmt;
        this.hpPotDropChance = hpPotDropChance;
        this.maxAmtToDrop = maxAmtToDrop;
        this.buyAmt = buyAmt;
        this.sellAmt = sellAmt;
    }

    public HpPotObj() {
    }

    @Override
    public String toString() {
        return "HpPotObj{" +
                "hpPotName='" + hpPotName + '\'' +
                ", recoverAmt=" + recoverAmt +
                ", hpPotDropChance=" + hpPotDropChance +
                ", maxAmtToDrop=" + maxAmtToDrop +
                ", buyAmt=" + buyAmt +
                ", sellAmt=" + sellAmt +
                '}';
    }

    public static HpPotObj getHpPotObject(String hpPotName) {
        if (hpPotName.equals(LESSER_HEALING_POTION.getHpPotName())) {
            return LESSER_HEALING_POTION;
        } else if (hpPotName.equals(GREATER_HEALING_POTION.getHpPotName())) {
            return GREATER_HEALING_POTION;
        } else if (hpPotName.equals(HEALING_ELIXIR.getHpPotName())) {
            return HEALING_ELIXIR;
        } else {
            return MINOR_HEALING_POTION;
        }
    }

    public int getBuyAmt() {
        return buyAmt;
    }

    public void setBuyAmt(int buyAmt) {
        this.buyAmt = buyAmt;
    }

    public int getSellAmt() {
        return sellAmt;
    }

    public void setSellAmt(int sellAmt) {
        this.sellAmt = sellAmt;
    }

    public int getMaxAmtToDrop() {
        return maxAmtToDrop;
    }

    public void setMaxAmtToDrop(int maxAmtToDrop) {
        this.maxAmtToDrop = maxAmtToDrop;
    }

    public int getHpPotDropChance() {
        return hpPotDropChance;
    }

    public void setHpPotDropChance(int hpPotDropChance) {
        this.hpPotDropChance = hpPotDropChance;
    }

    public String getHpPotName() {
        return hpPotName;
    }

    public void setHpPotName(String hpPotName) {
        this.hpPotName = hpPotName;
    }

    public int getRecoverAmt() {
        return recoverAmt;
    }

    public void setRecoverAmt(int recoverAmt) {
        this.recoverAmt = recoverAmt;
    }
}
