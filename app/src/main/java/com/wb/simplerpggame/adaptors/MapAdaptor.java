package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.wb.simplerpggame.R;

import java.util.ArrayList;

public class MapAdaptor extends RecyclerView.Adapter<MapAdaptor.ViewHolder> {

    private ArrayList<String> mapsArray;
    private String currentMap;
    private Context mContext;
    private CustomMapItemClickListener customMapItemClickListener;

    public MapAdaptor(ArrayList<String> mapsArray, String currentMap, Context mContext, CustomMapItemClickListener customMapItemClickListener) {
        this.mapsArray = mapsArray;
        this.currentMap = currentMap;
        this.mContext = mContext;
        this.customMapItemClickListener = customMapItemClickListener;
    }

    @NonNull
    @Override
    public MapAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_map, parent, false);
        return new ViewHolder(view, customMapItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MapAdaptor.ViewHolder holder, int position) {
        holder.mapItem.setText(mapsArray.get(position));

        if (mapsArray.get(position).equals(currentMap)) {
            holder.mapBackgroundSelectable.setBackgroundResource(0);
            holder.mapItem.setTextColor(mContext.getResources().getColor(R.color.darkGrey));
        } else {
            TypedValue outValue = new TypedValue();
            mContext.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);

            holder.mapBackgroundSelectable.setBackgroundResource(outValue.resourceId);
            holder.mapItem.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
        }
    }

    @Override
    public int getItemCount() {
        return mapsArray.size();
    }

    public void setCurrentMap(String currentMap) {
        this.currentMap = currentMap;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mapItem;
        private ConstraintLayout mapBackgroundSelectable;
        CustomMapItemClickListener customMapItemClickListener;

        public ViewHolder(@NonNull View itemView, CustomMapItemClickListener customMapItemClickListener) {
            super(itemView);

            mapItem = itemView.findViewById(R.id.mapItem);
            mapBackgroundSelectable = itemView.findViewById(R.id.mapBackgroundSelectable);
            this.customMapItemClickListener = customMapItemClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            customMapItemClickListener.onMapItemClick(getAdapterPosition());
        }
    }

    public interface CustomMapItemClickListener {
        void onMapItemClick(int position);
    }
}
