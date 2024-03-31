package com.example.medicineorganizer.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitMedicineOrganizerServerService {
    private static RetrofitMedicineOrganizerServerService retrofitMedicineOrganizerServerService;
    private static final String BASE_URL = "http://10.0.2.2:8080";
    private final Retrofit retrofit;

    private RetrofitMedicineOrganizerServerService() {
        HttpLoggingInterceptor logsInterceptor = new HttpLoggingInterceptor();
        logsInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .addInterceptor(logsInterceptor);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client.build())
                .build();
    }

    public static RetrofitMedicineOrganizerServerService getInstance() {
        if (retrofitMedicineOrganizerServerService == null) {
            retrofitMedicineOrganizerServerService = new RetrofitMedicineOrganizerServerService();
        }
        return retrofitMedicineOrganizerServerService;
    }

    public AuthService getAuthApi() {
        return retrofit.create(AuthService.class);
    }

    public ApiMainService getApiMainService() {
        return retrofit.create(ApiMainService.class);
    }
}