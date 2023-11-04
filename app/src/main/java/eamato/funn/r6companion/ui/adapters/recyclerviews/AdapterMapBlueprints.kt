package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.databinding.MapBlueprintItemViewBinding
import eamato.funn.r6companion.domain.entities.companion.maps.MapDetails

class AdapterMapBlueprints : ABaseAdapter<MapDetails.MapDetailsBlueprint>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<MapDetails.MapDetailsBlueprint>() {
            override fun areItemsTheSame(
                oldItem: MapDetails.MapDetailsBlueprint,
                newItem: MapDetails.MapDetailsBlueprint
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MapDetails.MapDetailsBlueprint,
                newItem: MapDetails.MapDetailsBlueprint
            ): Boolean {
                return oldItem.name == newItem.name && oldItem.imageUrl == newItem.imageUrl
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ABaseViewHolder<MapDetails.MapDetailsBlueprint> {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(MapBlueprintItemViewBinding.inflate(inflater, parent, false))
    }

    class ViewHolder(
        private val binding: MapBlueprintItemViewBinding
    ) : ABaseViewHolder<MapDetails.MapDetailsBlueprint>(binding.root) {

        private val errorDrawable = R.drawable.no_image_drawable.getDrawable(itemView.context)

        override fun bind(item: MapDetails.MapDetailsBlueprint) {
            if (item.imageUrl == null) {
                binding.ivMapBlueprint.setImageDrawable(errorDrawable)
            } else {
                GlideApp.with(binding.ivMapBlueprint)
                    .asDrawable()
                    .load(item.imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .error(errorDrawable)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .into(binding.ivMapBlueprint)
            }

            binding.tvMapBlueprintName.text = item.name
        }
    }
}