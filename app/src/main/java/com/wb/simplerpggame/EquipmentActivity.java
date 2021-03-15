package com.wb.simplerpggame;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wb.simplerpggame.adaptors.EquipmentAdaptor;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.EquipmentObj;
import com.wb.simplerpggame.objects.PlayerObj;

import java.util.ArrayList;

public class EquipmentActivity extends AppCompatActivity implements EquipmentAdaptor.CustomEquipmentItemClickListener {

    public static final String ALL = "ALL";
    private ImageView backBtn, homeBtn, helmetImage, armorImage, armoredBootsImage, weaponImage, shieldImage, unequipAll;
    private TextView playerAttackTextView, playerDefenseTextView;
    private DatabaseObj databaseObj;
    private PlayerObj playerObj;
    private EquipmentAdaptor equipmentAdaptor;
    private RecyclerView equipmentRecyclerView;
    private CardView headgearCardView, armorCardView, weaponCardView, shieldCardView, footwearCardView;
    private ArrayList<EquipmentObj> equipmentArray = new ArrayList<>();
    private ConstraintLayout equipmentBackground, noEquipmentText;
    private int playerAttack, playerDefense;
    private ArrayList<EquipmentObj> equipmentOwned;
    private int countClicks;
    private String currentEquipmentType = ALL;

