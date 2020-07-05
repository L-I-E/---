package kr.ac.skhu.project;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PointF;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.geometry.LatLngBounds;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.Symbol;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.Align;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.MultipartPathOverlay;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;
import com.naver.maps.map.util.MarkerIcons;
import com.odsay.odsayandroidsdk.API;
import com.odsay.odsayandroidsdk.ODsayData;
import com.odsay.odsayandroidsdk.ODsayService;
import com.odsay.odsayandroidsdk.OnResultCallbackListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kr.ac.skhu.project.adapter.RouteAdapter;
import kr.ac.skhu.project.domain.Driving;
import kr.ac.skhu.project.domain.Member;
import kr.ac.skhu.project.item.Route;
import kr.ac.skhu.project.item.User;
import kr.ac.skhu.project.service.DataService;
import kr.ac.skhu.project.traffic.Info;
import kr.ac.skhu.project.traffic.PassStopList;
import kr.ac.skhu.project.traffic.Path;
import kr.ac.skhu.project.traffic.Station;
import kr.ac.skhu.project.traffic.SubPath;
import kr.ac.skhu.project.traffic.Traffic;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static kr.ac.skhu.project.item.User.shared;

public class MapFragmentActivity extends FragmentActivity implements OnMapReadyCallback, NaverMap.OnMapClickListener {
    DataService dataService = new DataService();

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;

    RouteAdapter adapter = new RouteAdapter();
    ArrayList<String> firstname = new ArrayList<>();
    ArrayList<String> destiname = new ArrayList<>();
    ArrayList<Long> trafficType = new ArrayList<>();
    ArrayList<Long> sectionTime = new ArrayList<>();
    ArrayList<String> no = new ArrayList<>();

    private final User user = shared; //user 정보

    Marker wardMarker = new Marker();
    Marker startMarker = new Marker();  // 출발지 마커
    Marker destinationMarker = new Marker(); // 도착지 마커

    ArrayList<Marker> transitMarker = new ArrayList<>();

    LatLng wardLatLng; // 피보호자 위치
    public LatLng start; // 출발지 위치
    public LatLng destination; // 목적지 위치

    MapFragment mapFragment;

    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1"; // 알림 채널

    MultipartPathOverlay multipartPath = new MultipartPathOverlay();

    ListView listview;

    TextView tvTotalTime;
    TextView tvPayment;

