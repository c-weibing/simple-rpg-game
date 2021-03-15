package com.wb.simplerpggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wb.simplerpggame.adaptors.SettingsEventAdaptor;
import com.wb.simplerpggame.objects.SettingsObj;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

    public static final String SETTINGS_SWITCH = "SETTINGS_SWITCH";
    public static final String SETTINGS_COLOR = "SETTINGS_COLOR";
    public static final String SETTINGS_UPDATE_COLOR = "SETTINGS_UPDATE_COLOR";

    public static final String SETTINGS_EVENTS_GENERAL = "SETTINGS_EVENTS_GENERAL";
    public static final String SETTINGS_EVENTS_ITEM_DROPS = "SETTINGS_EVENTS_ITEM_DROPS";
    public static final String SETTINGS_EVENTS_BATTLE_WON = "SETTINGS_EVENTS_BATTLE_WON";
    public static final String SETTINGS_EVENTS_BATTLE_LOST = "SETTINGS_EVENTS_BATTLE_LOST";
    public static final String SETTINGS_ACHIEVEMENTS_SWITCH = "SETTINGS_ACHIEVEMENTS_SWITCH";
    public static final String SETTINGS_ACHIEVEMENTS_COLOR = "SETTINGS_ACHIEVEMENTS_COLOR";

    private ImageView homeBtn, backBtn;
    private ConstraintLayout resetDataLayout, resetSettingsLayout;
    private SettingsEventAdaptor settingsEventAdaptor;
    private ArrayList<SettingsObj> settingsEventArray = new ArrayList<>();
    private RecyclerView settingsEventRecycler;

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SETTINGS_UPDATE_COLOR);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(SETTINGS_UPDATE_COLOR);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(intentReceiver);
        } catch (Exception ignored) {
        }

        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initViews();
        initRecycler();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        resetSettingsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (SettingsObj s : settingsEventArray) {
                    if (s.getSettingsType().equals(SETTINGS_COLOR) && s.getSettingsDefaultColor() != 1) {
                        s.setSettingsDefaultColor(1);
                    } else if (s.getSettingsName().equals(SETTINGS_ACHIEVEMENTS_SWITCH) && !s.isSettingsEnabled()) {
                        s.setSettingsEnabled(true);
                    }

                    Utils.getInstance(SettingsActivity.this).clearPreferences();
                    settingsEventAdaptor.notifyDataSetChanged();

                    Intent i = new Intent();
                    i.setAction(SettingsEventAdaptor.UPDATE_MAIN_EVENTS);
                    SettingsActivity.this.sendBroadcast(i);
                }
            }
        });

        resetDataLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(view.getContext());
                View exitTitleLayout = inflater.inflate(R.layout.title_layout_left_align, null);

                TextView customTitle = exitTitleLayout.findViewById(R.id.title);
                customTitle.setText("Reset Data");

                final AlertDialog exitDialog = new MaterialAlertDialogBuilder(SettingsActivity.this, R.style.MaterialAlertDialog_Rounded)
                        .setCustomTitle(exitTitleLayout)
                        .setMessage("You won't be able to retrieve back your current progress. Are you sure you wish to proceed?")
                        .create();

                exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Utils.getInstance(SettingsActivity.this).clearPreferences();
                        SettingsActivity.this.deleteDatabase("app_db");

                        Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                        SettingsActivity.this.startActivity(intent);
                        SettingsActivity.this.finishAffinity();
                    }
                });

                exitDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        exitDialog.dismiss();
                    }
                });

                exitDialog.show();

                exitTitleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });

                exitDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);

                TextView messageText = (TextView) exitDialog.getWindow().findViewById(android.R.id.message);
                messageText.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));

                Button btnPositive = exitDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));

                Button btnNegative = exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));

                int width = (int) (SettingsActivity.this.getResources().getDisplayMetrics().widthPixels * 0.85);
                //int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.25);

                exitDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        });
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        }
    };

    private void initRecycler() {
        settingsEventAdaptor = new SettingsEventAdaptor(SettingsActivity.this);
        settingsEventArray.add(new SettingsObj("General", "", SETTINGS_COLOR, 1, SETTINGS_EVENTS_GENERAL));
        settingsEventArray.add(new SettingsObj("Item Drops", "", SETTINGS_COLOR, 1, SETTINGS_EVENTS_ITEM_DROPS));
        settingsEventArray.add(new SettingsObj("Battle Won", "", SETTINGS_COLOR, 1, SETTINGS_EVENTS_BATTLE_WON));
        settingsEventArray.add(new SettingsObj("Battle Lost", "", SETTINGS_COLOR, 1, SETTINGS_EVENTS_BATTLE_LOST));
        settingsEventArray.add(new SettingsObj("Achievements", "", SETTINGS_COLOR, 1, SETTINGS_ACHIEVEMENTS_COLOR));
        settingsEventArray.add(new SettingsObj("Achievements", "Show achievement progress", SETTINGS_SWITCH, true, SETTINGS_ACHIEVEMENTS_SWITCH));

        int count = 0;
        for (SettingsObj s : settingsEventArray) {
            if (s.getSettingsType().equals(SETTINGS_COLOR)) {
                if (s.getSettingsDefaultColor() != Utils.getInstance(SettingsActivity.this).getColor(s.getSettingsName())) {
                    s.setSettingsDefaultColor(Utils.getInstance(SettingsActivity.this).getColor(s.getSettingsName()));
                }
            } else {
                if (!(s.isSettingsEnabled() && Utils.getInstance(SettingsActivity.this).getSwitch(s.getSettingsName()))) {
                    s.setSettingsEnabled(Utils.getInstance(SettingsActivity.this).getSwitch(s.getSettingsName()));
                }
            }
            count++;
        }

        settingsEventAdaptor.setSettingsEventArray(settingsEventArray);

        settingsEventRecycler.setAdapter(settingsEventAdaptor);
        //settingsEventRecycler.suppressLayout(true);
        settingsEventRecycler.setLayoutManager(new LinearLayoutManager(SettingsActivity.this));
        settingsEventRecycler.setNestedScrollingEnabled(false);
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);
        resetDataLayout = findViewById(R.id.resetDataLayout);
        resetSettingsLayout = findViewById(R.id.resetSettingsLayout);

        settingsEventRecycler = findViewById(R.id.settingsEventRecycler);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(SettingsActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}