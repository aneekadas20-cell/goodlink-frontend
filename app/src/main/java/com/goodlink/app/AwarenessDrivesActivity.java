package com.goodlink.app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.Drive;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class AwarenessDrivesActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_awareness_drives);

        listContainer = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        loadDrives();
    }

    private void loadDrives() {
        progressBar.setVisibility(View.VISIBLE);
        listContainer.removeAllViews();

        AppRepository.getDrives(new AppRepository.ApiCallback<List<Drive>>() {
            @Override
            public void onSuccess(List<Drive> drives) {
                progressBar.setVisibility(View.GONE);
                if (drives == null || drives.isEmpty()) {
                    showEmptyMessage();
                    return;
                }
                addStatsBanner(drives);
                for (Drive drive : drives) {
                    addDriveView(drive);
                }
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(listContainer, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    // ---- #4: Stats banner ----
    private void addStatsBanner(List<Drive> drives) {
        float d = getResources().getDisplayMetrics().density;

        int total = drives.size();
        int thisMonth = 0;
        LocalDate today = LocalDate.now();
        for (Drive drive : drives) {
            Integer days = daysUntil(drive.getDate());
            if (days != null && days >= 0 && days <= 30) {
                thisMonth++;
            }
        }

        LinearLayout banner = new LinearLayout(this);
        banner.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        blp.setMargins((int) (4 * d), 0, (int) (4 * d), (int) (8 * d));
        banner.setLayoutParams(blp);

        banner.addView(statBox(String.valueOf(total), "Upcoming Drives", d));
        banner.addView(statBox(String.valueOf(thisMonth), "Within 30 Days", d));

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

    private void addDriveView(Drive drive) {
        View item = getLayoutInflater().inflate(R.layout.item_drive, listContainer, false);

        TextView titleView = item.findViewById(R.id.titleView);
        TextView organizerView = item.findViewById(R.id.organizerView);
        TextView dateView = item.findViewById(R.id.dateView);
        TextView locationView = item.findViewById(R.id.locationView);
        TextView descriptionView = item.findViewById(R.id.descriptionView);

        // ---- #3: Category icon prefixed onto the title ----
        titleView.setText(pickIcon(drive) + "  " + drive.getTitle());
        organizerView.setText(drive.getOrganizer());

        // ---- #1: Countdown appended to date ----
        Integer days = daysUntil(drive.getDate());
        String countdown = countdownLabel(days);
        dateView.setText(drive.getDate() + (countdown.isEmpty() ? "" : "  \u2022  " + countdown));

        if (drive.getLocation() != null && !drive.getLocation().isEmpty()) {
            locationView.setText("\uD83D\uDCCD " + drive.getLocation());
            locationView.setVisibility(View.VISIBLE);
        } else {
            locationView.setVisibility(View.GONE);
        }
        descriptionView.setText(drive.getDescription());

        // ---- #2: Urgency badge, added dynamically above description ----
        if (days != null) {
            TextView badge = new TextView(this);
            float d = getResources().getDisplayMetrics().density;
            badge.setPadding((int) (10 * d), (int) (3 * d), (int) (10 * d), (int) (3 * d));
            badge.setTextSize(11);
            badge.setTypeface(null, Typeface.BOLD);

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, (int) (6 * d), 0, (int) (6 * d));
            badge.setLayoutParams(lp);

            if (days < 0) {
                badge.setText("COMPLETED");
                badge.setTextColor(Color.parseColor("#6B7280"));
                badge.setBackgroundColor(Color.parseColor("#F3F4F6"));
            } else if (days <= 7) {
                badge.setText("\u26A0\uFE0F VOLUNTEERS NEEDED SOON");
                badge.setTextColor(Color.parseColor("#B91C1C"));
                badge.setBackgroundColor(Color.parseColor("#FEE2E2"));
            } else if (days <= 14) {
                badge.setText("COMING UP");
                badge.setTextColor(Color.parseColor("#B45309"));
                badge.setBackgroundColor(Color.parseColor("#FEF3C7"));
            } else {
                badge.setText("PLANNED");
                badge.setTextColor(Color.parseColor("#4338CA"));
                badge.setBackgroundColor(Color.parseColor("#EEF2FF"));
            }

            if (item instanceof LinearLayout) {
                ((LinearLayout) item).addView(badge, 1);
            } else if (item.findViewById(android.R.id.content) == null && item instanceof android.view.ViewGroup) {
                ((android.view.ViewGroup) item).addView(badge);
            }
        }

        listContainer.addView(item);
    }

    // ---- Helpers ----

    private Integer daysUntil(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            LocalDate target = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE);
            return (int) ChronoUnit.DAYS.between(LocalDate.now(), target);
        } catch (Exception e) {
            return null;
        }
    }

    private String countdownLabel(Integer days) {
        if (days == null) return "";
        if (days < 0) return "Completed";
        if (days == 0) return "Today!";
        if (days == 1) return "Tomorrow";
        return "In " + days + " days";
    }

    private String pickIcon(Drive drive) {
        String text = (drive.getTitle() + " " + drive.getOrganizer()).toLowerCase();
        if (text.contains("blood")) return "\uD83E\uDE78";
        if (text.contains("food") || text.contains("meal") || text.contains("hunger")) return "\uD83C\uDF72";
        if (text.contains("tree") || text.contains("plant") || text.contains("green") || text.contains("environment")) return "\uD83C\uDF33";
        if (text.contains("book") || text.contains("librar") || text.contains("education")) return "\uD83D\uDCDA";
        if (text.contains("pet") || text.contains("animal") || text.contains("shelter")) return "\uD83D\uDC3E";
        if (text.contains("elder") || text.contains("senior") || text.contains("companion")) return "\uD83E\uDDD3";
        if (text.contains("mental") || text.contains("wellness") || text.contains("awareness") || text.contains("youth")) return "\uD83E\uDDE0";
        return "\uD83D\uDCE2";
    }

    private void showEmptyMessage() {
        TextView tv = new TextView(this);
        tv.setText("No awareness drives available.");
        tv.setTextColor(0xFF1E1B4B);
        tv.setPadding(32, 32, 32, 32);
        listContainer.addView(tv);
    }
}