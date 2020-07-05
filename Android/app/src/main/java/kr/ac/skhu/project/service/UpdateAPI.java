package kr.ac.skhu.project.service;

import java.util.Map;

import kr.ac.skhu.project.domain.Member;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UpdateAPI {
    @POST("member/update/{id}")
    Call<Member> updateLocation(@Path("id") String id, @Body Map<String, Double> map);
}