    EditText etstart;
    EditText etdestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_fragment);

        // 싱글톤 생성, Key 값을 활용하여 객체 생성
        ODsayService odsayService = ODsayService.init(MapFragmentActivity.this, "~"); // TODO: key 입력
        // 서버 연결 제한 시간(단위(초), default : 5초)
        odsayService.setReadTimeout(5000);
        // 데이터 획득 제한 시간(단위(초), default : 5초)
        odsayService.setConnectionTimeout(5000);

        // [START] 지도 보여주기
        FragmentManager fm = getSupportFragmentManager();
        mapFragment = (MapFragment) fm.findFragmentById(R.id.map);

        listview = findViewById(R.id.list);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        tvPayment = findViewById(R.id.tvPayment);
        etstart = findViewById(R.id.etstart);
        etdestination = findViewById(R.id.etdestination);

        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        // [END]

        // [START] 탭 선택
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.getTabAt(1).select();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);  // 위치에 따라 탭 상태 변경
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // TODO : tab의 상태가 선택되지 않음으로 변경.
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // TODO : 이미 선택된 tab이 다시
            }
        });
        // [END]

        TextView tvName = findViewById(R.id.tvName);
        Button btSignIn = findViewById(R.id.btSignIn);
        Button btLogOut = findViewById(R.id.btLogOut);
        Button btConnect = findViewById(R.id.btConnect);
        Button btDisconnect = findViewById(R.id.btDisconnect);
        Button btWithdrawl = findViewById(R.id.btWithdrawal);
        Button btSearch = findViewById(R.id.btSearchRoute);

        Button btCamera = findViewById(R.id.btCamera);
        Button btCancel = findViewById(R.id.btCancel);
        Button btWard = findViewById(R.id.btWard);

        //String URL = "https://api.odsay.com/v1/api/searchPubTransPathR?lang=0";
        //StringBuilder urlBuilder = new StringBuilder(URL);
        //urlBuilder.append(URL+"&SX="+start.longitude+"&SY"+start.latitude+"&EX="+destination.longitude+"&EY="+destination.latitude);

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                multipartPath.setMap(null);

                // listView 초기화
                firstname.clear();
                destiname.clear();
                trafficType.clear();
                sectionTime.clear();
                no.clear();
                adapter.getItems().clear();
                adapter.notifyDataSetChanged();

                setTransitMarkerNull();
                multipartPath = new MultipartPathOverlay();
                odsayService.requestSearchPubTransPath(String.valueOf(start.longitude), String.valueOf(start.latitude), String.valueOf(destination.longitude), String.valueOf(destination.latitude), "0", "0", "0", onResultCallbackListener);
            }
        });

        // [START] 로그인 여부에 따른 화면 결과
        if (user == null || user.getId() == "" || user.getId() == null) {
            btSignIn.setVisibility(View.VISIBLE);
            btLogOut.setVisibility(View.GONE);
            btConnect.setVisibility(View.GONE);
            btDisconnect.setVisibility(View.GONE);
            btWithdrawl.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);

            user.shared = new User(); // 값 초기화
        } else {
            btSignIn.setVisibility(View.GONE);
            btLogOut.setVisibility(View.VISIBLE);
            btWithdrawl.setVisibility(View.VISIBLE);
            tvName.setText(user.getName() + "님 환영합니다.");
            tvName.setVisibility(View.VISIBLE);

            if (user.getWardId() == null || user.getGuardianId() == null) {
                btConnect.setVisibility(View.VISIBLE);
            } else {
                btDisconnect.setVisibility(View.VISIBLE);
            }
        }
        // [END]

        // [START] 로그인 버튼
        btSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                intent = new Intent(MapFragmentActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // [END]

        // [START] 보호자 - 피보호자 연동 (아이디 입력하는 창)
        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GuardWardDialog customDialog = new GuardWardDialog(MapFragmentActivity.this);
                // 커스텀 다이얼로그를 호출한다.
                customDialog.callFunction();
            }
        });
        // [END]

        // [START] 로그아웃 버튼
        btLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shared = new User();
                Intent intent = new Intent(MapFragmentActivity.this, MapFragmentActivity.class);
                startActivity(intent);
                finish();
            }
        });
        // [END]

        // [START] 회원탈퇴 버튼
        btWithdrawl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.delete.deleteOne(user.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        String strGuardId = user.getGuardianId();
                        dataService.select.existDriving(strGuardId).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                dataService.delete.deleteDriving(strGuardId).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        shared = new User();

                                        Intent intent = new Intent(MapFragmentActivity.this, MapFragmentActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MapFragmentActivity.this, "회원탈퇴 실패", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // [END]

        // [START] 길찾기 연결 버튼
        btWard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.getId() != null && user.getGuardianId() != null) { // 피보호자일 때
                    dataService.select.existDriving(user.getGuardianId()).enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            if (response.body() == true) { // Driving이 있을 때
                                dataService.select.selectDriving(user.getGuardianId()).enqueue(new Callback<Driving>() {
                                    @Override
                                    public void onResponse(Call<Driving> call, Response<Driving> response) { // 연결
                                        Driving driving = response.body();
                                        start = new LatLng(driving.getStartLatitude(), driving.getStartLongitude());
                                        destination = new LatLng(driving.getDestinationLatitude(), driving.getDestinationLongitude());

                                        setStartMarker("");
                                        setDestinationMarker("");
                                        multipartPath.setMap(null);
                                        multipartPath = new MultipartPathOverlay();

                                        // listView 초기화
                                        firstname.clear();
                                        destiname.clear();
                                        trafficType.clear();
                                        sectionTime.clear();
                                        no.clear();
                                        adapter.getItems().clear();
                                        adapter.notifyDataSetChanged();

                                        setTransitMarkerNull();
                                        odsayService.requestSearchPubTransPath(String.valueOf(start.longitude), String.valueOf(start.latitude), String.valueOf(destination.longitude), String.valueOf(destination.latitude), "0", "0", "0", onResultCallbackListener);
                                    }

                                    @Override
                                    public void onFailure(Call<Driving> call, Throwable t) {

                                    }
                                });
                            } else {
                                Toast.makeText(MapFragmentActivity.this, "연결된 길찾기가 없습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {

                        }
                    });
                }
            }
        });
        // [END]

        //[START] 보호자 - 피보호자 연동 해제 버튼
        btDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strGuardId = user.getGuardianId();
                dataService.delete.deleteGuardian(strGuardId).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dataService.select.existDriving(strGuardId).enqueue(new Callback<Boolean>() {
                            @Override
                            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                                dataService.delete.deleteDriving(strGuardId).enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        // [START] 위치정보 삭제
                                        startMarker.setMap(null);
                                        destinationMarker.setMap(null);
                                        wardMarker.setMap(null);

                                        startMarker = new Marker();
                                        destinationMarker = new Marker();
                                        wardMarker = new Marker();
                                        setTransitMarkerNull();

                                        wardLatLng = null;
                                        start = null;
                                        destination = null;

                                        user.setWardId(null);
                                        multipartPath.setMap(null);
                                        multipartPath = new MultipartPathOverlay();
                                        // [END]

                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                                    }
                                });
                            }

                            @Override
                            public void onFailure(Call<Boolean> call, Throwable t) {

                            }
                        });
                    }

                    @Override
                    public void onFailure(@NotNull Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MapFragmentActivity.this, "연결되지 않았습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        // [END]

        // [START] 카메라 버튼 -> 피보호자의 위치 카메라로 보여줌
        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (wardLatLng != null) {
                    CameraUpdate camera = CameraUpdate.scrollTo(wardLatLng);
                    naverMap.moveCamera(camera);
                }
            }
        });
        // [END]

        // [START] 길찾기 취소
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strGuardId = user.getGuardianId();
                dataService.delete.deleteDriving(strGuardId).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // [START] 위치정보 삭제
                        startMarker.setMap(null);
                        destinationMarker.setMap(null);
                        setTransitMarkerNull();

                        startMarker = new Marker();
                        destinationMarker = new Marker();

                        start = null;
                        destination = null;

                        multipartPath.setMap(null);
                        multipartPath = new MultipartPathOverlay();
                        // [END]

                        // listView 초기화
                        firstname.clear();
                        destiname.clear();
                        trafficType.clear();
                        sectionTime.clear();
                        no.clear();
                        adapter.getItems().clear();
                        adapter.notifyDataSetChanged();

                        tvTotalTime.setText("");
                        tvPayment.setText("");
                        etstart.setText("");
                        etdestination.setText("");

                        Toast.makeText(MapFragmentActivity.this, "길찾기가 취소되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }
        });
        // [END]

        // [START]
        mapFragment.getMapAsync(this);
        // [END]

    }

    // [START] 결과값 가져오는 함수
    private OnResultCallbackListener onResultCallbackListener = new OnResultCallbackListener() {
        @Override
        public void onSuccess(ODsayData oDsayData, API api) {
            String json = oDsayData.getJson().toString();
            Log.d("traffic", json);
            Gson gson = new Gson();
            Traffic traffic = gson.fromJson(json, Traffic.class);

            if (traffic != null && traffic.getResult() != null && traffic.getResult().getPath() != null) {
                Toast.makeText(MapFragmentActivity.this, "길찾기 완료", Toast.LENGTH_SHORT).show();
                polyline(traffic.getResult().getPath()[0]);
            } else {
                Toast.makeText(MapFragmentActivity.this, "검색된 정보로 길을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onError(int i, String errorMessage, API api) {

        }
    };

    // (1): SubPath[] 모든 path를 line에 저장 매개변수 타입 SubPath[]로 변경
    // (2): SubPath[]의 타입을 반복문으로 돌면서 모두 넣어준다. 단 Type이 3인 걷는 Path의 경우 StartMarker와 EndMarker의 LatLang로 대체
    // (3):  (2)를 Line으로 표시하기 위해 배열 리스트의 처음과 마지막 인덱스는 StartMarker와 EndMarker의 LatLng 객체를 넣어주어야 함
    public void polyline(Path path) {
        int size = path.getSubPath().length;

        List<List<LatLng>> listLine = new ArrayList<>();

        Info info = path.getInfo();

        tvTotalTime.setText(info.getTotalTime() + "분  ");
//        TextView tvTotalWalkTime = findViewById(R.id.tvTotalWalkTime);
//        tvTotalWalkTime.setText("도보 "+info.getTotalWalkTime()+"분 | ");
        String strPayment = String.format("카드 %,d원", info.getPayment());
        tvPayment.setText(strPayment);

        int transitcount = 0;
        for (int i = 0; i < size; i++) {
            SubPath subpath = path.getSubPath()[i];
            PassStopList stopList = subpath.getPassStopList();
            if (stopList != null) {

                // 경로 시작 마커
                transitMarker.add(new Marker());
//              transitMarker.get(transitcount).setIcon(OverlayImage.fromResource(R.drawable.transit2)); // 이미지 바꿀 것
                transitMarker.get(transitcount).setCaptionText(subpath.getStartName());
                transitMarker.get(transitcount).setCaptionTextSize(12);
                transitMarker.get(transitcount).setCaptionColor(Color.BLUE);
                transitMarker.get(transitcount).setIcon(MarkerIcons.BLACK);
                transitMarker.get(transitcount).setIconTintColor(Color.RED);
                transitMarker.get(transitcount).setWidth(70);
                transitMarker.get(transitcount).setHeight(70);
                transitMarker.get(transitcount).setPosition(new LatLng(subpath.getStartY(), subpath.getStartX()));
                transitcount++;

                List<LatLng> Llist = new ArrayList<>();
                for (int j = 0; j < stopList.getStations().length; j++) {
                    Station station = stopList.getStations()[j];
                    LatLng coord = new LatLng(Double.parseDouble(station.getY()), Double.parseDouble(station.getX()));
                    Llist.add(coord);
                }
                listLine.add(Llist);

                // 경로 끝 마커
                transitMarker.add(new Marker());
//              transitMarker.get(transitcount).setIcon(OverlayImage.fromResource(R.drawable.transit2)); // 이미지 바꿀 것
                transitMarker.get(transitcount).setCaptionText(subpath.getEndName());
                transitMarker.get(transitcount).setCaptionTextSize(12);
                transitMarker.get(transitcount).setCaptionColor(Color.BLUE);
                transitMarker.get(transitcount).setIcon(MarkerIcons.BLACK);
                transitMarker.get(transitcount).setIconTintColor(Color.RED);
                transitMarker.get(transitcount).setWidth(70);
                transitMarker.get(transitcount).setHeight(70);
                transitMarker.get(transitcount).setPosition(new LatLng(subpath.getEndY(), subpath.getEndX()));
                transitcount++;

                firstname.add(subpath.getStartName());
                destiname.add(subpath.getEndName());
                trafficType.add(subpath.getTrafficType());
                sectionTime.add(subpath.getSectionTime());
                if (subpath.getTrafficType() == 1) { //지하철이라면
                    no.add(subpath.getLane()[0].getName());
                } else if (subpath.getTrafficType() == 2) {
                    no.add(subpath.getLane()[0].getBusNo());
                }
            } else {
                if (subpath.getTrafficType() == 3) { //도보일 경우
                    firstname.add("");
                    destiname.add("");
                    trafficType.add(subpath.getTrafficType());
                    sectionTime.add(subpath.getSectionTime());
                    no.add("");
                }
            }
        }

        for (int i = 0; i < firstname.size(); i++) {
            adapter.addItem(new Route(firstname.get(i), destiname.get(i), trafficType.get(i), sectionTime.get(i), no.get(i)));
        }
        listview.setAdapter(adapter);

        for (int i = 0; i < transitMarker.size(); i++) {
            transitMarker.get(i).setMap(naverMap);
        }

        multipartPath.setCoordParts(listLine);

        // 생략 못함
        // MultipartPathOverlay.ColorPart https://navermaps.github.io/android-map-sdk/reference/com/naver/maps/map/overlay/MultipartPathOverlay.ColorPart.html
        multipartPath.setColorParts(Arrays.asList(
                new MultipartPathOverlay.ColorPart(
                        Color.RED, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.GREEN, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.RED, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.GREEN, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.RED, Color.WHITE, Color.GRAY, Color.LTGRAY),
                new MultipartPathOverlay.ColorPart(
                        Color.GREEN, Color.WHITE, Color.GRAY, Color.LTGRAY)
        ));

        multipartPath.setMap(naverMap);
    }
    // [END]

    private void changeView(int index) {
        FrameLayout map = findViewById(R.id.map);
        LinearLayout menu = findViewById(R.id.menu);
        LinearLayout search = findViewById(R.id.search);
        switch (index) {
            case 0:
                menu.setVisibility(View.VISIBLE);
                map.setVisibility(View.INVISIBLE);
                search.setVisibility(View.INVISIBLE);
                break;
            case 1:
                menu.setVisibility(View.INVISIBLE);
                map.setVisibility(View.VISIBLE);
                search.setVisibility(View.INVISIBLE);
                mapFragment.getMapAsync(this);
                break;
            case 2:
                menu.setVisibility(View.INVISIBLE);
                map.setVisibility(View.INVISIBLE);
                search.setVisibility(View.VISIBLE);
                break;

        }
    }

    @Override
    public void onMapReady(@NonNull final NaverMap naverMap) {
        this.naverMap = naverMap;
        LocationOverlay locationOverlay = naverMap.getLocationOverlay();
        locationOverlay.setVisible(true);
        locationOverlay.setCircleRadius(30); // 원 반경 지정

        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setScaleBarEnabled(true); // 기본값 : true
        uiSettings.setZoomControlEnabled(true); // 기본값 : true
        uiSettings.setLocationButtonEnabled(true); // 기본값 : false

        naverMap.setBuildingHeight(1.0f); // 건물 높이를 1.0f으로 설정

        naverMap.setLocationSource(locationSource);
        //naverMap.setLocationTrackingMode(LocationTrackingMode.Follow); // 위치 추적 모드를 follow로 설정

        naverMap.setExtent(new LatLngBounds
                (new LatLng(31.43, 122.37),
                        new LatLng(44.35, 132)));
        // 화면에 보이는 범위 설정

        // [START] 지도 클릭하는 메소드 (onClick에서 onSymbolClick으로 변경) => 확인 바람
        naverMap.setOnSymbolClickListener(new NaverMap.OnSymbolClickListener() { // 지도 심벌을 클릭하면
            @Override
            public boolean onSymbolClick(@NonNull Symbol symbol) {
                if (user.getId() != null && user.getGuardianId() != null && user.getId().equals(user.getGuardianId())) { // 보호자일 때
                    if (symbol.getCaption() != null) {
                        Toast.makeText(MapFragmentActivity.this, symbol.getCaption(), Toast.LENGTH_SHORT).show();
                        LatLng current = symbol.getPosition();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MapFragmentActivity.this);
                        builder.setTitle("출발지/목적지 선택")
                                .setMessage(symbol.getCaption())
                                .setNegativeButton("출발지", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        multipartPath.setMap(null);
                                        multipartPath = new MultipartPathOverlay();
                                        setTransitMarkerNull();

                                        // listView 초기화
                                        firstname.clear();
                                        destiname.clear();
                                        trafficType.clear();
                                        sectionTime.clear();
                                        no.clear();
                                        adapter.getItems().clear();
                                        adapter.notifyDataSetChanged();

                                        //
                                        tvTotalTime.setText("");
                                        tvPayment.setText("");

                                        start = current;
                                        etstart.setText(symbol.getCaption());
                                        setStartMarker(symbol.getCaption());
                                        if (start != null && destination != null) {
                                            insertStartDestination();
                                        }
                                    }
                                })
                                .setPositiveButton("목적지", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        multipartPath.setMap(null);
                                        multipartPath = new MultipartPathOverlay();
                                        setTransitMarkerNull();

                                        // listView 초기화
                                        firstname.clear();
                                        destiname.clear();
                                        trafficType.clear();
                                        sectionTime.clear();
                                        no.clear();
                                        adapter.getItems().clear();
                                        adapter.notifyDataSetChanged();

                                        tvTotalTime.setText("");
                                        tvPayment.setText("");

                                        destination = current;
                                        etdestination.setText(symbol.getCaption());
                                        setDestinationMarker(symbol.getCaption()); // 목적지 마커

                                        if (start != null && destination != null) {
                                            insertStartDestination();
                                        }
                                    }
                                })
                                .show();
                        //mapFragment.getMapAsync(MapFragmentActivity.this::onMapReady);
                        return true;
                    }
                } else { // 로그인이 되어있지 않고 피보호자 연결을 하지 않았다면
                    Toast.makeText(MapFragmentActivity.this, "로그인하고 피보호자와 연결하신 후에\n길찾기 서비스를 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                    return false;
                }
                return false;
            }
        });
        //[END]

        naverMap.addOnLocationChangeListener(new NaverMap.OnLocationChangeListener() {
            @Override
            public void onLocationChange(@NonNull Location location) {
                Log.d("gps", User.shared.getId() + " " + location.getLatitude() + " " + location.getLongitude());
                if (user.getId() != null && user.getWardId() != null && user.getId().equals(user.getWardId())) { // 만약 내가 보호자가 있다면 = 피보호자라면
                    // 위치 정보를 가져와서
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    wardLatLng = new LatLng(latitude, longitude);
                    Map<String, Double> map = new HashMap<>();
                    map.put("latitude", latitude);
                    map.put("longitude", longitude);

                    // 서버에 저장
                    dataService.update.updateLocation(user.getId(), map).enqueue(new Callback<Member>() {
                        @Override
                        public void onResponse(Call<Member> call, Response<Member> response) {
                            if (destination != null) {
                                if (destination.distanceTo(wardLatLng) <= 100) {
                                    // [START] 목적지 근처에 피보호자가 도착하면 푸쉬 알림
                                    pushNotification("목적지 도착", user.getWardId() + "님은 현재 목적지에 도착하였습니다.", "경로 안내를 종료합니다.");
                                    // [END]

                                    // [START] Driving database 삭제하고 초기화 시키고 종료
                                    dataService.delete.deleteDriving(user.getId()).enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            Toast.makeText(MapFragmentActivity.this, "경로 안내를 종료합니다.", Toast.LENGTH_SHORT).show();
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d("경로안내", "종료 실패");
                                        }
                                    });
                                    // [END]

                                    // [START] 위치정보 삭제
                                    startMarker.setMap(null);
                                    destinationMarker.setMap(null);
                                    setTransitMarkerNull();

                                    startMarker = new Marker();
                                    destinationMarker = new Marker();
                                    start = null;
                                    destination = null;
                                    // [END]
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<Member> call, Throwable t) {

                        }
                    });

                }
            }
        });
        // [END]

        // [START] 내가 보호자라면 피보호자의 위치를 가져와서 지도에 반영
        if (user.getId() != null && user.getGuardianId() != null && user.getId().equals(user.getGuardianId())) {
            (new Thread(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.interrupted())
                        try {
                            Thread.sleep(5000);
                            runOnUiThread(new Runnable() // start actions in UI thread
                            {
                                @Override
                                public void run() {
                                    dataService.select.selectOne(user.getWardId()).enqueue(new Callback<Member>() {
                                        @Override
                                        public void onResponse(Call<Member> call, Response<Member> response) {
                                            Member ward = response.body();
                                            //Toast.makeText(MapFragmentActivity.this, ward.getLatitude() + " " + ward.getLongitude(), Toast.LENGTH_LONG).show();
                                            wardLatLng = new LatLng(ward.getLatitude(), ward.getLongitude());
                                            wardMarker.setIcon(OverlayImage.fromResource(R.drawable.child));
                                            wardMarker.setPosition(wardLatLng);
                                            wardMarker.setCaptionText(ward.getName());
                                            wardMarker.setMap(naverMap);

                                            // [START] 피보호자가 환승구역 근처에 있으면 푸쉬알림

                                            // [END]

                                            if (destination != null) {
                                                if (destination.distanceTo(wardLatLng) <= 100) {
                                                    // [START] 목적지 근처에 피보호자가 도착하면 푸쉬 알림
                                                    pushNotification("목적지 도착", user.getWardId() + "님은 현재 목적지에 도착하였습니다.", "경로 안내를 종료합니다.");
                                                    // [END]

                                                    // [START] Driving database 삭제하고 초기화 시키고 종료
                                                    dataService.delete.deleteDriving(user.getId()).enqueue(new Callback<ResponseBody>() {
                                                        @Override
                                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                                            Toast.makeText(MapFragmentActivity.this, "경로 안내를 종료합니다.", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                                            Log.d("경로안내", "종료 실패");
                                                        }
                                                    });
                                                    // [END]

                                                    // [START] 위치정보 삭제
                                                    startMarker.setMap(null);
                                                    destinationMarker.setMap(null);
                                                    setTransitMarkerNull();

                                                    startMarker = new Marker();
                                                    destinationMarker = new Marker();
                                                    start = null;
                                                    destination = null;
                                                    // [END]
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Member> call, Throwable t) {

                                        }
                                    });
                                }
                            });
                        } catch (InterruptedException e) {
                            // ooops
                        }
                }
            })).start();
        }
        // [END]

    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) {
                naverMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {

    }

    public void insertStartDestination() {
        Map<String, String> map = new HashMap<>();
        map.put("guardId", user.getId());
        map.put("wardId", user.getWardId());
        map.put("startLatitude", String.valueOf(start.latitude));
        map.put("startLongitude", String.valueOf(start.longitude));
        map.put("destinationLatitude", String.valueOf(destination.latitude));
        map.put("destinationLongitude", String.valueOf(destination.longitude));
        map.put("isGuardian", "1");

        dataService.insert.insertDriving(map).enqueue(new Callback<Driving>() {
            @Override
            public void onResponse(Call<Driving> call, Response<Driving> response) {
//                Toast.makeText(MapFragmentActivity.this, destination.longitude+"", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Driving> call, Throwable t) {
                Toast.makeText(MapFragmentActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // [START] 푸쉬 알림
    public void pushNotification(String ticker, String title, String text) {
        final NotificationManager notificationManager = (NotificationManager) MapFragmentActivity.this.getSystemService(MapFragmentActivity.this.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT));
            builder = new NotificationCompat.Builder(MapFragmentActivity.this, CHANNEL_ID); //하위 버전일 경우
        } else {
            builder = new NotificationCompat.Builder(MapFragmentActivity.this, CHANNEL_ID);
        }

        final Intent intent = new Intent(MapFragmentActivity.this.getApplicationContext(), MapFragmentActivity.class);

        //푸시 알림을 터치하여 실행할 작업에 대한 Flag 설정 (현재 액티비티를 최상단으로 올린다 | 최상단 액티비티를 제외하고 모든 액티비티를 제거한다)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        //앞서 생성한 작업 내용을 Notification 객체에 담기 위한 PendingIntent 객체 생성
        PendingIntent pendnoti = PendingIntent.getActivity(MapFragmentActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // 푸쉬 알림 설정
        builder.setSmallIcon(R.drawable.icon).setTicker(ticker).setWhen(System.currentTimeMillis())
                .setNumber(1).setContentTitle(title).setContentText(text)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE).setContentIntent(pendnoti).setAutoCancel(true).setOngoing(true);

        //NotificationManager를 이용하여 푸시 알림 보내기
        notificationManager.notify(1, builder.build());
    }
    // [END]

    // [START] 출발지 마커 지정
    public void setStartMarker(String symbolCaption) {
        startMarker.setPosition(start);
        startMarker.setCaptionAligns(Align.Top);
        startMarker.setCaptionTextSize(16);
        startMarker.setCaptionColor(Color.YELLOW);
        startMarker.setCaptionHaloColor(Color.BLACK);
        startMarker.setIcon(MarkerIcons.YELLOW);
        if (symbolCaption.equals("") || symbolCaption == null) {
            startMarker.setCaptionText("출발지");
        } else {
            startMarker.setCaptionText("출발지" + ": " + symbolCaption);
        }
        startMarker.setMap(naverMap);
    }

    // [START] 목적지 마커 지정
    public void setDestinationMarker(String symbolCaption) {
        destinationMarker.setPosition(destination);
        destinationMarker.setCaptionAligns(Align.Top);
        destinationMarker.setCaptionTextSize(16);
        destinationMarker.setCaptionColor(Color.YELLOW);
        destinationMarker.setCaptionHaloColor(Color.BLACK);
        destinationMarker.setIcon(MarkerIcons.YELLOW);
        if (symbolCaption.equals("") || symbolCaption == null) {
            destinationMarker.setCaptionText("목적지");
        } else {
            destinationMarker.setCaptionText("목적지" + ": " + symbolCaption);
        }
        destinationMarker.setMap(naverMap);
    }
    // [END]

    // [START] transitMarker 초기화
    public void setTransitMarkerNull() {
        for (int i = 0; i < transitMarker.size(); i++) {
            transitMarker.get(i).setMap(null);
        }
        transitMarker.clear();
    }
    // [END]
}