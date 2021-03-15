package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wb.simplerpggame.R;
import com.wb.simplerpggame.objects.EquipmentObj;

import java.util.ArrayList;

public class EquipmentAdaptor extends RecyclerView.Adapter<EquipmentAdaptor.ViewHolder> {

    private Context context;
    private ArrayList<EquipmentObj> equipmentArray;
    private CustomEquipmentItemClickListener customEquipmentItemClickListener;

    public EquipmentAdaptor(CustomEquipmentItemClickListener customEquipmentItemClickListener, Context context) {
        this.customEquipmentItemClickListener = customEquipmentItemClickListener;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_equipment, parent, false);
        return new ViewHolder(view, customEquipmentItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (equipmentArray.get(position).getEquipmentType()) {
            case EquipmentObj.ARMOR:
                holder.equipmentTypeIcon.setImageResource(R.drawable.icon_light_black_body_armor_96);
                break;
            case EquipmentObj.WEAPON:
                holder.equipmentTypeIcon.setImageResource(R.drawable.icon_light_black_sword_96);
                break;
            case EquipmentObj.SHIELD:
                holder.equipmentTypeIcon.setImageResource(R.drawable.icon_light_black_shield_96);
                break;
            case EquipmentObj.FOOT_WEAR:
                holder.equipmentTypeIcon.setImageResource(R.drawable.icon_light_black_armored_boots);
                break;
            default:
                holder.equipmentTypeIcon.setImageResource(R.drawable.icon_light_black_spartan_helmet_96);
                break;
        }

        if (equipmentArray.get(position).getEquipmentId() != -1) {
            holder.parent.setVisibility(View.VISIBLE);

            int equipmentCount = equipmentArray.get(position).getEquipmentCount();
            String appendEquipmentCount;

            if (equipmentCount > 1) {
                appendEquipmentCount = " x " + equipmentArray.get(position).getEquipmentCount();
            } else {
                appendEquipmentCount = "";
            }

            holder.equipmentItem.setText(equipmentArray.get(position).getEquipmentName() + appendEquipmentCount);

            holder.equipmentAttackTextView.setText(String.valueOf(equipmentArray.get(position).getEquipmentAttack()));
            holder.equipmentDefenseTextView.setText(String.valueOf(equipmentArray.get(position).getEquipmentDefense()));

            if (equipmentArray.get(position).isEquipped()) {
                TransitionManager.beginDelayedTransition(holder.parent);
                holder.equippedIcon.setVisibility(View.VISIBLE);
            } else {
                TransitionManager.beginDelayedTransition(holder.parent);
                holder.equippedIcon.setVisibility(View.GONE);
            }

            RecyclerView.LayoutParams newLayoutParams = (RecyclerView.LayoutParams) holder.parentConstraint.getLayoutParams();
            newLayoutParams.topMargin = (int) context.getResources().getDimension(R.dimen._12sdp);

            if (position == 0) {
                newLayoutParams.topMargin = 0;
                holder.parentConstraint.setLayoutParams(newLayoutParams);

                holder.greyLine.setVisibility(View.VISIBLE);
                holder.equipmentHeader.setText(equipmentArray.get(position).getEquipmentType());
                holder.equipmentHeader.setVisibility(View.VISIBLE);
                holder.equipmentTypeIcon.setVisibility(View.VISIBLE);
            } else {
                if (equipmentArray.get(position).getEquipmentType().equals(equipmentArray.get(position - 1).getEquipmentType())) {
                    newLayoutParams.topMargin = 0;
                    holder.parentConstraint.setLayoutParams(newLayoutParams);

                    holder.greyLine.setVisibility(View.GONE);
                    holder.equipmentHeader.setVisibility(View.GONE);
                    holder.equipmentTypeIcon.setVisibility(View.GONE);
                } else {
                    newLayoutParams.topMargin = (int) context.getResources().getDimension(R.dimen._12sdp);
                    holder.parentConstraint.setLayoutParams(newLayoutParams);

                    holder.greyLine.setVisibility(View.VISIBLE);
                    holder.equipmentHeader.setText(equipmentArray.get(position).getEquipmentType());
                    holder.equipmentHeader.setVisibility(View.VISIBLE);
                    holder.equipmentTypeIcon.setVisibility(View.VISIBLE);
                }
            }
        } else {
            holder.greyLine.setVisibility(View.VISIBLE);
            holder.equipmentHeader.setText(equipmentArray.get(position).getEquipmentType());
            holder.equipmentHeader.setVisibility(View.VISIBLE);
            holder.equipmentTypeIcon.setVisibility(View.VISIBLE);
            holder.parent.setVisibility(View.GONE);
        }
    }

    public void setEquipmentArray(ArrayList<EquipmentObj> equipmentArray) {
        this.equipmentArray = equipmentArray;
    }

    @Override
    public int getItemCount() {
        return equipmentArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView equipmentItem, equipmentAttackTextView, equipmentDefenseTextView;
        private CardView parent;
        private TextView greyLine, equipmentHeader;
        private ConstraintLayout parentConstraint;
        CustomEquipmentItemClickListener customEquipmentItemClickListener;
        private ImageView equippedIcon, equipmentTypeIcon;

        public ViewHolder(@NonNull View itemView, CustomEquipmentItemClickListener customEquipmentItemClickListener) {
            super(itemView);

            equipmentItem = itemView.findViewById(R.id.equipmentItem);
            parent = itemView.findViewById(R.id.parent);
            greyLine = itemView.findViewById(R.id.greyLine);
            equipmentHeader = itemView.findViewById(R.id.equipmentHeader);
            parentConstraint = itemView.findViewById(R.id.parentConstraint);

            equipmentAttackTextView = itemView.findViewById(R.id.equipmentAttackTextView);
            equipmentDefenseTextView = itemView.findViewById(R.id.equipmentDefenseTextView);

            equippedIcon = itemView.findViewById(R.id.equippedIcon);

            equipmentTypeIcon = itemView.findViewById(R.id.equipmentTypeIcon);

            this.customEquipmentItemClickListener = customEquipmentItemClickListener;
            parent.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            customEquipmentItemClickListener.onEquipmentItemClick(getAdapterPosition());
        }
    }

    public interface CustomEquipmentItemClickListener {
        void onEquipmentItemClick(int position);
    }
}
