package com.wb.simplerpggame.objects;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseObj extends SQLiteOpenHelper {

    public static final String PLAYER_TABLE = "PLAYER_TABLE";
    public static final String PLAYER_ID = "PLAYER_ID";
    public static final String PLAYER_LEVEL = "PLAYER_LEVEL";
    public static final String PLAYER_CURRENT_HP = "PLAYER_CURRENT_HP";
    public static final String PLAYER_MAX_HP = "PLAYER_MAX_HP";
    public static final String PLAYER_CURRENT_EXP = "PLAYER_CURRENT_EXP";
    public static final String PLAYER_MAX_EXP = "PLAYER_MAX_EXP";
    public static final String PLAYER_GOLD = "PLAYER_GOLD";
    public static final String PLAYER_ATTACK = "PLAYER_ATTACK";
    public static final String PLAYER_DEFENSE = "PLAYER_DEFENSE";
    public static final String PLAYER_MAP = "PLAYER_MAP";

    public static final String EQUIPMENT_TABLE = "EQUIPMENT_TABLE";
    public static final String EQUIPMENT_ID = "EQUIPMENT_ID";
    public static final String EQUIPMENT_NAME = "EQUIPMENT_NAME";
    public static final String EQUIPMENT_TYPE = "EQUIPMENT_TYPE";
    public static final String EQUIPMENT_TIER = "EQUIPMENT_TIER";
    public static final String EQUIPMENT_ATTACK = "EQUIPMENT_ATTACK";
    public static final String EQUIPMENT_DEFENSE = "EQUIPMENT_DEFENSE";
    public static final String EQUIPMENT_DROP_CHANCE = "EQUIPMENT_DROP_CHANCE";
    public static final String IS_EQUIPPED = "IS_EQUIPPED";
    public static final String EQUIPMENT_BUY_AMT = "EQUIPMENT_BUY_AMT";
    public static final String EQUIPMENT_SELL_AMT = "EQUIPMENT_SELL_AMT";

    public static final String INVENTORY_TABLE = "INVENTORY_TABLE";
    public static final String INVENTORY_ID = "INVENTORY_ID";
    public static final String INVENTORY_NAME = "INVENTORY_NAME";
    public static final String INVENTORY_TYPE = "INVENTORY_TYPE";
    public static final String INVENTORY_AMT = "INVENTORY_AMT";

    public static final String COUNTER_TABLE = "COUNTER_TABLE";
    public static final String COUNTER_ID = "COUNTER_ID";
    public static final String MTIMELEFTINMILIS = "MTIMELEFTINMILIS";
    public static final String MTIMERRUNNING = "MTIMERRUNNING";

    public static final String ACHIEVEMENTS_TABLE = "ACHIEVEMENTS_TABLE";
    public static final String ACHIEVEMENT_ID = "ACHIEVEMENT_ID";
    public static final String ACHIEVEMENT_CURRENT_PROGRESS = "ACHIEVEMENT_CURRENT_PROGRESS";
    public static final String ACHIEVEMENT_GOAL = "ACHIEVEMENT_GOAL";
    public static final String ACHIEVEMENT_GOLD_REWARD = "ACHIEVEMENT_GOLD_REWARD";
    public static final String ACHIEVEMENT_MONSTER = "ACHIEVEMENT_MONSTER";
    public static final String ACHIEVEMENT_CLAIMED = "ACHIEVEMENT_CLAIMED";
    private AchievementObj achievementObj = new AchievementObj();
    private MonsterObj monsterObj = new MonsterObj();

    public DatabaseObj(@Nullable Context context) {
        super(context, "app_db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement1 = "CREATE TABLE " + PLAYER_TABLE + " ("
                + PLAYER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PLAYER_LEVEL + " INT, "
                + PLAYER_CURRENT_HP + " INT, "
                + PLAYER_MAX_HP + " INT, "
                + PLAYER_CURRENT_EXP + " INT, "
                + PLAYER_MAX_EXP + " INT, "
                + PLAYER_GOLD + " INT, "
                + PLAYER_ATTACK + " INT, "
                + PLAYER_DEFENSE + " INT, "
                + PLAYER_MAP + " TEXT)";
        db.execSQL(createTableStatement1);

        String createTableStatement2 = "CREATE TABLE " + EQUIPMENT_TABLE + " ("
                + EQUIPMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EQUIPMENT_NAME + " TEXT, "
                + EQUIPMENT_TYPE + " TEXT, "
                + EQUIPMENT_TIER + " TEXT, "
                + EQUIPMENT_ATTACK + " INT, "
                + EQUIPMENT_DEFENSE + " INT, "
                + EQUIPMENT_DROP_CHANCE + " INT, "
                + IS_EQUIPPED + " INT, "
                + EQUIPMENT_BUY_AMT + " INT, "
                + EQUIPMENT_SELL_AMT + " INT)";
        db.execSQL(createTableStatement2);

        String createTableStatement3 = "CREATE TABLE " + INVENTORY_TABLE + " ("
                + INVENTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + INVENTORY_NAME + " TEXT, "
                + INVENTORY_TYPE + " TEXT, "
                + INVENTORY_AMT + " INT)";
        db.execSQL(createTableStatement3);

        String createTableStatement4 = "CREATE TABLE " + COUNTER_TABLE + " ("
                + COUNTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MTIMELEFTINMILIS + " INTEGER, "
                + MTIMERRUNNING + " INT)";
        db.execSQL(createTableStatement4);

        String createTableStatement5 = "CREATE TABLE " + ACHIEVEMENTS_TABLE + " ("
                + ACHIEVEMENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACHIEVEMENT_CURRENT_PROGRESS + " INTEGER, "
                + ACHIEVEMENT_GOAL + " INTEGER, "
                + ACHIEVEMENT_GOLD_REWARD + " INTEGER, "
                + ACHIEVEMENT_MONSTER + " TEXT, "
                + ACHIEVEMENT_CLAIMED + " INT)";
        db.execSQL(createTableStatement5);

        initAchievementsTable(db);
    }

    public void initAchievementsTable(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();

        ArrayList<AchievementObj> listOfDefaultAchievements = achievementObj.getDefaultAchievementsList();

        for (AchievementObj a : listOfDefaultAchievements) {
            cv.put(ACHIEVEMENT_CURRENT_PROGRESS, a.getAchievementCurrentProgress());
            cv.put(ACHIEVEMENT_GOAL, a.getAchievementGoal());
            cv.put(ACHIEVEMENT_GOLD_REWARD, a.getAchievementGoldReward());
            cv.put(ACHIEVEMENT_MONSTER, a.getAchievementMonster().getMonsterName());
            cv.put(ACHIEVEMENT_CLAIMED, 0);

            db.insert(ACHIEVEMENTS_TABLE, null, cv);
        }
    }

    public void claimAchievement(int achievementId) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("UPDATE " + ACHIEVEMENTS_TABLE + " SET ACHIEVEMENT_CLAIMED = 1 WHERE ACHIEVEMENT_ID = " + achievementId);
        db.close();
    }

    public ArrayList<AchievementObj> getAchievementsCompletedList() {
        ArrayList<AchievementObj> listOfCompletedAchievements = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACHIEVEMENTS_TABLE + " WHERE ACHIEVEMENT_CURRENT_PROGRESS = ACHIEVEMENT_GOAL";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int achievementsId = cursor.getInt(0);
                int achievementCurrentProgress = cursor.getInt(1);
                int achievementGoal = cursor.getInt(2);
                int achievementGoldReward = cursor.getInt(3);
                String achievementMonster = cursor.getString(4);
                boolean achievementClaimed = cursor.getInt(5) > 0;

                listOfCompletedAchievements.add(new AchievementObj(achievementsId, achievementCurrentProgress, achievementGoal, achievementGoldReward, monsterObj.getMonsterObjectByName(achievementMonster), achievementClaimed));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listOfCompletedAchievements;
    }

    public ArrayList<AchievementObj> getAchievementsList() {
        ArrayList<AchievementObj> listOfAchievements = new ArrayList<>();

        String queryString = "SELECT * FROM " + ACHIEVEMENTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int achievementsId = cursor.getInt(0);
                int achievementCurrentProgress = cursor.getInt(1);
                int achievementGoal = cursor.getInt(2);
                int achievementGoldReward = cursor.getInt(3);
                String achievementMonster = cursor.getString(4);
                boolean achievementClaimed = cursor.getInt(5) > 0;

                listOfAchievements.add(new AchievementObj(achievementsId, achievementCurrentProgress, achievementGoal, achievementGoldReward, monsterObj.getMonsterObjectByName(achievementMonster), achievementClaimed));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOfAchievements;
    }

    public String getAchievementsCount() {
        String achievementsCount;
        int totalCount = 0;
        int completedCount = 0;

        String queryString = "SELECT COUNT(*) FROM " + ACHIEVEMENTS_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            totalCount = cursor.getInt(0);
        }

        String queryString1 = "SELECT COUNT(*) FROM " + ACHIEVEMENTS_TABLE + " WHERE ACHIEVEMENT_CURRENT_PROGRESS = ACHIEVEMENT_GOAL";
        SQLiteDatabase db1 = this.getReadableDatabase();

        Cursor cursor1 = db1.rawQuery(queryString1, null);

        if (cursor1.moveToFirst()) {
            completedCount = cursor1.getInt(0);
        }

        achievementsCount = String.valueOf(completedCount) + " / " + String.valueOf(totalCount);

        cursor.close();
        db.close();
        cursor1.close();
        db1.close();

        return achievementsCount;
    }

    public int[][] updateAchievements(String monsterName) {
        String queryString = "SELECT * FROM " + ACHIEVEMENTS_TABLE + " WHERE ACHIEVEMENT_MONSTER = \'" + monsterName + "\' AND ACHIEVEMENT_CURRENT_PROGRESS != ACHIEVEMENT_GOAL";

        SQLiteDatabase db = this.getReadableDatabase();

        int isUpdated = 0;
        int achievementCurrentProgress = 0;
        int achievementGoal = 0;

        Cursor cursor = db.rawQuery(queryString, null);
        int rows = cursor.getCount();
        int[][] array = new int[rows][3];

        int counter = 0;
        if (cursor.moveToFirst()) {
            do {
                isUpdated = 1;
                achievementCurrentProgress = cursor.getInt(1);
                achievementGoal = cursor.getInt(2);

                array[counter][0] = isUpdated;
                array[counter][1] = achievementCurrentProgress + 1;
                array[counter][2] = achievementGoal;

                counter++;
            } while (cursor.moveToNext());

            db.execSQL("UPDATE " + ACHIEVEMENTS_TABLE + " SET ACHIEVEMENT_CURRENT_PROGRESS = ACHIEVEMENT_CURRENT_PROGRESS + 1 WHERE ACHIEVEMENT_MONSTER = \'" + monsterName + "\' AND ACHIEVEMENT_CURRENT_PROGRESS != ACHIEVEMENT_GOAL");
        }

        db.close();

        return array;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
    }

    public boolean checkIfExistsCounterTable() {
        String queryString = "SELECT * FROM " + COUNTER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        int counterId = -1;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            counterId = cursor.getInt(0);
        }

        cursor.close();
        db.close();

        if (counterId != -1) {
            return true;
        }

        return false;
    }

    public void initCounterTable(long mTimeLeftInMilis) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MTIMELEFTINMILIS, mTimeLeftInMilis);
        cv.put(MTIMERRUNNING, 0);

        db.insert(COUNTER_TABLE, null, cv);
        db.close();
    }

    public void updatemTimeLeftInMilis(long timeLeftInMilis) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(MTIMELEFTINMILIS, timeLeftInMilis);

        db.update(COUNTER_TABLE, cv, "COUNTER_ID = ?", new String[]{String.valueOf(1)});
        db.close();
    }

    public void updatemTimerRunning(boolean isRunning) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        int running = 0;
        if (isRunning) {
            running = 1;
        }

        cv.put(MTIMERRUNNING, running);

        db.update(COUNTER_TABLE, cv, "COUNTER_ID = ?", new String[]{String.valueOf(1)});
        db.close();
    }

    public long getmTimeLeftInMilis() {
        long mTimeLeftInMilis = 0;

        String queryString = "SELECT * FROM " + COUNTER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            mTimeLeftInMilis = cursor.getInt(1);
        }

        cursor.close();
        db.close();
        return mTimeLeftInMilis;
    }

    public boolean getmTimerRunning() {
        boolean mTimerRunning = false;

        String queryString = "SELECT * FROM " + COUNTER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            mTimerRunning = cursor.getInt(2) > 0;
        }

        cursor.close();
        db.close();
        return mTimerRunning;
    }

    public void deleteOneEquipmentNotEquipped(String equipmentName) {
        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE EQUIPMENT_NAME=" + "\'" + equipmentName + "\'" + " AND IS_EQUIPPED = 0";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        int equipmentId = 0;

        if (cursor.moveToFirst()) {
            equipmentId = cursor.getInt(0);
        }

        db.delete(EQUIPMENT_TABLE, "EQUIPMENT_ID = ?", new String[]{String.valueOf(equipmentId)});
        db.close();
    }

    public void deleteOneEquipmentEquipped(String equipmentName) {
        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE EQUIPMENT_NAME = " + "\'" + equipmentName + "\'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        int equipmentId = 0;

        if (cursor.moveToFirst()) {
            equipmentId = cursor.getInt(0);
        }

        db.delete(EQUIPMENT_TABLE, "EQUIPMENT_ID = ?", new String[]{String.valueOf(equipmentId)});
        db.close();
    }

    public void decreaseByOneInventory(int inventoryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("UPDATE " + INVENTORY_TABLE + " SET INVENTORY_AMT = INVENTORY_AMT-1 WHERE INVENTORY_ID = " + inventoryId);
        db.close();
    }

    public void deleteFromInventory(int inventoryId) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(INVENTORY_TABLE, "INVENTORY_ID = ?", new String[]{String.valueOf(inventoryId)});
        db.close();
    }

    public void addToInventory(Object object, int numberOfObjects) {
        if (object.getClass().getSimpleName().equals(HpPotObj.class.getSimpleName())) {
            HpPotObj hpPotObj = (HpPotObj) object;

            SQLiteDatabase db = this.getReadableDatabase();
            ContentValues cv = new ContentValues();

            cv.put(INVENTORY_NAME, hpPotObj.getHpPotName());
            cv.put(INVENTORY_TYPE, hpPotObj.getClass().getSimpleName());
            cv.put(INVENTORY_AMT, numberOfObjects);

            db.insert(INVENTORY_TABLE, null, cv);
            db.close();
        }
    }

    public InventoryObj getLastInsertedInventory() {
        InventoryObj inventoryObj = null;

        String queryString = "SELECT * FROM " + INVENTORY_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToLast()) {
            int inventoryId = cursor.getInt(0);
            String inventoryName = cursor.getString(1);
            String inventoryType = cursor.getString(2);
            int inventoryAmt = cursor.getInt(3);

            inventoryObj = new InventoryObj(inventoryId, inventoryName, inventoryType, inventoryAmt);
        }

        cursor.close();
        db.close();
        return inventoryObj;
    }

    public int getInventoryRow(int searchInventoryId) {
        int row = 0;

        String queryString = "SELECT * FROM " + INVENTORY_TABLE +
                " ORDER BY " + INVENTORY_TYPE + " ASC, " +
                "(CASE " + INVENTORY_NAME + " WHEN \'"
                + HpPotObj.MINOR_HEALING_POTION.getHpPotName() + "\' THEN 1 WHEN \'"
                + HpPotObj.LESSER_HEALING_POTION.getHpPotName() + "\' THEN 2 WHEN \'"
                + HpPotObj.GREATER_HEALING_POTION.getHpPotName() + "\' THEN 3 WHEN \'"
                + HpPotObj.HEALING_ELIXIR.getHpPotName() + "\' THEN 4 END) ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int inventoryId = cursor.getInt(0);
                if (inventoryId == searchInventoryId) {
                    return row;
                }
                row++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return row;
    }

    public void updateIncrementInventory(int objectId, int numberOfObjects) {
        SQLiteDatabase db = this.getReadableDatabase();

        db.execSQL("UPDATE " + INVENTORY_TABLE + " SET INVENTORY_AMT = INVENTORY_AMT + " + numberOfObjects + " WHERE INVENTORY_ID = " + objectId);
        db.close();
    }

    public int getAmtInInventory(int objectId) {
        int amt = 0;

        String queryString = "SELECT * FROM " + INVENTORY_TABLE + " WHERE INVENTORY_ID = " + objectId;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            amt = cursor.getInt(3);
        }

        cursor.close();
        db.close();
        return amt;
    }

    public int checkIfExistsInInventory(String objectName) {
        String queryString = "SELECT * FROM " + INVENTORY_TABLE + " WHERE " + INVENTORY_NAME + "=\'" + objectName + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        int objectId = -1;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            objectId = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return objectId;
    }

    public int checkIfExistsInEquipmentOwned(String equipmentName) {
        //String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_NAME + "=\'" + equipmentName + "\'";
        String queryString = "SELECT COUNT(EQUIPMENT_NAME) FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_NAME + "=\'" + equipmentName + "\'";
        SQLiteDatabase db = this.getReadableDatabase();
        int objectCount = -1;

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            objectCount = cursor.getInt(0);
        }

        cursor.close();
        db.close();
        return objectCount;
    }

    public ArrayList<InventoryObj> getInventoryList() {
        ArrayList<InventoryObj> listOfInventory = new ArrayList<>();

        String queryString = "SELECT * FROM " + INVENTORY_TABLE +
                " ORDER BY " + INVENTORY_TYPE + " ASC, " +
                "(CASE " + INVENTORY_NAME + " WHEN \'"
                + HpPotObj.MINOR_HEALING_POTION.getHpPotName() + "\' THEN 1 WHEN \'"
                + HpPotObj.LESSER_HEALING_POTION.getHpPotName() + "\' THEN 2 WHEN \'"
                + HpPotObj.GREATER_HEALING_POTION.getHpPotName() + "\' THEN 3 WHEN \'"
                + HpPotObj.HEALING_ELIXIR.getHpPotName() + "\' THEN 4 END) ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int inventoryId = cursor.getInt(0);
                String inventoryName = cursor.getString(1);
                String inventoryType = cursor.getString(2);
                int inventoryAmt = cursor.getInt(3);

                listOfInventory.add(new InventoryObj(inventoryId, inventoryName, inventoryType, inventoryAmt));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOfInventory;
    }

    public ArrayList<Object> getInventoryObjectList() {
        ArrayList<Object> listOfInventory = new ArrayList<>();

        String queryString = "SELECT * FROM " + INVENTORY_TABLE +
                " ORDER BY " + INVENTORY_TYPE + " ASC, " +
                "(CASE " + INVENTORY_NAME + " WHEN \'"
                + HpPotObj.MINOR_HEALING_POTION.getHpPotName() + "\' THEN 1 WHEN \'"
                + HpPotObj.LESSER_HEALING_POTION.getHpPotName() + "\' THEN 2 WHEN \'"
                + HpPotObj.GREATER_HEALING_POTION.getHpPotName() + "\' THEN 3 WHEN \'"
                + HpPotObj.HEALING_ELIXIR.getHpPotName() + "\' THEN 4 END) ASC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int inventoryId = cursor.getInt(0);
                String inventoryName = cursor.getString(1);
                String inventoryType = cursor.getString(2);
                int inventoryAmt = cursor.getInt(3);

                listOfInventory.add(new InventoryObj(inventoryId, inventoryName, inventoryType, inventoryAmt));

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOfInventory;
    }

    public ArrayList<EquipmentObj> getListOfEquipmentOwned() {
        ArrayList<EquipmentObj> listOfEquipment = new ArrayList<>();

//        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
//                " ORDER BY (CASE " + EQUIPMENT_TYPE + " WHEN \'"
//                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
//                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
//                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
//                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
//                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END) ASC, " + EQUIPMENT_NAME;

        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
                " ORDER BY CASE " + EQUIPMENT_TYPE + " WHEN \'"
                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END, "
                + "CASE " + EQUIPMENT_TIER + " WHEN \'"
                + EquipmentObj.COMMON + "\' THEN 1 WHEN \'"
                + EquipmentObj.UNCOMMON + "\' THEN 2 WHEN \'"
                + EquipmentObj.RARE + "\' THEN 3 WHEN \'"
                + EquipmentObj.LEGENDARY + "\' THEN 4 END";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                String equipmentName = cursor.getString(1);
                String equipmentType = cursor.getString(2);
                String equipmentTier = cursor.getString(3);
                int equipmentAttack = cursor.getInt(4);
                int equipmentDefense = cursor.getInt(5);
                int equipmentDropChance = cursor.getInt(6);
                boolean isEquipped = cursor.getInt(7) > 0;
                int equipmentBuyAmt = cursor.getInt(8);
                int equipmentSellAmt = cursor.getInt(9);
                int equipmentCount = cursor.getInt(10);
                int equipmentIsEquippedTotal = cursor.getInt(11);

                if (equipmentIsEquippedTotal >= 1) {
                    isEquipped = true;
                } else {
                    isEquipped = false;
                }

                listOfEquipment.add(new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentCount, equipmentBuyAmt, equipmentSellAmt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listOfEquipment;
    }

    public ArrayList<Object> getEquipmentObjectList() {
        ArrayList<Object> listOfEquipment = new ArrayList<>();
//        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
//                " ORDER BY (CASE " + EQUIPMENT_TYPE + " WHEN \'"
//                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
//                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
//                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
//                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
//                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END) ASC";

        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
                " ORDER BY CASE " + EQUIPMENT_TYPE + " WHEN \'"
                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END, "
                + "CASE " + EQUIPMENT_TIER + " WHEN \'"
                + EquipmentObj.COMMON + "\' THEN 1 WHEN \'"
                + EquipmentObj.UNCOMMON + "\' THEN 2 WHEN \'"
                + EquipmentObj.RARE + "\' THEN 3 WHEN \'"
                + EquipmentObj.LEGENDARY + "\' THEN 4 END";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                String equipmentName = cursor.getString(1);
                String equipmentType = cursor.getString(2);
                String equipmentTier = cursor.getString(3);
                int equipmentAttack = cursor.getInt(4);
                int equipmentDefense = cursor.getInt(5);
                int equipmentDropChance = cursor.getInt(6);
                boolean isEquipped = cursor.getInt(7) > 0;
                int equipmentBuyAmt = cursor.getInt(8);
                int equipmentSellAmt = cursor.getInt(9);
                int equipmentCount = cursor.getInt(10);
                int equipmentIsEquippedTotal = cursor.getInt(11);

                if (equipmentIsEquippedTotal >= 1) {
                    isEquipped = true;
                } else {
                    isEquipped = false;
                }

                listOfEquipment.add(new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentCount, equipmentBuyAmt, equipmentSellAmt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return listOfEquipment;
    }

    public ArrayList<Object> getInventoryAndEquipmentObjectList() {
        ArrayList<Object> listOfInventoryAndEquipment = new ArrayList<>();

//        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
//                " ORDER BY (CASE " + EQUIPMENT_TYPE + " WHEN \'"
//                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
//                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
//                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
//                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
//                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END) ASC";

        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
                " ORDER BY CASE " + EQUIPMENT_TYPE + " WHEN \'"
                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END, "
                + "CASE " + EQUIPMENT_TIER + " WHEN \'"
                + EquipmentObj.COMMON + "\' THEN 1 WHEN \'"
                + EquipmentObj.UNCOMMON + "\' THEN 2 WHEN \'"
                + EquipmentObj.RARE + "\' THEN 3 WHEN \'"
                + EquipmentObj.LEGENDARY + "\' THEN 4 END";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                String equipmentName = cursor.getString(1);
                String equipmentType = cursor.getString(2);
                String equipmentTier = cursor.getString(3);
                int equipmentAttack = cursor.getInt(4);
                int equipmentDefense = cursor.getInt(5);
                int equipmentDropChance = cursor.getInt(6);
                boolean isEquipped = cursor.getInt(7) > 0;
                int equipmentBuyAmt = cursor.getInt(8);
                int equipmentSellAmt = cursor.getInt(9);
                int equipmentCount = cursor.getInt(10);
                int equipmentIsEquippedTotal = cursor.getInt(11);

                if (equipmentIsEquippedTotal >= 1) {
                    isEquipped = true;
                } else {
                    isEquipped = false;
                }

                listOfInventoryAndEquipment.add(new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentCount, equipmentBuyAmt, equipmentSellAmt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        queryString = "SELECT * FROM " + INVENTORY_TABLE +
                " ORDER BY " + INVENTORY_TYPE + " ASC, " +
                "(CASE " + INVENTORY_NAME + " WHEN \'"
                + HpPotObj.MINOR_HEALING_POTION.getHpPotName() + "\' THEN 1 WHEN \'"
                + HpPotObj.LESSER_HEALING_POTION.getHpPotName() + "\' THEN 2 WHEN \'"
                + HpPotObj.GREATER_HEALING_POTION.getHpPotName() + "\' THEN 3 WHEN \'"
                + HpPotObj.HEALING_ELIXIR.getHpPotName() + "\' THEN 4 END) ASC";
        db = this.getReadableDatabase();

        cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int inventoryId = cursor.getInt(0);
                String inventoryName = cursor.getString(1);
                String inventoryType = cursor.getString(2);
                int inventoryAmt = cursor.getInt(3);

                listOfInventoryAndEquipment.add(new InventoryObj(inventoryId, inventoryName, inventoryType, inventoryAmt));

            } while (cursor.moveToNext());
        }

        return listOfInventoryAndEquipment;
    }

    public EquipmentObj getLastInsertedEquipment(int numberOfEquips) {
        EquipmentObj equipmentObj = null;

        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToLast()) {
            while (numberOfEquips > 1) {
                cursor.moveToPrevious();
                numberOfEquips--;
            }

            int equipmentID = cursor.getInt(0);
            String equipmentName = cursor.getString(1);
            String equipmentType = cursor.getString(2);
            String equipmentTier = cursor.getString(3);
            int equipmentAttack = cursor.getInt(4);
            int equipmentDefense = cursor.getInt(5);
            int equipmentDropChance = cursor.getInt(6);
            boolean isEquipped = cursor.getInt(7) > 0;
            int equipmentBuyAmt = cursor.getInt(8);
            int equipmentSellAmt = cursor.getInt(9);

            equipmentObj = new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentBuyAmt, equipmentSellAmt);
        }

        cursor.close();
        db.close();
        return equipmentObj;
    }

    public void addToEquipmentOwned(EquipmentObj equipmentObj) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(EQUIPMENT_NAME, equipmentObj.getEquipmentName());
        cv.put(EQUIPMENT_TYPE, equipmentObj.getEquipmentType());
        cv.put(EQUIPMENT_TIER, equipmentObj.getEquipmentTier());
        cv.put(EQUIPMENT_ATTACK, equipmentObj.getEquipmentAttack());
        cv.put(EQUIPMENT_DEFENSE, equipmentObj.getEquipmentDefense());
        cv.put(EQUIPMENT_DROP_CHANCE, equipmentObj.getEquipmentDropChance());
        cv.put(IS_EQUIPPED, 0);
        cv.put(EQUIPMENT_BUY_AMT, equipmentObj.getEquipmentBuyAmt());
        cv.put(EQUIPMENT_SELL_AMT, equipmentObj.getEquipmentSellAmt());

        db.insert(EQUIPMENT_TABLE, null, cv);
        db.close();
    }

    public int getEquipmentRowAllCategory(int searchEquipmentId) {
        int row = 0;

//        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
//                " ORDER BY (CASE " + EQUIPMENT_TYPE + " WHEN \'"
//                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
//                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
//                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
//                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
//                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END) ASC, " + EQUIPMENT_NAME;

        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
                " ORDER BY CASE " + EQUIPMENT_TYPE + " WHEN \'"
                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END, "
                + "CASE " + EQUIPMENT_TIER + " WHEN \'"
                + EquipmentObj.COMMON + "\' THEN 1 WHEN \'"
                + EquipmentObj.UNCOMMON + "\' THEN 2 WHEN \'"
                + EquipmentObj.RARE + "\' THEN 3 WHEN \'"
                + EquipmentObj.LEGENDARY + "\' THEN 4 END";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                if (equipmentID == searchEquipmentId) {
                    return row;
                }
                row++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return row;
    }

    public int getEquipmentRowShop(int searchEquipmentId) {
        int row = 0;

//        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
//                " ORDER BY (CASE " + EQUIPMENT_TYPE + " WHEN \'"
//                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
//                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
//                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
//                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
//                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END) ASC";

        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " GROUP BY EQUIPMENT_NAME" +
                " ORDER BY CASE " + EQUIPMENT_TYPE + " WHEN \'"
                + EquipmentObj.HEAD_GEAR + "\' THEN 1 WHEN \'"
                + EquipmentObj.ARMOR + "\' THEN 2 WHEN \'"
                + EquipmentObj.WEAPON + "\' THEN 3 WHEN \'"
                + EquipmentObj.SHIELD + "\' THEN 4 WHEN \'"
                + EquipmentObj.FOOT_WEAR + "\' THEN 5 END, "
                + "CASE " + EQUIPMENT_TIER + " WHEN \'"
                + EquipmentObj.COMMON + "\' THEN 1 WHEN \'"
                + EquipmentObj.UNCOMMON + "\' THEN 2 WHEN \'"
                + EquipmentObj.RARE + "\' THEN 3 WHEN \'"
                + EquipmentObj.LEGENDARY + "\' THEN 4 END";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                if (equipmentID == searchEquipmentId) {
                    return row;
                }
                row++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return row;
    }

    public int getEquipmentRowTypeCategory(int searchEquipmentId, String typeOfEquip) {
        int row = 0;

        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_TYPE + "=\'" + typeOfEquip + "\'" + " GROUP BY EQUIPMENT_NAME";

        //String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_TYPE + "=\'" + typeOfEquip + "\'";

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                if (equipmentID == searchEquipmentId) {
                    return row;
                }
                row++;
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return row;
    }

    public ArrayList<EquipmentObj> getEquipmentOfType(String typeOfEquip) {
        ArrayList<EquipmentObj> listOfEquipment = new ArrayList<>();

        String queryString = "SELECT *, COUNT(EQUIPMENT_NAME), SUM(CASE WHEN IS_EQUIPPED = 1 THEN 1 ELSE 0 END) FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_TYPE + "=\'" + typeOfEquip + "\'" + " GROUP BY EQUIPMENT_NAME";

        //String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE " + EQUIPMENT_TYPE + "=\'" + typeOfEquip + "\'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                String equipmentName = cursor.getString(1);
                String equipmentType = cursor.getString(2);
                String equipmentTier = cursor.getString(3);
                int equipmentAttack = cursor.getInt(4);
                int equipmentDefense = cursor.getInt(5);
                int equipmentDropChance = cursor.getInt(6);
                boolean isEquipped = cursor.getInt(7) > 0;
                int equipmentBuyAmt = cursor.getInt(8);
                int equipmentSellAmt = cursor.getInt(9);
                int equipmentCount = cursor.getInt(10);
                int equipmentIsEquippedTotal = cursor.getInt(11);

                if (equipmentIsEquippedTotal >= 1) {
                    isEquipped = true;
                } else {
                    isEquipped = false;
                }

                listOfEquipment.add(new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentCount, equipmentBuyAmt, equipmentSellAmt));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return listOfEquipment;
    }

    public PlayerObj getEquippedEquipment() {
        ArrayList<EquipmentObj> listOfEquipment = new ArrayList<>();

        String queryString = "SELECT * FROM " + EQUIPMENT_TABLE + " WHERE " + IS_EQUIPPED + "=1";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                int equipmentID = cursor.getInt(0);
                String equipmentName = cursor.getString(1);
                String equipmentType = cursor.getString(2);
                String equipmentTier = cursor.getString(3);
                int equipmentAttack = cursor.getInt(4);
                int equipmentDefense = cursor.getInt(5);
                int equipmentDropChance = cursor.getInt(6);
                boolean isEquipped = cursor.getInt(7) > 0;
                int equipmentBuyAmt = cursor.getInt(8);
                int equipmentSellAmt = cursor.getInt(9);

                listOfEquipment.add(new EquipmentObj(equipmentID, equipmentName, equipmentType, equipmentTier, equipmentAttack, equipmentDefense, equipmentDropChance, isEquipped, equipmentBuyAmt, equipmentSellAmt));
            } while (cursor.moveToNext());
        }

        PlayerObj playerObj = new PlayerObj(null, null, null, null, null);

        for (EquipmentObj e : listOfEquipment) {
            switch (e.getEquipmentType()) {
                case EquipmentObj.HEAD_GEAR:
                    playerObj.setPlayerHeadGear(e);
                    break;
                case EquipmentObj.ARMOR:
                    playerObj.setPlayerArmor(e);
                    break;
                case EquipmentObj.WEAPON:
                    playerObj.setPlayerWeapon(e);
                    break;
                case EquipmentObj.SHIELD:
                    playerObj.setPlayerShield(e);
                    break;
                case EquipmentObj.FOOT_WEAR:
                    playerObj.setPlayerFootwear(e);
                    break;
                default:
                    break;
            }
        }

        cursor.close();
        db.close();
        return playerObj;
    }

    public void equipEquipment(int equipmentID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IS_EQUIPPED, 1);

        db.update(EQUIPMENT_TABLE, cv, "EQUIPMENT_ID = ?", new String[]{String.valueOf(equipmentID)});
        db.close();
    }

    public void unequipEquipment(String equipmentName) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(IS_EQUIPPED, 0);

        db.update(EQUIPMENT_TABLE, cv, "EQUIPMENT_NAME = ?", new String[]{String.valueOf(equipmentName)});
        db.close();
    }

    //get the player details if exist in database
    public PlayerObj getPlayer() {
        PlayerObj playerObj = null;

        //getWritable -> inserting
        //getReadable -> selecting
        String queryString = "SELECT * FROM " + PLAYER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        //cursor is a result set from the SQL statement
        Cursor cursor = db.rawQuery(queryString, null);

        //move to the first result and check if it exists
        if (cursor.moveToFirst()) {
            //int userID = cursor.getInt(0);
            int playerID = cursor.getInt(0);
            int playerLevel = cursor.getInt(1);

            int playerCurrentHp = cursor.getInt(2);
            int playerMaxHp = cursor.getInt(3);
            int playerCurrentExp = cursor.getInt(4);
            int playerMaxExp = cursor.getInt(5);

            int playerGold = cursor.getInt(6);
            int playerAttack = cursor.getInt(7);
            int playerDefense = cursor.getInt(8);

            String playerMap = cursor.getString(9);

            playerObj = new PlayerObj(playerID, playerLevel, playerCurrentHp, playerMaxHp, playerCurrentExp, playerMaxExp, playerGold, playerAttack, playerDefense, playerMap);
        }

        cursor.close();
        db.close();
        return playerObj;
    }

    public int[] getPlayerHp() {
        int playerHp = 0;
        int playerMaxHp = 0;
        int playerID = 0;

        String queryString = "SELECT * FROM " + PLAYER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            playerID = cursor.getInt(0);
            playerHp = cursor.getInt(2);
            playerMaxHp = cursor.getInt(3);
        }

        cursor.close();
        db.close();
        return new int[]{playerID, playerHp, playerMaxHp};
    }

    public int[] getPlayerGold() {
        int playerID = 0;
        int playerGold = 0;

        String queryString = "SELECT * FROM " + PLAYER_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            playerID = cursor.getInt(0);
            playerGold = cursor.getInt(6);
        }

        cursor.close();
        db.close();
        return new int[]{playerID, playerGold};
    }

    //add new level 1 player into database
    public void addNewPlayer() {
        //content values stores data in pairs cv.put("name", value)
        //cv.getString("name")
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        PlayerObj playerObj = new PlayerObj(0, 1, 0, 0, 0, 0, 0, 1, 0, "FOREST");
        playerObj.setPlayerCurrentHp((playerObj.getPlayerLevel()*5)+5);
        playerObj.setPlayerMaxHp((playerObj.getPlayerLevel()*5)+5);
        playerObj.setPlayerMaxExp(playerObj.getPlayerLevel()*100);

        //insert values into database
        cv.put(PLAYER_LEVEL, playerObj.getPlayerLevel());
        cv.put(PLAYER_CURRENT_HP, playerObj.getPlayerCurrentHp());
        cv.put(PLAYER_MAX_HP, playerObj.getPlayerMaxHp());
        cv.put(PLAYER_CURRENT_EXP, playerObj.getPlayerCurrentExp());
        cv.put(PLAYER_MAX_EXP, playerObj.getPlayerMaxExp());

        cv.put(PLAYER_GOLD, playerObj.getPlayerGold());
        cv.put(PLAYER_ATTACK, playerObj.getPlayerAttack());
        cv.put(PLAYER_DEFENSE, playerObj.getPlayerDefense());
        cv.put(PLAYER_MAP, playerObj.getPlayerMap());

        //the return variable is a long so meaning success
        //-1 is fail
        //insert into player table the values
        long insert = db.insert(PLAYER_TABLE, null, cv);
        db.close();

//        if (insert == -1) {
//            return false;
//        } else {
//            return true;
//        }
    }

    //update player table
    public void updatePlayerCol(String column, String columnValue, int playerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column, columnValue);

        db.update(PLAYER_TABLE, cv, "PLAYER_ID = ?", new String[]{String.valueOf(playerID)});
        db.close();
    }

    public void updatePlayerAttackDefense(int attack, int defense, int playerID) {
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(PLAYER_ATTACK, attack);
        cv.put(PLAYER_DEFENSE, defense);

        db.update(PLAYER_TABLE, cv, "PLAYER_ID = ?", new String[]{String.valueOf(playerID)});
        db.close();
    }
}
