package com.cbtool.silvermp3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.model.LibraryItem
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.ItemAddPlaylistBinding
import com.cbtool.silvermp3.interfaces.OnClickAddPlaylist
import com.cbtool.silvermp3.interfaces.OnClickPlaylist

class AddPlaylistAdapter(
    private val libItems: List<LibraryItem>,
    private val songInPlaylists: List<String>,
    private val onItemClick: OnClickAddPlaylist
) : RecyclerView.Adapter<AddPlaylistAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {

        return ViewHolder(
            ItemAddPlaylistBinding.inflate(
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
        holder.bindData(libItems[position])
    }

    override fun getItemCount(): Int = libItems.size


    inner class ViewHolder(private val binding: ItemAddPlaylistBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: LibraryItem) {
            when (item) {
                is LibraryItem.PlaylistItem -> bindPlaylistItem(item.playlist)
                is LibraryItem.FavouriteItem -> bindFavouriteItem(item.count)
            }
        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindPlaylistItem(playlist: Playlist) {
            if (playlist.coverUrl.isEmpty())
                binding.imageCover.background = itemView.context.getDrawable(R.drawable.bg_playlist)
            binding.addBtn.isSelected = songInPlaylists.contains(playlist.id)
            binding.tvName.text = playlist.title
            fun click() {
                binding.addBtn.isSelected = !binding.addBtn.isSelected
                onItemClick.playlist(playlist, binding.addBtn.isSelected)
            }
            binding.addBtn.setOnClickListener { click() }
            itemView.setOnClickListener { click() }

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindFavouriteItem(count: Int) {
            binding.imageCover.background = itemView.context.getDrawable(R.drawable.bg_favourite)
            binding.tvName.text = "Bài hát đã thích"
            binding.addBtn.isSelected = songInPlaylists.contains("favourites")
            fun click() {
                binding.addBtn.isSelected = !binding.addBtn.isSelected
                onItemClick.favourite(binding.addBtn.isSelected)
            }
            binding.addBtn.setOnClickListener { click() }
            itemView.setOnClickListener { click() }
        }

    }
}

