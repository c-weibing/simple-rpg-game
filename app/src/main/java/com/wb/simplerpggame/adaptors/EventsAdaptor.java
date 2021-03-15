package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.content.res.ColorStateList;
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

import com.wb.simplerpggame.MainActivity;
import com.wb.simplerpggame.R;
import com.wb.simplerpggame.SettingsActivity;
import com.wb.simplerpggame.Utils;
import com.wb.simplerpggame.objects.EventObj;

import java.util.ArrayList;

public class EventsAdaptor extends RecyclerView.Adapter<EventsAdaptor.ViewHolder> {

    private ArrayList<EventObj> eventsArray = new ArrayList<>();

    public EventsAdaptor() {
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_events, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();

        holder.eventTime.setText(eventsArray.get(position).getEventTime());
        holder.eventInfo.setText(eventsArray.get(position).getEventInfo());

        if (!eventsArray.get(position).getEventSettingsName().equals(SettingsActivity.SETTINGS_ACHIEVEMENTS_SWITCH)) {
            int colorNo = Utils.getInstance(context).getColor(eventsArray.get(position).getEventSettingsName());

            holder.parent.setCardBackgroundColor(setBackground(colorNo, context));
        }

        //check eventType
        if (eventsArray.get(position).getEventType().equals(MainActivity.BATTLE_WON) || eventsArray.get(position).getEventType().equals(MainActivity.BATTLE_LOST) || eventsArray.get(position).getEventType().equals(MainActivity.MONSTER_DROPS)) {
            if (eventsArray.get(position).getEventExtraInfo() != null) {
                //when extra info exists
                if (eventsArray.get(position).isExpanded()) {
                    //if expanded, show expanded
                    TransitionManager.beginDelayedTransition(holder.parent);
                    holder.eventExtraInfo.setText(eventsArray.get(position).getEventExtraInfo());
                    holder.expandBtn.setVisibility(View.INVISIBLE);
                    holder.minimizeBtn.setVisibility(View.VISIBLE);
                    holder.eventExtraInfoLayout.setVisibility(View.VISIBLE);
                } else {
                    //else show minimized
                    TransitionManager.beginDelayedTransition(holder.parent);
                    holder.expandBtn.setVisibility(View.VISIBLE);
                    holder.minimizeBtn.setVisibility(View.INVISIBLE);
                    holder.eventExtraInfoLayout.setVisibility(View.GONE);
                }
            } else {
                //if there is no extra info, hide it
                holder.expandBtn.setVisibility(View.INVISIBLE);
                holder.minimizeBtn.setVisibility(View.INVISIBLE);
                holder.eventExtraInfoLayout.setVisibility(View.GONE);
            }
        } else {
            //else if no extra info and type is different, hide it
            holder.expandBtn.setVisibility(View.INVISIBLE);
            holder.minimizeBtn.setVisibility(View.INVISIBLE);
            holder.eventExtraInfoLayout.setVisibility(View.GONE);
        }
    }

    private ColorStateList setBackground(int colorNo, Context context) {
        switch (colorNo) {
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
        return eventsArray.size();
    }

    public void setEventsArray(ArrayList<EventObj> eventsArray) {
        this.eventsArray = eventsArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView eventTime, eventInfo, eventExtraInfo;
        private ConstraintLayout eventExtraInfoLayout;
        private ImageView expandBtn, minimizeBtn;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTime = itemView.findViewById(R.id.eventTimeTextView);
            eventInfo = itemView.findViewById(R.id.eventInfoTextView);
            eventExtraInfo = itemView.findViewById(R.id.eventExtraInfoTextView);
            eventExtraInfoLayout = itemView.findViewById(R.id.eventExtraInfoLayout);
            expandBtn = itemView.findViewById(R.id.expandBtn);
            minimizeBtn = itemView.findViewById(R.id.minimizeBtn);
            parent = itemView.findViewById(R.id.parent);

            expandBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventObj eventObj = eventsArray.get(getAdapterPosition());
                    eventObj.setExpanded(!eventObj.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });

            minimizeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventObj eventObj = eventsArray.get(getAdapterPosition());
                    eventObj.setExpanded(!eventObj.isExpanded());
                    notifyItemChanged(getAdapterPosition());
                }
            });
        }
    }
}
