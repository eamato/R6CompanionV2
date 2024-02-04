package eamato.funn.r6companion.core.utils

import android.content.Context
import androidx.annotation.DrawableRes

class DialogContent private constructor(
    @DrawableRes private val icon: Int? = null,
    private val title: UiText? = null,
    private val message: UiText? = null,
    private val positiveText: UiText? = null,
    private val negativeText: UiText? = null,
    private var positiveClickListener: (() -> Unit)? = null,
    private var negativeClickListener: (() -> Unit)? = null
) {

    @DrawableRes
    fun getIcon(): Int? = icon

    fun getTitle(context: Context): String? = title?.asString(context)

    fun getMessage(context: Context): String? = message?.asString(context)

    fun getPositive(context: Context): Pair<String?, (() -> Unit)?> =
        positiveText?.asString(context) to positiveClickListener

    fun getNegative(context: Context): Pair<String?, (() -> Unit)?> =
        negativeText?.asString(context) to negativeClickListener

    object Builder {
        private var icon: Int? = null
        private var title: UiText? = null
        private var message: UiText? = null
        private var positiveText: UiText? = null
        private var negativeText: UiText? = null
        private var positiveClickListener: (() -> Unit)? = null
        private var negativeClickListener: (() -> Unit)? = null

        fun setIcon(icon: Int?): Builder {
            this.icon = icon
            return this
        }

        fun setTitle(title: UiText): Builder {
            this.title = title
            return this
        }

        fun setMessage(message: UiText): Builder {
            this.message = message
            return this
        }

        fun setPositiveButton(positiveText: UiText, onClickListener: (() -> Unit)? = null): Builder {
            this.positiveText = positiveText
            positiveClickListener = onClickListener
            return this
        }

        fun setNegativeButton(negativeText: UiText, onClickListener: (() -> Unit)? = null): Builder {
            this.negativeText = negativeText
            negativeClickListener = onClickListener
            return this
        }

        fun build(): DialogContent = DialogContent(
            icon,
            title,
            message,
            positiveText,
            negativeText,
            positiveClickListener,
            negativeClickListener
        )
    }
}