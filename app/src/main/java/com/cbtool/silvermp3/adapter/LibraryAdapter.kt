package com.cbtool.silvermp3.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cbtool.silvermp3.R
import com.cbtool.silvermp3.data.model.LibraryItem
import com.cbtool.silvermp3.data.model.Playlist
import com.cbtool.silvermp3.databinding.ItemPlaylistLibraryBinding
import com.cbtool.silvermp3.interfaces.OnClickPlaylist

class LibraryAdapter(
    private val onItemClick: OnClickPlaylist
) : ListAdapter<LibraryItem, LibraryAdapter.ViewHolder>(LibraryDiffCallback()) {
    private lateinit var context: Context

    class LibraryDiffCallback : DiffUtil.ItemCallback<LibraryItem>() {
        override fun areItemsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            return when {
                // Trường hợp 1: Cả 2 đều là Playlist -> so sánh ID của playlist
                oldItem is LibraryItem.PlaylistItem && newItem is LibraryItem.PlaylistItem -> {
                    oldItem.playlist.id == newItem.playlist.id
                }

                // Trường hợp 2: Cả 2 đều là FavouriteItem -> coi là giống nhau (vì chỉ có 1 item Favourite duy nhất)
                oldItem is LibraryItem.FavouriteItem && newItem is LibraryItem.FavouriteItem -> {
                    oldItem.count == newItem.count
                    true
                }

                // Các trường hợp khác (khác loại) -> chắc chắn khác nhau
                else -> false
            }
//            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: LibraryItem, newItem: LibraryItem): Boolean {
            // So sánh nội dung (tên, ca sĩ...) để biết có cần vẽ lại UI không
            // Data class trong Kotlin tự động generate hàm equals() nên có thể dùng ==
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemPlaylistLibraryBinding.inflate(
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
        holder.bindData(getItem(position))
    }


    inner class ViewHolder(private val binding: ItemPlaylistLibraryBinding) :
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
                binding.imageCover.background = context.getDrawable(R.drawable.bg_playlist)

            binding.tvTitle.text = playlist.title
            itemView.setOnClickListener {
                onItemClick.onClickPlaylist(playlist)
            }

        }

        @SuppressLint("UseCompatLoadingForDrawables")
        fun bindFavouriteItem(count: Int) {
            binding.imageCover.background = context.getDrawable(R.drawable.bg_favourite)
            binding.tv.text = "Danh sách phát - $count bài hát"
            itemView.setOnClickListener {
                onItemClick.onClickFavourite()
            }

        }

    }

}