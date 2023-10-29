package eamato.funn.r6companion.ui.entities.settings

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import eamato.funn.r6companion.core.utils.UiText
import eamato.funn.r6companion.databinding.SettingsPopupButtonBinding

class SettingsPopupButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding: SettingsPopupButtonBinding =
        SettingsPopupButtonBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
    )

    init {
        setSelectableForeground(true)
        setAutoScrollTextView(true)
    }

    fun setIsEnabled(isEnabled: Boolean) {
        binding.run {
            tvSettingsItemTitle.isEnabled = isEnabled
            tvSettingsItemSubtitle.isEnabled = isEnabled
            ivIcon.isEnabled = isEnabled
        }

        setSelectableForeground(isEnabled)
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

    private fun setSelectableForeground(isEnabled: Boolean) {
        if (isEnabled) {
            val outValue = TypedValue()
            context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
            foreground = AppCompatResources.getDrawable(context, outValue.resourceId)
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