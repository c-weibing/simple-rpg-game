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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wb.simplerpggame.adaptors.ShopAdaptor;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.EquipmentObj;
import com.wb.simplerpggame.objects.InventoryObj;
import com.wb.simplerpggame.objects.PlayerObj;

import java.util.ArrayList;

public class ShopActivity extends AppCompatActivity {

    private ImageView backBtn, homeBtn, greySword;
    private TextView playerLevelTextView, playerCurrentHpTextView, playerMaxHpTextView, playerCurrentExpTextView, playerMaxExpTextView, playerAttackTextView, playerDefenseTextView, playerGoldTextView, playerHpTimerTextView, noInventoryTextView;
    private ProgressBar hpBar, expBar;
    private DatabaseObj databaseObj;
    private EquipmentObj equipmentObj = new EquipmentObj();
    private InventoryObj inventoryObj = new InventoryObj();
    private PlayerObj playerObj;
    private SharedPreferences sharedPreferences;
    private ShopAdaptor shopAdaptor;
    private ArrayList<Object> inventoryArray = new ArrayList<>();
    public static ConstraintLayout noInventoryText;
    public static RecyclerView shopRecyclerView;
    public static Button allCategoryBtn, equipmentCategoryBtn, consumablesCategoryBtn, inventoryBtn;
    private ArrayList<Button> categoryButtons = new ArrayList<>();

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
        filter.addAction(ShopAdaptor.ViewHolder.UPDATE_GOLD_FROM_SHOP);
        filter.addAction(MainActivity.NEW_EQUIPMENT_ADDED);

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
        filter.addAction(ShopAdaptor.ViewHolder.UPDATE_GOLD_FROM_SHOP);
        filter.addAction(MainActivity.NEW_EQUIPMENT_ADDED);

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
        setContentView(R.layout.activity_shop);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        if (Utils.getInstance(this).getSharedPreferences() != null) {
            sharedPreferences = Utils.getInstance(this).getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        }

        initViews();
        initDatabase();
        displayInfo();
        initRecycler();

        if (Utils.getInstance(ShopActivity.this).isStartBtnEnabled()) {
            greySword.setVisibility(View.INVISIBLE);
        } else {
            greySword.setVisibility(View.VISIBLE);
        }

