package com.koniukhov.cinecircle.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecircle.core.domain.model.CrewMember
import com.koniukhov.cinecircle.core.network.api.TMDBEndpoints.IMAGE_URL_TEMPLATE
import com.koniukhov.cinecircle.core.ui.utils.CrewMemberDiffCallback
import com.koniukhov.cinecircle.feature.movie_details.databinding.ItemPersonBinding
import com.koniukhov.cinecircle.core.design.R.drawable.placeholder_image as placeholder_image

class MediaCrewAdapter(
    private val onCrewMemberClick: (CrewMember) -> Unit = {}
) : RecyclerView.Adapter<MediaCrewAdapter.CrewViewHolder>() {

    private var crewMembers: List<CrewMember> = emptyList()

    class CrewViewHolder(val binding: ItemPersonBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CrewViewHolder {
        val binding = ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CrewViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CrewViewHolder, position: Int) {
        val crewMember = crewMembers[position]

        holder.itemView.setOnClickListener {
            onCrewMemberClick(crewMember)
        }

        with(holder.binding) {
            personName.text = crewMember.name
            personRole.text = crewMember.job

            if (crewMember.profilePath.isNotEmpty()) {
                imgPerson.load(IMAGE_URL_TEMPLATE.format(crewMember.profilePath)) {
                    placeholder(placeholder_image)
                }
            } else {
                imgPerson.load(placeholder_image)
            }
        }
    }

    override fun getItemCount(): Int = crewMembers.size

    fun setCrewMembers(newCrewMembers: List<CrewMember>) {
        val diffCallback = CrewMemberDiffCallback(crewMembers, newCrewMembers)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        crewMembers = newCrewMembers
        diffResult.dispatchUpdatesTo(this)
    }
}