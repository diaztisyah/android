package com.diaztisyah.sisteminformasi.App

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.marufaldi.sisteminformasi.Data.SaintekActivity
import com.marufaldi.sisteminformasi.Data.SoshumActivity
import com.marufaldi.sisteminformasi.R
import com.marufaldi.sisteminformasi.Service.SetelMusikActivity
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        btn_saintek.setOnClickListener {
            val intent = Intent(this, SaintekActivity::class.java)
            startActivity(intent)
        }

        btn_soshum.setOnClickListener {
            val intent = Intent(this, SoshumActivity::class.java)
            startActivity(intent)
        }

        btn_profil.setOnClickListener{
            val intent = Intent(this, AkunActivity::class.java)
            startActivity(intent)
        }
        setel_musik.setOnClickListener{
            val intent = Intent(this, SetelMusikActivity::class.java)
            startActivity(intent)
            Log.d("SERVICE", "CEK SINI")
        }
    }

}