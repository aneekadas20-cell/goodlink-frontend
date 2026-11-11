
package com.goodlink.app;

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

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Get all NGOs
    @GET("api/ngos")
    Call<List<Ngo>> getNgos();

    // Get all volunteer activities
    @GET("api/activities")
    Call<List<ActivityItem>> getActivities();

    // Get all awareness drives
    @GET("api/drives")
    Call<List<Drive>> getDrives();

    // Get all achievements
    @GET("api/achievements")
    Call<List<Achievement>> getAchievements();

    // Get all urgent requests
    @GET("api/urgent")
    Call<List<UrgentRequest>> getUrgentRequests();

    // Register a user
    @POST("api/register")
    Call<ApiResponse> register(
            @Body RegisterRequest request
    );

    // Join an activity
    @POST("api/activities/{id}/join")
    Call<ApiResponse> joinActivity(
            @Path("id") long id,
            @Body JoinRequest request
    );

    // Submit a donation
    @POST("api/donations")
    Call<ApiResponse> donate(
            @Body DonationRequest request
    );

    // Create an urgent-help request
    @POST("api/urgent")
    Call<ApiResponse> createUrgentRequest(
            @Body UrgentRequest request
    );

    // Submit an NGO application
    @POST("api/ngo-applications")
    Call<ApiResponse> submitNgoApplication(
            @Body NgoApplicationRequest request
    );

    // Respond to an urgent request
    @POST("api/urgent/{id}/respond")
    Call<ApiResponse> respondToUrgentRequest(
            @Path("id") long id,
            @Body com.goodlink.app.model.UrgentRespondRequest request
    );
}
