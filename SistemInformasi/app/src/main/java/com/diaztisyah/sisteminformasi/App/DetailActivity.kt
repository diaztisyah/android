package com.diaztisyah.sisteminformasi.App

import android.os.Build
import android.os.Bundle
import android.text.Layout.JUSTIFICATION_MODE_INTER_WORD
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marufaldi.sisteminformasi.Data.MyData
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_detail.*

class DetailActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        setSupportActionBar(toolbar)

        tv_detail_description.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD)

        val myData = intent.getParcelableExtra(EXTRA_MYDATA) as MyData
        supportActionBar?.title = myData.name.toString()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tv_detail_description.text = myData.des.toString()
        Glide.with(this)
            .load(myData.photo.toString())
            .apply(RequestOptions().override(700, 700))
            .into(iv_detail_photo)


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_MYDATA = "extra_mydata"
    }
}

