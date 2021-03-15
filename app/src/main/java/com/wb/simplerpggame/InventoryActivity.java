package com.wb.simplerpggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wb.simplerpggame.adaptors.InventoryAdaptor;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.InventoryObj;
import com.wb.simplerpggame.objects.PlayerObj;

import java.util.ArrayList;

public class InventoryActivity extends AppCompatActivity {

    private ImageView backBtn, homeBtn, greySword;
    private TextView playerLevelTextView, playerCurrentHpTextView, playerMaxHpTextView, playerCurrentExpTextView, playerMaxExpTextView, playerAttackTextView, playerDefenseTextView, playerGoldTextView, playerHpTimerTextView;
    private ProgressBar hpBar, expBar;
    private DatabaseObj databaseObj;
    private PlayerObj playerObj;
    private SharedPreferences sharedPreferences;
    private InventoryAdaptor inventoryAdaptor;
    private ArrayList<InventoryObj> inventoryArray = new ArrayList<>();
    public static ConstraintLayout noInventoryText;
    public static RecyclerView inventoryRecyclerView;

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
        filter.addAction(MainActivity.INCREMENT_INVENTORY_AMT);
        filter.addAction(MainActivity.NEW_INVENTORY_ITEM);
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
        filter.addAction(MainActivity.INCREMENT_INVENTORY_AMT);
        filter.addAction(MainActivity.NEW_INVENTORY_ITEM);
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
        setContentView(R.layout.activity_inventory);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        if (Utils.getInstance(this).getSharedPreferences() != null) {
            sharedPreferences = Utils.getInstance(this).getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        }

        initViews();
        initDatabase();
        displayInfo();
        initRecycler();

        if (Utils.getInstance(InventoryActivity.this).isStartBtnEnabled()) {
            greySword.setVisibility(View.INVISIBLE);
        } else {
            greySword.setVisibility(View.VISIBLE);
        }

        if (Utils.getInstance(InventoryActivity.this).isStartBtnEnabled() || CounterClass.getInstance(InventoryActivity.this).ismTimerRunning()) {
            startHpTimer();
        } else if (!Utils.getInstance(InventoryActivity.this).isStartBtnEnabled() && !CounterClass.getInstance(InventoryActivity.this).ismTimerRunning() && playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
            updateCountDownText();
            playerHpTimerTextView.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();

//                onBackPressed();
//                overridePendingTransition(0, 0);
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Utils.PLAYER)) {
                playerObj = Utils.getInstance(InventoryActivity.this).getPlayer();
                displayInfo();
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
                inventoryAdaptor.notifyDataSetChanged();
            } else if (intent.getAction().equals(Utils.STARTDISABLED)) {
                greySword.setVisibility(View.VISIBLE);
                //re-enable useBtn
                inventoryAdaptor.notifyDataSetChanged();
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
                    InventoryActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerHpTimerTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    startHpTimer();
                }
            } else if (intent.getAction().equals(MainActivity.NEW_INVENTORY_ITEM)) {
                InventoryObj lastInserted = databaseObj.getLastInsertedInventory();
                int index = databaseObj.getInventoryRow(lastInserted.getInventoryId());

                if (index > inventoryArray.size()) {
                    inventoryArray.add(lastInserted);
                } else {
                    inventoryArray.add(index, lastInserted);
                }
                inventoryAdaptor.notifyItemInserted(index);

                showRecyclerOrText();
            } else if (intent.getAction().equals(MainActivity.INCREMENT_INVENTORY_AMT)) {
                int lastUpdatedId = intent.getIntExtra(MainActivity.UPDATED_INVENTORY_ID, 0);

                int index = 0;
                for (InventoryObj i : inventoryArray) {
                    if (i.getInventoryId() == lastUpdatedId) {
                        i.setInventoryAmt(databaseObj.getAmtInInventory(lastUpdatedId));
                        inventoryAdaptor.notifyDataSetChanged();
                    }
                    index++;
                }
            } else if (intent.getAction().equals(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY)) {
                int updatedHpValue = intent.getIntExtra(InventoryAdaptor.ViewHolder.UPDATED_HP_VALUE, 0);
                playerObj.setPlayerCurrentHp(updatedHpValue);

                if (updatedHpValue == playerObj.getPlayerMaxHp()) {
                    CounterClass.getInstance(InventoryActivity.this).stopAndResetTimer();
                    playerHpTimerTextView.setVisibility(View.INVISIBLE);
                }

                displayInfo();
            }
        }
    };

    private void updateCountDownText() {
        InventoryActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerHpTimerTextView.setText(String.valueOf(CounterClass.getCurrentTimeString()));
            }
        });
    }

    private void startHpTimer() {
        InventoryActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
                    playerHpTimerTextView.setVisibility(View.VISIBLE);

                    CounterClass.getInstance(InventoryActivity.this).resumeTimer();
                    CounterClass.getInstance(InventoryActivity.this).setmTimerRunning(true);
                }
            }
        });
    }

    private void displayInfo() {
        InventoryActivity.this.runOnUiThread(new Runnable() {
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

    private void initRecycler() {
        inventoryAdaptor = new InventoryAdaptor(InventoryActivity.this);
        inventoryArray = databaseObj.getInventoryList();
        inventoryAdaptor.setInventoryArray(inventoryArray);

        inventoryRecyclerView.setAdapter(inventoryAdaptor);
        inventoryRecyclerView.setLayoutManager(new LinearLayoutManager(InventoryActivity.this));

        showRecyclerOrText();
    }

    public void showRecyclerOrText() {
        InventoryActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inventoryArray.size() > 0) {
                    inventoryRecyclerView.setVisibility(View.VISIBLE);
                    noInventoryText.setVisibility(View.INVISIBLE);
                } else {
                    inventoryRecyclerView.setVisibility(View.INVISIBLE);
                    noInventoryText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(InventoryActivity.this);

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
        homeBtn = findViewById(R.id.homeBtn);

        greySword = findViewById(R.id.greySword);
        inventoryRecyclerView = findViewById(R.id.inventoryRecyclerView);

        noInventoryText = findViewById(R.id.noInventoryText);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(InventoryActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}