package com.wb.simplerpggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.wb.simplerpggame.adaptors.AchievementsAdaptor;
import com.wb.simplerpggame.objects.AchievementObj;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.PlayerObj;

import java.util.ArrayList;

public class AchievementsActivity extends AppCompatActivity {

    private ImageView backBtn, homeBtn;
    private TextView achievementsCount;
    private Button completedBtn;
    private RecyclerView achievementsRecyclerView;
    private AchievementsAdaptor achievementsAdaptor;
    private ArrayList<AchievementObj> achievementsArray;
    private DatabaseObj databaseObj;
    private PlayerObj playerObj;
    private ConstraintLayout noInventoryText;

    @Override
    public void onResume() {
        super.onResume();
        //update playerObj
        initDatabase();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.UPDATE_ACHIEVEMENTS);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //update playerObj
        initDatabase();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.UPDATE_ACHIEVEMENTS);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(intentReceiver);
        } catch (Exception e) {
        }

        super.onDestroy();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        initViews();
        initDatabase();
        initRecycler();
        updateAchievementsCount();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementsActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AchievementsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        completedBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    boolean isPressed = !completedBtn.isPressed();
                    completedBtn.setPressed(isPressed);

                    achievementsArray.clear();
                    if (isPressed) {
                        achievementsArray.addAll(databaseObj.getAchievementsCompletedList());
                    } else {
                        achievementsArray.addAll(databaseObj.getAchievementsList());
                    }
                    achievementsAdaptor.notifyDataSetChanged();
                    showRecyclerOrText();

                    return true;
                }

                return true;
            }
        });
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.UPDATE_ACHIEVEMENTS)) {
                achievementsArray.clear();
                if (completedBtn.isPressed()) {
                    achievementsArray.addAll(databaseObj.getAchievementsCompletedList());
                } else {
                    achievementsArray.addAll(databaseObj.getAchievementsList());
                }
                achievementsAdaptor.notifyDataSetChanged();
                showRecyclerOrText();
            }
        }
    };

    private void showRecyclerOrText() {
        AchievementsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (achievementsArray.size() > 0) {
                    achievementsRecyclerView.setVisibility(View.VISIBLE);
                    noInventoryText.setVisibility(View.INVISIBLE);
                } else {
                    achievementsRecyclerView.setVisibility(View.INVISIBLE);
                    noInventoryText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void updateAchievementsCount() {
        AchievementsActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                achievementsCount.setText(databaseObj.getAchievementsCount());
            }
        });
    }

    private void initRecycler() {
        achievementsAdaptor = new AchievementsAdaptor(AchievementsActivity.this);
        achievementsArray = databaseObj.getAchievementsList();
        achievementsAdaptor.setAchievementsArray(achievementsArray);
        showRecyclerOrText();

        achievementsRecyclerView.setAdapter(achievementsAdaptor);
        achievementsRecyclerView.setLayoutManager(new LinearLayoutManager(AchievementsActivity.this));
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(AchievementsActivity.this);

        if (databaseObj.getPlayer() == null) {
            //add new player into database
            databaseObj.addNewPlayer();
        }

        //get player object from stored database
        playerObj = databaseObj.getPlayer();
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);
        completedBtn = findViewById(R.id.completedBtn);

        noInventoryText = findViewById(R.id.noInventoryText);
        achievementsCount = findViewById(R.id.achievementsCount);

        achievementsRecyclerView = findViewById(R.id.achievementsRecyclerView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(AchievementsActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}