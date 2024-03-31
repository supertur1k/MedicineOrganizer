package com.example.medicineorganizer.Retrofit;

import java.util.Collection;
import java.util.List;

import dto.FirstAidKit;
import dto.FirstAidKitCreateRequestDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiMainService {

    @GET("/getFirstAndKitsByUsername")
    Call<List<FirstAidKit>> getFirstAndKitsByUsername(@Query("username") String username);

    @POST("/creteFirstAndKitForUser")
    Call<Collection<FirstAidKit>> createFirstAndKitForUser(@Body FirstAidKitCreateRequestDTO firstAidKitCreateRequestDTO);

}