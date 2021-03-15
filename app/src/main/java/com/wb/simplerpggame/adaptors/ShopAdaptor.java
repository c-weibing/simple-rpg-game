package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.wb.simplerpggame.R;
import com.wb.simplerpggame.ShopActivity;
import com.wb.simplerpggame.objects.DatabaseObj;
import com.wb.simplerpggame.objects.EquipmentObj;
import com.wb.simplerpggame.objects.HpPotObj;
import com.wb.simplerpggame.objects.InventoryObj;

import java.util.ArrayList;

public class ShopAdaptor extends RecyclerView.Adapter<ShopAdaptor.ViewHolder> {

    private ArrayList<Object> inventoryArray;
    private DatabaseObj databaseObj;
    private Context context;
    private Toast toast;

    public ShopAdaptor(Context context) {
        this.context = context;
        databaseObj = new DatabaseObj(context);
    }

    public void setInventoryArray(ArrayList<Object> inventoryArray) {
        this.inventoryArray = inventoryArray;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_shop, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.buySellItemBtn.setVisibility(View.VISIBLE);
        if (ShopActivity.inventoryBtn.isPressed()) {
            holder.buySellItemBtn.setText("SELL");
        } else {
            holder.buySellItemBtn.setText("BUY");
        }

        holder.equippedIcon.setVisibility(View.INVISIBLE);

        if (inventoryArray.get(position).getClass().getSimpleName().equals(InventoryObj.class.getSimpleName())) {
            InventoryObj inventoryObj = (InventoryObj) inventoryArray.get(position);

            if (inventoryObj.getInventoryType().equals(HpPotObj.class.getSimpleName())) {
                HpPotObj hpPotObj = HpPotObj.getHpPotObject(inventoryObj.getInventoryName());

                String hpPotName = hpPotObj.getHpPotName();
                holder.inventoryItem.setText(hpPotName);

                holder.inventoryDescriptionTextView.setVisibility(View.VISIBLE);
                holder.inventoryDescriptionTextView.setText("Recovers " + String.valueOf(hpPotObj.getRecoverAmt()) + " hp");

                if (hpPotName.equals(HpPotObj.LESSER_HEALING_POTION.getHpPotName())) {
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_lesser_96);
                } else if (hpPotName.equals(HpPotObj.GREATER_HEALING_POTION.getHpPotName())) {
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_greater_96);
                } else if (hpPotName.equals(HpPotObj.HEALING_ELIXIR.getHpPotName())) {
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_elixir_96);
                } else {
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_minor_96);
                }

                if (ShopActivity.inventoryBtn.isPressed()) {
                    holder.buySellAmtTextView.setText(hpPotObj.getSellAmt() + "g");

                    holder.inventoryAmtTextView.setVisibility(View.VISIBLE);
                    holder.inventoryAmtTextView.setText(String.valueOf(inventoryObj.getInventoryAmt()) + " x");
                } else {
                    holder.buySellAmtTextView.setText(hpPotObj.getBuyAmt() + "g");

                    holder.inventoryAmtTextView.setVisibility(View.INVISIBLE);
                }
            }
        } else if (inventoryArray.get(position).getClass().getSimpleName().equals(EquipmentObj.class.getSimpleName())) {
            EquipmentObj equipmentObj = (EquipmentObj) inventoryArray.get(position);

            holder.inventoryItem.setText(equipmentObj.getEquipmentName());

            switch (equipmentObj.getEquipmentType()) {
                case EquipmentObj.ARMOR:
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_light_black_body_armor_96);
                    break;
                case EquipmentObj.WEAPON:
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_light_black_sword_96);
                    break;
                case EquipmentObj.SHIELD:
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_light_black_shield_96);
                    break;
                case EquipmentObj.FOOT_WEAR:
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_light_black_armored_boots);
                    break;
                default:
                    holder.inventoryItemIcon.setImageResource(R.drawable.icon_light_black_spartan_helmet_96);
                    break;
            }

            holder.inventoryDescriptionTextView.setText("Attack: " + String.valueOf(equipmentObj.getEquipmentAttack()) + " | Defense: " + String.valueOf(equipmentObj.getEquipmentDefense()));

            if (ShopActivity.inventoryBtn.isPressed()) {
                holder.buySellAmtTextView.setText(equipmentObj.getEquipmentSellAmt() + "g");

                holder.inventoryAmtTextView.setVisibility(View.VISIBLE);
                holder.inventoryAmtTextView.setText(String.valueOf(equipmentObj.getEquipmentCount()) + " x");
            } else {
                holder.buySellAmtTextView.setText(equipmentObj.getEquipmentBuyAmt() + "g");

                holder.inventoryAmtTextView.setVisibility(View.INVISIBLE);
            }

            if (equipmentObj.isEquipped()) {
                holder.equippedIcon.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return inventoryArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public static final String UPDATE_GOLD_FROM_SHOP = "UPDATE_GOLD_FROM_SHOP";
        public static final String UPDATED_GOLD_VALUE = "UPDATED_GOLD_VALUE";
        private TextView inventoryItem, inventoryAmtTextView, inventoryDescriptionTextView, buySellAmtTextView;
        private ImageView inventoryItemIcon, equippedIcon;
        private Button buySellItemBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            inventoryItem = itemView.findViewById(R.id.inventoryItem);
            inventoryItemIcon = itemView.findViewById(R.id.inventoryItemIcon);
            inventoryAmtTextView = itemView.findViewById(R.id.inventoryAmtTextView);
            buySellItemBtn = itemView.findViewById(R.id.buySellItemBtn);
            inventoryDescriptionTextView = itemView.findViewById(R.id.inventoryDescriptionTextView);
            equippedIcon = itemView.findViewById(R.id.equippedIcon);
            buySellAmtTextView = itemView.findViewById(R.id.buySellAmtTextView);

            buySellItemBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (ShopActivity.inventoryBtn.isPressed()) {
                        //SELL
                        if (inventoryArray.get(getAdapterPosition()).getClass().getSimpleName().equals(InventoryObj.class.getSimpleName())) {
                            //if inventory equipment clicked
                            InventoryObj inventoryObj = (InventoryObj) inventoryArray.get(getAdapterPosition());

                            int resultAmt = inventoryObj.getInventoryAmt() - 1;

                            //update gold amt
                            if (inventoryObj.getInventoryType().equals(HpPotObj.class.getSimpleName())) {
                                HpPotObj hpPotObj = HpPotObj.getHpPotObject(inventoryObj.getInventoryName());

                                int[] values = databaseObj.getPlayerGold();
                                int playerId = values[0];
                                int playerGold = values[1];

                                int resultGold = playerGold + hpPotObj.getSellAmt();

                                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                                Intent i = new Intent();
                                i.setAction(UPDATE_GOLD_FROM_SHOP);
                                i.putExtra(UPDATED_GOLD_VALUE, resultGold);
                                context.sendBroadcast(i);
                            }

                            //update number of item
                            if (resultAmt > 0) {
                                inventoryObj.setInventoryAmt(resultAmt);
                                databaseObj.decreaseByOneInventory(inventoryObj.getInventoryId());
                                notifyItemChanged(getAdapterPosition(), new Object());
                            } else {
                                //delete item
                                inventoryArray.remove(inventoryObj);
                                databaseObj.deleteFromInventory(inventoryObj.getInventoryId());
                                notifyItemRemoved(getAdapterPosition());
                                buySellItemBtn.setVisibility(View.INVISIBLE);

                                ((ShopActivity) context).showRecyclerOrText();
                            }

                            showToast(centeredText("You sold\n").append(boldText("『 " + inventoryObj.getInventoryName() + " 』")));
                        } else if (inventoryArray.get(getAdapterPosition()).getClass().getSimpleName().equals(EquipmentObj.class.getSimpleName())) {
                            //if equipment object clicked
                            final EquipmentObj equipmentObj = (EquipmentObj) inventoryArray.get(getAdapterPosition());

                            int resultAmt = equipmentObj.getEquipmentCount() - 1;

                            //update number of item
                            if (resultAmt > 0) {
                                //update gold amt
                                int values[] = databaseObj.getPlayerGold();
                                int playerId = values[0];
                                int playerGold = values[1];

                                int resultGold = playerGold + equipmentObj.getEquipmentSellAmt();

                                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                                Intent i = new Intent();
                                i.setAction(UPDATE_GOLD_FROM_SHOP);
                                i.putExtra(UPDATED_GOLD_VALUE, resultGold);
                                context.sendBroadcast(i);

                                equipmentObj.setEquipmentCount(resultAmt);
                                databaseObj.deleteOneEquipmentNotEquipped(equipmentObj.getEquipmentName());
                                notifyItemChanged(getAdapterPosition(), new Object());
                                showToast(centeredText("You sold\n").append(boldText("『 " + equipmentObj.getEquipmentName() + " 』")));
                            } else {
                                if (equipmentObj.isEquipped()) {
                                    //check to delete last piece of equipment
                                    final boolean inventoryBtnPressed, allCategoryBtnPressed, equipmentCategoryBtnPressed, consumablesCategoryBtnPressed;
                                    inventoryBtnPressed = ShopActivity.inventoryBtn.isPressed();
                                    allCategoryBtnPressed = ShopActivity.allCategoryBtn.isPressed();
                                    equipmentCategoryBtnPressed = ShopActivity.equipmentCategoryBtn.isPressed();
                                    consumablesCategoryBtnPressed = ShopActivity.consumablesCategoryBtn.isPressed();

                                    LayoutInflater inflater = LayoutInflater.from(view.getContext());
                                    View exitTitleLayout = inflater.inflate(R.layout.title_layout_left_align, null);

                                    TextView customTitle = exitTitleLayout.findViewById(R.id.title);
                                    customTitle.setText("Sell Confirmation");

                                    final AlertDialog exitDialog = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_Rounded)
                                            .setCustomTitle(exitTitleLayout)
                                            .setMessage("This item is currently equipped.\nAre you sure you wish to sell?")
                                            .create();

                                    exitDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogInterface dialogInterface) {
                                            if (inventoryBtnPressed) {
                                                ShopActivity.inventoryBtn.setPressed(true);
                                            }

                                            if (allCategoryBtnPressed) {
                                                ShopActivity.allCategoryBtn.setPressed(true);
                                            }

                                            if (equipmentCategoryBtnPressed) {
                                                ShopActivity.equipmentCategoryBtn.setPressed(true);
                                            }

                                            if (consumablesCategoryBtnPressed) {
                                                ShopActivity.consumablesCategoryBtn.setPressed(true);
                                            }
                                        }
                                    });

                                    exitDialog.setButton(AlertDialog.BUTTON_POSITIVE, "YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            //update gold amt
                                            int[] values = databaseObj.getPlayerGold();
                                            int playerId = values[0];
                                            int playerGold = values[1];

                                            int resultGold = playerGold + equipmentObj.getEquipmentSellAmt();

                                            databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                                            Intent i2 = new Intent();
                                            i2.setAction(UPDATE_GOLD_FROM_SHOP);
                                            i2.putExtra(UPDATED_GOLD_VALUE, resultGold);
                                            context.sendBroadcast(i2);

                                            inventoryArray.remove(equipmentObj);
                                            databaseObj.deleteOneEquipmentEquipped(equipmentObj.getEquipmentName());
                                            notifyItemRemoved(getAdapterPosition());
                                            buySellItemBtn.setVisibility(View.INVISIBLE);

                                            ((ShopActivity) context).showRecyclerOrText();

                                            showToast(centeredText("You sold\n").append(boldText("『 " + equipmentObj.getEquipmentName() + " 』")));

                                            ((ShopActivity) context).updateAttackDefense();
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
                                    messageText.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen._12ssp));

                                    Button btnPositive = exitDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnPositive.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen._12ssp));

                                    Button btnNegative = exitDialog.getButton(AlertDialog.BUTTON_NEGATIVE);
                                    btnNegative.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen._12ssp));

                                    int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.85);
                                    //int height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.25);

                                    exitDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);
                                } else {
                                    //just delete if it's not equipped
                                    inventoryArray.remove(equipmentObj);
                                    databaseObj.deleteOneEquipmentEquipped(equipmentObj.getEquipmentName());
                                    notifyItemRemoved(getAdapterPosition());
                                    buySellItemBtn.setVisibility(View.INVISIBLE);

                                    ((ShopActivity) context).showRecyclerOrText();

                                    showToast(centeredText("You sold\n").append(boldText("『 " + equipmentObj.getEquipmentName() + " 』")));
                                }
                            }
                        }
                    } else {
                        //BUY
                        if (inventoryArray.get(getAdapterPosition()).getClass().getSimpleName().equals(InventoryObj.class.getSimpleName())) {
                            //if inventory obj clicked
                            InventoryObj inventoryObj = (InventoryObj) inventoryArray.get(getAdapterPosition());

                            //update gold amt
                            if (inventoryObj.getInventoryType().equals(HpPotObj.class.getSimpleName())) {
                                //buy hp pot
                                HpPotObj hpPotObj = HpPotObj.getHpPotObject(inventoryObj.getInventoryName());

                                int values[] = databaseObj.getPlayerGold();
                                int playerId = values[0];
                                int playerGold = values[1];

                                if (playerGold >= hpPotObj.getBuyAmt()) {
                                    int resultGold = playerGold - hpPotObj.getBuyAmt();

                                    databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                                    Intent i = new Intent();
                                    i.setAction(UPDATE_GOLD_FROM_SHOP);
                                    i.putExtra(UPDATED_GOLD_VALUE, resultGold);
                                    context.sendBroadcast(i);

                                    int idInInventory = databaseObj.checkIfExistsInInventory(hpPotObj.getHpPotName());
                                    if (idInInventory == -1) {
                                        databaseObj.addToInventory(hpPotObj, 1);
                                    } else {
                                        //increment by 1
                                        databaseObj.updateIncrementInventory(idInInventory, 1);
                                    }

                                    showToast(centeredText("You bought\n").append(boldText("『 " + hpPotObj.getHpPotName() + " 』")));
                                } else {
                                    showToast(centeredText("You do not have enough gold"));
                                }
                            }
                        } else if (inventoryArray.get(getAdapterPosition()).getClass().getSimpleName().equals(EquipmentObj.class.getSimpleName())) {
                            //buy equipment
                            EquipmentObj equipmentObj = (EquipmentObj) inventoryArray.get(getAdapterPosition());

                            int values[] = databaseObj.getPlayerGold();
                            int playerId = values[0];
                            int playerGold = values[1];

                            if (playerGold >= equipmentObj.getEquipmentBuyAmt()) {
                                int resultGold = playerGold - equipmentObj.getEquipmentBuyAmt();

                                databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                                Intent i = new Intent();
                                i.setAction(UPDATE_GOLD_FROM_SHOP);
                                i.putExtra(UPDATED_GOLD_VALUE, resultGold);
                                context.sendBroadcast(i);

                                databaseObj.addToEquipmentOwned(equipmentObj);

                                showToast(centeredText("You bought\n").append(boldText("『 " + equipmentObj.getEquipmentName() + " 』")));
                            } else {
                                showToast(centeredText("You do not have enough gold"));
                            }
                        }
                    }
                }
            });
        }
    }

    private SpannableStringBuilder boldText(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        sb.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    private SpannableStringBuilder centeredText(String text) {
        SpannableStringBuilder sb = new SpannableStringBuilder(text);
        sb.setSpan(new AlignmentSpan.Standard(Layout.Alignment.ALIGN_CENTER), 0, text.length() - 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        return sb;
    }

    private void showToast(SpannableStringBuilder text) {
        if (toast != null) {
            toast.cancel();
        }

        toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }
}
