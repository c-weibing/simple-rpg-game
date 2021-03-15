package com.wb.simplerpggame.objects;

import java.util.ArrayList;

public class AchievementObj {
    private int achievementId;
    private int achievementCurrentProgress;
    private int achievementGoal;
    private int achievementGoldReward;
    private MonsterObj achievementMonster;
    private boolean achievementClaimed;

    //forest
    public static final AchievementObj ACHIEVEMENT_1 = new AchievementObj(0, 5, 10, MonsterObj.SLIME); //2
    public static final AchievementObj ACHIEVEMENT_2 = new AchievementObj(0, 15, 30, MonsterObj.SLIME);
    public static final AchievementObj ACHIEVEMENT_3 = new AchievementObj(0, 50, 500, MonsterObj.SLIME);

    public static final AchievementObj ACHIEVEMENT_4 = new AchievementObj(0, 5, 10, MonsterObj.CAVE_BAT); //2
    public static final AchievementObj ACHIEVEMENT_5 = new AchievementObj(0, 5, 60, MonsterObj.KOBOLD); //12
    public static final AchievementObj ACHIEVEMENT_6 = new AchievementObj(0, 5, 80, MonsterObj.GOBLIN); //16

    public static final AchievementObj ACHIEVEMENT_7 = new AchievementObj(0, 5, 500, MonsterObj.HIGH_ORC);

    //ravine
    public static final AchievementObj ACHIEVEMENT_8 = new AchievementObj(0, 5, 100, MonsterObj.PIXIE); //20
    public static final AchievementObj ACHIEVEMENT_9 = new AchievementObj(0, 15, 300, MonsterObj.PIXIE);
    public static final AchievementObj ACHIEVEMENT_10 = new AchievementObj(0, 50, 1100, MonsterObj.PIXIE);

    public static final AchievementObj ACHIEVEMENT_11 = new AchievementObj(0, 5, 1100, MonsterObj.GRIFFIN);

    //sea
    public static final AchievementObj ACHIEVEMENT_12 = new AchievementObj(0, 5, 200, MonsterObj.WATER_ELEMENTAL); //40
    public static final AchievementObj ACHIEVEMENT_13 = new AchievementObj(0, 15, 600, MonsterObj.WATER_ELEMENTAL);
    public static final AchievementObj ACHIEVEMENT_14 = new AchievementObj(0, 50, 1500, MonsterObj.WATER_ELEMENTAL);

    public static final AchievementObj ACHIEVEMENT_15 = new AchievementObj(0, 5, 260, MonsterObj.SIREN); //52
    public static final AchievementObj ACHIEVEMENT_16 = new AchievementObj(0, 5, 1500, MonsterObj.KRAKEN);

    //abyss
    public static final AchievementObj ACHIEVEMENT_17 = new AchievementObj(0, 5, 350, MonsterObj.UNDEAD); //70
    public static final AchievementObj ACHIEVEMENT_18 = new AchievementObj(0, 15, 1050, MonsterObj.UNDEAD);
    public static final AchievementObj ACHIEVEMENT_19 = new AchievementObj(0, 50, 2100, MonsterObj.UNDEAD);

    public static final AchievementObj ACHIEVEMENT_20 = new AchievementObj(0, 5, 2100, MonsterObj.MANTICORE);

    public ArrayList<AchievementObj> getDefaultAchievementsList(){
        ArrayList<AchievementObj> listOfDefaultAchievements = new ArrayList<>();

        listOfDefaultAchievements.add(ACHIEVEMENT_1);
        listOfDefaultAchievements.add(ACHIEVEMENT_2);
        listOfDefaultAchievements.add(ACHIEVEMENT_3);
        listOfDefaultAchievements.add(ACHIEVEMENT_4);
        listOfDefaultAchievements.add(ACHIEVEMENT_5);
        listOfDefaultAchievements.add(ACHIEVEMENT_6);
        listOfDefaultAchievements.add(ACHIEVEMENT_7);
        listOfDefaultAchievements.add(ACHIEVEMENT_8);
        listOfDefaultAchievements.add(ACHIEVEMENT_9);
        listOfDefaultAchievements.add(ACHIEVEMENT_10);
        listOfDefaultAchievements.add(ACHIEVEMENT_11);
        listOfDefaultAchievements.add(ACHIEVEMENT_12);
        listOfDefaultAchievements.add(ACHIEVEMENT_13);
        listOfDefaultAchievements.add(ACHIEVEMENT_14);
        listOfDefaultAchievements.add(ACHIEVEMENT_15);
        listOfDefaultAchievements.add(ACHIEVEMENT_16);
        listOfDefaultAchievements.add(ACHIEVEMENT_17);
        listOfDefaultAchievements.add(ACHIEVEMENT_18);
        listOfDefaultAchievements.add(ACHIEVEMENT_19);
        listOfDefaultAchievements.add(ACHIEVEMENT_20);

        return listOfDefaultAchievements;
    }

    public AchievementObj() {
    }

    public AchievementObj(int achievementCurrentProgress, int achievementGoal, int achievementGoldReward, MonsterObj achievementMonster) {
        this.achievementCurrentProgress = achievementCurrentProgress;
        this.achievementGoal = achievementGoal;
        this.achievementGoldReward = achievementGoldReward;
        this.achievementMonster = achievementMonster;
    }

    public AchievementObj(int achievementId, int achievementCurrentProgress, int achievementGoal, int achievementGoldReward, MonsterObj achievementMonster, boolean achievementClaimed) {
        this.achievementId = achievementId;
        this.achievementCurrentProgress = achievementCurrentProgress;
        this.achievementGoal = achievementGoal;
        this.achievementGoldReward = achievementGoldReward;
        this.achievementMonster = achievementMonster;
        this.achievementClaimed = achievementClaimed;
    }

    @Override
    public String toString() {
        return "AchievementObj{" +
                "achievementId=" + achievementId +
                ", achievementCurrentProgress=" + achievementCurrentProgress +
                ", achievementGoal=" + achievementGoal +
                ", achievementGoldReward=" + achievementGoldReward +
                ", achievementMonster=" + achievementMonster +
                ", achievementClaimed=" + achievementClaimed +
                '}';
    }

    public boolean isAchievementClaimed() {
        return achievementClaimed;
    }

    public void setAchievementClaimed(boolean achievementClaimed) {
        this.achievementClaimed = achievementClaimed;
    }

    public int getAchievementId() {
        return achievementId;
    }

    public void setAchievementId(int achievementId) {
        this.achievementId = achievementId;
    }

    public MonsterObj getAchievementMonster() {
        return achievementMonster;
    }

    public void setAchievementMonster(MonsterObj achievementMonster) {
        this.achievementMonster = achievementMonster;
    }

    public int getAchievementCurrentProgress() {
        return achievementCurrentProgress;
    }

    public void setAchievementCurrentProgress(int achievementCurrentProgress) {
        this.achievementCurrentProgress = achievementCurrentProgress;
    }

    public int getAchievementGoal() {
        return achievementGoal;
    }

    public void setAchievementGoal(int achievementGoal) {
        this.achievementGoal = achievementGoal;
    }

    public int getAchievementGoldReward() {
        return achievementGoldReward;
    }

    public void setAchievementGoldReward(int achievementGoldReward) {
        this.achievementGoldReward = achievementGoldReward;
    }
}
