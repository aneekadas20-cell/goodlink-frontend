package com.goodlink.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.UrgentRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class UrgentHelpActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private TextInputEditText cityInput;
    private com.google.android.material.textfield.TextInputLayout detailsInputLayout;
    private TextInputEditText detailsInput;
    
    private LinearLayout bloodDetailsContainer;
    private Spinner bloodTypeSpinner;
    private TextInputEditText unitsInput;
    private TextInputEditText hospitalInput;
    private TextInputEditText reasonInput;
    private TextInputEditText phoneInput;
    
    private ProgressBar postProgressBar;
    private MaterialButton submitButton;

    private LinearLayout urgentListContainer;
    private ProgressBar listProgressBar;

    private static final String[] TYPES = {"BLOOD", "FOOD"};
    private static final String[] BLOOD_TYPES = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_help);

        typeSpinner = findViewById(R.id.typeSpinner);
        cityInput = findViewById(R.id.cityInput);
        detailsInputLayout = findViewById(R.id.detailsInputLayout);
        detailsInput = findViewById(R.id.detailsInput);
        
        bloodDetailsContainer = findViewById(R.id.bloodDetailsContainer);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        unitsInput = findViewById(R.id.unitsInput);
        hospitalInput = findViewById(R.id.hospitalInput);
        reasonInput = findViewById(R.id.reasonInput);
        phoneInput = findViewById(R.id.phoneInput);
        
        postProgressBar = findViewById(R.id.postProgressBar);
        submitButton = findViewById(R.id.submitButton);

        urgentListContainer = findViewById(R.id.urgentListContainer);
        listProgressBar = findViewById(R.id.listProgressBar);
        
        MaterialButton refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(v -> loadUrgentRequests());

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, TYPES);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);
        
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, BLOOD_TYPES);
        bloodAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(bloodAdapter);

        typeSpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                if ("BLOOD".equals(TYPES[position])) {
                    detailsInputLayout.setVisibility(View.GONE);
                    bloodDetailsContainer.setVisibility(View.VISIBLE);
                } else {
                    detailsInputLayout.setVisibility(View.VISIBLE);
                    bloodDetailsContainer.setVisibility(View.GONE);
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        submitButton.setOnClickListener(v -> postUrgentRequest());

        loadUrgentRequests();
    }

    private void loadUrgentRequests() {
        listProgressBar.setVisibility(View.VISIBLE);
        urgentListContainer.removeAllViews();

        AppRepository.getUrgentRequests(new AppRepository.ApiCallback<List<UrgentRequest>>() {
            @Override
            public void onSuccess(List<UrgentRequest> requests) {
                listProgressBar.setVisibility(View.GONE);
                if (requests == null || requests.isEmpty()) {
                    showEmptyMessage();
                    return;
                }
                for (UrgentRequest req : requests) {
                    addUrgentRequestView(req);
                }
            }

            @Override
            public void onError(String errorMessage) {
                listProgressBar.setVisibility(View.GONE);
                Snackbar.make(urgentListContainer, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void addUrgentRequestView(UrgentRequest req) {
        View item = getLayoutInflater().inflate(R.layout.item_urgent_request, urgentListContainer, false);

        TextView typeBadge = item.findViewById(R.id.typeBadge);
        TextView cityView = item.findViewById(R.id.cityView);
        TextView dateView = item.findViewById(R.id.dateView);
        TextView requestIdView = item.findViewById(R.id.requestIdView);
        TextView unitsView = item.findViewById(R.id.unitsView);
        TextView hospitalView = item.findViewById(R.id.hospitalView);
        TextView reasonView = item.findViewById(R.id.reasonView);
        TextView contactView = item.findViewById(R.id.contactView);
        TextView detailsView = item.findViewById(R.id.detailsView);
        MaterialButton donateButton = item.findViewById(R.id.donateButton);

        typeBadge.setText(req.getType());
        if ("BLOOD".equalsIgnoreCase(req.getType())) {
            typeBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFC62828));
        } else {
            typeBadge.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFF57C00));
        }
        typeBadge.setTextColor(0xFFFFFFFF);

        cityView.setText(req.getCity());
        dateView.setText(req.getDate() != null && !req.getDate().isEmpty() ? req.getDate() : "URGENT");
        
        requestIdView.setText(getString(R.string.request_id_format, req.getRequestId()));
        
        if ("BLOOD".equalsIgnoreCase(req.getType())) {
            detailsView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 24);
            detailsView.setTextColor(0xFFC62828);
            detailsView.setTypeface(null, android.graphics.Typeface.BOLD);
            
            unitsView.setVisibility(View.VISIBLE);
            item.findViewById(R.id.unitsLabel).setVisibility(View.VISIBLE);
        } else {
            detailsView.setTextSize(android.util.TypedValue.COMPLEX_UNIT_SP, 16);
            detailsView.setTextColor(0xFF1F1B4D);
            detailsView.setTypeface(null, android.graphics.Typeface.NORMAL);
            
            unitsView.setVisibility(View.GONE);
            item.findViewById(R.id.unitsLabel).setVisibility(View.GONE);
        }
        detailsView.setText(req.getDetails());
        
        unitsView.setText(String.valueOf(req.getUnitsNeeded()));
        hospitalView.setText(req.getHospitalName());
        reasonView.setText(req.getReason());
        contactView.setText(req.getContactPhone());
        
        boolean alreadyOffered = false;
        String myEmail = UserPrefs.getUserEmail(this);
        if (req.getPotentialDonors() != null) {
            for (com.goodlink.app.model.PotentialDonor donor : req.getPotentialDonors()) {
                if (donor.getEmail() != null && donor.getEmail().equalsIgnoreCase(myEmail)) {
                    alreadyOffered = true;
                    break;
                }
            }
        }
        
        if (alreadyOffered) {
            donateButton.setText("Already offered");
            donateButton.setEnabled(false);
            donateButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFBDBDBD));
            donateButton.setTextColor(0xFF424242);
        } else {
            donateButton.setText("I Can Donate");
            donateButton.setEnabled(true);
            donateButton.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFFC62828));
            donateButton.setTextColor(0xFFFFFFFF);
            donateButton.setOnClickListener(v -> handleRespondClick(req, donateButton));
        }

        urgentListContainer.addView(item);
    }
    
    private void handleRespondClick(UrgentRequest req, MaterialButton donateButton) {
        String email = UserPrefs.getUserEmail(this);
        if (TextUtils.isEmpty(email)) {
            Snackbar.make(urgentListContainer, "Please register first.", Snackbar.LENGTH_LONG).show();
            return;
        }
        
        if ("BLOOD".equalsIgnoreCase(req.getType())) {
            String savedBloodType = UserPrefs.getBloodType(this);
            if (TextUtils.isEmpty(savedBloodType)) {
                // Show dropdown dialog
                android.widget.Spinner spinner = new android.widget.Spinner(this);
                ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BLOOD_TYPES);
                bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(bloodAdapter);
                
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Select Blood Type")
                        .setMessage("Please select your blood type to respond:")
                        .setView(spinner)
                        .setPositiveButton("Confirm", (dialog, which) -> {
                            String selected = spinner.getSelectedItem().toString();
                            UserPrefs.saveBloodType(this, selected);
                            sendRespondRequest(req, donateButton, selected);
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            } else {
                // Show confirm dialog
                new androidx.appcompat.app.AlertDialog.Builder(this)
                        .setTitle("Confirm Offer")
                        .setMessage("Your blood type: " + savedBloodType + ". Offer to help with request #" + req.getRequestId() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            sendRespondRequest(req, donateButton, savedBloodType);
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        } else {
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Confirm Offer")
                    .setMessage("Offer to help with request #" + req.getRequestId() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        sendRespondRequest(req, donateButton, null);
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
    
    private void sendRespondRequest(UrgentRequest req, MaterialButton donateButton, String bloodType) {
        String email = UserPrefs.getUserEmail(this);
        donateButton.setEnabled(false);
        
        com.goodlink.app.model.UrgentRespondRequest respondReq = new com.goodlink.app.model.UrgentRespondRequest(email, bloodType);
        
        AppRepository.respondToUrgentRequest(req.getRequestId(), respondReq, new AppRepository.ApiCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                donateButton.setEnabled(true);
                String msg = response.getMessage() != null && !response.getMessage().isEmpty() ? response.getMessage() : "Response sent.";
                
                // Show in alert dialog for clarity, especially if it's "not compatible" or "already offered"
                new androidx.appcompat.app.AlertDialog.Builder(UrgentHelpActivity.this)
                        .setMessage(msg)
                        .setPositiveButton("OK", null)
                        .show();
                        
                if ("SUCCESS".equals(response.getStatus()) || "INFO".equals(response.getStatus())) {
                    loadUrgentRequests();
                }
            }

            @Override
            public void onError(String errorMessage) {
                donateButton.setEnabled(true);
                Snackbar.make(urgentListContainer, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void postUrgentRequest() {
        String type = typeSpinner.getSelectedItem() != null ? typeSpinner.getSelectedItem().toString() : "BLOOD";
        String city = cityInput.getText() != null ? cityInput.getText().toString().trim() : "";
        String email = UserPrefs.getUserEmail(this);
        
        String details;
        int unitsNeeded = 1;
        String hospitalName = "Not specified";
        String reason = null;
        String contactPhone = "Not provided";
        
        if ("BLOOD".equals(type)) {
            details = bloodTypeSpinner.getSelectedItem() != null ? bloodTypeSpinner.getSelectedItem().toString() : "A+";
            String unitsStr = unitsInput.getText() != null ? unitsInput.getText().toString().trim() : "";
            hospitalName = hospitalInput.getText() != null ? hospitalInput.getText().toString().trim() : "Not specified";
            String reasonStr = reasonInput.getText() != null ? reasonInput.getText().toString().trim() : "";
            contactPhone = phoneInput.getText() != null ? phoneInput.getText().toString().trim() : "Not provided";
            
            if (!reasonStr.isEmpty()) reason = reasonStr;
            
            if (TextUtils.isEmpty(unitsStr)) {
                unitsInput.setError("Units needed is required");
                unitsInput.requestFocus();
                return;
            }
            try {
                unitsNeeded = Integer.parseInt(unitsStr);
                if (unitsNeeded < 1) {
                    unitsInput.setError("Minimum 1 unit required");
                    unitsInput.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                unitsInput.setError("Invalid number");
                unitsInput.requestFocus();
                return;
            }
            
            if (TextUtils.isEmpty(hospitalName) || hospitalName.equals("Not specified")) {
                hospitalInput.setError("Hospital name is required");
                hospitalInput.requestFocus();
                return;
            }
            
            if (TextUtils.isEmpty(contactPhone) || contactPhone.equals("Not provided")) {
                phoneInput.setError("Contact phone is required");
                phoneInput.requestFocus();
                return;
            }
            
        } else {
            details = detailsInput.getText() != null ? detailsInput.getText().toString().trim() : "";
            if (TextUtils.isEmpty(details)) {
                detailsInput.setError("Details are required");
                detailsInput.requestFocus();
                return;
            }
        }

        if (TextUtils.isEmpty(city)) {
            cityInput.setError("City is required");
            cityInput.requestFocus();
            return;
        }

        postProgressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        UrgentRequest req = new UrgentRequest(type, details, city, email, unitsNeeded, hospitalName, reason, contactPhone);
        
        final String fType = type;
        final int fUnits = unitsNeeded;
        final String fHospital = hospitalName;
        final String fDetails = details;
        
        AppRepository.createUrgentRequest(req, new AppRepository.ApiCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                postProgressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);

                detailsInput.setText("");
                unitsInput.setText("");
                hospitalInput.setText("");
                reasonInput.setText("");
                phoneInput.setText("");

                String msg = response.getMessage() != null && !response.getMessage().isEmpty()
                        ? response.getMessage()
                        : "Urgent request posted!";
                        
                if (response.getMessage() == null || response.getMessage().isEmpty() || response.getMessage().startsWith("Urgent request posted")) {
                    if ("BLOOD".equals(fType)) {
                        msg = "Posted request for " + fUnits + " unit(s) of " + fDetails + " blood at " + fHospital + ".";
                    }
                }
                
                Snackbar.make(submitButton, msg, Snackbar.LENGTH_SHORT).show();

                loadUrgentRequests();
            }

            @Override
            public void onError(String errorMessage) {
                postProgressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
                Snackbar.make(submitButton, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void showEmptyMessage() {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(android.view.Gravity.CENTER);
        layout.setPadding(32, 64, 32, 64);
        
        TextView emoji = new TextView(this);
        emoji.setText("🩸");
        emoji.setTextSize(48);
        emoji.setGravity(android.view.Gravity.CENTER);
        
        TextView tv = new TextView(this);
        tv.setText("No open requests right now.");
        tv.setTextColor(0xFF6B7280);
        tv.setTextSize(16);
        tv.setGravity(android.view.Gravity.CENTER);
        tv.setPadding(0, 16, 0, 0);
        
        layout.addView(emoji);
        layout.addView(tv);
        
        urgentListContainer.addView(layout);
    }
}
