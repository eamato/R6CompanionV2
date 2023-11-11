package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.OUR_TEAM_IMAGE_HEIGHT
import eamato.funn.r6companion.core.OUR_TEAM_IMAGE_WIDTH
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.ImageResizeTransformation
import eamato.funn.r6companion.databinding.OurTeamRowBinding
import eamato.funn.r6companion.domain.entities.settings.SettingsAboutInfo

class AdapterOurTeam : ABaseAdapter<SettingsAboutInfo.OurTeam.TeamMember>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK =
            object : DiffUtil.ItemCallback<SettingsAboutInfo.OurTeam.TeamMember>() {
                override fun areItemsTheSame(
                    oldItem: SettingsAboutInfo.OurTeam.TeamMember,
                    newItem: SettingsAboutInfo.OurTeam.TeamMember
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: SettingsAboutInfo.OurTeam.TeamMember,
                    newItem: SettingsAboutInfo.OurTeam.TeamMember
                ): Boolean {
                    return oldItem == newItem
                }
            }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(OurTeamRowBinding.inflate(inflater, parent, false))
    }

    class ViewHolder(private val binding: OurTeamRowBinding) : ABaseViewHolder<SettingsAboutInfo.OurTeam.TeamMember>(binding.root) {

        override fun bind(item: SettingsAboutInfo.OurTeam.TeamMember) {
            GlideApp.with(binding.ivImage)
                .load(item.imageUrl)
                .transform(ImageResizeTransformation(OUR_TEAM_IMAGE_WIDTH, OUR_TEAM_IMAGE_HEIGHT))
                .fallback(R.drawable.img_no_image_placeholder)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .circleCrop()
                .into(binding.ivImage)

            binding.tvFirstLastName.text = itemView.context.getString(
                R.string.first_name_last_name_pattern,
                item.firstName.replaceFirstChar { it.uppercaseChar() },
                item.lastName.replaceFirstChar { it.uppercaseChar() }
            )
            binding.tvPositions.text = item.positions.joinToString()
        }
    }
}