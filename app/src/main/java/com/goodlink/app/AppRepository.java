package com.goodlink.app;

import android.os.Handler;
import android.os.Looper;

import com.goodlink.app.model.Achievement;
import com.goodlink.app.model.ActivityItem;
import com.goodlink.app.model.ApiResponse;
import com.goodlink.app.model.DonationRequest;
import com.goodlink.app.model.Drive;
import com.goodlink.app.model.JoinRequest;
import com.goodlink.app.model.Ngo;
import com.goodlink.app.model.NgoApplicationRequest;
import com.goodlink.app.model.RegisterRequest;
import com.goodlink.app.model.UrgentRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppRepository {

    public interface ApiCallback<T> {
        void onSuccess(T data);
        void onError(String errorMessage);
    }

    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    // In-memory demo data store
    private static final List<Ngo> sampleNgos = new ArrayList<>();
    private static final List<ActivityItem> sampleActivities = new ArrayList<>();
    private static final List<Drive> sampleDrives = new ArrayList<>();
    private static final List<Achievement> sampleAchievements = new ArrayList<>();
    private static final List<UrgentRequest> sampleUrgentRequests = new ArrayList<>();

    static {
        // Initialize sample NGOs
        sampleNgos.add(new Ngo(1L, "Silver Years Elder Haven", "Elderly care and companionship", "New York"));
        sampleNgos.add(new Ngo(2L, "Little Sparks Children Foundation", "Education for underprivileged youth", "Chicago"));
        sampleNgos.add(new Ngo(3L, "LifePulse Blood Network", "Blood drive organization", "Boston"));
        sampleNgos.add(new Ngo(4L, "Annapurna Food Rescue", "Food redistribution and surplus management", "San Francisco"));
        sampleNgos.add(new Ngo(5L, "Paws & Tails Shelter", "Animal rescue and pet grooming", "Seattle"));
        sampleNgos.add(new Ngo(6L, "Green Roots Initiative", "Environmental conservation and tree planting", "Austin"));

        // Initialize sample activities
        sampleActivities.add(new ActivityItem(1L, "Weekend Companion Care at Silver Oak", "Silver Years Elder Haven", "2026-10-25"));
        sampleActivities.add(new ActivityItem(2L, "Weekend Math & Art for Street Children", "Little Sparks Children Foundation", "2026-10-28"));
        sampleActivities.add(new ActivityItem(3L, "Mega Blood Donation Drive at Town Hall", "LifePulse Blood Network", "2026-11-02"));
        sampleActivities.add(new ActivityItem(4L, "Community Food Packaging & Distribution", "Annapurna Food Rescue", "2026-11-05"));
        sampleActivities.add(new ActivityItem(5L, "Weekend Shelter Pet Grooming & Care", "Paws & Tails Shelter", "2026-11-10"));
        sampleActivities.add(new ActivityItem(6L, "Green Neighbourhood Tree Plantation Drive", "Green Roots Initiative", "2026-11-16"));
        sampleActivities.add(new ActivityItem(7L, "Community Library Setup & Book Donation", "ReadRise Foundation", "2026-11-25"));
        sampleActivities.add(new ActivityItem(8L, "Youth Mental Wellness Awareness Campaign", "Mindful Youth Initiative", "2026-12-04"));

        // Initialize sample drives
        sampleDrives.add(new Drive(1L, "Clean Water & Sanitation Drive", "Green Roots Initiative", "2026-10-30", "Educating communities on clean water harvesting and hygiene.", "Town Center Hall"));
        sampleDrives.add(new Drive(2L, "Digital Literacy for Seniors", "Silver Years Elder Haven", "2026-11-12", "Teaching smartphone usage and internet safety to senior citizens.", "Community Center Room B"));
        sampleDrives.add(new Drive(3L, "Zero Food Waste Awareness Movement", "Annapurna Food Rescue", "2026-11-20", "Workshop on food donation and reducing household waste.", "Central Market Square"));

        // Initialize sample achievements
        sampleAchievements.add(new Achievement(1L, "Volunteer of the Month", "Over 50 hours of companion care provided this month.", "Sarah Jenkins", "2026-10-01", "\uD83C\uDFC6"));
        sampleAchievements.add(new Achievement(2L, "1000 Meals Distributed", "Successfully packaged and served 1,000 warm meals to shelter homes.", "Annapurna Food Rescue Team", "2026-09-28", "\uD83C\uDF72"));
        sampleAchievements.add(new Achievement(3L, "Green Hero Award", "Planted 250 native saplings across suburban parks.", "David Chen", "2026-09-15", "\uD83C\uDF32"));
        sampleAchievements.add(new Achievement(4L, "Blood Donation Champion", "Organized 5 successful blood donation drives saving hundreds of lives.", "LifePulse Network", "2026-08-30", "\u2764\uFE0F"));

        // Initialize sample urgent requests
        sampleUrgentRequests.add(new UrgentRequest(1L, "BLOOD", "O negative blood urgently required for surgery patient at City General Hospital.", "New York", "admin@goodlink.app", "2026-10-24", "URGENT"));
        sampleUrgentRequests.add(new UrgentRequest(2L, "FOOD", "Emergency dry food packets needed for 30 families affected by local rain flood.", "Springfield", "admin@goodlink.app", "2026-10-24", "URGENT"));
        sampleUrgentRequests.add(new UrgentRequest(3L, "BLOOD", "A+ blood needed for emergency trauma care unit.", "Chicago", "admin@goodlink.app", "2026-10-23", "URGENT"));
    }

    // --- GET /api/ngos ---
    public static void getNgos(ApiCallback<List<Ngo>> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ArrayList<>(sampleNgos)), 300);
            return;
        }
        ApiClient.getApiService().getNgos().enqueue(new Callback<List<Ngo>>() {
            @Override
            public void onResponse(Call<List<Ngo>> call, Response<List<Ngo>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch NGOs (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Ngo>> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- GET /api/activities ---
    public static void getActivities(ApiCallback<List<ActivityItem>> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ArrayList<>(sampleActivities)), 300);
            return;
        }
        ApiClient.getApiService().getActivities().enqueue(new Callback<List<ActivityItem>>() {
            @Override
            public void onResponse(Call<List<ActivityItem>> call, Response<List<ActivityItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch activities (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<ActivityItem>> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- GET /api/drives ---
    public static void getDrives(ApiCallback<List<Drive>> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ArrayList<>(sampleDrives)), 300);
            return;
        }
        ApiClient.getApiService().getDrives().enqueue(new Callback<List<Drive>>() {
            @Override
            public void onResponse(Call<List<Drive>> call, Response<List<Drive>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch awareness drives (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Drive>> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- GET /api/achievements ---
    public static void getAchievements(ApiCallback<List<Achievement>> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ArrayList<>(sampleAchievements)), 300);
            return;
        }
        ApiClient.getApiService().getAchievements().enqueue(new Callback<List<Achievement>>() {
            @Override
            public void onResponse(Call<List<Achievement>> call, Response<List<Achievement>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch achievements (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<Achievement>> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- GET /api/urgent ---
    public static void getUrgentRequests(ApiCallback<List<UrgentRequest>> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ArrayList<>(sampleUrgentRequests)), 300);
            return;
        }
        ApiClient.getApiService().getUrgentRequests().enqueue(new Callback<List<UrgentRequest>>() {
            @Override
            public void onResponse(Call<List<UrgentRequest>> call, Response<List<UrgentRequest>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Failed to fetch urgent requests (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<List<UrgentRequest>> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/register ---
    public static void register(RegisterRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ApiResponse(true, "Registered successfully (Demo Mode)")), 300);
            return;
        }
        ApiClient.getApiService().register(req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse body = response.body();
                    callback.onSuccess(body != null ? body : new ApiResponse(true, "Registration successful"));
                } else {
                    callback.onError("Registration failed (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/activities/{id}/join ---
    public static void joinActivity(long activityId, JoinRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            for (ActivityItem act : sampleActivities) {
                if (act.getId().equals(activityId)) {
                    act.setJoined(true);
                    break;
                }
            }
            mainHandler.postDelayed(() -> callback.onSuccess(new ApiResponse(true, "Joined activity successfully!")), 300);
            return;
        }
        ApiClient.getApiService().joinActivity(activityId, req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse body = response.body();
                    callback.onSuccess(body != null ? body : new ApiResponse(true, "Joined activity successfully"));
                } else {
                    callback.onError("Failed to join activity (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/donations ---
    public static void donate(DonationRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ApiResponse(true, "Donation offer submitted! Thank you.")), 300);
            return;
        }
        ApiClient.getApiService().donate(req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse body = response.body();
                    callback.onSuccess(body != null ? body : new ApiResponse(true, "Donation submitted successfully"));
                } else {
                    callback.onError("Failed to submit donation (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/urgent ---
    public static void createUrgentRequest(UrgentRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            UrgentRequest newReq = new UrgentRequest(
                    System.currentTimeMillis(),
                    req.getType(),
                    req.getDetails(),
                    req.getCity(),
                    req.getEmail(),
                    "Today",
                    "URGENT"
            );
            sampleUrgentRequests.add(0, newReq);
            mainHandler.postDelayed(() -> callback.onSuccess(new ApiResponse(true, "Urgent request posted successfully!")), 300);
            return;
        }
        ApiClient.getApiService().createUrgentRequest(req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse body = response.body();
                    callback.onSuccess(body != null ? body : new ApiResponse(true, "Urgent request posted successfully"));
                } else {
                    callback.onError("Failed to post urgent request (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/ngo-applications ---
    public static void submitNgoApplication(NgoApplicationRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> callback.onSuccess(new ApiResponse(true, "NGO application submitted successfully!")), 300);
            return;
        }
        ApiClient.getApiService().submitNgoApplication(req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    ApiResponse body = response.body();
                    callback.onSuccess(body != null ? body : new ApiResponse(true, "NGO application submitted successfully"));
                } else {
                    callback.onError("Failed to submit NGO application (HTTP " + response.code() + ")");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }

    // --- POST /api/urgent/{id}/respond ---
    public static void respondToUrgentRequest(long requestId, com.goodlink.app.model.UrgentRespondRequest req, ApiCallback<ApiResponse> callback) {
        if (ApiClient.DEMO_MODE) {
            mainHandler.postDelayed(() -> {
                ApiResponse resp = new ApiResponse(true, "Mock response success");
                resp.setStatus("SUCCESS");
                callback.onSuccess(resp);
            }, 300);
            return;
        }
        ApiClient.getApiService().respondToUrgentRequest(requestId, req).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    try {
                        String errorJson = response.errorBody().string();
                        com.google.gson.JsonObject json = com.google.gson.JsonParser.parseString(errorJson).getAsJsonObject();
                        String msg = json.has("message") ? json.get("message").getAsString() : "Error " + response.code();
                        callback.onError(msg);
                    } catch (Exception e) {
                        callback.onError("Request failed (HTTP " + response.code() + ")");
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                callback.onError("Network error: " + t.getLocalizedMessage());
            }
        });
    }
}
