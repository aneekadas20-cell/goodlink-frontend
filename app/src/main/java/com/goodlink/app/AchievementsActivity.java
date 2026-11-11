package com.goodlink.app;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.Achievement;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AchievementsActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achievements);

        listContainer = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        loadAchievements();
    }

    private void loadAchievements() {
        progressBar.setVisibility(View.VISIBLE);
        listContainer.removeAllViews();

        AppRepository.getAchievements(new AppRepository.ApiCallback<List<Achievement>>() {
            @Override
            public void onSuccess(List<Achievement> achievements) {
                progressBar.setVisibility(View.GONE);
                if (achievements == null || achievements.isEmpty()) {
                    showEmptyMessage();
                    return;
                }
                addStatsBanner(achievements);
                for (Achievement achievement : achievements) {
                    addAchievementView(achievement);
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(listContainer, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addStatsBanner(List<Achievement> achievements) {
        float d = getResources().getDisplayMetrics().density;

        Set<String> contributors = new HashSet<>();
        for (Achievement a : achievements) {
            if (a.getVolunteerName() != null) {
                contributors.add(a.getVolunteerName().split(",")[0].trim());
            }
        }

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        banner.setWeightSum(2f);
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        blp.setMargins((int) (4 * d), 0, (int) (4 * d), (int) (8 * d));
        banner.setLayoutParams(blp);

        banner.addView(statBox(String.valueOf(achievements.size()), "Milestones Reached", d));
        banner.addView(statBox(String.valueOf(contributors.size()), "Contributors", d));

        listContainer.addView(banner);
    }

    private MaterialCardView statBox(String number, String label, float d) {
        MaterialCardView card = new MaterialCardView(this);
        card.setRadius(18 * d);
        card.setCardElevation(2 * d);
        card.setCardBackgroundColor(Color.parseColor("#EEF2FF"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        lp.setMargins((int) (4 * d), 0, (int) (4 * d), 0);
        card.setLayoutParams(lp);

        LinearLayout inner = new LinearLayout(this);
        inner.setOrientation(LinearLayout.VERTICAL);
        inner.setPadding((int) (14 * d), (int) (14 * d), (int) (14 * d), (int) (14 * d));

        TextView num = new TextView(this);
        num.setText(number);
        num.setTextColor(Color.parseColor("#4338CA"));
        num.setTextSize(22);
        num.setTypeface(null, Typeface.BOLD);
        inner.addView(num);

        TextView lab = new TextView(this);
        lab.setText(label);
        lab.setTextColor(Color.parseColor("#6B7280"));
        lab.setTextSize(12);
        inner.addView(lab);

        card.addView(inner);
        return card;
    }

    private void addAchievementView(Achievement ach) {
        View item = getLayoutInflater().inflate(R.layout.item_achievement, listContainer, false);

        TextView iconView = item.findViewById(R.id.iconView);
        TextView titleView = item.findViewById(R.id.titleView);
        TextView volunteerView = item.findViewById(R.id.volunteerView);
        TextView descriptionView = item.findViewById(R.id.descriptionView);
        TextView dateView = item.findViewById(R.id.dateView);

        iconView.setText(ach.getIcon());
        titleView.setText(ach.getTitle());
        volunteerView.setText(ach.getVolunteerName());
        descriptionView.setText(ach.getDescription());
        dateView.setText(ach.getDate());

        listContainer.addView(item);
    }

    private void showEmptyMessage() {
        TextView tv = new TextView(this);
        tv.setText("No achievements recorded yet.");
        tv.setTextColor(0xFF1E1B4B);
        tv.setPadding(32, 32, 32, 32);
        listContainer.addView(tv);
    }
}