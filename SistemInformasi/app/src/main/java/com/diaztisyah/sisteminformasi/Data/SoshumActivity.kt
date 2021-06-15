package com.diaztisyah.sisteminformasi.Data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager

import com.marufaldi.sisteminformasi.App.DashboardActivity
import com.marufaldi.sisteminformasi.R
import kotlinx.android.synthetic.main.activity_saintek.*

class SoshumActivity : AppCompatActivity() {

    private val list = ArrayList<MyData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_soshum)
        rv_mydata.setHasFixedSize(true)
        list.addAll(getListMyDatas())
        showRecyclerGrid()

        btn_kembali.setOnClickListener{
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

    }

    fun getListMyDatas(): ArrayList<MyData> {
        val dataName = resources.getStringArray(R.array.listsoshum)
        val dataPhoto = resources.getStringArray(R.array.soshum)
        val dataDes = resources.getStringArray(R.array.karakter2)
        val listMyData = ArrayList<MyData>()
        for (position in dataName.indices) {
            val myData = MyData(
                dataName[position],
                dataPhoto[position],
                dataDes[position]
            )
            listMyData.add(myData)
        }
        return listMyData
    }


    private fun showRecyclerGrid() {
        rv_mydata.layoutManager = GridLayoutManager(this, 2)
        val gridMyDataAdapter =
            SaintekAdapter(list, this)
        rv_mydata.adapter = gridMyDataAdapter
    }
}

