package kr.ac.skhu.project.service;

import java.util.List;

import kr.ac.skhu.project.domain.Driving;
import kr.ac.skhu.project.domain.Member;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface SelectAPI {
    @GET(value = "member/select/{id}")
    Call<Member> selectOne(@Path("id") String id);

    @GET("member/select")
    Call<List<Member>> selectAll();

    @GET("member/exist/{id}")
    Call<Boolean> exist(@Path("id") String id);

    @GET("driving/exist/{guardId}")
    Call<Boolean> existDriving(@Path("guardId") String guardId);

    @GET(value = "driving/select/{guardId}")
    Call<Driving> selectDriving(@Path("guardId")String guardId);
}
