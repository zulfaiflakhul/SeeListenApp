package com.daicov.seelisten.view.adapter

import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.daicov.seelisten.R

@Suppress("UNREACHABLE_CODE")
class ViewPagerImageAdapter (var alfabetTv : List<String>, var alfabetImg : List<Int>) :
    RecyclerView.Adapter<ViewPagerImageAdapter.PageViewHolder>() {

    class PageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val alfabetTv: TextView = itemView.findViewById(R.id.alfabetImgTv)
        val imageAlfabet : ImageView = itemView.findViewById(R.id.imageAlfabet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vp_belajarimagealfabet_item, parent, false))
    }

    override fun getItemCount(): Int {
        return alfabetTv.size
        return alfabetImg.size
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.alfabetTv.text = alfabetTv[position]
        holder.imageAlfabet.setImageResource(alfabetImg[position])
    }


}