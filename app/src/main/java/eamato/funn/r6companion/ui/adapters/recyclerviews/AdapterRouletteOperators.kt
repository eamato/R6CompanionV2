package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_ICON_HEIGHT
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_ICON_WIDTH
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_IMAGE_HEIGHT
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_IMAGE_WIDTH
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.ImageResizeTransformation
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.databinding.RouletteOperatorItemViewBinding
import eamato.funn.r6companion.domain.entities.roulette.Operator

class AdapterRouletteOperators :
    ListAdapter<SelectableObject<Operator>, AdapterRouletteOperators.ViewHolder>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<SelectableObject<Operator>>() {
            override fun areItemsTheSame(
                oldItem: SelectableObject<Operator>,
                newItem: SelectableObject<Operator>
            ): Boolean {
                return oldItem.data.id == newItem.data.id
            }

            override fun areContentsTheSame(
                oldItem: SelectableObject<Operator>,
                newItem: SelectableObject<Operator>
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(RouletteOperatorItemViewBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAtPosition(position: Int): SelectableObject<Operator> = getItem(position)

    class ViewHolder(private val binding: RouletteOperatorItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val errorDrawable = R.drawable.no_image_drawable.getDrawable(itemView.context)

        fun bind(operator: SelectableObject<Operator>) {
            GlideApp.with(binding.ivOperatorImage)
                .load(operator.data.imgLink)
                .override(ROULETTE_OPERATOR_IMAGE_WIDTH, ROULETTE_OPERATOR_IMAGE_HEIGHT)
                .transform(
                    ImageResizeTransformation(
                        ROULETTE_OPERATOR_IMAGE_WIDTH, ROULETTE_OPERATOR_IMAGE_HEIGHT
                    )
                )
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.transparent_300)
                .error(errorDrawable)
                .transition(DrawableTransitionOptions.withCrossFade(500))
                .dontAnimate()
                .into(binding.ivOperatorImage)

            GlideApp.with(binding.ivOperatorIcon)
                .load(operator.data.iconLink)
                .override(ROULETTE_OPERATOR_ICON_WIDTH, ROULETTE_OPERATOR_ICON_HEIGHT)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .placeholder(R.drawable.transparent_75)
                .dontAnimate()
                .into(binding.ivOperatorIcon)

            binding.tvOperatorName.text = operator.data.name

            binding.ivCheck.isVisible = operator.isSelected
        }
    }
}