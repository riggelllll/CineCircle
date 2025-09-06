package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecircle.core.domain.model.CastMember
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemPersonBinding
import com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image as placeholder_image

class MovieCastAdapter(
    private val onCastMemberClick: (CastMember) -> Unit = {}
) : RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {

    private var castMembers: List<CastMember> = emptyList()

    class CastViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castMember = castMembers[position]

        holder.itemView.setOnClickListener {
            onCastMemberClick(castMember)
        }

        with(holder.binding) {
            personName.text = castMember.name

            if (castMember.profilePath.isNotEmpty()) {
                imgPerson.load(IMAGE_URL_TEMPLATE.format(castMember.profilePath)) {
                    placeholder(placeholder_image)
                }
            } else {
                imgPerson.load(placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = castMembers.size

    fun setCastMembers(newCastMembers: List<CastMember>) {
        castMembers = newCastMembers
        notifyDataSetChanged()
    }
}