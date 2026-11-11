package com.goodlink.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.RegisterRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private android.widget.Spinner bloodTypeSpinner;
    private ProgressBar progressBar;
    private MaterialButton registerButton;
    
    private static final String[] BLOOD_TYPES = {"Select blood type", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        bloodTypeSpinner = findViewById(R.id.bloodTypeSpinner);
        progressBar = findViewById(R.id.progressBar);
        registerButton = findViewById(R.id.registerButton);
        
        android.widget.ArrayAdapter<String> bloodAdapter = new android.widget.ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BLOOD_TYPES);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bloodTypeSpinner.setAdapter(bloodAdapter);

        // Pre-fill if existing user
        String currentName = UserPrefs.getUserName(this);
        String currentEmail = UserPrefs.getUserEmail(this);
        String currentBlood = UserPrefs.getBloodType(this);
        if (!TextUtils.isEmpty(currentName)) {
            nameInput.setText(currentName);
        }
        if (!TextUtils.isEmpty(currentEmail)) {
            emailInput.setText(currentEmail);
        }
        if (!TextUtils.isEmpty(currentBlood)) {
            for (int i = 0; i < BLOOD_TYPES.length; i++) {
                if (BLOOD_TYPES[i].equals(currentBlood)) {
                    bloodTypeSpinner.setSelection(i);
                    break;
                }
            }
        }

        registerButton.setOnClickListener(v -> performRegistration());
    }

    private void performRegistration() {
        String name = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String bloodType = bloodTypeSpinner.getSelectedItem() != null ? bloodTypeSpinner.getSelectedItem().toString() : "";

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("Valid email is required");
            emailInput.requestFocus();
            return;
        }
        
        if (bloodType.equals("Select blood type")) {
            Snackbar.make(registerButton, "Please select your blood type", Snackbar.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        RegisterRequest request = new RegisterRequest(name, email, bloodType);
        AppRepository.register(request, new AppRepository.ApiCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                progressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);

                UserPrefs.saveUser(RegisterActivity.this, name, email);
                UserPrefs.saveBloodType(RegisterActivity.this, bloodType);
                Snackbar.make(registerButton, "Welcome, " + name + "!", Snackbar.LENGTH_SHORT).show();

                // Open MainActivity if launched as standalone or finish
                if (isTaskRoot()) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                }
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                registerButton.setEnabled(true);
                Snackbar.make(registerButton, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
