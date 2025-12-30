package com.cbtool.silvermp3.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import com.cbtool.silvermp3.data.model.Song
import com.cbtool.silvermp3.databinding.ItemSongBinding // Ví dụ binding của item
import com.cbtool.silvermp3.utils.glideCustom

class SongAdapter(
    private val onItemClick: (Song) -> Unit, private val moreClick: (Song) -> Unit
) : ListAdapter<Song, SongAdapter.ViewHolder>(SongDiffCallback()) {
    private lateinit var context: Context

    // DiffUtil giúp so sánh danh sách cũ và mới để chỉ update những item thay đổi
    class SongDiffCallback : DiffUtil.ItemCallback<Song>() {
        override fun areItemsTheSame(oldItem: Song, newItem: Song): Boolean {
            // So sánh ID (hoặc khóa chính) để biết có phải cùng 1 bài hát không
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Song, newItem: Song): Boolean {
            // So sánh nội dung (tên, ca sĩ...) để biết có cần vẽ lại UI không
            // Data class trong Kotlin tự động generate hàm equals() nên có thể dùng ==
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        context = parent.context
        return ViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(
        holder: ViewHolder, position: Int
    ) {
        val song = getItem(position) // ListAdapter có sẵn hàm getItem
        holder.bind(song)
//        holder.bind(songs[position])
        holder.itemView.setOnClickListener {
            onItemClick(song)
        }
    }


    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            song.apply {
                binding.tvTitle.text = title
                binding.tvArtist.text = artistName
                glideCustom(context, binding.imageCover, coverUrl, 10)
                binding.moreBtn.setOnClickListener {
                    moreClick(song)
                }
            }

        }

    }
}