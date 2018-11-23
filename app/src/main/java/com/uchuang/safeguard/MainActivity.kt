package com.uchuang.safeguard

import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import com.uchuang.safeguard.map.BaiduMapUtil


class MainActivity : AppCompatActivity() {

    var baiduMapUtil: BaiduMapUtil? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        baiduMapUtil = BaiduMapUtil(bmapView,this)
        baiduMapUtil!!.initLocationOption()
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//            initMap();
//        } else {
//            PermissionsDispatcher.ApplySuccessWithCheck(this);
//        }

        fab.setOnClickListener { view ->
            baiduMapUtil?.center()
        }

        // Example of a call to a native method
        //sample_text.text = stringFromJNI()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    companion object {

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")

        }
    }
}
