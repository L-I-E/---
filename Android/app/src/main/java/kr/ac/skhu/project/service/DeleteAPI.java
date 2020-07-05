package kr.ac.skhu.project.service;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DeleteAPI {
    @POST("member/delete/{id}")
    Call<ResponseBody> deleteOne(@Path("id") String id);

    @POST("guardian/delete/{guardianId}")
    Call<ResponseBody> deleteGuardian(@Path("guardianId")String guardianId);

    @POST("driving/delete/{guardId}")
    Call<ResponseBody> deleteDriving(@Path("guardId") String guardId);
}
