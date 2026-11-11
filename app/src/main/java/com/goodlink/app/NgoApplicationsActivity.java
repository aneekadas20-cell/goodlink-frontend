package com.goodlink.app;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.Ngo;
import com.goodlink.app.model.NgoApplicationRequest;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class NgoApplicationsActivity extends AppCompatActivity {

    private Spinner ngoSpinner;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText messageInput;
    private ProgressBar progressBar;
    private MaterialButton submitButton;

    private final List<Ngo> ngoList = new ArrayList<>();
    private ArrayAdapter<Ngo> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ngo_applications);

        ngoSpinner = findViewById(R.id.ngoSpinner);
        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        messageInput = findViewById(R.id.messageInput);
        progressBar = findViewById(R.id.progressBar);
        submitButton = findViewById(R.id.submitButton);

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ngoList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ngoSpinner.setAdapter(spinnerAdapter);

        // Pre-fill user data
        nameInput.setText(UserPrefs.getUserName(this));
        emailInput.setText(UserPrefs.getUserEmail(this));

        submitButton.setOnClickListener(v -> submitApplication());

        loadNgos();
    }

    private void loadNgos() {
        progressBar.setVisibility(View.VISIBLE);
        AppRepository.getNgos(new AppRepository.ApiCallback<List<Ngo>>() {
            @Override
            public void onSuccess(List<Ngo> ngos) {
                progressBar.setVisibility(View.GONE);
                ngoList.clear();
                if (ngos != null && !ngos.isEmpty()) {
                    ngoList.addAll(ngos);
                } else {
                    ngoList.add(new Ngo(0L, "General Application", "General volunteering inquiry", "Global"));
                }
                spinnerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                Snackbar.make(submitButton, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }

    private void submitApplication() {
        Ngo selectedNgo = (Ngo) ngoSpinner.getSelectedItem();
        Long ngoId = selectedNgo != null ? selectedNgo.getId() : 0L;

        String name = nameInput.getText() != null ? nameInput.getText().toString().trim() : "";
        String email = emailInput.getText() != null ? emailInput.getText().toString().trim() : "";
        String message = messageInput.getText() != null ? messageInput.getText().toString().trim() : "";

        if (TextUtils.isEmpty(name)) {
            nameInput.setError("Name is required");
            nameInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailInput.setError("Email is required");
            emailInput.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(message)) {
            messageInput.setError("Please write a short message");
            messageInput.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        NgoApplicationRequest req = new NgoApplicationRequest(ngoId, name, email, message);
        AppRepository.submitNgoApplication(req, new AppRepository.ApiCallback<ApiResponse>() {
            @Override
            public void onSuccess(ApiResponse response) {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
                messageInput.setText("");

                String msg = response.getMessage() != null && !response.getMessage().isEmpty()
                        ? response.getMessage()
                        : "Application submitted successfully!";
                Snackbar.make(submitButton, msg, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onError(String errorMessage) {
                progressBar.setVisibility(View.GONE);
                submitButton.setEnabled(true);
                Snackbar.make(submitButton, errorMessage, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}
