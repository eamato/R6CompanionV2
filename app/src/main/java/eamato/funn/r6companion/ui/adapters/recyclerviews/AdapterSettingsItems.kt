package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.databinding.SettingsItemPopupItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemScreenItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemSwitchItemRowBinding
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_POPUP
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SCREEN
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SWITCH

class AdapterSettingsItems(private val onItemClicked: (SelectableObject<SettingsItem>) -> Unit) : ABaseAdapter<SelectableObject<SettingsItem>>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<SelectableObject<SettingsItem>>() {
            override fun areItemsTheSame(
                oldItem: SelectableObject<SettingsItem>,
                newItem: SelectableObject<SettingsItem>
            ): Boolean {
                return oldItem.data.id == newItem.data.id
            }

            override fun areContentsTheSame(
                oldItem: SelectableObject<SettingsItem>,
                newItem: SelectableObject<SettingsItem>
            ): Boolean {
                if (oldItem.isSelected != newItem.isSelected) {
                    return false
                }

                if (oldItem.data is SettingsItem.SettingsItemPopup &&
                    newItem.data is SettingsItem.SettingsItemPopup) {
                    return oldItem.data.isEnabled == newItem.data.isEnabled &&
                            oldItem.data.title == newItem.data.title &&
                            oldItem.data.subTitle == newItem.data.subTitle &&
                            oldItem.data.icon == newItem.data.icon
                }

                if (oldItem.data is SettingsItem.SettingsItemSwitch &&
                    newItem.data is SettingsItem.SettingsItemSwitch) {
                    return oldItem.data.isEnabled == newItem.data.isEnabled &&
                            oldItem.data.isChecked == newItem.data.isChecked &&
                            oldItem.data.title == newItem.data.title &&
                            oldItem.data.subTitle == newItem.data.subTitle &&
                            oldItem.data.icon == newItem.data.icon
                }

                if (oldItem.data is SettingsItem.SettingsItemScreen &&
                    newItem.data is SettingsItem.SettingsItemScreen) {
                    return oldItem.data.isEnabled == newItem.data.isEnabled &&
                            oldItem.data.title == newItem.data.title &&
                            oldItem.data.subTitle == newItem.data.subTitle &&
                            oldItem.data.icon == newItem.data.icon
                }

                return false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_POPUP -> ViewHolder.SettingsItemPopupViewHolder(
                SettingsItemPopupItemRowBinding.inflate(inflater, parent, false),
                onItemClicked
            )

            VIEW_TYPE_SWITCH -> ViewHolder.SettingsItemSwitchViewHolder(
                SettingsItemSwitchItemRowBinding.inflate(inflater, parent, false),
                onItemClicked
            )

            VIEW_TYPE_SCREEN -> ViewHolder.SettingsItemScreenViewHolder(
                SettingsItemScreenItemRowBinding.inflate(inflater, parent, false),
                onItemClicked
            )

            else -> throw Exception("Unknown viewType found ${this.javaClass.name}")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).data.getItemViewType()
    }

    sealed class ViewHolder(
        itemView: View
    ) : ABaseViewHolder<SelectableObject<SettingsItem>>(itemView) {

        class SettingsItemPopupViewHolder(
            private val binding: SettingsItemPopupItemRowBinding,
            private val onItemClicked: (SelectableObject<SettingsItem>) -> Unit
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemPopup) {
                    return
                }

                itemView.isSelected = item.isSelected
                binding.settingsPopupItem.run {
                    setOnClickListener { onItemClicked(item) }
                    setIsEnabled(item.data.isEnabled)
                    setTitle(item.data.title)
                    setSubtitle(item.data.subTitle)
                    setIcon(item.data.icon)
                }
            }
        }

        class SettingsItemSwitchViewHolder(
            private val binding: SettingsItemSwitchItemRowBinding,
            private val onItemClicked: (SelectableObject<SettingsItem>) -> Unit
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemSwitch) {
                    return
                }

                binding.settingsSwitchItem.run {
                    setOnClickListener { onItemClicked(item) }
                    setIsEnabled(item.data.isEnabled)
                    setIsChecked(item.data.isChecked)
                    setTitle(item.data.title)
                    setSubtitle(item.data.subTitle)
                    setIcon(item.data.icon)
                    setOnCheckedChangeListener { _, isChecked ->
                        item.data.onCheckedListener(isChecked)
                    }
                }
            }
        }

        class SettingsItemScreenViewHolder(
            private val binding: SettingsItemScreenItemRowBinding,
            private val onItemClicked: (SelectableObject<SettingsItem>) -> Unit
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemScreen) {
                    return
                }

                itemView.isSelected = item.isSelected
                binding.settingsScreenItem.run {
                    setOnClickListener { onItemClicked(item) }
                    setIsEnabled(item.data.isEnabled)
                    setTitle(item.data.title)
                    setSubtitle(item.data.subTitle)
                    setIcon(item.data.icon)
                }
            }
        }
    }
}