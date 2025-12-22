package com.cbtool.silvermp3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.model.LibraryItem
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.ItemPlaylistLibraryBinding
import com.cbtool.silvermp3.interfaces.OnClickPlaylist

class LibraryAdapter(
    private val libItems: List<LibraryItem>,
    private val onItemClick: OnClickPlaylist
): RecyclerView.Adapter<LibraryAdapter.ViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(ItemPlaylistLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bindData(libItems[position])
    }

    override fun getItemCount(): Int = libItems.size


    inner class ViewHolder(private val binding: ItemPlaylistLibraryBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(item: LibraryItem){
            when (item){
                is LibraryItem.PlaylistItem -> bindPlaylistItem(item.playlist)
                is LibraryItem.FavouriteItem -> bindFavouriteItem(item.count)
            }
        }
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindPlaylistItem(playlist: Playlist){
            if (playlist.coverUrl.isEmpty())
                binding.imageCover.background = context.getDrawable(R.drawable.bg_playlist)

            binding.tvTitle.text = playlist.title
            itemView.setOnClickListener {
                onItemClick.onClickPlaylist(playlist)
            }

        }
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindFavouriteItem(count: Int){
            binding.imageCover.background = context.getDrawable(R.drawable.bg_favourite)
            binding.tv.text = "Danh sách phát - $count bài hát"
            itemView.setOnClickListener {
                onItemClick.onClickFavourite()
            }

        }

    }

}