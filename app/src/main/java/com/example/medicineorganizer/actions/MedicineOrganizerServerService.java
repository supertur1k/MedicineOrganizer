package com.example.medicineorganizer.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.medicineorganizer.Retrofit.RetrofitMedicineOrganizerServerService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dto.FirstAidKit;
import dto.FirstAidKitCreateRequestDTO;
import dto.JwtRequest;
import dto.JwtResponse;
import dto.UserDTO;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MedicineOrganizerServerService {

    public static void authAndGetToken(String username, String password, Callback<JwtResponse> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getAuthApi()
                .createAuthToken(new JwtRequest(username, password))
                .enqueue(callback);
    }

    public static void getFirstAndKitsByUsername(String username, Callback<List<FirstAidKit>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .getFirstAndKitsByUsername(username)
                .enqueue(callback);
    }

    public static void createFirstAndKitForUser(String username, String name, String description, Callback<Collection<FirstAidKit>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .createFirstAndKitForUser(new FirstAidKitCreateRequestDTO(username, name, description))
                .enqueue(callback);
    }

    public static boolean checkIfResponseBodyWithTokenIsEmpty(Response<JwtResponse> response) {
        return response.body() == null || response.body().getToken() == null || response.body().getId() == null ||
                response.body().getToken().isEmpty();
    }

    public static boolean checkIfResponseBodyRegistrationIsEmpty(Response<UserDTO> response) {
        return response.body() == null || response.body().getEmail() == null || response.body().getId() == null ||
                response.body().getUsername().isEmpty();
    }



}
