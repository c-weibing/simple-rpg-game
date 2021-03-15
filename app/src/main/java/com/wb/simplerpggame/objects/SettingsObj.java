package com.wb.simplerpggame.objects;

public class SettingsObj {
    private String settingsTitle;
    private String settingsDescription;
    private String settingsType;
    private int settingsDefaultColor;
    private boolean settingsEnabled;
    private String settingsName;

    public SettingsObj(String settingsTitle, String settingsDescription, String settingsType, int settingsDefaultColor, String settingsName) {
        this.settingsTitle = settingsTitle;
        this.settingsDescription = settingsDescription;
        this.settingsType = settingsType;
        this.settingsDefaultColor = settingsDefaultColor;
        this.settingsName = settingsName;
    }

    public SettingsObj(String settingsTitle, String settingsDescription, String settingsType, boolean settingsEnabled, String settingsName) {
        this.settingsTitle = settingsTitle;
        this.settingsDescription = settingsDescription;
        this.settingsType = settingsType;
        this.settingsEnabled = settingsEnabled;
        this.settingsName = settingsName;
    }

    @Override
    public String toString() {
        return "SettingsObj{" +
                "settingsTitle='" + settingsTitle + '\'' +
                ", settingsDescription='" + settingsDescription + '\'' +
                ", settingsType='" + settingsType + '\'' +
                ", settingsDefaultColor=" + settingsDefaultColor +
                ", settingsEnabled=" + settingsEnabled +
                ", settingsName='" + settingsName + '\'' +
                '}';
    }

    public String getSettingsName() {
        return settingsName;
    }

    public void setSettingsName(String settingsName) {
        this.settingsName = settingsName;
    }

    public boolean isSettingsEnabled() {
        return settingsEnabled;
    }

    public void setSettingsEnabled(boolean settingsEnabled) {
        this.settingsEnabled = settingsEnabled;
    }

    public int getSettingsDefaultColor() {
        return settingsDefaultColor;
    }

    public void setSettingsDefaultColor(int settingsDefaultColor) {
        this.settingsDefaultColor = settingsDefaultColor;
    }

    public String getSettingsTitle() {
        return settingsTitle;
    }

    public void setSettingsTitle(String settingsTitle) {
        this.settingsTitle = settingsTitle;
    }

    public String getSettingsDescription() {
        return settingsDescription;
    }

    public void setSettingsDescription(String settingsDescription) {
        this.settingsDescription = settingsDescription;
    }

    public String getSettingsType() {
        return settingsType;
    }

    public void setSettingsType(String settingsType) {
        this.settingsType = settingsType;
    }
}
