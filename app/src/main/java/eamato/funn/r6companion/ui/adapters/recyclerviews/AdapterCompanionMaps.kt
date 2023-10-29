package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.IDoAfterTerminateGlide
import eamato.funn.r6companion.databinding.CompanionMapItemViewBinding
import eamato.funn.r6companion.domain.entities.companion.maps.Map

class AdapterCompanionMaps : ABaseAdapter<Map>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<Map>() {
            override fun areItemsTheSame(oldItem: Map, newItem: Map): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Map, newItem: Map): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ABaseViewHolder<Map> {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CompanionMapItemViewBinding.inflate(inflater, parent, false))
    }

    class ViewHolder(
        private val binding: CompanionMapItemViewBinding
    ) : ABaseViewHolder<Map>(binding.root) {

        private val errorDrawable = R.drawable.no_image_drawable.getDrawable(itemView.context)

        override fun bind(item: Map) {
            binding.clpbNewsImage.hide()

            val imageUrl = item.imageUrl
            if (imageUrl == null) {
                binding.ivMapImage.setImageDrawable(errorDrawable)
            } else {
                binding.clpbNewsImage.show()

                GlideApp.with(binding.ivMapImage)
                    .load(item.imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .centerCrop()
                    .error(errorDrawable)
                    .listener(object : IDoAfterTerminateGlide {
                        override fun doAfterTerminate() {
                            binding.clpbNewsImage.hide()
                        }
                    })
                    .dontAnimate()
                    .into(binding.ivMapImage)
            }

            binding.tvMapName.text = item.name
        }
    }
}