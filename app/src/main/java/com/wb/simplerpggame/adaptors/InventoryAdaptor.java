package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wb.simplerpggame.InventoryActivity;
import com.wb.simplerpggame.R;
import com.wb.simplerpggame.Utils;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.HpPotObj;
import com.wb.simplerpggame.objects.InventoryObj;

import java.util.ArrayList;

public class InventoryAdaptor extends RecyclerView.Adapter<InventoryAdaptor.ViewHolder> {

    private ArrayList<InventoryObj> inventoryArray;
    private HpPotObj hpPotObj = new HpPotObj();
    private DatabaseObj databaseObj;
    private Context context;

    public InventoryAdaptor(Context context) {
        this.context = context;
        databaseObj = new DatabaseObj(context);
    }

    public void setInventoryArray(ArrayList<InventoryObj> inventoryArray) {
        this.inventoryArray = inventoryArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_inventory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (inventoryArray.get(position).getInventoryType().equals(HpPotObj.class.getSimpleName())) {

            if (Utils.getInstance(context).isStartBtnEnabled()) {
                holder.useItemBtn.setEnabled(true);
            } else {
                holder.useItemBtn.setEnabled(false);
            }

            holder.useItemBtn.setVisibility(View.VISIBLE);
            HpPotObj object = hpPotObj.getHpPotObject(inventoryArray.get(position).getInventoryName());

            String hpPotName = object.getHpPotName();
            holder.inventoryItem.setText(hpPotName);

            if (hpPotName.equals(HpPotObj.LESSER_HEALING_POTION.getHpPotName())) {
                holder.inventoryItemIcon.setImageResource(R.drawable.icon_lesser_96);
            } else if (hpPotName.equals(HpPotObj.GREATER_HEALING_POTION.getHpPotName())) {
                holder.inventoryItemIcon.setImageResource(R.drawable.icon_greater_96);
            } else if (hpPotName.equals(HpPotObj.HEALING_ELIXIR.getHpPotName())) {
                holder.inventoryItemIcon.setImageResource(R.drawable.icon_elixir_96);
            } else {
                holder.inventoryItemIcon.setImageResource(R.drawable.icon_minor_96);
            }

            holder.inventoryAmtTextView.setText(String.valueOf(inventoryArray.get(position).getInventoryAmt()) + " x");
            holder.inventoryDescriptionTextView.setText("Recovers " + String.valueOf(object.getRecoverAmt()) + " hp");
        }
    }

    @Override
    public int getItemCount() {
        return inventoryArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public static final String UPDATE_HP_FROM_INVENTORY = "UPDATE_HP_FROM_INVENTORY";
        public static final String UPDATED_HP_VALUE = "UPDATED_HP_VALUE";
        private TextView inventoryItem, inventoryAmtTextView, inventoryDescriptionTextView;
        private ImageView inventoryItemIcon;
        private Button useItemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            inventoryItem = itemView.findViewById(R.id.inventoryItem);
            inventoryItemIcon = itemView.findViewById(R.id.inventoryItemIcon);
            inventoryAmtTextView = itemView.findViewById(R.id.inventoryAmtTextView);
            useItemBtn = itemView.findViewById(R.id.useItemBtn);
            inventoryDescriptionTextView = itemView.findViewById(R.id.inventoryDescriptionTextView);

            useItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    InventoryObj inventoryObj = inventoryArray.get(getAdapterPosition());
                    int resultAmt = inventoryObj.getInventoryAmt() - 1;

                    if (inventoryObj.getInventoryType().equals(HpPotObj.class.getSimpleName())) {
                        HpPotObj object = hpPotObj.getHpPotObject(inventoryObj.getInventoryName());

                        int[] values = databaseObj.getPlayerHp();
                        int playerId = values[0];
                        int playerHp = values[1];
                        int playerMaxHp = values[2];

                        //don't broadcast anything since the hp isn't going to change
                        if (playerHp != playerMaxHp) {
                            int resultHp = playerHp + object.getRecoverAmt();

                            if (resultHp > playerMaxHp) {
                                resultHp = playerMaxHp;
                            }

                            databaseObj.updatePlayerCol(DatabaseObj.PLAYER_CURRENT_HP, String.valueOf(resultHp), playerId);

                            Intent i = new Intent();
                            i.setAction(UPDATE_HP_FROM_INVENTORY);
                            i.putExtra(UPDATED_HP_VALUE, resultHp);
                            context.sendBroadcast(i);
                        }
                    }

                    if (resultAmt > 0) {
                        inventoryObj.setInventoryAmt(resultAmt);
                        databaseObj.decreaseByOneInventory(inventoryObj.getInventoryId());
                        notifyItemChanged(getAdapterPosition(), new Object());
                    } else {
                        inventoryArray.remove(inventoryObj);
                        databaseObj.deleteFromInventory(inventoryObj.getInventoryId());
                        notifyItemRemoved(getAdapterPosition());
                        useItemBtn.setVisibility(View.INVISIBLE);

                        ((InventoryActivity) context).showRecyclerOrText();
                    }
                }
            });
        }
    }
}