    @Override
    public void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.NEW_EQUIPMENT_ADDED);

        registerReceiver(intentReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        overridePendingTransition(0, 0);

        initViews();
        initDatabase();
        displayInfo();
        initRecycler();

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EquipmentActivity.this, MenuActivity.class);
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
                Intent intent = new Intent(EquipmentActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        headgearCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getEquipmentOfType(EquipmentObj.HEAD_GEAR));
                if (equipmentArray.isEmpty()) {
                    equipmentArray.add(new EquipmentObj(-1, null, EquipmentObj.HEAD_GEAR, EquipmentObj.UNCOMMON, 0, 0, 0, false, 0, 0));
                }
                updateAdaptor();
                currentEquipmentType = EquipmentObj.HEAD_GEAR;
            }
        });

        armorCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getEquipmentOfType(EquipmentObj.ARMOR));
                if (equipmentArray.isEmpty()) {
                    equipmentArray.add(new EquipmentObj(-1, null, EquipmentObj.ARMOR, EquipmentObj.UNCOMMON,0, 0, 0, false, 0, 0));
                }
                updateAdaptor();
                currentEquipmentType = EquipmentObj.ARMOR;
            }
        });

        weaponCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getEquipmentOfType(EquipmentObj.WEAPON));
                if (equipmentArray.isEmpty()) {
                    equipmentArray.add(new EquipmentObj(-1, null, EquipmentObj.WEAPON, EquipmentObj.UNCOMMON,0, 0, 0, false, 0, 0));
                }
                updateAdaptor();
                currentEquipmentType = EquipmentObj.WEAPON;
            }
        });

        shieldCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getEquipmentOfType(EquipmentObj.SHIELD));
                if (equipmentArray.isEmpty()) {
                    equipmentArray.add(new EquipmentObj(-1, null, EquipmentObj.SHIELD, EquipmentObj.UNCOMMON,0, 0, 0, false, 0, 0));
                }
                updateAdaptor();
                currentEquipmentType = EquipmentObj.SHIELD;
            }
        });

        footwearCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getEquipmentOfType(EquipmentObj.FOOT_WEAR));
                if (equipmentArray.isEmpty()) {
                    equipmentArray.add(new EquipmentObj(-1, null, EquipmentObj.FOOT_WEAR, EquipmentObj.UNCOMMON,0, 0, 0, false, 0, 0));
                }
                updateAdaptor();
                currentEquipmentType = EquipmentObj.FOOT_WEAR;
            }
        });

        equipmentBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                equipmentArray.clear();
                equipmentArray.addAll(databaseObj.getListOfEquipmentOwned());
                updateAdaptor();
                currentEquipmentType = ALL;
            }
        });

        unequipAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;

                //equipmentArray: current equipment on 'page' connected to what is shown on page
                //e.g. shoes

                for (EquipmentObj e : equipmentArray) {
                    if (e.isEquipped()) {
                        databaseObj.unequipEquipment(e.getEquipmentName());
                        e.setEquipped(false);
                        setImageUnequipped(e.getEquipmentType());
                        equipmentAdaptor.notifyItemChanged(count, new Object());
                    }
                    count++;
                }

                //equipmentOwned: search in the list of other equipment types
                //e.g. anything other than shoes
                for (EquipmentObj e : equipmentOwned) {
                    if (e.isEquipped()) {
                        databaseObj.unequipEquipment(e.getEquipmentName());
                        e.setEquipped(false);
                        setImageUnequipped(e.getEquipmentType());
                    }
                }

                //set equipped image
                updateAttackDefense();
            }
        });
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MainActivity.NEW_EQUIPMENT_ADDED)) {
                int numOfEquipsAdded = intent.getIntExtra(MainActivity.NUMBER_OF_EQUIP_ADDED, 0);
                int index = 0;

                while (numOfEquipsAdded > 0) {
                    EquipmentObj lastInserted = databaseObj.getLastInsertedEquipment(numOfEquipsAdded);

                    //get the index where it's supposed to be
                    if (currentEquipmentType.equals(ALL)) {
                        index = databaseObj.getEquipmentRowAllCategory(lastInserted.getEquipmentId());
                    } else {
                        index = databaseObj.getEquipmentRowTypeCategory(lastInserted.getEquipmentId(), currentEquipmentType);
                    }

                    //check if existing equipment name
                    if (databaseObj.checkIfExistsInEquipmentOwned(lastInserted.getEquipmentName()) > 1) {
                        //if the second updated equipment isn't shown on the screen, ignore it
                        //it will be updated when user clicks on another category since database would update again
                        if (currentEquipmentType.equals(lastInserted.getEquipmentType()) || currentEquipmentType.equals(ALL)) {
                            int updatedEquipmentCount = databaseObj.checkIfExistsInEquipmentOwned(lastInserted.getEquipmentName());
                            equipmentArray.get(index).setEquipmentCount(updatedEquipmentCount);
                            equipmentAdaptor.notifyItemChanged(index, new Object());
                        }
                    } else {
                        if (lastInserted.getEquipmentType().equals(currentEquipmentType) || currentEquipmentType.equals(ALL)) {
                            if (lastInserted.getEquipmentType().equals(currentEquipmentType) && equipmentArray.size() == 1) {
                                //remove the header
                                equipmentArray.clear();
                                updateAdaptor();
                            }

                            if (index > equipmentArray.size()) {
                                //add it at the top new item
                                equipmentArray.add(lastInserted);
                                equipmentAdaptor.notifyItemInserted(index);
                            } else {
                                //append it
                                equipmentArray.add(index, lastInserted);
                                equipmentAdaptor.notifyItemInserted(index);

                                if (index == 0 && equipmentArray.size() > 0) {
                                    //if you are inserted at first item
                                    //you use itemchange to remove the header on index + 1
                                    //so that now the new index =0, has its own header and index + 1 header has been removed
                                    equipmentAdaptor.notifyItemChanged(index + 1);
                                }
                            }
                        }
                    }

                    numOfEquipsAdded--;
                }

                showRecyclerOrText();
            }
        }
    };

    private void updateAdaptor() {
        EquipmentActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                equipmentAdaptor.notifyDataSetChanged();
            }
        });
    }

    private void initRecycler() {
        equipmentAdaptor = new EquipmentAdaptor(this, EquipmentActivity.this);
        equipmentArray = databaseObj.getListOfEquipmentOwned();
        equipmentAdaptor.setEquipmentArray(equipmentArray);

        equipmentRecyclerView.setAdapter(equipmentAdaptor);
        equipmentRecyclerView.setLayoutManager(new LinearLayoutManager(EquipmentActivity.this));

        showRecyclerOrText();
    }

    private void showRecyclerOrText() {
        EquipmentActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (equipmentArray.size() > 0) {
                    equipmentRecyclerView.setVisibility(View.VISIBLE);
                    noEquipmentText.setVisibility(View.INVISIBLE);
                } else {
                    equipmentRecyclerView.setVisibility(View.INVISIBLE);
                    noEquipmentText.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initDatabase() {
        //will create player if it doesn't exist else you get existing info from existing database
        databaseObj = new DatabaseObj(EquipmentActivity.this);

        if (databaseObj.getPlayer() == null) {
            //add new player into database
            databaseObj.addNewPlayer();
        }

        //get player object from stored database
        playerObj = databaseObj.getPlayer();

        equipmentOwned = databaseObj.getListOfEquipmentOwned();
        updateAttackDefense();
    }

    private void updateAttackDefense() {
        playerAttack = 1;
        playerDefense = 0;

        equipmentOwned = databaseObj.getListOfEquipmentOwned();
        for (EquipmentObj e : equipmentOwned) {
            if (e.isEquipped()) {
                playerAttack += e.getEquipmentAttack();
                playerDefense += e.getEquipmentDefense();

                setImageEquipped(e.getEquipmentType());
            }
        }
        displayInfo();
        databaseObj.updatePlayerAttackDefense(playerAttack, playerDefense, playerObj.getPlayerId());
    }

    private void setImageEquipped(String equipmentType) {
        switch (equipmentType) {
            case EquipmentObj.HEAD_GEAR:
                helmetImage.setColorFilter(ContextCompat.getColor(EquipmentActivity.this, R.color.lighterLightBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case EquipmentObj.ARMOR:
                armorImage.setColorFilter(ContextCompat.getColor(EquipmentActivity.this, R.color.lighterLightBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case EquipmentObj.WEAPON:
                weaponImage.setColorFilter(ContextCompat.getColor(EquipmentActivity.this, R.color.lighterLightBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case EquipmentObj.SHIELD:
                shieldImage.setColorFilter(ContextCompat.getColor(EquipmentActivity.this, R.color.lighterLightBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            case EquipmentObj.FOOT_WEAR:
                armoredBootsImage.setColorFilter(ContextCompat.getColor(EquipmentActivity.this, R.color.lighterLightBlack), android.graphics.PorterDuff.Mode.MULTIPLY);
                break;
            default:
                break;
        }
    }

    private void displayInfo() {
        EquipmentActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerAttackTextView.setText(String.valueOf(playerAttack));
                playerDefenseTextView.setText(String.valueOf(playerDefense));
            }
        });
    }

    private void initViews() {
        backBtn = findViewById(R.id.backBtn);
        homeBtn = findViewById(R.id.homeBtn);

        headgearCardView = findViewById(R.id.headgearCardView);
        armorCardView = findViewById(R.id.armorCardView);
        weaponCardView = findViewById(R.id.weaponCardView);
        shieldCardView = findViewById(R.id.shieldCardView);
        footwearCardView = findViewById(R.id.footwearCardView);

        equipmentRecyclerView = findViewById(R.id.equipmentRecyclerView);
        equipmentBackground = findViewById(R.id.equipmentBackground);

        playerAttackTextView = findViewById(R.id.playerAttackTextView);
        playerDefenseTextView = findViewById(R.id.playerDefenseTextView);
        noEquipmentText = findViewById(R.id.noEquipmentText);

        helmetImage = findViewById(R.id.helmetImage);
        armorImage = findViewById(R.id.armorImage);
        armoredBootsImage = findViewById(R.id.armoredBootsImage);
        weaponImage = findViewById(R.id.weaponImage);
        shieldImage = findViewById(R.id.shieldImage);
        unequipAll = findViewById(R.id.unequipAll);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(EquipmentActivity.this, MenuActivity.class);
        //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    @Override
    public void onEquipmentItemClick(final int position) {
        countClicks++;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (countClicks == 1) {
                } else if (countClicks == 2) {
                    //equip/unequip the selected equipment
                    EquipmentObj selectedEquipment = equipmentArray.get(position);
                    String equipmentType = selectedEquipment.getEquipmentType();

                    selectedEquipment.setEquipped(!selectedEquipment.isEquipped());
                    equipmentAdaptor.notifyItemChanged(position, new Object());

                    if (selectedEquipment.isEquipped()) {
                        //unequip the other equipments of the same type and name
                        int count = 0;
                        for (EquipmentObj e : equipmentArray) {
                            //if it is the same equipment type, and its not the selected equipment, and it is equipped,
                            if (e.getEquipmentType().equals(equipmentType) && e.isEquipped() && !e.getEquipmentName().equals(selectedEquipment.getEquipmentName())) {
                                e.setEquipped(!e.isEquipped());
                                databaseObj.unequipEquipment(e.getEquipmentName());
                                equipmentAdaptor.notifyItemChanged(count, new Object());
                            }
                            count++;
                        }

                        //unequip everything else that could possibly have been equipped underneath the same equipment name
                        databaseObj.unequipEquipment(selectedEquipment.getEquipmentName());

                        //now equip the top most equipment name shown
                        databaseObj.equipEquipment(selectedEquipment.getEquipmentId());
                    } else {
                        //unequip selected equipment name
                        databaseObj.unequipEquipment(selectedEquipment.getEquipmentName());
                        setImageUnequipped(selectedEquipment.getEquipmentType());
                    }
                    //set image equipped as well
                    updateAttackDefense();
                }
                countClicks = 0;
            }
        }, 400);
    }

    private void setImageUnequipped(String equipmentType) {
        switch (equipmentType) {
            case EquipmentObj.HEAD_GEAR:
                helmetImage.setColorFilter(null);
                break;
            case EquipmentObj.ARMOR:
                armorImage.setColorFilter(null);
                break;
            case EquipmentObj.WEAPON:
                weaponImage.setColorFilter(null);
                break;
            case EquipmentObj.SHIELD:
                shieldImage.setColorFilter(null);
                break;
            case EquipmentObj.FOOT_WEAR:
                armoredBootsImage.setColorFilter(null);
                break;
            default:
                break;
        }
    }
}