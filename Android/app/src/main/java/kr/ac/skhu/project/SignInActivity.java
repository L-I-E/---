package kr.ac.skhu.project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import kr.ac.skhu.project.domain.Member;
import kr.ac.skhu.project.item.User;
import kr.ac.skhu.project.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {

    DataService dataService = new DataService();


    private static final String TAG = "OAuthSampleActivity";

    /**
     * client 정보를 넣어준다.
     */
    private static String OAUTH_CLIENT_ID = "";
    private static String OAUTH_CLIENT_SECRET = "";
    // TODO: 키 입력

    private static String OAUTH_CLIENT_NAME = "네이버 아이디로 로그인";

    private static OAuthLogin mOAuthLoginInstance;
    private static Context mContext;

    private OAuthLoginButton mOAuthLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        final EditText etId = findViewById(R.id.etId);
        final EditText etPw = findViewById(R.id.etPw);
        final Button btSignIn = findViewById(R.id.btSignIn);
        final Button btSignUp = findViewById(R.id.btSignUp);

        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String strId = etId.getText().toString().trim();
                final String strPw = etPw.getText().toString().trim();

                dataService.select.exist(strId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body() == true) {
                            dataService.select.selectOne(strId).enqueue(new Callback<Member>() {
                                @Override
                                public void onResponse(Call<Member> call, Response<Member> response) {
                                    Member member = response.body();
                                    if (member.getPassword().equals(strPw)) {
                                        User.shared.setId(strId);
                                        User.shared.setName(member.getName());
                                        if(member.isGuardian()==1){
                                            User.shared.setGuardianId(member.getId());
                                        }else{
                                            User.shared.setWardId(member.getId());
                                        }
                                        Toast.makeText(SignInActivity.this, "로그인 완료", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(getApplicationContext(), MapFragmentActivity.class);
                                        startActivity(intent); // 액티비티 띄우기
                                        SignInActivity.this.finish();
                                    } else {
                                        Toast.makeText(SignInActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Member> call, Throwable t) {

                                }
                            });
                        } else {
                            Toast.makeText(SignInActivity.this, "존재하지 않는 아이디입니다.", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
                finish();
            }
        });
        /*
        // context 저장
        mContext = SignInActivity.this;
        // 1. 초기화
        initData();
        // 2. 로그인 버튼 세팅
        mOAuthLoginButton = (OAuthLoginButton) findViewById(R.id.btNaverLogin);
//        mOAuthLoginButton.setOAuthLoginHandler(mOAuthLoginHandler);

//        initView();

        this.setTitle("OAuthLoginSample Ver." + OAuthLogin.getVersion());
        */
    }

    private void initData() {
        mOAuthLoginInstance = OAuthLogin.getInstance();

        mOAuthLoginInstance.showDevelopersLog(true);
        mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME);

        /*
         * 2015년 8월 이전에 등록하고 앱 정보 갱신을 안한 경우 기존에 설정해준 callback intent url 을 넣어줘야 로그인하는데 문제가 안생긴다.
         * 2015년 8월 이후에 등록했거나 그 뒤에 앱 정보 갱신을 하면서 package name 을 넣어준 경우 callback intent url 을 생략해도 된다.
         */
        //mOAuthLoginInstance.init(mContext, OAUTH_CLIENT_ID, OAUTH_CLIENT_SECRET, OAUTH_CLIENT_NAME, OAUTH_callback_intent_url);
    }

//    private OAuthLoginHandler mOAuthLoginHandler = new OAuthLoginHandler() {
//        @Override
//        public void run(boolean success) {
//            if (success) { // 로그인 인증 성공
//                String accessToken = mOAuthLoginInstance.getAccessToken(mContext);
//                String refreshToken = mOAuthLoginInstance.getRefreshToken(mContext);
//                long expiresAt = mOAuthLoginInstance.getExpiresAt(mContext);
//                String tokenType = mOAuthLoginInstance.getTokenType(mContext);
////                mOauthAT.setText(accessToken);
////                mOauthRT.setText(refreshToken);
////                mOauthExpires.setText(String.valueOf(expiresAt));
////                mOauthTokenType.setText(tokenType);
////                mOAuthState.setText(mOAuthLoginModule.getState(mContext).toString());
//            } else { // 로그인 인증 실패
//                String errorCode = mOAuthLoginInstance.getLastErrorCode(mContext).getCode();
//                String errorDesc = mOAuthLoginInstance.getLastErrorDesc(mContext);
//                Toast.makeText(mContext, "errorCode:" + errorCode
//                        + ", errorDesc:" + errorDesc, Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        ;
//    };

}
