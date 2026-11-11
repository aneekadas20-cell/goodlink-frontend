package com.goodlink.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.DonationRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class DonateItemsActivity extends AppCompatActivity {

    private TextInputEditText emailInput;
    private TextInputEditText itemInput;
    private TextInputEditText quantityInput;
    private android.widget.Spinner categorySpinner;
    private android.widget.LinearLayout conditionContainer;
    private android.widget.Spinner conditionSpinner;
    private android.widget.RadioGroup deliveryRadioGroup;
    private android.widget.LinearLayout pickupDetailsContainer;
    private TextInputEditText pickupAddressInput;
    private TextInputEditText preferredTimeInput;
    private ProgressBar progressBar;
    private MaterialButton submitButton;

    private static final String[] CATEGORIES = {"Clothes", "Food", "Books", "Electronics", "Medical", "Other"};
    private static final String[] CONDITIONS = {"New", "Gently Used", "Old"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_items);

        emailInput = findViewById(R.id.emailInput);
        itemInput = findViewById(R.id.itemInput);
        quantityInput = findViewById(R.id.quantityInput);
        
        categorySpinner = findViewById(R.id.categorySpinner);
        conditionContainer = findViewById(R.id.conditionContainer);
        conditionSpinner = findViewById(R.id.conditionSpinner);
        deliveryRadioGroup = findViewById(R.id.deliveryRadioGroup);
        pickupDetailsContainer = findViewById(R.id.pickupDetailsContainer);
        pickupAddressInput = findViewById(R.id.pickupAddressInput);
        preferredTimeInput = findViewById(R.id.preferredTimeInput);

        progressBar = findViewById(R.id.progressBar);
        submitButton = findViewById(R.id.submitButton);

        emailInput.setText(UserPrefs.getUserEmail(this));
        
        android.widget.ArrayAdapter<String> catAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CATEGORIES);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(catAdapter);

        android.widget.ArrayAdapter<String> condAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, CONDITIONS);
        condAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conditionSpinner.setAdapter(condAdapter);

        categorySpinner.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String cat = CATEGORIES[position];
                if ("Food".equals(cat) || "Medical".equals(cat)) {
                    conditionContainer.setVisibility(View.GONE);
                } else {
                    conditionContainer.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        deliveryRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioPickup) {
                pickupDetailsContainer.setVisibility(View.VISIBLE);
            } else {
                pickupDetailsContainer.setVisibility(View.GONE);
            }
        });
        
        // Select Drop-off by default
        deliveryRadioGroup.check(R.id.radioDropoff);

        submitButton.setOnClickListener(v -> submitDonation());
    }

    private void submitDonation() {
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String item = itemInput.getText() != null ? itemInput.getText().toString().trim() : "";
        String quantityStr = quantityInput.getText() != null ? quantityInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(item)) {
            itemInput.setError("Item description is required");
            itemInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(quantityStr)) {
            quantityInput.setError("Quantity is required");
            quantityInput.requestFocus();
            return;
        }

        int qty = 1;
        try {
            qty = Integer.parseInt(quantityStr);
        } catch (NumberFormatException e) {
            String digits = quantityStr.replaceAll("\\D+", "");
            if (!digits.isEmpty()) {
                try {
                    qty = Integer.parseInt(digits);
                } catch (Exception ignored) {
                }
            }
        }

        String category = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "Other";
        String condition = "N/A";
        if (conditionContainer.getVisibility() == View.VISIBLE) {
            condition = conditionSpinner.getSelectedItem() != null ? conditionSpinner.getSelectedItem().toString() : "Gently Used";
        }
        
        String deliveryMethod = deliveryRadioGroup.getCheckedRadioButtonId() == R.id.radioPickup ? "Pickup" : "Drop-off";
        String pickupAddress = "";
        String preferredTime = "";
        
        if ("Pickup".equals(deliveryMethod)) {
            pickupAddress = pickupAddressInput.getText() != null ? pickupAddressInput.getText().toString().trim() : "";
            preferredTime = preferredTimeInput.getText() != null ? preferredTimeInput.getText().toString().trim() : "";
            
            if (TextUtils.isEmpty(pickupAddress)) {
                pickupAddressInput.setError("Pickup address is required");
                pickupAddressInput.requestFocus();
                return;
            }
            if (TextUtils.isEmpty(preferredTime)) {
                preferredTimeInput.setError("Preferred time is required");
                preferredTimeInput.requestFocus();
                return;
            }
        }

        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        DonationRequest req = new DonationRequest(email, item, qty, category, condition, deliveryMethod, pickupAddress, preferredTime);
        final String fCategory = category;
        final String fCondition = condition;
        final String fDeliveryMethod = deliveryMethod;
        final String fPreferredTime = preferredTime;
        
        AppRepository.donate(req, new AppRepository.ApiCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);

                itemInput.setText("");
                quantityInput.setText("");
                pickupAddressInput.setText("");
                preferredTimeInput.setText("");

                String msg = response.getMessage() != null && !response.getMessage().isEmpty()
                        ? response.getMessage()
                        : "Donation submitted successfully!";
                        
                // Override with custom message if possible
                if (response.getMessage() == null || response.getMessage().isEmpty() || response.getMessage().startsWith("Donation submitted")) {
                    if ("Pickup".equals(fDeliveryMethod)) {
                        msg = "Thanks! Your " + fCategory + " donation" + ("N/A".equals(fCondition) ? "" : " (" + fCondition + ")") + " will be picked up on " + fPreferredTime + ".";
                    } else {
                        msg = "Thanks! Please drop off your " + fCategory + " donation" + ("N/A".equals(fCondition) ? "" : " (" + fCondition + ")") + " at our center.";
                    }
                }
                
                Snackbar.make(submitButton, msg, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
                Snackbar.make(submitButton, errorMessage, Snackbar.LENGTH_LONG).show();
                android.util.Log.e("GoodLinkDonation", "Donation failed: " + errorMessage);
            }
        });
    }
}
