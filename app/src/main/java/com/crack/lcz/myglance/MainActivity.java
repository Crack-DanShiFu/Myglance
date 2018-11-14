package com.crack.lcz.myglance;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;



public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位
    private RelativeLayout relativeLayout;
    private TextView miniFab02Text;
    private TextView miniFabHertText;


    private CheckBox cbDelay;
    private FloatingActionButton fab01Add;
    private boolean isAdd = false;
    private boolean islukuang = false;
    private boolean isHert = false;



    private RelativeLayout rlAddBill;
    private int[] llId = new int[]{R.id.ll01,R.id.ll02,R.id.ll03,R.id.ll04,R.id.ll05,R.id.ll06};
    private LinearLayout[] ll = new LinearLayout[llId.length];
    private int[] fabId = new int[]{R.id.miniFab01,R.id.miniFab02,R.id.miniFab03,R.id.miniFab04,R.id.miniFab05,R.id.miniFab06};
    private FloatingActionButton[] fab = new FloatingActionButton[fabId.length];
    private AnimatorSet addBillTranslate1;
    private AnimatorSet addBillTranslate2;
    private AnimatorSet addBillTranslate3;
    private AnimatorSet addBillTranslate4;
    private AnimatorSet addBillTranslate5;
    private AnimatorSet addBillTranslate6;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        initView();
        initMap();
        setDefaultValues();
        bindEvents();



    }


    private void initMap() {
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        //默认显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        //开启交通图
        //mBaiduMap.setTrafficEnabled(true);
        //开启热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();



    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            //mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MainActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MainActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MainActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MainActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.baiduMapView);
        relativeLayout =(RelativeLayout)findViewById(R.id.rlAddBill);
        relativeLayout.setOnClickListener(new RelListen());
        miniFab02Text =(TextView)findViewById(R.id.miniFab02Text);
        miniFabHertText =(TextView)findViewById(R.id.miniFabHertText);


        // 隐藏logo
        View child = mMapView.getChildAt(1);
        if (child != null && (child instanceof ImageView || child instanceof ZoomControls)){
            child.setVisibility(View.INVISIBLE);
        }

        cbDelay  = (CheckBox)findViewById(R.id.cbDelay);
        fab01Add = (FloatingActionButton)findViewById(R.id.fab01Add);
        rlAddBill = (RelativeLayout)findViewById(R.id.rlAddBill);
        for (int i = 0; i < llId.length;i++){
            ll[i] = (LinearLayout)findViewById(llId[i]);
        }
        for (int i = 0;i < fabId.length; i++){
            fab[i] = (FloatingActionButton)findViewById(fabId[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab01Add:
                //fab01Add.setImageResource(isAdd ? mip:R.mipmap.ic_launcher_round);
                isAdd = !isAdd;
                rlAddBill.setVisibility(isAdd ? View.VISIBLE : View.GONE);
                if (isAdd) {
                    addBillTranslate1.setTarget(ll[0]);
                    addBillTranslate1.start();
                    addBillTranslate2.setTarget(ll[1]);
                    addBillTranslate2.setStartDelay(cbDelay.isChecked() ? 150 : 0);
                    addBillTranslate2.start();
                    addBillTranslate3.setTarget(ll[2]);
                    addBillTranslate3.setStartDelay(cbDelay.isChecked() ? 200 : 0);
                    addBillTranslate3.start();
                    addBillTranslate4.setTarget(ll[3]);
                    addBillTranslate4.setStartDelay(cbDelay.isChecked() ? 250 : 0);
                    addBillTranslate4.start();
                    addBillTranslate5.setTarget(ll[4]);
                    addBillTranslate5.setStartDelay(cbDelay.isChecked() ? 300 : 0);
                    addBillTranslate5.start();
                    addBillTranslate6.setTarget(ll[5]);
                    addBillTranslate6.setStartDelay(cbDelay.isChecked() ? 350 : 0);
                    addBillTranslate6.start();
                }
                break;
            case R.id.miniFab01:
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                hideFABMenu();
                break;
            case R.id.miniFab02:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                hideFABMenu();
                break;
            case R.id.miniFab03:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                hideFABMenu();
                break;
            case R.id.miniFab04:
                if(!islukuang) {
                    mBaiduMap.setTrafficEnabled(true);
                    mBaiduMap.setCustomTrafficColor("#ffba0101", "#fff33131", "#ffff9e19", "#00000000");


                    miniFab02Text.setText("关闭路况");
                }else {
                    mBaiduMap.setTrafficEnabled(false);
                    miniFab02Text.setText("显示路况");

                }
                islukuang=!islukuang;
                hideFABMenu();
                break;
            case R.id.miniFab05:
                if(!isHert) {
                    mBaiduMap.setBaiduHeatMapEnabled(true);

                    miniFabHertText.setText("关闭热力图");
                }else {
                    mBaiduMap.setBaiduHeatMapEnabled(false);

                    miniFabHertText.setText("城市热力图");
                }
                isHert=!isHert;

                hideFABMenu();
                break;
            case R.id.miniFab06:
                hideFABMenu();
                break;
            default:
                break;
        }

    }


    private void setDefaultValues(){
        addBillTranslate1 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate2 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate3 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate4 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate5 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
        addBillTranslate6 = (AnimatorSet) AnimatorInflater.loadAnimator(this,R.animator.add_bill_anim);
    }
    private void bindEvents(){
        fab01Add.setOnClickListener(this);
        for (int i = 0;i < fabId.length; i++){
            fab[i].setOnClickListener(this);
        }
    }
    private void hideFABMenu(){
        rlAddBill.setVisibility(View.GONE);
        isAdd = false;
    }
    public class RelListen implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            hideFABMenu();
        }
    }








}

