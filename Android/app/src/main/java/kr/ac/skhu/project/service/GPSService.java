//package kr.ac.skhu.project.service;
//
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.IBinder;
//import android.util.Log;
//
//import androidx.annotation.Nullable;
//import androidx.core.app.NotificationCompat;
//import androidx.core.app.NotificationManagerCompat;
//import androidx.core.content.ContextCompat;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import kr.ac.skhu.project.MapFragmentActivity;
//import kr.ac.skhu.project.domain.Member;
//import kr.ac.skhu.project.item.User;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class GPSService extends Service implements LocationListener {
//
//    DataService dataService = new DataService();
//    private final String id = User.shared.getId();
//    Location location;
//
//    protected LocationManager locationManager;
//
//    //push 알람
//
//    NotificationManagerCompat manager;
//    NotificationCompat.Builder Init;
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//    }
//
//
//    //서비스가 죽었다가 다시 실행이 될 때, 호출되는 함수
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//
//        Log.d("test", "실행");
//        if (Build.VERSION.SDK_INT >= 23 &&
//                ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return START_NOT_STICKY;
//        } else {
//
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
//                    1000,
//                    1,
//                    this);
//            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                    1000,
//                    1,
//                    this);
//        }
//
//        //알람 등록
//        {
//            manager = NotificationManagerCompat.from(this);
//            Intent FineIntent = new Intent(this, MapFragmentActivity.class);
//
//            PendingIntent FinePendingIntent = PendingIntent.getActivity(this
//                    , 0, FineIntent, 0); //알람을 눌렀을 때 해당 엑티비티로
//
////
////            Init = new NotificationCompat.Builder(this, CHAANEL_ID)
////                    .setContentTitle("Pro-Miss")
////                    .setAutoCancel(true)// 사용자가 알람을 탭했을 때, 알람이 사라짐
////                    .setContentText("위치정보를 수신중입니다")
////                    .setSmallIcon(R.drawable.flag_icon)
////                    .setContentIntent(FinePendingIntent)
////                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
////
//
//            startForeground(10, Init.build());
//
//            //  manager.notify(30, New_Alert);
//            return START_STICKY;  //서비스가 종료되어도 다시 시작
//        }
//    }
//
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        try {
//            locationManager.removeUpdates(this);//더 이상 필요하지 않을 때, 자원 누락을 방지하기 위해
//
//        } catch (NullPointerException e) {
//            //locationmanager가 null일 때
//        }
//    }
//
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//
//    //사용자의 gps가 변할 때, 서버와 통신하는 Callback 함수
//    @Override
//    public void onLocationChanged(final Location location) {
//    //    Log.d("gps", "LocationChanged");
//        this.location = location;
//        double latitude = location.getLatitude();
//        double longitude = location.getLongitude();
//        Map<String, Double> map = new HashMap<>();
//        map.put("latitude", latitude);
//        map.put("longitude", longitude);
//
//        dataService.update.updateLocation(id, map).enqueue(new Callback<Member>() {
//            @Override
//            public void onResponse(Call<Member> call, Response<Member> response) {
//             //   Log.d("gps change", "gps change");
//            }
//
//            @Override
//            public void onFailure(Call<Member> call, Throwable t) {
//            //    Log.d("gps change","변경 실패");
//            }
//        });
//
//    }
//
//    // LocationListener
//    ////////////////////////////////////////////////////////////////////////////////////////
//    @Override
//    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Log.d("gps", "statusChange");
//    }
//
//    @Override
//    public void onProviderEnabled(String provider) {
//        Log.d("gps", "providerEnabled");
//    }
//
//    @Override
//    public void onProviderDisabled(String provider) {
//        Log.d("gps", "providerDisabled");
//    }
//
//
//}