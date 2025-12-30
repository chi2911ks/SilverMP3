package com.cbtool.silvermp3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.ItemPlaylistBinding

class PlaylistAdapter(
    private val onItemClick: (Playlist) -> Unit
) :
    ListAdapter<Playlist, PlaylistAdapter.PlaylistViewHolder>(PlaylistDiffCallback()) {
    private lateinit var context: Context

    class PlaylistDiffCallback() : DiffUtil.ItemCallback<Playlist>() {
        override fun areItemsTheSame(
            oldItem: Playlist,
            newItem: Playlist
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Playlist,
            newItem: Playlist
        ): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistViewHolder {
        context = parent.context
        return PlaylistViewHolder(
            ItemPlaylistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: PlaylistViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onItemClick(getItem(position))
        }

    }

    inner class PlaylistViewHolder(private val binding: ItemPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.titlePlaylist.text = playlist.title
            Glide
                .with(context)
                .load(playlist.coverUrl)
                .transform(CenterCrop(), RoundedCorners(10))
                .into(binding.coverImage)
        }
    }
}