package com.wb.simplerpggame.objects;

import android.text.SpannableStringBuilder;

public class EventObj {
    private String eventTime;
    private String eventType;
    private SpannableStringBuilder eventInfo;
    private SpannableStringBuilder eventExtraInfo;
    private boolean expanded = false;
    private String eventSettingsName;

    public EventObj(String eventTime, String eventType, SpannableStringBuilder eventInfo, SpannableStringBuilder eventExtraInfo, String eventSettingsName) {
        this.eventTime = eventTime;
        this.eventType = eventType;
        this.eventInfo = eventInfo;
        this.eventExtraInfo = eventExtraInfo;
        this.eventSettingsName = eventSettingsName;
    }

    @Override
    public String toString() {
        return "EventObj{" +
                "eventTime='" + eventTime + '\'' +
                ", eventType='" + eventType + '\'' +
                ", eventInfo=" + eventInfo +
                ", eventExtraInfo=" + eventExtraInfo +
                ", expanded=" + expanded +
                ", eventSettingsName='" + eventSettingsName + '\'' +
                '}';
    }

    public String getEventSettingsName() {
        return eventSettingsName;
    }

    public void setEventSettingsName(String eventSettingsName) {
        this.eventSettingsName = eventSettingsName;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public SpannableStringBuilder getEventInfo() {
        return eventInfo;
    }

    public void setEventInfo(SpannableStringBuilder eventInfo) {
        this.eventInfo = eventInfo;
    }

    public SpannableStringBuilder getEventExtraInfo() {
        return eventExtraInfo;
    }

    public void setEventExtraInfo(SpannableStringBuilder eventExtraInfo) {
        this.eventExtraInfo = eventExtraInfo;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }
}
