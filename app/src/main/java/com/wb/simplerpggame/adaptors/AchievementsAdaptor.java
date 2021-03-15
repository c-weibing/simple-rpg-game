package com.wb.simplerpggame.adaptors;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AlignmentSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wb.simplerpggame.R;
import com.wb.simplerpggame.objects.AchievementObj;
import com.wb.simplerpggame.objects.DatabaseObj;

import java.util.ArrayList;

public class AchievementsAdaptor extends RecyclerView.Adapter<AchievementsAdaptor.ViewHolder> {

    private ArrayList<AchievementObj> achievementsArray = new ArrayList<>();
    private DatabaseObj databaseObj;
    private Context context;
    private Toast toast;

    public AchievementsAdaptor(Context context) {
        this.context = context;
        databaseObj = new DatabaseObj(context);
    }

    @NonNull
    @Override
    public AchievementsAdaptor.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_achievements, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AchievementsAdaptor.ViewHolder holder, int position) {
        String monsterName = achievementsArray.get(position).getAchievementMonster().getMonsterName();
        int achievementGoal = achievementsArray.get(position).getAchievementGoal();
        int achievementCurrentProgress = achievementsArray.get(position).getAchievementCurrentProgress();
        int achievementGoldReward = achievementsArray.get(position).getAchievementGoldReward();
        boolean isClaimed = achievementsArray.get(position).isAchievementClaimed();

        holder.achievementTitle.setText(monsterName);
        holder.achievementCurrentProgress.setText(String.valueOf(achievementCurrentProgress));
        holder.achievementGoal.setText(String.valueOf(achievementGoal));
        holder.achievementGoldReward.setText(String.valueOf(achievementGoldReward));
        holder.achievementsProgressBar.setProgress((int) (((float) achievementCurrentProgress / (float) achievementGoal) * 100));

        if (isClaimed) {
            holder.claimReward.setEnabled(false);
            holder.claimReward.setText("CLAIMED");
        } else {
            if (achievementCurrentProgress == achievementGoal) {
                holder.claimReward.setEnabled(true);
            } else {
                holder.claimReward.setEnabled(false);
            }
            holder.claimReward.setText("CLAIM");
        }
    }

    @Override
    public int getItemCount() {
        return achievementsArray.size();
    }

    public void setAchievementsArray(ArrayList<AchievementObj> achievementsArray) {
        this.achievementsArray = achievementsArray;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView achievementTitle, achievementCurrentProgress, achievementGoal, achievementGoldReward;
        private ProgressBar achievementsProgressBar;
        private Button claimReward;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            achievementTitle = itemView.findViewById(R.id.achievementTitle);
            achievementCurrentProgress = itemView.findViewById(R.id.achievementCurrentProgress);
            achievementGoal = itemView.findViewById(R.id.achievementGoal);
            achievementGoldReward = itemView.findViewById(R.id.achievementGoldReward);

            achievementsProgressBar = itemView.findViewById(R.id.achievementsProgressBar);

            claimReward = itemView.findViewById(R.id.claimReward);
            claimReward.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AchievementObj achievementObj = achievementsArray.get(getAdapterPosition());
                    achievementObj.setAchievementClaimed(true);
                    databaseObj.claimAchievement(achievementObj.getAchievementId());
                    notifyItemChanged(getAdapterPosition(), new Object());

                    int[] values = databaseObj.getPlayerGold();
                    int playerId = values[0];
                    int playerGold = values[1];

                    int resultGold = playerGold + achievementObj.getAchievementGoldReward();

                    databaseObj.updatePlayerCol(DatabaseObj.PLAYER_GOLD, String.valueOf(resultGold), playerId);

                    showToast(centeredText("You claimed " + achievementObj.getAchievementGoldReward() + " gold"));
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
