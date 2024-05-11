package com.example.medicineorganizer.Retrofit;

import java.util.Collection;
import java.util.List;

import dto.AddMedIntoFirstAidKitDTO;
import dto.AddMedicamentIntoFAKBarcodeRequest;
import dto.FirstAidKit;
import dto.FirstAidKitCreateRequestDTO;
import dto.FirstAidKitIdUsernameDTO;
import dto.FirstAidKitIdUsernameDTO2Users;
import dto.Medicament;
import dto.NotificationDto;
import dto.ScheduleCreateRequestDTO;
import dto.ScheduleCreateResponseDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiMainService {

    @GET("/getFirstAndKitsByUsername")
    Call<List<FirstAidKit>> getFirstAndKitsByUsername(@Query("username") String username);

    @POST("/creteFirstAndKitForUser")
    Call<Collection<FirstAidKit>> createFirstAndKitForUser(@Body FirstAidKitCreateRequestDTO firstAidKitCreateRequestDTO);

    @POST("/addMedicamentIntoFirstAndKitById")
    Call<Collection<Medicament>> addMedicamentIntoFirstAndKit(@Body AddMedIntoFirstAidKitDTO addMedIntoFirstAidKitDTO);

    @POST("/addMedicamentIntoFirstAndKitByBarcode")
    Call<Collection<Medicament>> addMedicamentIntoFirstAndKitByBarcode(@Body AddMedicamentIntoFAKBarcodeRequest addMedicamentIntoFAKBarcodeRequest);

    @DELETE("/hardDeleteMedicamentForUser")
    Call<Collection<Medicament>> addMedicamentIntoFirstAndKitByBarcode(@Query("id") Long id, @Query("medicament_name") String medicament_name);

    @POST("/createSchedule")
    Call<Collection<ScheduleCreateResponseDTO>> createScheduleForUser(@Body ScheduleCreateRequestDTO scheduleDTO);

    @GET("/getSchedulesForUser")
    Call<Collection<ScheduleCreateResponseDTO>> getSchedulesForUser(@Query("username") String username);

    @DELETE("/deleteSchedule")
    Call<Collection<ScheduleCreateResponseDTO>> deleteSchedulesForUser(@Query("id") Long id);

    @GET("/getNotifications")
    Call<Collection<NotificationDto>> getNotifications(@Query("username") String username);

    @HTTP(method = "DELETE", path = "/removeFirstAndFromForUser", hasBody = true)
    Call<Collection<FirstAidKit>> removeFirstAndFromForUser(@Body FirstAidKitIdUsernameDTO firstAidKitIdUsernameDTO);

    @POST("/createNotificationInviteToFak")
    Call<Collection<NotificationDto>> createNotificationInviteToFak(@Body FirstAidKitIdUsernameDTO2Users firstAidKitIdUsernameDTO2Users);

    @DELETE("/deleteNotification")
    Call<Collection<NotificationDto>> deleteNotification(@Query("id") Long id);

    @POST("/readNotification")
    Call<Collection<NotificationDto>> readNotification(@Query("id") Long idOfNotification, @Query("username") String username);


}
