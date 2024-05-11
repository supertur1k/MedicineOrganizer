package com.example.medicineorganizer.actions;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.medicineorganizer.Retrofit.RetrofitMedicineOrganizerServerService;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import dto.AddMedIntoFirstAidKitDTO;
import dto.AddMedicamentIntoFAKBarcodeRequest;
import dto.FirstAidKit;
import dto.FirstAidKitCreateRequestDTO;
import dto.FirstAidKitIdUsernameDTO;
import dto.JwtRequest;
import dto.JwtResponse;
import dto.Medicament;
import dto.NotificationDto;
import dto.ScheduleCreateRequestDTO;
import dto.ScheduleCreateResponseDTO;
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

    public static void setMedicamentByBarcode(Long id, String barcode, Callback<Collection<Medicament>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .addMedicamentIntoFirstAndKitByBarcode(new AddMedicamentIntoFAKBarcodeRequest(id, barcode))
                .enqueue(callback);
    }

    public static void addMedicamentWithData(Long id, String nameOfTheMedicament, String description, String amount, Callback<Collection<Medicament>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .addMedicamentIntoFirstAndKit(new AddMedIntoFirstAidKitDTO(id, nameOfTheMedicament, description, amount))
                .enqueue(callback);
    }

    public static void deleteMedicamentForUser(Long id, String name, Callback<Collection<Medicament>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .addMedicamentIntoFirstAndKitByBarcode(id, name)
                .enqueue(callback);
    }

    public static void createSchedule(ScheduleCreateRequestDTO scheduleCreateRequestDTO, Callback<Collection<ScheduleCreateResponseDTO>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .createScheduleForUser(scheduleCreateRequestDTO)
                .enqueue(callback);
    }

    public static void getSchedule(String username, Callback<Collection<ScheduleCreateResponseDTO>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .getSchedulesForUser(username)
                .enqueue(callback);
    }

    public static void deleteSchedule(Long id, Callback<Collection<ScheduleCreateResponseDTO>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .deleteSchedulesForUser(id)
                .enqueue(callback);
    }


    public static void getNotifications(String username, Callback<Collection<NotificationDto>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .getNotifications(username)
                .enqueue(callback);
    }

    public static void removeFirstAndFromForUser(String username, Long id, Callback<Collection<FirstAidKit>> callback) {
        RetrofitMedicineOrganizerServerService.getInstance()
                .getApiMainService()
                .removeFirstAndFromForUser(new FirstAidKitIdUsernameDTO(username, id))
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
