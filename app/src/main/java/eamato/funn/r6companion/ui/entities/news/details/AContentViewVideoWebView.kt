package eamato.funn.r6companion.ui.entities.news.details

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import eamato.funn.r6companion.databinding.ContentViewVideoContainerBinding

abstract class AContentViewVideoWebView : AContentViewVideo() {

    abstract val videoContent: String

    private var webView: WebView? = null
    private var binding: ContentViewVideoContainerBinding? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(parent: ViewGroup): View {
        val appContext = appContext

        val layoutInflater = LayoutInflater.from(parent.context)

        return ContentViewVideoContainerBinding.inflate(layoutInflater, parent, false)
            .apply {
                if (appContext != null) {
                    WebView(appContext)
                        .apply {
                            settings.javaScriptEnabled = true
                            webChromeClient = WebChromeClient()

                            layoutParams = FrameLayout.LayoutParams(
                                FrameLayout.LayoutParams.MATCH_PARENT,
                                FrameLayout.LayoutParams.MATCH_PARENT
                            )
                        }
                        .also {
                            it.loadData(videoContent, "text/html", "utf-8")
                            playerView.addView(it)
                            webView = it
                        }
                } else {
                    ivNoData.visibility = View.VISIBLE
                }
            }
            .also { binding = it }
            .root
    }

    override fun onResume() {
        webView?.onResume()
    }

    override fun onPause() {
        webView?.onPause()
    }

    override fun onDestroy() {
        webView?.run {
            binding?.playerView?.removeView(this)
            binding?.playerView?.removeAllViews()
            stopLoading()
            settings.javaScriptEnabled = false
            webChromeClient = null
            clearHistory()
            clearCache(true)
            removeAllViews()
            destroy()
        }
        appContext = null
        webView = null
        binding = null
    }
}