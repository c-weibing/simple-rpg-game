package com.wb.simplerpggame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wb.simplerpggame.objects.PlayerObj;

import java.lang.reflect.Type;

public class Utils {

    public static final String PLAYER = "PLAYER";
    public static final String STARTDISABLED = "STARTDISABLED";
    public static final String STARTENABLED = "STARTENABLED";

    private static Utils instance;
    private SharedPreferences sharedPreferences;
    private boolean startBtnEnabled = true;
    private Context context;

    private Utils(Context context) {
        sharedPreferences = context.getSharedPreferences("app_db", Context.MODE_PRIVATE);
        this.context = context;
    }

    public static Utils getInstance(Context context) {
        if (null != instance) {
            return instance;
        } else {
            instance = new Utils(context);
            return instance;
        }
    }

    public void setStartBtn(boolean isEnabled) {
        if (isEnabled) {
            startBtnEnabled = true;

            Intent i = new Intent();
            i.setAction(STARTENABLED);
            context.sendBroadcast(i);
        } else {
            startBtnEnabled = false;

            Intent i = new Intent();
            i.setAction(STARTDISABLED);
            context.sendBroadcast(i);
        }
    }

    public boolean isStartBtnEnabled() {
        return startBtnEnabled;
    }

    public PlayerObj getPlayer() {
        Gson gson = new Gson();
        Type type = new TypeToken<PlayerObj>() {
        }.getType();
        PlayerObj playerObj = gson.fromJson(sharedPreferences.getString(PLAYER, null), type);
        return playerObj;
    }

    public void setPlayer(PlayerObj playerObj) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        editor.putString(PLAYER, gson.toJson(playerObj));
        editor.apply();
    }

    public int getColor(String settingsName) {
        return sharedPreferences.getInt(settingsName, 1);
    }

    public void setColor(String settingsName, int colorPicked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt(settingsName, colorPicked);
        editor.apply();
    }

    public boolean getSwitch(String settingsName) {
        return sharedPreferences.getBoolean(settingsName, true);
    }

    public void setSwitch(String settingsName, boolean checked) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(settingsName, checked);
        editor.apply();
    }

    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }
}

