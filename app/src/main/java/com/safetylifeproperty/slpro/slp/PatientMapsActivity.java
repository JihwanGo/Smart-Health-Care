package com.safetylifeproperty.slpro.slp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PatientMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener {
    private Geocoder geocoder = new Geocoder(this);
    private GoogleMap mMap;
    public String x, y;
    private LocationManager locationManager;
    private LocationListener locationListener;

    TextView addrs, tv_marker;
    View marker_root_view;
    Marker selectedMarker;
    ScrollView linear;
    TextView tv;
    String location_str_tmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_maps);
        TextView xx = (TextView) findViewById(R.id.textView6);
        TextView yy = (TextView) findViewById(R.id.textView7);
        addrs = (TextView) findViewById(R.id.textView8);

        Button Details_btn = (Button) findViewById(R.id.Details_btn);
        settingGPS();
        Snackbar.make(getWindow().getDecorView().getRootView(), "위치찾는중...", 10000).show();


        Details_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                linear = (ScrollView) View.inflate(PatientMapsActivity.this, R.layout.activity_main_sc, null);
                tv = (TextView) linear.findViewById(R.id.txview_sc);
                tv.setText(location_str_tmp);
                AlertDialog.Builder builder = new AlertDialog.Builder(PatientMapsActivity.this);
                builder.setTitle("S.L.P 지킴이 - Maps");
                builder.setView(linear);
                builder.setIcon(android.R.drawable.ic_dialog_alert);
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });


        Intent intent = getIntent();
        x = intent.getStringExtra("x");
        y = intent.getStringExtra("y");
        xx.setText(x.toString());
        yy.setText(y.toString());

        addrs.setText(trand_addr());

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        if (!isScreenOn) {
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long millisecond = 3000;
            vibe.vibrate(millisecond);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void settingGPS() {
        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                mMap.clear();
                getMyLocation();
                double medic_latitude = location.getLatitude();
                double medic_longitude = location.getLongitude();

                station_locat(medic_latitude, medic_longitude, "Medic", 14);


                LatLng locaions_x = new LatLng(Double.parseDouble(x), Double.parseDouble(y));
                LatLng locaions_y = new LatLng(medic_latitude, medic_longitude);
                s_n_line(locaions_x, locaions_y, 5);

                ArrayList<MarkerItem> sampleList = new ArrayList();
                //거리 계산 알고리즘
                double distance;
                Location locationA = new Location("point A");
                locationA.setLatitude(Double.parseDouble(x));
                locationA.setLongitude(Double.parseDouble(y));
                Location locationB = new Location("point B");
                locationB.setLatitude(medic_latitude);
                locationB.setLongitude(medic_longitude);
                distance = locationA.distanceTo(locationB);


                location_str_tmp = String.format("- 현재 환자 위치\n\t위도:%f\n\t경도:%f\n\t거리:%s km\n\t- 현재 의료진 위치\n\t위도:%f\n\t경도:%f\n\t고도:%f", Double.parseDouble(x), Double.parseDouble(y), String.format("%.1f", (Math.ceil(distance) / 1000)), location.getLatitude(), location.getLongitude(), location.getAltitude());
                linear = (ScrollView) View.inflate(PatientMapsActivity.this, R.layout.activity_main_sc, null);
                tv = (TextView) linear.findViewById(R.id.txview_sc);
                tv.setText(location_str_tmp);
                sampleList.add(new MarkerItem(Double.parseDouble(x), Double.parseDouble(y), String.format("환자\n%.1f km", (Math.ceil(distance) / 1000))));

                for (MarkerItem markerItem : sampleList) {
                    addMarker(markerItem, false);
                }

                // TODO 위도, 경도로 하고 싶은 것
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    private Location getMyLocation() {
        Location currentLocation = null;
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

            // 수동으로 위치 구하기
            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
            }
        } catch (SecurityException e) {

        }

        return currentLocation;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        locationManager.removeUpdates(locationListener);
        mMap.clear();
    }


    public String trand_addr() {
        String result = "";

        List<Address> list = null;
        try {
            double d1 = Double.parseDouble(x.toString());
            double d2 = Double.parseDouble(y.toString());
            list = geocoder.getFromLocation(d1, d2, 1);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("test", "입출력 오류 - 서버에서 주소변환시 에러발생");
        }
        if (list != null) {
            if (list.size() == 0) {
                result = "해당되는 주소 정보는 없습니다";
            } else {
                Log.d("log", list.get(0).toString());
                Log.e("log", list.get(0).toString());
                Log.i("log", list.get(0).toString());

                String tx = list.get(0).toString();
                int xq1 = tx.indexOf("[0:");
                int xq2 = tx.indexOf("],");
                tx = tx.substring(xq1 + 4, xq2 - 1);

                result = tx;
            }
        }
        return result;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        double latitude = 0;
        double longitude = 0;
        Location userLocation = getMyLocation();
        if (userLocation != null) {
            // TODO 위치를 처음 얻어왔을 때 하고 싶은 것
            latitude = userLocation.getLatitude();
            longitude = userLocation.getLongitude();
            station_locat(latitude, longitude, "Medic", 16);
        }


        mMap.setOnMarkerClickListener(this);
        mMap.setOnMapClickListener(this);

        setCustomMarkerView();


    }

    public void s_n_line(LatLng start, LatLng end, int line_size) {

        mMap.addPolyline(new PolylineOptions().add(start, end).width(line_size).color(Color.RED));

    }

    public void station_locat(Double x, Double y, String title, int zoom) {
        LatLng locaions = new LatLng(x, y);
        mMap.addMarker(new MarkerOptions().position(locaions).title(title));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locaions, zoom));
    }


    private void setCustomMarkerView() {

        marker_root_view = LayoutInflater.from(this).inflate(R.layout.marker_layout, null);
        tv_marker = (TextView) marker_root_view.findViewById(R.id.tv_marker);
    }


    private Marker addMarker(MarkerItem markerItem, boolean isSelectedMarker) {


        LatLng position = new LatLng(markerItem.getLat(), markerItem.getLon());
        String formatted = markerItem.getSatus();

        tv_marker.setText(formatted);

        if (isSelectedMarker) {
            tv_marker.setBackgroundResource(R.drawable.ic_marker_red);
            tv_marker.setTextColor(Color.WHITE);
        } else {
            tv_marker.setBackgroundResource(R.drawable.ic_markers);
            tv_marker.setTextColor(Color.BLACK);
        }

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(formatted);
        markerOptions.position(position);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(this, marker_root_view)));


        return mMap.addMarker(markerOptions);

    }


    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }


    private Marker addMarker(Marker marker, boolean isSelectedMarker) {
        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        String name = marker.getTitle();
        MarkerItem temp = new MarkerItem(lat, lon, name);
        return addMarker(temp, isSelectedMarker);

    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        CameraUpdate center = CameraUpdateFactory.newLatLng(marker.getPosition());
        mMap.animateCamera(center);

        changeSelectedMarker(marker);

        return true;
    }


    private void changeSelectedMarker(Marker marker) {
        // 선택했던 마커 되돌리기
        if (selectedMarker != null) {
            addMarker(selectedMarker, false);
            selectedMarker.remove();
        }
        // 선택한 마커 표시
        if (marker != null) {
            selectedMarker = addMarker(marker, true);
            marker.remove();
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        changeSelectedMarker(null);
    }

}
