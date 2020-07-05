package kr.ac.skhu.project.service;

import java.util.Map;

import kr.ac.skhu.project.domain.Driving;
import kr.ac.skhu.project.domain.Guardian;
import kr.ac.skhu.project.domain.Member;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface InsertAPI {
    @POST("member/insert")
    Call<Member> insertOne(@Body Map<String, String> map);

    @POST("member/insert")
    Call<Member> insertLocation(@Body Member member);

    @POST("guardian/insert")
    Call<Guardian> insertGuardian(@Body Map<String, String> map);

    @POST("driving/insert")
    Call<Driving> insertDriving(@Body Map<String, String> map);
}
