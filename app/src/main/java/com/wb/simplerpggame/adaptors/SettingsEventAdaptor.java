package com.wb.simplerpggame.adaptors;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.wb.simplerpggame.R;
import com.wb.simplerpggame.SettingsActivity;
import com.wb.simplerpggame.Utils;
import com.wb.simplerpggame.objects.SettingsObj;

import java.util.ArrayList;

public class SettingsEventAdaptor extends RecyclerView.Adapter<SettingsEventAdaptor.ViewHolder> {
    public static final String UPDATE_MAIN_EVENTS = "UPDATE_MAIN_EVENTS";
    private ArrayList<SettingsObj> settingsEventArray;
    private Context context;

    public SettingsEventAdaptor(Context context) {
        this.context = context;
    }

    public void setSettingsEventArray(ArrayList<SettingsObj> settingsEventArray) {
        this.settingsEventArray = settingsEventArray;
    }

    @NonNull
    @Override
    public SettingsEventAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_settings_event, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsEventAdaptor.ViewHolder holder, final int position) {
        if (settingsEventArray.get(position).getSettingsDescription().equals("")) {
            holder.layoutTitleOnly.setVisibility(View.VISIBLE);
            holder.layoutTitleAndDescription.setVisibility(View.GONE);

            holder.settingsTitleOnly.setText(settingsEventArray.get(position).getSettingsTitle());

            if (settingsEventArray.get(position).getSettingsType().equals(SettingsActivity.SETTINGS_COLOR)) {
                holder.settingsColorOnly.setVisibility(View.VISIBLE);

                holder.settingsColorOnly.setBackgroundTintList(setBackground(settingsEventArray.get(position).getSettingsDefaultColor()));

                holder.settingsSwitchOnly.setVisibility(View.INVISIBLE);
            } else if (settingsEventArray.get(position).getSettingsType().equals(SettingsActivity.SETTINGS_SWITCH)) {
                holder.settingsColorOnly.setVisibility(View.GONE);
                holder.settingsSwitchOnly.setVisibility(View.VISIBLE);
                holder.settingsSwitchOnly.setChecked(settingsEventArray.get(position).isSettingsEnabled());
            }
        } else {
            holder.layoutTitleOnly.setVisibility(View.GONE);
            holder.layoutTitleAndDescription.setVisibility(View.VISIBLE);

            holder.settingsTitle.setText(settingsEventArray.get(position).getSettingsTitle());
            holder.settingsDescription.setText(settingsEventArray.get(position).getSettingsDescription());

            if (settingsEventArray.get(position).getSettingsType().equals(SettingsActivity.SETTINGS_COLOR)) {
                holder.settingsColor.setVisibility(View.VISIBLE);

                holder.settingsColor.setBackgroundTintList(setBackground(settingsEventArray.get(position).getSettingsDefaultColor()));

                holder.settingsSwitch.setVisibility(View.INVISIBLE);
            } else if (settingsEventArray.get(position).getSettingsType().equals(SettingsActivity.SETTINGS_SWITCH)) {
                holder.settingsColor.setVisibility(View.GONE);
                holder.settingsSwitch.setVisibility(View.VISIBLE);
                holder.settingsSwitch.setChecked(settingsEventArray.get(position).isSettingsEnabled());
            }
        }
    }

    private ColorStateList setBackground(int settingsDefaultColor) {
        switch (settingsDefaultColor) {
            case 1:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick1));
            case 2:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick2));
            case 3:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick3));
            case 4:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick4));
            case 5:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick5));
            case 6:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick6));
            case 7:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick7));
            case 8:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.colorPick8));
            default:
                return ColorStateList.valueOf(context.getResources().getColor(R.color.transparent));
        }
    }

    @Override
    public int getItemCount() {
        return settingsEventArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView settingsTitle, settingsDescription, settingsTitleOnly;
        private SwitchMaterial settingsSwitch, settingsSwitchOnly;
        private ConstraintLayout layoutTitleAndDescription, layoutTitleOnly, clickableLayout;
        private MaterialButton settingsColorOnly, settingsColor;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            layoutTitleAndDescription = itemView.findViewById(R.id.layoutTitleAndDescription);
            settingsTitle = itemView.findViewById(R.id.settingsTitle);
            settingsDescription = itemView.findViewById(R.id.settingsDescription);
            settingsSwitch = itemView.findViewById(R.id.settingsSwitch);
            settingsColor = itemView.findViewById(R.id.settingsColor);

            layoutTitleOnly = itemView.findViewById(R.id.layoutTitleOnly);
            settingsTitleOnly = itemView.findViewById(R.id.settingsTitleOnly);
            settingsSwitchOnly = itemView.findViewById(R.id.settingsSwitchOnly);
            settingsColorOnly = itemView.findViewById(R.id.settingsColorOnly);

            clickableLayout = itemView.findViewById(R.id.clickableLayout);

            clickableLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final SettingsObj settingsObj = settingsEventArray.get(getAdapterPosition());

                    if (settingsObj.getSettingsType().equals(SettingsActivity.SETTINGS_COLOR)) {

                        LayoutInflater inflater = LayoutInflater.from(view.getContext());
                        View colorPickerLayout = inflater.inflate(R.layout.settings_event_color_picker, null);

                        final AlertDialog colorPickerDialog = new MaterialAlertDialogBuilder(context, R.style.MaterialAlertDialog_Rounded)
                                .setCustomTitle(colorPickerLayout)
                                .create();
                        colorPickerDialog.show();

                        ArrayList<MaterialButton> buttonsList = new ArrayList<>();
                        MaterialButton colorPick1 = colorPickerLayout.findViewById(R.id.colorPick1);
                        MaterialButton colorPick2 = colorPickerLayout.findViewById(R.id.colorPick2);
                        MaterialButton colorPick3 = colorPickerLayout.findViewById(R.id.colorPick3);
                        MaterialButton colorPick4 = colorPickerLayout.findViewById(R.id.colorPick4);
                        MaterialButton colorPick5 = colorPickerLayout.findViewById(R.id.colorPick5);
                        MaterialButton colorPick6 = colorPickerLayout.findViewById(R.id.colorPick6);
                        MaterialButton colorPick7 = colorPickerLayout.findViewById(R.id.colorPick7);
                        MaterialButton colorPick8 = colorPickerLayout.findViewById(R.id.colorPick8);

                        buttonsList.add(colorPick1);
                        buttonsList.add(colorPick2);
                        buttonsList.add(colorPick3);
                        buttonsList.add(colorPick4);
                        buttonsList.add(colorPick5);
                        buttonsList.add(colorPick6);
                        buttonsList.add(colorPick7);
                        buttonsList.add(colorPick8);

                        int count = 1;
                        for (MaterialButton b : buttonsList) {
                            final int finalCount = count;
                            if (settingsObj.getSettingsDefaultColor() == count) {
                                b.setEnabled(false);
                                b.setIcon(context.getResources().getDrawable(R.drawable.icon_checkmark_96));
                            }

                            b.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    settingsObj.setSettingsDefaultColor(finalCount);
                                    notifyItemChanged(getAdapterPosition(), new Object());
                                    Utils.getInstance(context).setColor(settingsEventArray.get(getAdapterPosition()).getSettingsName(), finalCount);

                                    Intent i = new Intent();
                                    i.setAction(UPDATE_MAIN_EVENTS);
                                    context.sendBroadcast(i);

                                    colorPickerDialog.dismiss();
                                }
                            });
                            count++;
                        }

                        colorPickerDialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_border);

                        int width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.80);

                        colorPickerDialog.getWindow().setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT);

                        colorPickerLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                            }
                        });
                    } else {
                        boolean switchStatus = !settingsObj.isSettingsEnabled();

                        settingsObj.setSettingsEnabled(switchStatus);
                        notifyItemChanged(getAdapterPosition(), new Object());
                        Utils.getInstance(context).setSwitch(settingsEventArray.get(getAdapterPosition()).getSettingsName(), switchStatus);
                    }
                }
            });
        }
    }
}
