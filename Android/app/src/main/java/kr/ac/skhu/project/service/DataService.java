package kr.ac.skhu.project.service;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DataService {
    private String BASE_URL = "http://127.0.0.1:8080/"; // TODO REST API 퍼블릭 IP로 변경

    Retrofit retrofitClient =
            new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(new OkHttpClient.Builder().build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

    public SelectAPI select = retrofitClient.create(SelectAPI.class);
    public InsertAPI insert = retrofitClient.create(InsertAPI.class);
    public UpdateAPI update = retrofitClient.create(UpdateAPI.class);
    public DeleteAPI delete = retrofitClient.create(DeleteAPI.class);
}
