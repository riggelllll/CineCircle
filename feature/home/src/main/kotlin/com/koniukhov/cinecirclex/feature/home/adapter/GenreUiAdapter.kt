package com.koniukhov.cinecirclex.feature.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import coil3.request.transformations
import coil3.transform.RoundedCornersTransformation
import com.koniukhov.cinecirclex.core.design.R
import com.koniukhov.cinecirclex.feature.home.databinding.ItemHomeGenreUiBinding
import com.koniukhov.cinecirclex.core.common.Constants
import com.koniukhov.cinecirclex.core.common.model.GenreUi
import com.koniukhov.cinecirclex.core.ui.utils.GenreUiDiffCallback

class GenreUiAdapter(private val onItemClick: (Int, String) -> Unit)
    : RecyclerView.Adapter<GenreUiAdapter.GenreUiViewHolder>(){
    private var genres: List<GenreUi> = emptyList()

    class GenreUiViewHolder(val binding: ItemHomeGenreUiBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreUiViewHolder {
        val binding = ItemHomeGenreUiBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return GenreUiViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreUiViewHolder, position: Int) {
        val genre = genres[position]
        holder.itemView.setOnClickListener {
            onItemClick(genre.id, genre.name)
        }
        holder.binding.genre.text = genre.name
        holder.binding.poster.load(genre.imageResId){
            placeholder(R.drawable.placeholder_image)
            transformations(RoundedCornersTransformation(Constants.IMAGE_RADIUS))
        }
    }

    override fun getItemCount(): Int {
        return genres.size
    }

    fun setData(newData: List<GenreUi>) {
        val diffCallback = GenreUiDiffCallback(genres, newData)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        genres = newData
        diffResult.dispatchUpdatesTo(this)
    }
}