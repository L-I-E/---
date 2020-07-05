package kr.ac.skhu.project;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import kr.ac.skhu.project.domain.Member;
import kr.ac.skhu.project.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {
    DataService dataService = new DataService();
    boolean bPwEqual = false;
    boolean bIdExistence = false;
    int isGuardian = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final EditText etId = findViewById(R.id.etId);
        final EditText etName = findViewById(R.id.etName);
        final EditText etPw = findViewById(R.id.etPw);
        final EditText etPwok = findViewById(R.id.etPwok);
        final TextView tvPwEqual = findViewById(R.id.tvPwEqual);

        final Button btIdExistence = findViewById(R.id.btIdExistence);
        btIdExistence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.select.exist(etId.getText().toString().trim()).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        if (response.body() == false) { // 존재하지 않으면 가능
//                            Toast.makeText(SignUpActivity.this, "해도 됨", Toast.LENGTH_LONG).show();
                            bIdExistence = true;
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setTitle("아이디 중복 확인")
                                    .setMessage("아이디 사용 가능합니다.")
                                    .setPositiveButton("예",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                    .show();
                        } else { // 존재하면 불가능
//                            Toast.makeText(SignUpActivity.this, "안됨", Toast.LENGTH_LONG).show();
                            bIdExistence = false;
                            AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                            builder.setTitle("아이디 중복 확인")
                                    .setMessage("아이디 사용 불가능합니다.\n아이디를 변경하세요.")
                                    .setPositiveButton("예",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            })
                                    .show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {

                    }
                });
            }
        });

        etId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        etPw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strEtPw = etPw.getText().toString();
                String strEtPwOk = etPwok.getText().toString();

                if (strEtPw.equals(strEtPwOk)) {
                    tvPwEqual.setText("비밀번호가 일치합니다.");
                    tvPwEqual.setVisibility(View.VISIBLE);
                    bPwEqual = true;
                } else {
                    tvPwEqual.setText("비밀번호가 일치하지 않습니다.");
                    tvPwEqual.setVisibility(View.VISIBLE);
                    bPwEqual = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        etPwok.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String strEtPw = etPw.getText().toString();
                String strEtPwOk = etPwok.getText().toString();

                if (strEtPw.equals(strEtPwOk)) {
                    tvPwEqual.setText("비밀번호가 일치합니다.");
                    tvPwEqual.setVisibility(View.VISIBLE);
                    bPwEqual = true;
                } else {
                    tvPwEqual.setText("비밀번호가 일치하지 않습니다.");
                    tvPwEqual.setVisibility(View.VISIBLE);
                    bPwEqual = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        final Button btSignUp = findViewById(R.id.btSignUp);
        final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.rgWardOrGuard);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbGuard) {
                    isGuardian = 1;
                } else if (checkedId == R.id.rbWard) {
                    isGuardian = 0;
                }
            }
        });

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bPwEqual && bIdExistence) {
                    Map<String, String> map = new HashMap();
                    map.put("id", etId.getText().toString().trim());
                    map.put("name", etName.getText().toString().trim());
                    map.put("password", etPw.getText().toString().trim());
                    map.put("isGuardian",isGuardian+"");

                    dataService.insert.insertOne(map).enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                            startActivity(intent);//액티비티 띄우기
                            SignUpActivity.this.finish();
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {
                            Log.d("회원가입 실패 : 네트워크 확인 ", t.toString());
                        }
                    });
                } else {
                    Toast.makeText(SignUpActivity.this, "값이 정확하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}