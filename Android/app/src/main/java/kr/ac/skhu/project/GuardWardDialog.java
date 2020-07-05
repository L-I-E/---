package kr.ac.skhu.project;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import kr.ac.skhu.project.domain.Guardian;
import kr.ac.skhu.project.item.User;
import kr.ac.skhu.project.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.ac.skhu.project.item.User.shared;

public class GuardWardDialog extends AppCompatActivity {
    DataService dataService = new DataService();

    private Context context;
    private final User user = shared;
    boolean bExist = false;

    public GuardWardDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {
        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);
        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.activity_guard_ward_dialog);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        final EditText etId = dlg.findViewById(R.id.etId);

        final ImageButton btSearch = (ImageButton) dlg.findViewById(R.id.btSearch);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strId = etId.getText().toString();
                dataService.select.exist(strId).enqueue(new Callback<Boolean>() {
                    @Override
                    public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                        bExist = response.body().booleanValue();
                        if (bExist) {
                            Toast.makeText(context, "아이디가 존재합니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "아이디가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Boolean> call, Throwable t) {
                        Toast.makeText(context, "서버가 불안정합니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        final Button btConnect = dlg.findViewById(R.id.btConnect);

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getId() != null && user.getWardId() != null && user.getId().equals(user.getWardId())) { // 피보호자라면
                    String strConnect = etId.getText().toString();
                    Map<String, String> map = new HashMap();
                    map.put("guardianId", strConnect);
                    map.put("wardId", user.getId());

                    dataService.insert.insertGuardian(map).enqueue(new Callback<Guardian>() {
                        @Override
                        public void onResponse(Call<Guardian> call, Response<Guardian> response) {
                            Toast.makeText(context, "보호자와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                            user.setGuardianId(response.body().getGuardianId());
                            dlg.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Guardian> call, Throwable t) {
                            Toast.makeText(context, "피보호자와 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    });
                } else if (user.getId() != null && user.getGuardianId() != null && user.getId().equals(user.getGuardianId())) { // 보호자라면
                    String strConnect = etId.getText().toString();
                    Map<String, String> map = new HashMap();
                    map.put("guardianId", user.getId());
                    map.put("wardId", strConnect);

                    dataService.insert.insertGuardian(map).enqueue(new Callback<Guardian>() {
                        @Override
                        public void onResponse(Call<Guardian> call, Response<Guardian> response) {
                            Toast.makeText(context, "피보호자와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                            user.setWardId(response.body().getWardId());
                            dlg.dismiss();
                        }

                        @Override
                        public void onFailure(Call<Guardian> call, Throwable t) {
                            Toast.makeText(context, "피보호자와 연결에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            dlg.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(context, "로그인에 오류가 생겼습니다. 로그아웃 후 다시 로그인해주세요.", Toast.LENGTH_LONG).show();
                    dlg.dismiss();
                }
            }
        });

        final Button btCancel = dlg.findViewById(R.id.btCancel);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
            }
        });
    }
}
