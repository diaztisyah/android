package com.diaztisyah.sisteminformasi.Data

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.marufaldi.sisteminformasi.App.DetailActivity
import com.marufaldi.sisteminformasi.R

class SaintekAdapter(val listMyDatas: ArrayList<MyData>, val context: Context) :
    RecyclerView.Adapter<SaintekAdapter.GridViewHolder>() {
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): GridViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(
            R.layout.item_saintek, viewGroup,
            false)
        return GridViewHolder(view)
    }
    override fun onBindViewHolder(holder: GridViewHolder, position: Int) {
        val myData = listMyDatas[position]
        Glide.with(holder.itemView.context)
            .load(myData.photo)
            .apply(RequestOptions().override(350, 550))
            .into(holder.imgPhoto)
        holder.tvName.text = myData.name
        holder.itemView.setOnClickListener {
                val moveWithObjectIntent = Intent(context, DetailActivity::class.java)
                moveWithObjectIntent.putExtra(DetailActivity.EXTRA_MYDATA, myData)
                context.startActivity(moveWithObjectIntent)
            }
    }
    override fun getItemCount(): Int {
        return listMyDatas.size
    }
    inner class GridViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var imgPhoto: ImageView = itemView.findViewById(R.id.img_item_photo)
        var tvName: TextView = itemView.findViewById(R.id.tv_item_name)
    }
}