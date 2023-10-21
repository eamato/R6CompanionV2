package eamato.funn.r6companion.ui.entities.settings

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.databinding.SettingsSwitchButtonBinding

class SettingsSwitchButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SettingsSwitchButtonBinding =
        SettingsSwitchButtonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

    private var checkedChangeListener: ((View, Boolean) -> Unit)? = null

    init {
        setSelectableBackground(true)
        setAutoScrollTextView(true)
    }

    fun setIsChecked(isChecked: Boolean) {
        binding.settingsItemSwitch.run {
            this.setOnCheckedChangeListener(null)
            this.isChecked = isChecked
            this.setOnCheckedChangeListener { view, isChecked ->
                checkedChangeListener?.invoke(view, isChecked)
            }
        }
    }

    fun setIsEnabled(isEnabled: Boolean) {
        binding.run {
            settingsItemSwitch.isEnabled = isEnabled
            tvSettingsItemTitle.isEnabled = isEnabled
            tvSettingsItemSubtitle.isEnabled = isEnabled
            ivIcon.isEnabled = isEnabled
        }

        setSelectableBackground(isEnabled)
        setAutoScrollTextView(isEnabled)
    }

    fun setTitle(title: UiText?) {
        if (title == null) {
            binding.tvSettingsItemTitle.text = ""
            binding.tvSettingsItemTitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemTitle.text = title.asString(context)
        binding.tvSettingsItemTitle.visibility = View.VISIBLE
    }

    fun setTitle(title: String?) {
        if (title == null) {
            binding.tvSettingsItemTitle.text = ""
            binding.tvSettingsItemTitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemTitle.text = title
        binding.tvSettingsItemTitle.visibility = View.VISIBLE
    }

    fun setTitle(@StringRes title: Int?) {
        if (title == null) {
            binding.tvSettingsItemTitle.text = ""
            binding.tvSettingsItemTitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemTitle.setText(title)
        binding.tvSettingsItemTitle.visibility = View.VISIBLE
    }

    fun setSubtitle(subtitle: UiText?) {
        if (subtitle == null) {
            binding.tvSettingsItemSubtitle.text = ""
            binding.tvSettingsItemSubtitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemSubtitle.text = subtitle.asString(context)
        binding.tvSettingsItemSubtitle.visibility = View.VISIBLE
    }

    fun setSubtitle(subtitle: String?) {
        if (subtitle == null) {
            binding.tvSettingsItemSubtitle.text = ""
            binding.tvSettingsItemSubtitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemSubtitle.text = subtitle
        binding.tvSettingsItemSubtitle.visibility = View.VISIBLE
    }

    fun setSubtitle(@StringRes subtitle: Int?) {
        if (subtitle == null) {
            binding.tvSettingsItemSubtitle.text = ""
            binding.tvSettingsItemSubtitle.visibility = View.GONE
            return
        }

        binding.tvSettingsItemSubtitle.setText(subtitle)
        binding.tvSettingsItemSubtitle.visibility = View.VISIBLE
    }

    fun setIcon(@DrawableRes icon: Int?) {
        if (icon == null) {
            binding.ivIcon.setImageDrawable(null)
            return
        }

        binding.ivIcon.setImageResource(icon)
    }

    fun setOnCheckedChangeListener(action: (View, Boolean) -> Unit) {
        setOnClickListener { binding.settingsItemSwitch.toggle() }
        checkedChangeListener = action
    }

    private fun setSelectableBackground(isEnabled: Boolean) {
        if (isEnabled) {
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            setBackgroundResource(outValue.resourceId)
        } else {
            background = null
        }
        isClickable = isEnabled
    }

    private fun setAutoScrollTextView(isEnabled: Boolean) {
//        binding.tvSettingsItemTitle.isSelected = isEnabled
//        binding.tvSettingsItemSubtitle.isSelected = isEnabled
    }
}