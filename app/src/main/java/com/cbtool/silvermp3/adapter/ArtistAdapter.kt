package com.cbtool.silvermp3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cbtool.silvermp3.data.model.Artist
import com.cbtool.silvermp3.databinding.ItemArtistBinding
import com.cbtool.silvermp3.utils.glideCustom

class ArtistAdapter(
    private val onClickListener: (Artist) -> Unit
) :
    ListAdapter<Artist, ArtistAdapter.ArtistViewHolder>(
        ArtistDiffCallback()
    ) {


    class ArtistDiffCallback : DiffUtil.ItemCallback<Artist>() {
        override fun areItemsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean = oldItem.id == newItem.id


        override fun areContentsTheSame(
            oldItem: Artist,
            newItem: Artist
        ): Boolean = oldItem == newItem

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ArtistViewHolder {
        return ArtistViewHolder(
            ItemArtistBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: ArtistViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.itemView.setOnClickListener {
            onClickListener(getItem(position))
        }
    }

    inner class ArtistViewHolder(private val binding: ItemArtistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist) {
            binding.nameArtist.text = artist.name
            glideCustom(itemView.context, binding.imageArtist, artist.photoUrl)
        }
    }

}