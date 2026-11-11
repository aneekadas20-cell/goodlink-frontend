
package com.goodlink.app;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.ActivityItem;
import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.JoinRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class ActivitiesActivity extends AppCompatActivity {

    private LinearLayout listContainer;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activities);

        listContainer = findViewById(R.id.list);
        progressBar = findViewById(R.id.progressBar);

        loadActivities();
    }

    private void loadActivities() {
        progressBar.setVisibility(View.VISIBLE);
        listContainer.removeAllViews();

        AppRepository.getActivities(
                new AppRepository.ApiCallback<List<ActivityItem>>() {

                    @Override
                    public void onSuccess(List<ActivityItem> activities) {
                        progressBar.setVisibility(View.GONE);

                        if (activities == null || activities.isEmpty()) {
                            showEmptyMessage();
                            return;
                        }

                        for (ActivityItem activity : activities) {
                            addActivityView(activity);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                        progressBar.setVisibility(View.GONE);

                        Snackbar.make(
                                listContainer,
                                errorMessage,
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void addActivityView(ActivityItem itemData) {
        View item = getLayoutInflater().inflate(
                R.layout.item_activity,
                listContainer,
                false
        );

        TextView titleView = item.findViewById(R.id.title);
        TextView ngoView = item.findViewById(R.id.ngo);
        TextView dateView = item.findViewById(R.id.date);
        MaterialButton joinBtn = item.findViewById(R.id.join);

        titleView.setText(itemData.getTitle());
        ngoView.setText(itemData.getNgoName());
        dateView.setText(itemData.getDate());

        if (itemData.isJoined()) {
            joinBtn.setText("Joined");
            joinBtn.setEnabled(false);
            joinBtn.setBackgroundTintList(
                    getColorStateList(android.R.color.darker_gray)
            );
        } else {
            joinBtn.setText("Join");
            joinBtn.setEnabled(true);

            joinBtn.setOnClickListener(
                    v -> handleJoin(itemData, joinBtn)
            );
        }

        listContainer.addView(item);
    }

    private void handleJoin(
            ActivityItem itemData,
            MaterialButton joinBtn
    ) {
        String email = UserPrefs.getUserEmail(this);

        if (email == null || email.trim().isEmpty()) {
            Snackbar.make(
                    listContainer,
                    "Please register or log in first.",
                    Snackbar.LENGTH_LONG
            ).show();
            return;
        }

        // Convert the activity ID to a whole number for the API URL.
        long activityId = ((Number) itemData.getId()).longValue();

        joinBtn.setEnabled(false);
        joinBtn.setText("Joining...");

        AppRepository.joinActivity(
                activityId,
                new JoinRequest(email),
                new AppRepository.ApiCallback<ApiResponse>() {

                    @Override
                    public void onSuccess(ApiResponse response) {
                        itemData.setJoined(true);

                        joinBtn.setText("Joined");
                        joinBtn.setEnabled(false);
                        joinBtn.setBackgroundTintList(
                                getColorStateList(
                                        android.R.color.darker_gray
                                )
                        );

                        String msg =
                                response != null
                                        && response.getMessage() != null
                                        && !response.getMessage().isEmpty()
                                        ? response.getMessage()
                                        : "Joined: " + itemData.getTitle();

                        Snackbar.make(
                                listContainer,
                                msg,
                                Snackbar.LENGTH_SHORT
                        ).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        joinBtn.setEnabled(true);
                        joinBtn.setText("Join");

                        Snackbar.make(
                                listContainer,
                                errorMessage,
                                Snackbar.LENGTH_LONG
                        ).show();
                    }
                }
        );
    }

    private void showEmptyMessage() {
        TextView tv = new TextView(this);
        tv.setText("No activities available right now.");
        tv.setTextColor(0xFF1E1B4B);
        tv.setPadding(32, 32, 32, 32);

        listContainer.addView(tv);
    }
}
