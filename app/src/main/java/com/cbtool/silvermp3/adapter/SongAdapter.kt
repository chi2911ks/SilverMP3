package com.cbtool.silvermp3.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.geometry.CornerRadius
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cbtool.silvermp3.data.model.Country
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.ItemSongBinding

class SongAdapter(
    private val songs: List<Song>,
    private val onItemClick: (Song) -> Unit,
    private val moreClick: (Song) -> Unit,
) :
    RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    private lateinit var context: Context

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            onItemClick(songs[position])
        }
    }

    override fun getItemCount(): Int = songs.size

    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            song.apply {
                binding.tvTitle.text = title
                binding.tvArtist.text = artistName
                Glide
                    .with(context)
                    .load(coverUrl)
                    .transform(CenterCrop(), RoundedCorners(10))
                    .into(binding.imageCover)
                binding.moreBtn.setOnClickListener {
                    moreClick(song)
                }
            }

        }

    }
}