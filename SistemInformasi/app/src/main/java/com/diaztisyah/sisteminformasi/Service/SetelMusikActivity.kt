package com.diaztisyah.sisteminformasi.Service

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_setel_musik.*

class SetelMusikActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setel_musik)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        goToMediaPlayerService(getString(R.string.musik))
    }

    fun goToMediaPlayerService(title:String){
        supportActionBar?.title = title
        val mFragmentManager = supportFragmentManager
        val myFragment = ServiceMediaPlayerFragment()
        val fragment =
            mFragmentManager.findFragmentByTag(ServiceMediaPlayerFragment::class.java.simpleName)
        if (fragment !is ServiceMediaPlayerFragment) {
            mFragmentManager
                .beginTransaction()
                .replace(R.id.frame_container, myFragment,
                    ServiceMediaPlayerFragment::class.java.simpleName)
                .commit()
        }
    }

    private val mOnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            val fragment: Fragment
            val bundle: Bundle
            when (item.itemId) {
                R.id.nav_media_player_service-> {
                    goToMediaPlayerService(getString(R.string.musik))
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
