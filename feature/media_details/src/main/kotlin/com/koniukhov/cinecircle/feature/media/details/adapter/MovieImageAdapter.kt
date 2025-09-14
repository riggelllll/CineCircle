package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecircle.core.domain.model.Image
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemMovieImageBinding
import com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image as placeholder_image

class MovieImageAdapter(
    private val onImageClick: (String) -> Unit
) : RecyclerView.Adapter<MovieImageAdapter.ImageViewHolder>() {

    private var images: List<Image> = emptyList()

    class ImageViewHolder(val binding: ItemMovieImageBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemMovieImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = images[position]

        holder.itemView.setOnClickListener {
            onImageClick(image.filePath)
        }

        if (image.filePath.isNotEmpty()){
            holder.binding.img.load(IMAGE_URL_TEMPLATE.format(image.filePath)) {
                placeholder(placeholder_image)
            }
        }else{
            holder.binding.img.load(placeholder_image)
        }
    }

    override fun getItemCount(): Int = images.size

    fun setImages(newImages: List<Image>) {
        images = newImages
        notifyDataSetChanged()
    }
}