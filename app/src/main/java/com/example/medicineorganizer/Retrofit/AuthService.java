package com.example.medicineorganizer.Retrofit;

import java.util.List;

import dto.FirstAidKit;
import dto.JwtRequest;
import dto.JwtResponse;
import dto.RegUserDto;
import dto.UserDTO;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("/auth")
    Call<JwtResponse> createAuthToken(@Body JwtRequest jwtRequest);

    @POST("/registration")
    Call<UserDTO> registration(@Body RegUserDto regUserDto);
}
