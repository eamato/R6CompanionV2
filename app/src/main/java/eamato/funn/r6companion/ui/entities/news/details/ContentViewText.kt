package eamato.funn.r6companion.ui.entities.news.details

import android.content.Context
import android.graphics.Typeface
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.text.util.Linkify
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.text.HtmlCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.util.LinkifyCompat
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.openUrlInBrowser
import java.util.regex.Matcher
import java.util.regex.Pattern

class ContentViewText(
    private val text: String,
    @StyleRes private val style: Int = R.style.R6Companion_DefaultTextViewStyle,
    private val topMargin: Int = 10
) : AContentView() {

    private val textLink = "\\[(.*?)]\\((.*?)\\)".toRegex()
    private val textBold = "_{2}(.*?)_{2}".toRegex()

    override fun onCreateView(parent: ViewGroup): View {
        return TextView(parent.context, null, 0, style)
            .apply {
                text = this@ContentViewText.text
                    .applyInnerHtml()
                    .applyInnerLink(context)
                    .applyInnerBold()

                movementMethod = LinkMovementMethod.getInstance()
                LinkifyCompat.addLinks(this, Linkify.WEB_URLS or Linkify.EMAIL_ADDRESSES)
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, this@ContentViewText.topMargin, 0, 0) }
            }
    }

    override fun onDestroy() {

    }

    private fun SpannableStringBuilder.applyInnerLink(context: Context): SpannableStringBuilder {
        textLink.findAll(this@ContentViewText.text).forEach { matchResult ->
            val linkText = matchResult.groups[1]?.value ?: return@forEach
            val linkUrl = matchResult.groups[2]?.value ?: return@forEach
            val matchValue = matchResult.value

            val replacingStartIndex = indexOf(matchValue)
            val replacingEndIndex = replacingStartIndex + matchValue.length
            replace(replacingStartIndex, replacingEndIndex, linkText)

            val startIndex = indexOf(linkText)
            val endIndex = startIndex + linkText.length

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(p0: View) {
                    linkUrl.openUrlInBrowser(context)
                }
            }

            setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

        return this
    }

    private fun SpannableStringBuilder.applyInnerBullet(): SpannableStringBuilder {
        val bulletSymbol = '•'
        var hyphenIndex = this@ContentViewText.text.indexOf('-')
        while (hyphenIndex >= 0) {
            replace(hyphenIndex, hyphenIndex + 1, bulletSymbol.toString())
            hyphenIndex = this@ContentViewText.text.indexOf('-', hyphenIndex + 1)
        }

        return this
    }

    private fun String.applyInnerHtml(): SpannableStringBuilder {
        return SpannableStringBuilder(
            HtmlCompat.fromHtml(
                toString(),
                HtmlCompat.FROM_HTML_MODE_COMPACT
            )
        )
    }

    private fun SpannableStringBuilder.applyInnerBold(): SpannableStringBuilder {
        textBold.findAll(this@ContentViewText.text).forEach { matchResult ->
            val replacement = matchResult.groups[0]?.value ?: return@forEach
            val value = matchResult.groups[1]?.value ?: return@forEach

            val replacingStartIndex = indexOf(replacement)
            val replacingEndIndex = replacingStartIndex + replacement.length
            replace(replacingStartIndex, replacingEndIndex, value)

            val span = StyleSpan(Typeface.BOLD)

            val startIndex = indexOf(value)
            val endIndex = startIndex + value.length

            setSpan(span, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return this
    }
}