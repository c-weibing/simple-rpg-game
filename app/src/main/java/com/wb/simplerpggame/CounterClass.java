package com.wb.simplerpggame;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import com.wb.simplerpggame.objects.DatabaseObj;

import java.util.Locale;

public class CounterClass extends CountDownTimer {

    public static final long START_TIME_IN_MILIS = 5000;
    public static final long COUNT_DOWN_INTERVAL = 1000;
    public static final String ONTICK = "ONTICK";
    public static final String ONFINISH = "ONFINISH";
    public static final String START = "START";

    private static CounterClass instance;
    private static String currentTimeString;
    private static long mTimeLeftInMilis = START_TIME_IN_MILIS;
    private static boolean mTimerRunning;
    private static DatabaseObj databaseObj;
    private Context context;

    public CounterClass(Context context, long mTimeLeftInMilis, long countDownInterval) {
        super(mTimeLeftInMilis, countDownInterval);
        this.context = context;
    }

    public static CounterClass getInstance(Context context) {
        if (null != instance) {
            return instance;
        } else {
            databaseObj = new DatabaseObj(context);
            if (databaseObj.checkIfExistsCounterTable()) {
                mTimeLeftInMilis = databaseObj.getmTimeLeftInMilis();
                mTimerRunning = databaseObj.getmTimerRunning();
            } else {
                databaseObj.initCounterTable(mTimeLeftInMilis);
            }

            instance = new CounterClass(context, mTimeLeftInMilis, COUNT_DOWN_INTERVAL);
            return instance;
        }
    }

    public static String getCurrentTimeString() {
        return currentTimeString;
    }

    @Override
    public void onTick(long milisUntilFinished) {
        mTimeLeftInMilis = milisUntilFinished;
        databaseObj.updatemTimeLeftInMilis(mTimeLeftInMilis);

        int minutes = (int) mTimeLeftInMilis / 1000 / 60;
        int seconds = (int) mTimeLeftInMilis / 1000 % 60;

        currentTimeString = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

        Intent i = new Intent();
        i.setAction(ONTICK);
        context.sendBroadcast(i);
    }

    @Override
    public void onFinish() {
        mTimeLeftInMilis = START_TIME_IN_MILIS;
        databaseObj.updatemTimeLeftInMilis(mTimeLeftInMilis);

        mTimerRunning = false;
        databaseObj.updatemTimerRunning(mTimerRunning);

        Intent i = new Intent();
        i.setAction(ONFINISH);
        context.sendBroadcast(i);
    }

    public void stopTimer() {
        this.cancel();
        mTimerRunning = false;
        databaseObj.updatemTimerRunning(mTimerRunning);
    }

    public void resumeTimer() {
        instance.cancel();
        instance = new CounterClass(context, mTimeLeftInMilis, COUNT_DOWN_INTERVAL);
        instance.start();

        Intent i = new Intent();
        i.setAction(START);
        context.sendBroadcast(i);

        mTimerRunning = true;
        databaseObj.updatemTimerRunning(mTimerRunning);
    }

    public void stopAndResetTimer() {
        this.cancel();

        mTimeLeftInMilis = START_TIME_IN_MILIS;
        databaseObj.updatemTimeLeftInMilis(mTimeLeftInMilis);

        mTimerRunning = false;
        databaseObj.updatemTimerRunning(mTimerRunning);
    }

    public boolean ismTimerRunning() {
        return mTimerRunning;
    }

    public void setmTimerRunning(boolean isRunning) {
        mTimerRunning = isRunning;
        databaseObj.updatemTimerRunning(mTimerRunning);
    }

    @Override
    public String toString() {
        return "CounterClass{" +
                "mTimeLeftInMilis=" + mTimeLeftInMilis +
                ", mTimerRunning=" + mTimerRunning +
                ", context=" + context +
                '}';
    }
}
