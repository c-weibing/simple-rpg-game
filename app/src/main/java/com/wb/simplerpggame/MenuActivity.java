package com.wb.simplerpggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wb.simplerpggame.adaptors.InventoryAdaptor;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.PlayerObj;

public class MenuActivity extends AppCompatActivity {

    private TextView playerLevelTextView, playerCurrentHpTextView, playerMaxHpTextView, playerCurrentExpTextView, playerMaxExpTextView, playerAttackTextView, playerDefenseTextView, playerGoldTextView, playerHpTimerTextView;
    private ImageView greySword;
    private ProgressBar hpBar, expBar;
    private Button backBtn, menuEquipmentBtn, menuInventoryBtn, menuShopBtn, menuAchievementsBtn, menuAboutBtn, menuSettingsBtn;
    private DatabaseObj databaseObj;
    private PlayerObj playerObj;
    private SharedPreferences sharedPreferences;
    private AlertDialog mapInfoDialog;

    @Override
    public void onResume() {
        super.onResume();
        //update playerObj
        initDatabase();
        displayInfo();
        updateCountDownText();

        IntentFilter filter = new IntentFilter();
        filter.addAction(CounterClass.ONTICK);
        filter.addAction(CounterClass.ONFINISH);
        filter.addAction(CounterClass.START);
        filter.addAction(Utils.STARTENABLED);
        filter.addAction(Utils.STARTDISABLED);
        filter.addAction(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //update playerObj
        initDatabase();
        displayInfo();
        updateCountDownText();

        IntentFilter filter = new IntentFilter();
        filter.addAction(CounterClass.ONTICK);
        filter.addAction(CounterClass.ONFINISH);
        filter.addAction(CounterClass.START);
        filter.addAction(Utils.STARTENABLED);
        filter.addAction(Utils.STARTDISABLED);
        filter.addAction(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY);

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        if (Utils.getInstance(this).getSharedPreferences() != null) {
            sharedPreferences = Utils.getInstance(this).getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        }

        initViews();
        initDatabase();
        displayInfo();

        if (Utils.getInstance(MenuActivity.this).isStartBtnEnabled()) {
            greySword.setVisibility(View.INVISIBLE);
        } else {
            greySword.setVisibility(View.VISIBLE);
        }

        if (Utils.getInstance(MenuActivity.this).isStartBtnEnabled() || CounterClass.getInstance(MenuActivity.this).ismTimerRunning()) {
            startHpTimer();
        } else if (!Utils.getInstance(MenuActivity.this).isStartBtnEnabled() && !CounterClass.getInstance(MenuActivity.this).ismTimerRunning() && playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
            updateCountDownText();
            playerHpTimerTextView.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
//                onBackPressed();
//                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        menuEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, EquipmentActivity.class);
                startActivity(intent);
            }
        });

        menuInventoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, InventoryActivity.class);
                startActivity(intent);
            }
        });

        menuShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        menuAchievementsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, AchievementsActivity.class);
                startActivity(intent);
            }
        });

        menuSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        menuAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MenuActivity.this);

                View infoTitleLayout = inflater.inflate(R.layout.title_layout_about, null);

                mapInfoDialog = new MaterialAlertDialogBuilder(MenuActivity.this, R.style.MaterialAlertDialog_Rounded)
                        .setCustomTitle(infoTitleLayout)
                        .create();
                mapInfoDialog.show();

                TextView recoLevelView = mapInfoDialog.findViewById(android.R.id.message);
                recoLevelView.setGravity(Gravity.CENTER);
                recoLevelView.setTextSize((int) getResources().getDimension(R.dimen._8ssp));

                mapInfoDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);

                mapInfoDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                infoTitleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Utils.PLAYER)) {
                playerObj = Utils.getInstance(MenuActivity.this).getPlayer();
                displayInfo();
                startHpTimer();
            }
        }
    };

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CounterClass.ONTICK)) {
                updateCountDownText();
            } else if (intent.getAction().equals(CounterClass.START)) {
                updateCountDownText();
                playerHpTimerTextView.setVisibility(View.VISIBLE);
            } else if (intent.getAction().equals(Utils.STARTENABLED)) {
                greySword.setVisibility(View.INVISIBLE);
            } else if (intent.getAction().equals(Utils.STARTDISABLED)) {
                greySword.setVisibility(View.VISIBLE);
            } else if (intent.getAction().equals(CounterClass.ONFINISH)) {
                int increment = (int) (playerObj.getPlayerMaxHp()*MainActivity.HP_PERCENTAGE_INCREASE);
                int resultHp = playerObj.getPlayerCurrentHp() + (Math.max(increment, 1));

                if (resultHp > playerObj.getPlayerMaxHp()) {
                    playerObj.setPlayerCurrentHp(playerObj.getPlayerMaxHp());
                } else {
                    playerObj.setPlayerCurrentHp(resultHp);
                }

                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_CURRENT_HP, String.valueOf(playerObj.getPlayerCurrentHp()), playerObj.getPlayerId());
                displayInfo();

                if (playerObj.getPlayerCurrentHp() == playerObj.getPlayerMaxHp()) {
                    MenuActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerHpTimerTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    startHpTimer();
                }
            } else if (intent.getAction().equals(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY)) {
                int updatedHpValue = intent.getIntExtra(InventoryAdaptor.ViewHolder.UPDATED_HP_VALUE, 0);
                playerObj.setPlayerCurrentHp(updatedHpValue);

                if (updatedHpValue == playerObj.getPlayerMaxHp()) {
                    CounterClass.getInstance(MenuActivity.this).stopAndResetTimer();
                    playerHpTimerTextView.setVisibility(View.INVISIBLE);
                }

                displayInfo();
            }
        }
    };

    private void updateCountDownText() {
        MenuActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerHpTimerTextView.setText(String.valueOf(CounterClass.getCurrentTimeString()));
            }
        });
    }

    private void startHpTimer() {
        MenuActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
                    playerHpTimerTextView.setVisibility(View.VISIBLE);

                    CounterClass.getInstance(MenuActivity.this).resumeTimer();
                    CounterClass.getInstance(MenuActivity.this).setmTimerRunning(true);
                }
            }
        });
    }

    private void displayInfo() {
        MenuActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerLevelTextView.setText(String.valueOf(playerObj.getPlayerLevel()));

                playerCurrentHpTextView.setText(String.valueOf(playerObj.getPlayerCurrentHp()));
                playerMaxHpTextView.setText(String.valueOf(playerObj.getPlayerMaxHp()));

                playerCurrentExpTextView.setText(String.valueOf(playerObj.getPlayerCurrentExp()));
                playerMaxExpTextView.setText(String.valueOf(playerObj.getPlayerMaxExp()));

                playerGoldTextView.setText(String.valueOf(playerObj.getPlayerGold()));
                playerAttackTextView.setText(String.valueOf(playerObj.getPlayerAttack()));
                playerDefenseTextView.setText(String.valueOf(playerObj.getPlayerDefense()));

                hpBar.setProgress((int) (((float) playerObj.getPlayerCurrentHp() / (float) playerObj.getPlayerMaxHp()) * 100));
                expBar.setProgress((int) (((float) playerObj.getPlayerCurrentExp() / (float) playerObj.getPlayerMaxExp()) * 100));
            }
        });
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(MenuActivity.this);

        if (databaseObj.getPlayer() == null) {
            //add new player into database
            databaseObj.addNewPlayer();
        }

        //get player object from stored database
        playerObj = databaseObj.getPlayer();
    }

    private void initViews() {
        playerHpTimerTextView = findViewById(R.id.playerHpTimer);
        playerLevelTextView = findViewById(R.id.playerLevelInnerCircle);

        playerCurrentHpTextView = findViewById(R.id.playerCurrentHpTextView);
        playerMaxHpTextView = findViewById(R.id.playerMaxHpTextView);

        playerCurrentExpTextView = findViewById(R.id.playerCurrentExpTextView);
        playerMaxExpTextView = findViewById(R.id.playerMaxExpTextView);

        playerGoldTextView = findViewById(R.id.playerGoldTextView);
        playerAttackTextView = findViewById(R.id.playerAttackTextView);
        playerDefenseTextView = findViewById(R.id.playerDefenseTextView);

        hpBar = findViewById(R.id.playerHpProgressBar);
        expBar = findViewById(R.id.playerExpProgressBar);

        backBtn = findViewById(R.id.backBtn);
        menuEquipmentBtn = findViewById(R.id.menuEquipmentBtn);
        menuInventoryBtn = findViewById(R.id.menuInventoryBtn);
        menuShopBtn = findViewById(R.id.menuShopBtn);
        menuAchievementsBtn = findViewById(R.id.menuAchievementsBtn);
        menuAboutBtn = findViewById(R.id.menuAboutBtn);
        menuSettingsBtn = findViewById(R.id.menuSettingsBtn);

        greySword = findViewById(R.id.greySword);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MenuActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}