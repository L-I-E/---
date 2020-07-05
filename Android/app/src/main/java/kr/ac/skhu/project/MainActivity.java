package kr.ac.skhu.project;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.kakao.util.maps.helper.Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      카카오 지도
//        MapView mapView = new MapView(this);
//
//        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
//        mapViewContainer.addView(mapView);

        String key = getKeyHash(this);
        Log.d("키 해시: ", key);
        Button btNext = findViewById(R.id.btNext);
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MainActivity.this, MapFragmentActivity.class);
                startActivity(intent);
            }
        });
    }

    public String getKeyHash(final Context context) {
        PackageInfo packageInfo = Utility.getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                Log.d("트라이 캐치 에러", "Unable to get MessageDigest. signature = " + signature);
            }
        }
        return null;
    }

}
