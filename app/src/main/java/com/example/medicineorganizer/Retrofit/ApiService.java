package com.example.medicineorganizer.Retrofit;

import dto.JwtRequest;
import dto.JwtResponse;
import dto.RegUserDto;
import dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {

    @POST("/auth")
    Call<JwtResponse> createAuthToken(@Body JwtRequest jwtRequest);

    @POST("/registration")
    Call<UserDTO> registration(@Body RegUserDto regUserDto);
}
