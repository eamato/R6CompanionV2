package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_IMAGE_HEIGHT
import eamato.funn.r6companion.core.ROULETTE_OPERATOR_IMAGE_WIDTH
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.ImageResizeTransformation
import eamato.funn.r6companion.databinding.CompanionOperatorItemViewBinding
import eamato.funn.r6companion.domain.entities.companion.operators.Operator

class AdapterCompanionOperators :
    ListAdapter<Operator, AdapterCompanionOperators.ViewHolder>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<Operator>() {
            override fun areItemsTheSame(oldItem: Operator, newItem: Operator): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Operator, newItem: Operator): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(CompanionOperatorItemViewBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAtPosition(position: Int): Operator = getItem(position)

    class ViewHolder(private val binding: CompanionOperatorItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val errorDrawable = R.drawable.no_image_drawable.getDrawable(itemView.context)

        fun bind(operator: Operator) {
            GlideApp.with(binding.ivOperatorImage)
                .load(operator.imgLink)
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

            when (operator.role) {
                Operator.ROLE_ATTACKER -> {
                    binding.ivOperatorSideIcon.visibility = View.VISIBLE
                    binding.ivOperatorSideIcon.setImageResource(R.drawable.ic_attack_role)
                }
                Operator.ROLE_DEFENDER -> {
                    binding.ivOperatorSideIcon.visibility = View.VISIBLE
                    binding.ivOperatorSideIcon.setImageResource(R.drawable.ic_defend_role)
                }
                else -> binding.ivOperatorSideIcon.visibility = View.GONE
            }

            binding.tvOperatorName.text = operator.name
        }
    }
}