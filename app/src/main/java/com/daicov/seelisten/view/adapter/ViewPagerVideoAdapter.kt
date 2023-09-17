package com.daicov.seelisten.view.adapter

import android.animation.ObjectAnimator
import android.media.MediaPlayer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.daicov.seelisten.R


@Suppress("UNREACHABLE_CODE")
class ViewPagerVideoAdapter (var alfabetTv : List<String>, var alfabetVideo : List<String>) :
    RecyclerView.Adapter<ViewPagerVideoAdapter.PageViewHolder>() {

    class PageViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val alfabetTv : TextView = itemView.findViewById(R.id.alfabetTv)
        val videoAlfabet : VideoView = itemView.findViewById(R.id.videoAlfabet)
        val timerVideo : ProgressBar = itemView.findViewById(R.id.timerVideo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        return PageViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.vp_belajarvideoalfabet_item, parent, false))
    }

    override fun getItemCount(): Int {
        return alfabetVideo.size
        return alfabetTv.size
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.alfabetTv.text = alfabetTv[position]
        holder.videoAlfabet.setVideoPath(alfabetVideo[position])
        holder.videoAlfabet.setOnPreparedListener {
            it.start()
            it.isLooping = true
            it.setVolume(0f, 0f)
            holder.timerVideo.progress = 0
            setProgressMax(holder.timerVideo)
            setProgressAnimate(holder.timerVideo)
        }
    }

    private fun setProgressMax(pb: ProgressBar) {
        pb.max = 5000
    }

    private fun setProgressAnimate(pb: ProgressBar) {
        val animation = ObjectAnimator.ofInt(pb, "progress", pb.progress, 5000)
        animation.duration = 5000
        animation.setAutoCancel(true)
        animation.interpolator = DecelerateInterpolator()
        animation.start()
        animation.repeatCount = Animation.INFINITE
    }
}