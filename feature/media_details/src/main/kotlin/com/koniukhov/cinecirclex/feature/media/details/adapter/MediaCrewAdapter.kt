package com.koniukhov.cinecirclex.feature.media.details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil3.load
import coil3.request.placeholder
import com.koniukhov.cinecirclex.core.domain.model.CrewMember
import com.koniukhov.cinecirclex.core.network.api.TMDBEndpoints.ImageSizes.PROFILE_MEDIUM
import com.koniukhov.cinecirclex.core.ui.utils.CrewMemberDiffCallback
import com.koniukhov.cinecirclex.feature.movie_details.databinding.ItemPersonBinding
import com.koniukhov.cinecirclex.core.design.R.drawable.placeholder_image as placeholder_image

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
                imgPerson.load(PROFILE_MEDIUM.format(crewMember.profilePath)) {
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