package com.uchuang.safeguard.map

import com.baidu.mapapi.map.MapStatus
import com.baidu.mapapi.search.poi.*

class PoiSearchUtil {

    companion object {
        var poiSearch: PoiSearch = PoiSearch.newInstance()
        fun destroy(){
            poiSearch.destroy()
        }
        fun collect(mapStatus: MapStatus, baiduMapUtil: BaiduMapUtil){
            poiSearch.setOnGetPoiSearchResultListener(object:OnGetPoiSearchResultListener{
                override fun onGetPoiIndoorResult(p0: PoiIndoorResult?) {


                }

                override fun onGetPoiResult(p0: PoiResult?) {
                    var ms: MutableList<Marker> = ArrayList()
                    for (p in p0?.allPoi!!){
                        var m = Marker()
                        m.latLng = p.location
                        ms.add(m)
                    }
                    baiduMapUtil.drawMarkers(ms)
                }

                override fun onGetPoiDetailResult(p0: PoiDetailResult?) {

                }

                override fun onGetPoiDetailResult(p0: PoiDetailSearchResult?) {

                }

            })
            poiSearch.searchNearby(
                PoiNearbySearchOption()
                    .keyword("餐厅")
                    .sortType(PoiSortType.distance_from_near_to_far)
                    .location(mapStatus.target)
                    .radius(500)
                    .pageNum(10)
            )

        }
    }


}