        if (Utils.getInstance(ShopActivity.this).isStartBtnEnabled() || CounterClass.getInstance(ShopActivity.this).ismTimerRunning()) {
            startHpTimer();
        } else if (!Utils.getInstance(ShopActivity.this).isStartBtnEnabled() && !CounterClass.getInstance(ShopActivity.this).ismTimerRunning() && playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
            updateCountDownText();
            playerHpTimerTextView.setVisibility(View.VISIBLE);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        });

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShopActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        allCategoryBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    allCategoryBtn.setPressed(true);
                    for (Button b : categoryButtons) {
                        if (b.isPressed() && b != allCategoryBtn && b != inventoryBtn) {
                            b.setPressed(false);
                        }
                    }

                    if (inventoryBtn.isPressed()) {
                        inventoryArray.clear();
                        inventoryArray.addAll(databaseObj.getInventoryAndEquipmentObjectList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    } else {
                        inventoryArray.clear();
                        inventoryArray.addAll(equipmentObj.getShopEquipmentList());
                        inventoryArray.addAll(inventoryObj.getInventoryList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    }
                    return true;
                }

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }

                return true;
            }
        });

        equipmentCategoryBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    equipmentCategoryBtn.setPressed(true);
                    for (Button b : categoryButtons) {
                        if (b.isPressed() && b != equipmentCategoryBtn && b != inventoryBtn) {
                            b.setPressed(false);
                        }
                    }

                    if (inventoryBtn.isPressed()) {
                        inventoryArray.clear();
                        inventoryArray.addAll(databaseObj.getEquipmentObjectList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    } else {
                        inventoryArray.clear();
                        inventoryArray.addAll(equipmentObj.getShopEquipmentList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    }

                    return true;
                }

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }

                return true;
            }
        });

        consumablesCategoryBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    consumablesCategoryBtn.setPressed(true);
                    for (Button b : categoryButtons) {
                        if (b.isPressed() && b != consumablesCategoryBtn && b != inventoryBtn) {
                            b.setPressed(false);
                        }
                    }

                    if (inventoryBtn.isPressed()) {
                        inventoryArray.clear();
                        inventoryArray.addAll(databaseObj.getInventoryObjectList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    } else {
                        inventoryArray.clear();
                        inventoryArray.addAll(inventoryObj.getInventoryList());
                        shopAdaptor.notifyDataSetChanged();
                        showRecyclerOrText();
                    }

                    return true;
                }

                // don't handle event unless its ACTION_UP so "doSomething()" only runs once.
                if (event.getAction() != MotionEvent.ACTION_UP) {
                    return false;
                }

                return true;
            }
        });


        inventoryBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                // show interest in events resulting from ACTION_DOWN
                if (event.getAction() == MotionEvent.ACTION_DOWN) {

                    boolean isPressed = !inventoryBtn.isPressed();
                    inventoryBtn.setPressed(isPressed);

                    if (isPressed) {
                        if (allCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(databaseObj.getInventoryAndEquipmentObjectList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        } else if (equipmentCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(databaseObj.getEquipmentObjectList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        } else if (consumablesCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(databaseObj.getInventoryObjectList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        }
                    } else {
                        if (allCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(equipmentObj.getShopEquipmentList());
                            inventoryArray.addAll(inventoryObj.getInventoryList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        } else if (equipmentCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(equipmentObj.getShopEquipmentList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        } else if (consumablesCategoryBtn.isPressed()) {
                            inventoryArray.clear();
                            inventoryArray.addAll(inventoryObj.getInventoryList());
                            shopAdaptor.notifyDataSetChanged();
                            showRecyclerOrText();
                        }
                    }

                    return true;
                }

                return true;
            }
        });
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            if (key.equals(Utils.PLAYER)) {
                playerObj = Utils.getInstance(ShopActivity.this).getPlayer();
                displayInfo();
            }
        }
    };

    public void updateAttackDefense() {
        int playerAttack = 1;
        int playerDefense = 0;

        ArrayList<EquipmentObj> equipmentOwned = databaseObj.getListOfEquipmentOwned();
        for (EquipmentObj e : equipmentOwned) {
            if (e.isEquipped()) {
                playerAttack += e.getEquipmentAttack();
                playerDefense += e.getEquipmentDefense();
            }
        }

        playerObj.setPlayerAttack(playerAttack);
        playerObj.setPlayerDefense(playerDefense);

        displayInfo();

        databaseObj.updatePlayerAttackDefense(playerAttack, playerDefense, playerObj.getPlayerId());
    }

    public void showRecyclerOrText() {
        ShopActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (inventoryArray.size() > 0) {
                    shopRecyclerView.setVisibility(View.VISIBLE);
                    noInventoryText.setVisibility(View.INVISIBLE);
                } else {
                    shopRecyclerView.setVisibility(View.INVISIBLE);
                    noInventoryText.setVisibility(View.VISIBLE);
                    if (equipmentCategoryBtn.isPressed()) {
                        noInventoryTextView.setText("You do not have any equipment currently.");
                    } else {
                        noInventoryTextView.setText("You do not have any items currently.");
                    }
                }
            }
        });
    }

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
                    ShopActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            playerHpTimerTextView.setVisibility(View.INVISIBLE);
                        }
                    });
                } else {
                    startHpTimer();
                }
            } else if (intent.getAction().equals(ShopAdaptor.ViewHolder.UPDATE_GOLD_FROM_SHOP)) {
                int updatedGoldValue = intent.getIntExtra(ShopAdaptor.ViewHolder.UPDATED_GOLD_VALUE, 0);
                playerObj.setPlayerGold(updatedGoldValue);

                displayInfo();
            } else if (intent.getAction().equals(MainActivity.NEW_INVENTORY_ITEM)) {
                if (inventoryBtn.isPressed() && consumablesCategoryBtn.isPressed()) {
                    InventoryObj lastInserted = databaseObj.getLastInsertedInventory();
                    //get the index where it would be if it was sorted
                    int index = databaseObj.getInventoryRow(lastInserted.getInventoryId());

                    if (index > inventoryArray.size()) {
                        inventoryArray.add(lastInserted);
                    } else {
                        inventoryArray.add(index, lastInserted);
                    }
                    shopAdaptor.notifyItemInserted(index);

                    showRecyclerOrText();
                }
            } else if (intent.getAction().equals(MainActivity.INCREMENT_INVENTORY_AMT)) {
                if (inventoryBtn.isPressed() && consumablesCategoryBtn.isPressed()) {
                    int lastUpdatedId = intent.getIntExtra(MainActivity.UPDATED_INVENTORY_ID, 0);

                    int index = 0;
                    for (Object i : inventoryArray) {
                        InventoryObj item = (InventoryObj) i;
                        if (item.getInventoryId() == lastUpdatedId) {
                            item.setInventoryAmt(databaseObj.getAmtInInventory(lastUpdatedId));
                            shopAdaptor.notifyDataSetChanged();
                        }
                        index++;
                    }
                }
            } else if (intent.getAction().equals(MainActivity.NEW_EQUIPMENT_ADDED)) {
                if (inventoryBtn.isPressed() && equipmentCategoryBtn.isPressed()) {
                    int numOfEquipsAdded = intent.getIntExtra(MainActivity.NUMBER_OF_EQUIP_ADDED, 0);
                    int index = 0;

                    while (numOfEquipsAdded > 0) {
                        EquipmentObj lastInserted = databaseObj.getLastInsertedEquipment(numOfEquipsAdded);

                        boolean exists = false;
                        int count = 0;
                        for (Object o : inventoryArray) {
                            EquipmentObj e = (EquipmentObj) o;
                            if (e.getEquipmentName().equals(lastInserted.getEquipmentName())) {
                                //if item already exists, change the number shown
                                exists = true;
                                count = e.getEquipmentCount();
                                e.setEquipmentCount(count + 1);
                                shopAdaptor.notifyDataSetChanged();
                            }
                        }

                        if (!exists) {
                            //if doesn't exist
                            index = databaseObj.getEquipmentRowShop(lastInserted.getEquipmentId());

                            lastInserted.setEquipmentCount(1);
                            if (index > inventoryArray.size()) {
                                //if it's the first ever item to be inserted like index = 1, size = 0
                                inventoryArray.add((Object) lastInserted);
                                shopAdaptor.notifyItemInserted(index);
                            } else {
                                //else if list is already populated, insert it at it's supposed position
                                inventoryArray.add(index, (Object) lastInserted);
                                shopAdaptor.notifyItemInserted(index);
                            }

                            showRecyclerOrText();
                        }

                        numOfEquipsAdded--;
                    }
                }
            }
        }
    };

    private void updateCountDownText() {
        ShopActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerHpTimerTextView.setText(String.valueOf(CounterClass.getCurrentTimeString()));
            }
        });
    }

    private void startHpTimer() {
        ShopActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
                    playerHpTimerTextView.setVisibility(View.VISIBLE);

                    CounterClass.getInstance(ShopActivity.this).resumeTimer();
                    CounterClass.getInstance(ShopActivity.this).setmTimerRunning(true);
                }
            }
        });
    }

    private void displayInfo() {
        ShopActivity.this.runOnUiThread(new Runnable() {
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
        shopAdaptor = new ShopAdaptor(ShopActivity.this);
        inventoryArray = equipmentObj.getShopEquipmentList();
        inventoryArray.addAll(inventoryObj.getInventoryList());
        shopAdaptor.setInventoryArray(inventoryArray);
        showRecyclerOrText();

        shopRecyclerView.setAdapter(shopAdaptor);
        shopRecyclerView.setLayoutManager(new LinearLayoutManager(ShopActivity.this));
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(ShopActivity.this);

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
        noInventoryTextView = findViewById(R.id.noInventoryTextView);

        hpBar = findViewById(R.id.playerHpProgressBar);
        expBar = findViewById(R.id.playerExpProgressBar);

        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);

        allCategoryBtn = findViewById(R.id.allCategoryBtn);
        equipmentCategoryBtn = findViewById(R.id.equipmentCategoryBtn);
        consumablesCategoryBtn = findViewById(R.id.consumablesCategoryBtn);
        inventoryBtn = findViewById(R.id.inventoryBtn);

        allCategoryBtn.setPressed(true);
        categoryButtons.add(allCategoryBtn);
        categoryButtons.add(equipmentCategoryBtn);
        categoryButtons.add(consumablesCategoryBtn);
        categoryButtons.add(inventoryBtn);

        greySword = findViewById(R.id.greySword);
        shopRecyclerView = findViewById(R.id.shopRecyclerView);

        noInventoryText = findViewById(R.id.noInventoryText);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ShopActivity.this, MenuActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}