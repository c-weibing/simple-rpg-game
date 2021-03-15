package com.wb.simplerpggame.objects;

public class PlayerObj {
    private int playerId;
    private int playerLevel;
    private int playerCurrentHp;
    private int playerMaxHp;
    private int playerCurrentExp;
    private int playerMaxExp;
    private int playerGold;
    private int playerAttack;
    private int playerDefense;
    private String playerMap;
    private EquipmentObj playerHeadGear;
    private EquipmentObj playerArmor;
    private EquipmentObj playerShield;
    private EquipmentObj playerWeapon;
    private EquipmentObj playerFootwear;

    public PlayerObj(int playerId, int playerLevel, int playerCurrentHp, int playerMaxHp, int playerCurrentExp, int playerMaxExp, int playerGold, int playerAttack, int playerDefense, String playerMap) {
        this.playerId = playerId;
        this.playerLevel = playerLevel;
        this.playerCurrentHp = playerCurrentHp;
        this.playerMaxHp = playerMaxHp;
        this.playerCurrentExp = playerCurrentExp;
        this.playerMaxExp = playerMaxExp;
        this.playerGold = playerGold;
        this.playerAttack = playerAttack;
        this.playerDefense = playerDefense;
        this.playerMap = playerMap;
    }

    public PlayerObj(EquipmentObj playerHeadGear, EquipmentObj playerArmor, EquipmentObj playerShield, EquipmentObj playerWeapon, EquipmentObj playerFootwear) {
        this.playerHeadGear = playerHeadGear;
        this.playerArmor = playerArmor;
        this.playerShield = playerShield;
        this.playerWeapon = playerWeapon;
        this.playerFootwear = playerFootwear;
    }

    @Override
    public String toString() {
        return "PlayerObj{" +
                "playerId=" + playerId +
                ", playerLevel=" + playerLevel +
                ", playerCurrentHp=" + playerCurrentHp +
                ", playerMaxHp=" + playerMaxHp +
                ", playerCurrentExp=" + playerCurrentExp +
                ", playerMaxExp=" + playerMaxExp +
                ", playerGold=" + playerGold +
                ", playerAttack=" + playerAttack +
                ", playerDefense=" + playerDefense +
                ", playerMap='" + playerMap + '\'' +
                ", playerHeadGear=" + playerHeadGear +
                ", playerArmor=" + playerArmor +
                ", playerGloves=" + playerShield +
                ", playerWeapon=" + playerWeapon +
                ", playerFootwear=" + playerFootwear +
                '}';
    }

    public EquipmentObj getPlayerHeadGear() {
        return playerHeadGear;
    }

    public void setPlayerHeadGear(EquipmentObj playerHeadGear) {
        this.playerHeadGear = playerHeadGear;
    }

    public EquipmentObj getPlayerArmor() {
        return playerArmor;
    }

    public void setPlayerArmor(EquipmentObj playerArmor) {
        this.playerArmor = playerArmor;
    }

    public EquipmentObj getPlayerShield() {
        return playerShield;
    }

    public void setPlayerShield(EquipmentObj playerShield) {
        this.playerShield = playerShield;
    }

    public EquipmentObj getPlayerWeapon() {
        return playerWeapon;
    }

    public void setPlayerWeapon(EquipmentObj playerWeapon) {
        this.playerWeapon = playerWeapon;
    }

    public EquipmentObj getPlayerFootwear() {
        return playerFootwear;
    }

    public void setPlayerFootwear(EquipmentObj playerFootwear) {
        this.playerFootwear = playerFootwear;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;

    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int getPlayerCurrentHp() {
        return playerCurrentHp;
    }

    public void setPlayerCurrentHp(int playerCurrentHp) {
        this.playerCurrentHp = playerCurrentHp;
    }

    public int getPlayerMaxHp() {
        return playerMaxHp;
    }

    public void setPlayerMaxHp(int playerMaxHp) {
        this.playerMaxHp = playerMaxHp;
    }

    public int getPlayerCurrentExp() {
        return playerCurrentExp;
    }

    public void setPlayerCurrentExp(int playerCurrentExp) {
        this.playerCurrentExp = playerCurrentExp;
    }

    public int getPlayerMaxExp() {
        return playerMaxExp;
    }

    public void setPlayerMaxExp(int playerMaxExp) {
        this.playerMaxExp = playerMaxExp;
    }

    public int getPlayerGold() {
        return playerGold;
    }

    public void setPlayerGold(int playerGold) {
        this.playerGold = playerGold;
    }

    public int getPlayerAttack() {
        return playerAttack;
    }

    public void setPlayerAttack(int playerAttack) {
        this.playerAttack = playerAttack;
    }

    public int getPlayerDefense() {
        return playerDefense;
    }

    public void setPlayerDefense(int playerDefense) {
        this.playerDefense = playerDefense;
    }

    public String getPlayerMap() {
        return playerMap;
    }

    public void setPlayerMap(String playerMap) {
        this.playerMap = playerMap;
    }
}
