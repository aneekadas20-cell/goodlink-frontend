package com.goodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    // icon, title, subtitle
    private final String[][] features = {
            {"\uD83C\uDFE0", "NGO Applications", "Apply to old age homes and more"},
            {"\uD83E\uDDF9", "Activities", "Join cleaning and care drives"},
            {"\uD83D\uDC55", "Donate Items", "Clothes, food and books"},
            {"\uD83C\uDFC6", "Achievements", "Celebrate our volunteers"},
            {"\uD83D\uDEA8", "Urgent Help", "Blood and food requests"},
            {"\uD83D\uDCE2", "Awareness Drives", "Spread the word"}
    };

    // soft card tints (indigo, sky, mint, amber, red, violet)
    private final int[] tints = {
            0xFFE0E7FF, 0xFFE0F2FE, 0xFFDCFCE7,
            0xFFFEF3C7, 0xFFFEE2E2, 0xFFEDE9FE
    };

    private TextView userInfoText;
    private TextView logoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInfoText = findViewById(R.id.userInfoText);
        logoutText = findViewById(R.id.logoutText);

        logoutText.setOnClickListener(v -> {
            UserPrefs.clearUser(this);
            startActivity(new Intent(this, RegisterActivity.class));
        });

        setupGrid();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!UserPrefs.isLoggedIn(this)) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            String name = UserPrefs.getUserName(this);
            String email = UserPrefs.getUserEmail(this);
            String bloodType = UserPrefs.getBloodType(this);
            String btText = (bloodType != null && !bloodType.isEmpty()) ? " | Blood: " + bloodType : "";
            userInfoText.setText("User: " + name + " (" + email + ")" + btText);
        }
    }

    private void setupGrid() {
        GridLayout grid = findViewById(R.id.grid);
        grid.removeAllViews();

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (6 * density);

        for (int i = 0; i < features.length; i++) {
            String[] f = features[i];
            View view = getLayoutInflater().inflate(R.layout.card_feature, grid, false);
            ((TextView) view.findViewById(R.id.icon)).setText(f[0]);
            ((TextView) view.findViewById(R.id.title)).setText(f[1]);
            ((TextView) view.findViewById(R.id.subtitle)).setText(f[2]);
            ((MaterialCardView) view).setCardBackgroundColor(tints[i]);

            GridLayout.LayoutParams lp = new GridLayout.LayoutParams(
                    GridLayout.spec(GridLayout.UNDEFINED, 1f),
                    GridLayout.spec(GridLayout.UNDEFINED, 1f));
            lp.width = 0;
            lp.setMargins(margin, margin, margin, margin);
            view.setLayoutParams(lp);

            final String name = f[1];
            view.setOnClickListener(v -> openFeature(name));

            grid.addView(view);
        }
    }

    private void openFeature(String featureName) {
        switch (featureName) {
            case "NGO Applications":
                startActivity(new Intent(this, NgoApplicationsActivity.class));
                break;
            case "Activities":
                startActivity(new Intent(this, ActivitiesActivity.class));
                break;
            case "Donate Items":
                startActivity(new Intent(this, DonateItemsActivity.class));
                break;
            case "Achievements":
                startActivity(new Intent(this, AchievementsActivity.class));
                break;
            case "Urgent Help":
                startActivity(new Intent(this, UrgentHelpActivity.class));
                break;
            case "Awareness Drives":
                startActivity(new Intent(this, AwarenessDrivesActivity.class));
                break;
        }
    }
}
