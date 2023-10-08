package eamato.funn.r6companion.ui.entities.news.details

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import androidx.appcompat.widget.LinearLayoutCompat
import eamato.funn.r6companion.R

class ContentViewDivider(
    @StyleRes private val style: Int = R.style.R6Companion_ContentDividerStyle,
    private val topMargin: Int = 40,
    private val bottomMargin: Int = 30,
) : AContentView() {

    override fun onCreateView(parent: ViewGroup): View {
        return View(parent.context, null, 0, style)
            .apply {
                layoutParams = LinearLayoutCompat.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    3
                ).apply {
                    setMargins(
                        0,
                        this@ContentViewDivider.topMargin,
                        0,
                        this@ContentViewDivider.bottomMargin
                    )
                }
            }
    }

    override fun onDestroy() {

    }
}