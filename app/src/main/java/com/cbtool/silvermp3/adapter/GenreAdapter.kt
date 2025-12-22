package com.cbtool.silvermp3.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.CustomTarget
import com.cbtool.silvermp3.data.model.Genre
import com.cbtool.silvermp3.databinding.ItemGenreBinding
import com.cbtool.silvermp3.utils.loadImagePalette

class GenreAdapter(
    private val genres: List<Genre>
): RecyclerView.Adapter<GenreAdapter.GenreViewHolder>() {
    private lateinit var context: Context
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenreViewHolder {
        context = parent.context
        return GenreViewHolder(ItemGenreBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(
        holder: GenreViewHolder,
        position: Int
    ) {
        holder.bind(genres[position])
    }

    override fun getItemCount(): Int = genres.size

    inner class GenreViewHolder(private val binding: ItemGenreBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(genre: Genre){
            binding.tvTitle.text = genre.name
            loadImagePalette(context, binding.imgCover, genre.imageURL){
                binding.cardView.setCardBackgroundColor(it)
            }

        }

    }
}