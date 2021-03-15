package com.wb.simplerpggame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wb.simplerpggame.adaptors.EventsAdaptor;
import com.wb.simplerpggame.adaptors.InventoryAdaptor;
import com.wb.simplerpggame.adaptors.MapAdaptor;
import com.wb.simplerpggame.adaptors.SettingsEventAdaptor;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.EquipmentObj;
import com.wb.simplerpggame.objects.EventObj;
import com.wb.simplerpggame.objects.HpPotObj;
import com.wb.simplerpggame.objects.MapObj;
import com.wb.simplerpggame.objects.MonsterObj;
import com.wb.simplerpggame.objects.PlayerObj;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements MapAdaptor.CustomMapItemClickListener {

    public static final String ENCOUNTERED_MONSTER = "ENCOUNTERED_MONSTER";
    public static final String BATTLE_WON = "BATTLE_LOOTS";
    public static final String ACHIEVEMENTS = "ACHIEVEMENTS";
    public static final String LEVEL_UP = "LEVEL_UP";
    public static final String ADVENTURE_START = "ADVENTURE_START";
    public static final String BATTLE_LOST = "BATTLE_LOST";
    public static final String INSUFFICIENT_HP = "INSUFFICIENT_HP";
    public static final String MAP_CHANGE = "MAP_CHANGE";
    public static final String DISALLOW_MAP_CHANGE_ON_ADVENTURE = "DISALLOW_MAP_CHANGE_ON_ADVENTURE";
    public static final double MAX_EVENTS_ITEM = 30;
    public static final double HP_PERCENTAGE_INCREASE = 0.1;
    public static final String MONSTER_DROPS = "MONSTER_DROPS";
    public static final String CONSUMABLE_DROPS = "CONSUMABLE_DROPS";
    public static final String NEW_INVENTORY_ITEM = "NEW_INVENTORY_ITEM";
    public static final String INCREMENT_INVENTORY_AMT = "INCREMENT_INVENTORY_AMT";
    public static final String UPDATED_INVENTORY_ID = "UPDATED_INVENTORY_ID";
    public static final String NEW_EQUIPMENT_ADDED = "NEW_EQUIPMENT_ADDED";
    public static final String NUMBER_OF_EQUIP_ADDED = "NUMBER_OF_EQUIP_ADDED";
    public static final String UPDATE_ACHIEVEMENTS = "UPDATE_ACHIEVEMENTS";

    private TextView playerLevelTextView, playerCurrentHpTextView, playerMaxHpTextView, playerCurrentExpTextView, playerMaxExpTextView, playerAttackTextView, playerDefenseTextView, playerGoldTextView, playerHpTimerTextView;
    private Button menuBtn, startBtn, changeMapBtn;
    private ProgressBar hpBar, expBar;
    private RecyclerView mainRecyclerView, mapRecyclerView;
    private ImageView infoImage, greySword;

    private EventsAdaptor eventsAdaptor;
    private MapAdaptor mapAdaptor;

    private PlayerObj playerObj;
    private MapObj mapObj;
    private MonsterObj encounteredMonster;
    private int encounteredMonsterLevel;

    private ArrayList<MonsterObj> monstersArray = new ArrayList<>();
    private ArrayList<String> mapsArray = new ArrayList<>();
    private ArrayList<EventObj> eventsArray = new ArrayList<>();

    private Random random = new Random();
    private Timer timerStartBtn, timer;
    private DatabaseObj databaseObj;
    private AlertDialog mapDialog, mapInfoDialog;
    private String battleText = "";

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
        filter.addAction(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY);
        filter.addAction(SettingsEventAdaptor.UPDATE_MAIN_EVENTS);

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
        filter.addAction(InventoryAdaptor.ViewHolder.UPDATE_HP_FROM_INVENTORY);
        filter.addAction(SettingsEventAdaptor.UPDATE_MAIN_EVENTS);

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
        setContentView(R.layout.activity_main);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        initViews();
        initRecycler();
        initDatabase();
        initMapMonster();
        displayInfo();
        initMapAdaptor();
        startHpTimer();

        changeMapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View mapListViewLayout = inflater.inflate(R.layout.recycler_view_layout_map, null);

                mapRecyclerView = mapListViewLayout.findViewById(R.id.mapRecyclerView);
                mapRecyclerView.setAdapter(mapAdaptor);

                mapRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

                View dialogTitleLayout = inflater.inflate(R.layout.title_layout_centered, null);

                TextView customTitle = dialogTitleLayout.findViewById(R.id.title);
                customTitle.setText("CHANGE MAP");

                mapDialog = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MaterialAlertDialog_Rounded)
                        .setView(mapListViewLayout)
                        .setCustomTitle(dialogTitleLayout)
                        .create();
                mapDialog.show();

                mapDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
                int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.75);

                if (mapsArray.size() > 6) {
                    mapDialog.getWindow().setLayout(width, height);
                } else {
                    mapDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                }

                dialogTitleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });

        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);

                View infoTitleLayout = inflater.inflate(R.layout.title_layout_centered, null);

                TextView customTitle = infoTitleLayout.findViewById(R.id.title);
                customTitle.setText("RECOMMENDED LEVEL");

                mapInfoDialog = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MaterialAlertDialog_Rounded)
                        .setCustomTitle(infoTitleLayout)
                        .setMessage(mapObj.getMapDescription(playerObj.getPlayerMap()))
                        .create();
                mapInfoDialog.show();

                TextView recoLevelView = mapInfoDialog.findViewById(android.R.id.message);
                recoLevelView.setGravity(Gravity.CENTER);
                recoLevelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._15ssp));

                mapInfoDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);

                int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);

                mapInfoDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                infoTitleLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                });
            }
        });

        menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                startActivity(intent);

                Timer delay = new Timer();
                delay.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        int count = 0;
                        for (EventObj e : eventsArray) {
                            if (e.isExpanded()) {
                                e.setExpanded(false);
                                final int finalCount = count;
                                MainActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        eventsAdaptor.notifyItemChanged(finalCount);
                                    }
                                });
                            }
                            count++;
                        }
                    }
                }, 500);
            }
        });

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (playerObj.getPlayerCurrentHp() > 0) {
                    disableStartBtn();
                    CounterClass.getInstance(MainActivity.this).stopTimer();

                    eventsArray.add(new EventObj(getCurrentTime(), ADVENTURE_START, new SpannableStringBuilder("Started adventure."), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
                    updateAdaptor();

                    timerStartBtn = new Timer();
                    timerStartBtn.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            getEncounteredMonster();
                            eventsArray.add(new EventObj(getCurrentTime(), ENCOUNTERED_MONSTER, new SpannableStringBuilder(getEncounteredText()), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
                            updateAdaptor();
                        }
                    }, 5000);

                    timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            int resultHp = simulateBattle(playerObj, encounteredMonster);
                            playerObj.setPlayerCurrentHp(resultHp);
                            databaseObj.updatePlayerCol(DatabaseObj.PLAYER_CURRENT_HP, String.valueOf(playerObj.getPlayerCurrentHp()), playerObj.getPlayerId());

                            if (resultHp > 0) {
                                int[][] values = databaseObj.updateAchievements(encounteredMonster.getMonsterName());

                                for (int[] row : values) {
                                    boolean isUpdated = row[0] > 0;
                                    int achievementCurrentProgress = row[1];
                                    int achievementGoal = row[2];

                                    if (isUpdated) {
                                        Intent i = new Intent();
                                        i.setAction(UPDATE_ACHIEVEMENTS);
                                        MainActivity.this.sendBroadcast(i);

                                        if (Utils.getInstance(MainActivity.this).getSwitch(SettingsActivity.SETTINGS_ACHIEVEMENTS_SWITCH)) {
                                            SpannableStringBuilder achievementText = new SpannableStringBuilder("");
                                            achievementText.append(boldText("『 " + encounteredMonster.getMonsterName() + " 』"));
                                            achievementText.append("  ( " + achievementCurrentProgress + " / " + achievementGoal + " )");

                                            eventsArray.add(new EventObj(getCurrentTime(), ACHIEVEMENTS, achievementText, new SpannableStringBuilder(achievementText), SettingsActivity.SETTINGS_ACHIEVEMENTS_COLOR));
                                            updateAdaptor();

                                            if (achievementCurrentProgress == achievementGoal) {
                                                SpannableStringBuilder achievementText2 = new SpannableStringBuilder("");
                                                achievementText2.append(boldText("Achievement Completed\n"));
                                                achievementText2.append("You can claim your rewards now.");

                                                eventsArray.add(new EventObj(getCurrentTime(), ACHIEVEMENTS, achievementText2, new SpannableStringBuilder(achievementText2), SettingsActivity.SETTINGS_ACHIEVEMENTS_COLOR));
                                                updateAdaptor();
                                            }
                                        }
                                    }
                                }

                                SpannableStringBuilder battleWonText = new SpannableStringBuilder("You won the battle.\nYou received ");

                                int monsterGoldTotal = encounteredMonsterLevel*2;
                                int monsterExpTotal = (int)(encounteredMonsterLevel*100*0.05);

                                if (encounteredMonsterLevel == 10 || encounteredMonsterLevel == 25 || encounteredMonsterLevel == 35 || encounteredMonsterLevel == 50) {
                                    monsterGoldTotal*=2;
                                    monsterExpTotal*=2;
                                }

                                battleWonText.append(boldText(monsterGoldTotal + " gold"));
                                battleWonText.append(" and ");
                                battleWonText.append(boldText(monsterExpTotal + " exp"));
                                battleWonText.append(".");

                                //increase gold and exp
                                eventsArray.add(new EventObj(getCurrentTime(), BATTLE_WON, battleWonText, new SpannableStringBuilder(battleText), SettingsActivity.SETTINGS_EVENTS_BATTLE_WON));
                                updateAdaptor();

                                equipmentDrop(encounteredMonster);
                                hpPotDrop(encounteredMonster);

                                playerObj.setPlayerGold(playerObj.getPlayerGold() + monsterGoldTotal);
                                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(playerObj.getPlayerGold()), playerObj.getPlayerId());

                                updatePlayerLevel(monsterExpTotal);
                            } else {
                                eventsArray.add(new EventObj(getCurrentTime(), BATTLE_LOST, new SpannableStringBuilder("You lost the battle."), new SpannableStringBuilder(battleText), SettingsActivity.SETTINGS_EVENTS_BATTLE_LOST));
                                updateAdaptor();
                            }

                            battleText = "";
                            displayInfo();
                            enableStartBtn();
                            initDatabase();
                            Utils.getInstance(MainActivity.this).setPlayer(playerObj);
                            startHpTimer();
                        }
                    }, 10000);
                } else {
                    eventsArray.add(new EventObj(getCurrentTime(), INSUFFICIENT_HP, new SpannableStringBuilder("You do not have enough HP.\nPlease wait for it to regenerate."), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
                    updateAdaptor();
                }
            }
        });
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(CounterClass.ONTICK)) {
                updateCountDownText();
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
                    MainActivity.this.runOnUiThread(new Runnable() {
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
                    CounterClass.getInstance(MainActivity.this).stopAndResetTimer();
                    playerHpTimerTextView.setVisibility(View.INVISIBLE);
                }

                displayInfo();
            } else if (intent.getAction().equals(SettingsEventAdaptor.UPDATE_MAIN_EVENTS)) {
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eventsAdaptor.notifyDataSetChanged();
                    }
                });
            }
        }
    };

    private void equipmentDrop(MonsterObj encounteredMonster) {
        ArrayList<EquipmentObj> equipmentDropped = new ArrayList<>();

        for (EquipmentObj e : encounteredMonster.getMonsterEquipmentDrop()) {
            int equipmentDropChance = random.nextInt(100) + 1;

            if (equipmentDropChance <= e.getEquipmentDropChance()) {
                databaseObj.addToEquipmentOwned(e);
                equipmentDropped.add(e);
            }
        }

        SpannableStringBuilder monsterDropText = new SpannableStringBuilder();

        int numberOfEquipsObtained = 0;
        for (EquipmentObj e : equipmentDropped) {
            if (numberOfEquipsObtained != 0) {
                monsterDropText.append("\n ");
            }

            monsterDropText.append(boldText("『 " + e.getEquipmentName() + " 』" + " x 1"));
            numberOfEquipsObtained++;
        }

        if (numberOfEquipsObtained > 1) {
            eventsArray.add(new EventObj(getCurrentTime(), MONSTER_DROPS, new SpannableStringBuilder("Equipment Drops"), monsterDropText, SettingsActivity.SETTINGS_EVENTS_ITEM_DROPS));
            updateAdaptor();

            Intent i = new Intent();
            i.setAction(NEW_EQUIPMENT_ADDED);
            i.putExtra(NUMBER_OF_EQUIP_ADDED, numberOfEquipsObtained);
            MainActivity.this.sendBroadcast(i);
        } else if (numberOfEquipsObtained == 1) {
            eventsArray.add(new EventObj(getCurrentTime(), MONSTER_DROPS, new SpannableStringBuilder("Obtained ").append(boldText("『 " + equipmentDropped.get(0).getEquipmentName() + " 』")), null, SettingsActivity.SETTINGS_EVENTS_ITEM_DROPS));
            updateAdaptor();

            Intent i = new Intent();
            i.setAction(NEW_EQUIPMENT_ADDED);
            i.putExtra(NUMBER_OF_EQUIP_ADDED, 1);
            MainActivity.this.sendBroadcast(i);
        }
    }

    private SpannableStringBuilder boldText(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }

    private String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        return dateFormat.format(calendar.getTime());
    }

    private void updateAdaptor() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                eventsAdaptor.notifyItemInserted(eventsArray.size());
                scrollToTop();

                while (eventsArray.size() > MAX_EVENTS_ITEM) {
                    eventsArray.remove(0);
                    eventsAdaptor.notifyItemRemoved(0);
                }
            }
        });
    }

    private void scrollToTop() {
        mainRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                mainRecyclerView.scrollToPosition(eventsAdaptor.getItemCount() - 1);
            }
        });
    }

    private void hpPotDrop(MonsterObj encounteredMonster) {
        HpPotObj hpPotObjDropped = encounteredMonster.getHpPotDrop();

        if (hpPotObjDropped != null) {
            int hpPotDropChance = random.nextInt(100) + 1;

            if (hpPotDropChance <= hpPotObjDropped.getHpPotDropChance()) {
                int numberOfHpPotsDropped = random.nextInt(hpPotObjDropped.getMaxAmtToDrop()) + 1;

                if (numberOfHpPotsDropped > 1) {
                    eventsArray.add(new EventObj(getCurrentTime(), CONSUMABLE_DROPS, new SpannableStringBuilder("Obtained\n").append(boldText("『 " + hpPotObjDropped.getHpPotName() + " 』x" + numberOfHpPotsDropped)), null, SettingsActivity.SETTINGS_EVENTS_ITEM_DROPS));
                    updateAdaptor();
                } else if (numberOfHpPotsDropped == 1) {
                    eventsArray.add(new EventObj(getCurrentTime(), CONSUMABLE_DROPS, new SpannableStringBuilder("Obtained\n").append(boldText("『 " + hpPotObjDropped.getHpPotName() + " 』")), null, SettingsActivity.SETTINGS_EVENTS_ITEM_DROPS));
                    updateAdaptor();
                }

                int idInInventory = databaseObj.checkIfExistsInInventory(hpPotObjDropped.getHpPotName());
                if (idInInventory == -1) {
                    databaseObj.addToInventory(hpPotObjDropped, numberOfHpPotsDropped);

                    Intent i = new Intent();
                    i.setAction(NEW_INVENTORY_ITEM);
                    MainActivity.this.sendBroadcast(i);
                } else {
                    databaseObj.updateIncrementInventory(idInInventory, numberOfHpPotsDropped);

                    Intent i = new Intent();
                    i.setAction(INCREMENT_INVENTORY_AMT);
                    i.putExtra(UPDATED_INVENTORY_ID, idInInventory);
                    MainActivity.this.sendBroadcast(i);
                }
            }
        }
    }

    private SpannableStringBuilder getEncounteredText() {
        SpannableStringBuilder encounteredText = new SpannableStringBuilder("You have encountered ");

        String monsterName = encounteredMonster.getMonsterName();
        encounteredText.append("a ");

        String monsterLevelName = "Lv. " + encounteredMonsterLevel + " " + monsterName;

//        char ch = monsterName.charAt(0);
//
//        if (ch == 'A' || ch == 'E' || ch == 'I' || ch == 'O' || ch == 'U') {
//            encounteredText.append("an ");
//        } else {
//            encounteredText.append("a ");
//        }

        if (monsterLevelName.split("\\w+").length > 1) {
            encounteredText.append(boldText("\n"));
        }

        encounteredText.append(boldText("『 " + monsterLevelName + " 』"));
        encounteredText.append("\nGet ready for battle.");

        return encounteredText;
    }

    private void initMapAdaptor() {
        mapsArray.addAll(mapObj.getListOfMaps());
        mapAdaptor = new MapAdaptor(mapsArray, playerObj.getPlayerMap(), MainActivity.this, this);
    }

    private void disableStartBtn() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startBtn.setEnabled(false);
                greySword.setVisibility(View.VISIBLE);
            }
        });
        Utils.getInstance(MainActivity.this).setStartBtn(false);
    }

    private void enableStartBtn() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                startBtn.setEnabled(true);
                greySword.setVisibility(View.INVISIBLE);
            }
        });
        Utils.getInstance(MainActivity.this).setStartBtn(true);
    }

    private void startHpTimer() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (playerObj.getPlayerCurrentHp() < playerObj.getPlayerMaxHp()) {
                    playerHpTimerTextView.setVisibility(View.VISIBLE);

                    CounterClass.getInstance(MainActivity.this).resumeTimer();
                    CounterClass.getInstance(MainActivity.this).setmTimerRunning(true);
                }
            }
        });
    }

    private void updateCountDownText() {
        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerHpTimerTextView.setText(String.valueOf(CounterClass.getCurrentTimeString()));
            }
        });
    }

    private void updatePlayerLevel(int monsterExp) {
        //if there is overflow of exp after the addition
        //update level, exp and maxEXP

        int playerLvl = playerObj.getPlayerLevel();
        int totalExp = monsterExp + playerObj.getPlayerCurrentExp();

        int playerInitialEXP = playerObj.getPlayerCurrentExp();
        int playerMaxEXP = playerObj.getPlayerMaxExp();

        int maxLvl = 50;

        if (totalExp >= playerMaxEXP) {
            do {
                if (playerLvl <= (maxLvl-1)) {
                    playerLvl += 1;
                    totalExp = totalExp - playerMaxEXP;
                    playerMaxEXP = playerLvl * 100;
                } else {
                    //e.g. max lvl 50 with exp capped at 5000
                    totalExp = maxLvl*100;
                }
            } while (totalExp > playerMaxEXP);

            //if total exp gained is the same as max exp, also increase lvl
            //e.g. lv 1 1200/2000 exp gained 800 exp = 2000/2000 exp
            //result lv 2, 0/{lv 2 max exp} exp
            if (totalExp == playerMaxEXP && playerLvl != maxLvl) {
                playerLvl+=1;
                totalExp = 0;
            }

            if (playerObj.getPlayerLevel() <= (maxLvl-1)) {
                eventsArray.add(new EventObj(getCurrentTime(), LEVEL_UP, new SpannableStringBuilder("You have leveled up! " + playerObj.getPlayerLevel() + " -> " + playerLvl), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
                updateAdaptor();
            }

            playerObj.setPlayerLevel(playerLvl);
            playerObj.setPlayerCurrentExp(totalExp);
            playerObj.setPlayerMaxExp(playerLvl*100);
            playerObj.setPlayerMaxHp((playerLvl * 5)+5);
        } else {
            playerObj.setPlayerCurrentExp(playerInitialEXP+monsterExp);
        }

        databaseObj.updatePlayerCol(DatabaseObj.PLAYER_LEVEL, String.valueOf(playerObj.getPlayerLevel()), playerObj.getPlayerId());
        databaseObj.updatePlayerCol(DatabaseObj.PLAYER_CURRENT_EXP, String.valueOf(playerObj.getPlayerCurrentExp()), playerObj.getPlayerId());
        databaseObj.updatePlayerCol(DatabaseObj.PLAYER_MAX_EXP, String.valueOf(playerObj.getPlayerMaxExp()), playerObj.getPlayerId());
        databaseObj.updatePlayerCol(DatabaseObj.PLAYER_MAX_HP, String.valueOf(playerObj.getPlayerMaxHp()), playerObj.getPlayerId());
    }

    private int simulateBattle(PlayerObj playerObj, MonsterObj encounteredMonster) {
        //get initial hp
        int playerHp = playerObj.getPlayerCurrentHp();
        int monsterHp = encounteredMonster.getMonsterHp();

        int playerLevelStat = playerObj.getPlayerLevel();
        int playerAttackStat = playerObj.getPlayerAttack();
        int playerDefenseStat = playerObj.getPlayerDefense();

        int playerDamageTotal = playerLevelStat+playerAttackStat+2;
        int monsterDamageTotal = ((encounteredMonsterLevel+2)-playerDefenseStat) <= 0 ? 1 : (encounteredMonsterLevel+2)-playerDefenseStat;

        int monsterDamageMin = (monsterDamageTotal-2 <= 0) ? 1 : monsterDamageTotal-2;
        int playerDamageMin = (playerDamageTotal-2 <= 0) ? 1 : playerDamageTotal-2;

        battleText += "Your HP: " + playerHp + "\nMonster HP: " + monsterHp;

        //while monster is still alive
        do {
            int monsterAttack = random.nextInt((monsterDamageTotal-monsterDamageMin)+1) + monsterDamageMin;
            int playerAttack = random.nextInt((playerDamageTotal-playerDamageMin)+1) + playerDamageMin;

            monsterHp = monsterHp - playerAttack;
            String monsterHpInfo = "Monster HP: " + monsterHp;
            battleText += "\n\nYou dealt " + playerAttack + " damage.\n";
            battleText += monsterHpInfo;
            if (monsterHp <= 0) {
                monsterHp = 0;
                break;
            }

            playerHp = playerHp - monsterAttack;
            String playerHpInfo = "Your HP: " + playerHp;
            battleText += "\n\nThe monster dealt " + monsterAttack + " damage.\n";
            battleText += playerHpInfo;
            if (playerHp <= 0) {
                playerHp = 0;
                break;
            }
        } while (monsterHp > 0);

        return playerHp;
    }

    private void getEncounteredMonster() {
        //generate 1 - 100
        int randomChance = random.nextInt(100) + 1;

        for (MonsterObj m : monstersArray) {
            if (randomChance >= m.getMonsterChance()) {
                encounteredMonster = m;
                break;
            }
        }

        int[] monsterLevelRange = encounteredMonster.getMonsterLevel();

        if (monsterLevelRange.length > 1) {
            encounteredMonsterLevel = random.nextInt((monsterLevelRange[1]-monsterLevelRange[0])+1) + monsterLevelRange[0];
        } else {
            encounteredMonsterLevel = monsterLevelRange[0];
        }
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(MainActivity.this);

        if (databaseObj.getPlayer() == null) {
            //add new player into database
            databaseObj.addNewPlayer();
        }

        //get player object from stored database
        playerObj = databaseObj.getPlayer();

        PlayerObj equippedPlayer;
        equippedPlayer = databaseObj.getEquippedEquipment();
        if (equippedPlayer.getPlayerHeadGear() != null) {
            playerObj.setPlayerHeadGear(equippedPlayer.getPlayerHeadGear());
        }
        if (equippedPlayer.getPlayerArmor() != null) {
            playerObj.setPlayerArmor(equippedPlayer.getPlayerArmor());
        }
        if (equippedPlayer.getPlayerWeapon() != null) {
            playerObj.setPlayerWeapon(equippedPlayer.getPlayerWeapon());
        }
        if (equippedPlayer.getPlayerShield() != null) {
            playerObj.setPlayerShield(equippedPlayer.getPlayerShield());
        }
        if (equippedPlayer.getPlayerFootwear() != null) {
            playerObj.setPlayerFootwear(equippedPlayer.getPlayerFootwear());
        }
    }

    private void initRecycler() {
        eventsAdaptor = new EventsAdaptor();
        eventsArray.add(new EventObj(getCurrentTime(), "REST", new SpannableStringBuilder("You are currently resting."), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
        eventsAdaptor.setEventsArray(eventsArray);

        //mainRecyclerView.setItemAnimator(null);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        mainRecyclerView.setAdapter(eventsAdaptor);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);

        mainRecyclerView.setLayoutManager(linearLayoutManager);
        mainRecyclerView.scrollToPosition(eventsAdaptor.getItemCount() - 1);
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

        changeMapBtn = findViewById(R.id.currentMapBtn);

        hpBar = findViewById(R.id.playerHpProgressBar);
        expBar = findViewById(R.id.playerExpProgressBar);

        menuBtn = findViewById(R.id.menuBtn);
        startBtn = findViewById(R.id.startBtn);

        mainRecyclerView = findViewById(R.id.mainRecyclerView);

        infoImage = findViewById(R.id.infoImage);
        greySword = findViewById(R.id.greySword);
    }

    @Override
    public void onBackPressed() {
        if (!startBtn.isEnabled()) {
            LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
            View exitTitleLayout = inflater.inflate(R.layout.title_layout_left_align, null);

            TextView customTitle = exitTitleLayout.findViewById(R.id.title);
            customTitle.setText("Exit Game");

            final AlertDialog exitDialog = new MaterialAlertDialogBuilder(MainActivity.this, R.style.MaterialAlertDialog_Rounded)
                    .setCustomTitle(exitTitleLayout)
                    .setMessage("You are currently on an adventure.\nAre you sure you wish to exit?")
                    .create();

            exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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

            //Typeface font = ResourcesCompat.getFont(MainActivity.this, R.font.aldrich);

            Button btnPositive = exitDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));

            Button btnNegative = exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
            btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen._12ssp));

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
            //int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.25);

            exitDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }

    @Override
    public void onMapItemClick(int position) {
        if (!mapsArray.get(position).equals(String.valueOf(playerObj.getPlayerMap()))) {
            if (startBtn.isEnabled()) {
                playerObj.setPlayerMap(mapsArray.get(position));
                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_MAP, playerObj.getPlayerMap(), playerObj.getPlayerId());

                mapAdaptor.setCurrentMap(playerObj.getPlayerMap());

                displayInfo();
                initMapMonster();
                mapDialog.dismiss();

                eventsArray.add(new EventObj(getCurrentTime(), MAP_CHANGE, new SpannableStringBuilder("You have arrived at ").append(boldText("『 " + playerObj.getPlayerMap() + " 』")).append("."), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
            } else {
                mapDialog.dismiss();
                eventsArray.add(new EventObj(getCurrentTime(), DISALLOW_MAP_CHANGE_ON_ADVENTURE, new SpannableStringBuilder("You cannot change map while you are on an adventure."), null, SettingsActivity.SETTINGS_EVENTS_GENERAL));
            }
            updateAdaptor();
        }
    }

    private void displayInfo() {
        MainActivity.this.runOnUiThread(new Runnable() {
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

                changeMapBtn.setText(playerObj.getPlayerMap());

                hpBar.setProgress((int) (((float) playerObj.getPlayerCurrentHp() / (float) playerObj.getPlayerMaxHp()) * 100));
                expBar.setProgress((int) (((float) playerObj.getPlayerCurrentExp() / (float) playerObj.getPlayerMaxExp()) * 100));
            }
        });
    }

    private void initMapMonster() {
        mapObj = new MapObj();
        monstersArray = mapObj.getCurrentMapMonsters(playerObj.getPlayerMap());
    }
}