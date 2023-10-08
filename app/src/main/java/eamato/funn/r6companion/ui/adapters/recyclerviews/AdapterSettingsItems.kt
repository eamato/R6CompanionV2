package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import eamato.funn.r6companion.databinding.SettingsItemPopupItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemScreenItemRowBinding
import eamato.funn.r6companion.databinding.SettingsItemSwitchItemRowBinding
import eamato.funn.r6companion.ui.entities.settings.SettingsItem
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_POPUP
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SCREEN
import eamato.funn.r6companion.ui.entities.settings.SettingsItem.Companion.VIEW_TYPE_SWITCH

class AdapterSettingsItems :
    ListAdapter<SettingsItem, AdapterSettingsItems.ViewHolder>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<SettingsItem>() {
            override fun areItemsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
                return true
            }

            override fun areContentsTheSame(oldItem: SettingsItem, newItem: SettingsItem): Boolean {
                if (oldItem is SettingsItem.SettingsItemPopup && newItem is SettingsItem.SettingsItemPopup) {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).getItemViewType()
    }

    fun getItemAtPosition(position: Int): SettingsItem = getItem(position)

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun <T : SettingsItem> bind(settingsItem: T)

        class SettingsItemPopupViewHolder(private val binding: SettingsItemPopupItemRowBinding) :
            ViewHolder(binding.root) {

            override fun <T : SettingsItem> bind(settingsItem: T) {
                if (settingsItem !is SettingsItem.SettingsItemPopup) {
                    return
                }

                binding.settingsPopupItem.run {
                    setIsEnabled(settingsItem.isEnabled)
                    setTitle(settingsItem.title)
                    setSubtitle(settingsItem.subTitle)
                    setIcon(settingsItem.icon)
                }
            }
        }

        class SettingsItemSwitchViewHolder(
            private val binding: SettingsItemSwitchItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun <T : SettingsItem> bind(settingsItem: T) {
                if (settingsItem !is SettingsItem.SettingsItemSwitch) {
                    return
                }

                binding.settingsSwitchItem.run {
                    setIsEnabled(settingsItem.isEnabled)
                    setIsChecked(settingsItem.isChecked)
                    setTitle(settingsItem.title)
                    setSubtitle(settingsItem.subTitle)
                    setIcon(settingsItem.icon)
                    setOnCheckedChangeListener { _, isChecked ->
                        settingsItem.onCheckedListener(isChecked)
                    }
                }
            }
        }

        class SettingsItemScreenViewHolder(
            private val binding: SettingsItemScreenItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun <T : SettingsItem> bind(settingsItem: T) {
                if (settingsItem !is SettingsItem.SettingsItemScreen) {
                    return
                }

                binding.settingsScreenItem.run {
                    setIsEnabled(settingsItem.isEnabled)
                    setTitle(settingsItem.title)
                    setSubtitle(settingsItem.subTitle)
                    setIcon(settingsItem.icon)
                }
            }
        }
    }
}