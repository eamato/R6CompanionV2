package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.databinding.SettingsItemPopupContentItemRowBinding
import eamato.funn.r6companion.ui.entities.PopupContentItem

class AdapterPopupContent :
    ListAdapter<PopupContentItem, AdapterPopupContent.ViewHolder>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<PopupContentItem>() {
            override fun areItemsTheSame(
                oldItem: PopupContentItem,
                newItem: PopupContentItem
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: PopupContentItem,
                newItem: PopupContentItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            SettingsItemPopupContentItemRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun getItemAtPosition(position: Int): PopupContentItem = getItem(position)

    class ViewHolder(private val binding: SettingsItemPopupContentItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PopupContentItem) {
            item.icon
                ?.run { binding.ivIcon.setImageResource(this) }
                ?: kotlin.run { binding.ivIcon.setImageDrawable(null) }

            binding.tvTitle.text = item.title.asString(itemView.context)

            item.subTitle
                ?.asString(itemView.context)
                ?.run {
                    binding.tvSubTitle.text = this
                    binding.tvSubTitle.visibility = View.VISIBLE
                }
                ?: kotlin.run {
                    binding.tvSubTitle.text = ""
                    binding.tvSubTitle.visibility = View.GONE
                }
        }
    }
}