package com.uchuang.safeguard.map

import android.content.Context
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.BDLocation
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption
import com.baidu.mapapi.SDKInitializer
import com.baidu.mapapi.map.*
import com.baidu.mapapi.model.LatLng
import com.baidu.mapapi.utils.CoordinateConverter
import com.baidu.mapapi.map.MarkerOptions
import com.baidu.mapapi.map.BitmapDescriptorFactory


class BaiduMapUtil(val bmapView: MapView, val applicationContext: Context) : BaiduMap.OnMapStatusChangeListener {
    override fun onMapStatusChangeStart(mapStatus: MapStatus) {
    }

    override fun onMapStatusChangeStart(p0: MapStatus?, p1: Int) {
    }

    override fun onMapStatusChange(mapStatus: MapStatus) {
    }

    override fun onMapStatusChangeFinish(mapStatus: MapStatus) {
        println(""+mapStatus.target.longitude + ":" + mapStatus.target.latitude)
        PoiSearchUtil.collect(mapStatus,this)
    }

    //百度地图
    var baiduMap: BaiduMap? = null
    private var locationClient: LocationClient? = null

    companion object {
        var LOCATION: BDLocation? = null
        var already: MutableList<Overlay> = ArrayList()
    }
    /**
     * 初始化定位参数配置
     */
    fun initLocationOption() {
        baiduMap = bmapView.map
        baiduMap?.isMyLocationEnabled = true

        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        locationClient = LocationClient(applicationContext)
        //声明LocationClient类实例并配置定位参数
        val locationOption = LocationClientOption()
        val myLocationListener = BaiduMapListener()
        //注册监听函数
        locationClient?.registerLocationListener(myLocationListener)
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        locationOption.locationMode = LocationClientOption.LocationMode.Hight_Accuracy
        //可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
        locationOption.setCoorType(SDKInitializer.getCoordType().name)
        //可选，默认0，即仅定位一次，设置发起连续定位请求的间隔需要大于等于1000ms才是有效的
        locationOption.setScanSpan(1000)
        //可选，设置是否需要地址信息，默认不需要
        //locationOption.setIsNeedAddress(true)
        //可选，设置是否需要地址描述
        locationOption.setIsNeedLocationDescribe(true)
        //可选，设置是否需要设备方向结果
        locationOption.setNeedDeviceDirect(true)
        //可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        locationOption.isLocationNotify = true
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        locationOption.setIgnoreKillProcess(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        locationOption.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        locationOption.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否收集CRASH信息，默认收集
        locationOption.SetIgnoreCacheException(false)
        //可选，默认false，设置是否开启Gps定位
        locationOption.isOpenGps = true
        //可选，默认false，设置定位时是否需要海拔信息，默认不需要，除基础定位版本都可用
        locationOption.setIsNeedAltitude(false)
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者，该模式下开发者无需再关心定位间隔是多少，定位SDK本身发现位置变化就会及时回调给开发者
        locationOption.setOpenAutoNotifyMode()
        //设置打开自动回调位置模式，该开关打开后，期间只要定位SDK检测到位置变化就会主动回调给开发者
        locationOption.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT)

        //开始定位
        locationClient?.start()

        //地图状态改变相关接口
        bmapView.map.setOnMapStatusChangeListener(this)
    }

    inner class BaiduMapListener : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {

            //获取定位结果
//            location.getTime();    //获取定位时间
//            location.getLocationID();    //获取定位唯一ID，v7.2版本新增，用于排查定位问题
//            location.getLocType();    //获取定位类型
//            location.getRadius();    //获取定位精准度
//            location.getAddrStr();    //获取地址信息
//            location.getCountry();    //获取国家信息
//            location.getCountryCode(); //获取国家码
//            location.getCity();    //获取城市信息
//            location.getCityCode(); //获取城市码
//            location.getDistrict();//获取区县信息
//            location.getStreet();    //获取街道信息
//            location.getStreetNumber();    //获取街道码
//            location.getLocationDescribe();    //获取当前位置描述信息
//            location.getPoiList();    //获取当前位置周边POI信息
//
//            location.getBuildingID();    //室内精准定位下，获取楼宇ID
//            location.getBuildingName();    //室内精准定位下，获取楼宇名称
//            location.getFloor();    //室内精准定位下，获取当前位置所处的楼层信息

//            //经纬度
//            lat = location?.latitude;
//            lon = location?.longitude;
//            println(""+lon +","+lat)
//            val converter = CoordinateConverter()
//            converter.from(CoordinateConverter.CoordType.COMMON)
//            converter.coord(LatLng(lat!!.toDouble(),lon!!.toDouble()))
//            val desLatLng = converter.convert()
            LOCATION = location
            center()
        }
    }

    fun center() {
        val ll = LatLng(LOCATION!!.latitude, LOCATION!!.longitude)
        val converter = CoordinateConverter()
        converter.from(CoordinateConverter.CoordType.COMMON)
        converter.coord(ll)
        val desLatLng = converter.convert()

        val myLocationData = MyLocationData.Builder()
            .accuracy(0f)
            .direction(LOCATION!!.direction)
            .latitude(desLatLng.latitude)
            .longitude(desLatLng.longitude)
            .build()

        baiduMap?.setMyLocationData(myLocationData)
        val builder = MapStatus.Builder()
        builder.target(desLatLng).zoom(19.0f)
        baiduMap?.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()))
    }

    fun clearMarkers(){
        if(!already.isEmpty()){
            for (overlay in already){
                overlay.remove()
            }
            already.clear()
        }
    }

    /**
     * 绘制marker
     */
    fun drawMarkers(markers : List<Marker>){
        clearMarkers()
        for (marker in markers.listIterator()) {
            val bitmap = BitmapDescriptorFactory
                .fromResource(marker.icon!!)
            //构建MarkerOption，用于在地图上添加Marker
            val option = MarkerOptions()
                .position(marker.latLng)
                .icon(bitmap)
            //在地图上添加Marker，并显示
            already.add(baiduMap!!.addOverlay(option))
        }
    }

    fun drawMarker(marker:Marker){
        var ms: MutableList<Marker> = ArrayList()
        ms.add(marker)
        drawMarkers(ms)
    }
}