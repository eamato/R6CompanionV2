package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import eamato.funn.r6companion.core.utils.SelectableObject
import eamato.funn.r6companion.databinding.SettingsItemPopupItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemScreenItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemSwitchItemRowBinding
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_POPUP
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SCREEN
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SWITCH

class AdapterSettingsItems : ABaseAdapter<SelectableObject<SettingsItem>>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<SelectableObject<SettingsItem>>() {
            override fun areItemsTheSame(
                oldItem: SelectableObject<SettingsItem>,
                newItem: SelectableObject<SettingsItem>
            ): Boolean {
                return true
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
                    return oldItem == newItem
                }

                if (oldItem.data is SettingsItem.SettingsItemSwitch &&
                    newItem.data is SettingsItem.SettingsItemSwitch) {
                    return oldItem == newItem
                }

                if (oldItem.data is SettingsItem.SettingsItemScreen &&
                    newItem.data is SettingsItem.SettingsItemScreen) {
                    return oldItem == newItem
                }

                return false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            VIEW_TYPE_POPUP -> ViewHolder.SettingsItemPopupViewHolder(
                SettingsItemPopupItemRowBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_SWITCH -> ViewHolder.SettingsItemSwitchViewHolder(
                SettingsItemSwitchItemRowBinding.inflate(inflater, parent, false)
            )

            VIEW_TYPE_SCREEN -> ViewHolder.SettingsItemScreenViewHolder(
                SettingsItemScreenItemRowBinding.inflate(inflater, parent, false)
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
            private val binding: SettingsItemPopupItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemPopup) {
                    return
                }

                itemView.isSelected = item.isSelected
                binding.settingsPopupItem.run {
                    setIsEnabled(item.data.isEnabled)
                    setTitle(item.data.title)
                    setSubtitle(item.data.subTitle)
                    setIcon(item.data.icon)
                }
            }
        }

        class SettingsItemSwitchViewHolder(
            private val binding: SettingsItemSwitchItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemSwitch) {
                    return
                }

                binding.settingsSwitchItem.run {
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
            private val binding: SettingsItemScreenItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun bind(item: SelectableObject<SettingsItem>) {
                if (item.data !is SettingsItem.SettingsItemScreen) {
                    return
                }

                itemView.isSelected = item.isSelected
                binding.settingsScreenItem.run {
                    setIsEnabled(item.data.isEnabled)
                    setTitle(item.data.title)
                    setSubtitle(item.data.subTitle)
                    setIcon(item.data.icon)
                }
            }
        }
    }
}