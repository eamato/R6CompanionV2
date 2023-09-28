package eamato.funn.r6companion.ui.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.FrameLayout
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.databinding.FragmentSettingsRootBinding
import eamato.funn.r6companion.ui.fragments.ABaseFragment

@AndroidEntryPoint
class FragmentSettingsRoot : ABaseFragment<FragmentSettingsRootBinding>() {

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentSettingsRootBinding::inflate

    private var webView: WebView? = null

    private val htmlContent = """
            <html>
            <head></head>
            <body style='margin:0;padding:0;'>
                <div id="videoContainer">
                    <video controls width="100%" height="100%" preload="metadata" poster="null">
                        <source src="https://staticctf.ubisoft.com/J3yJr34U2pZ2Ieem48Dwy9uqj5PNUQTn/1sQGrBqYLs5CVeB9oZ26Sn/1ea0162b2024e22589d0a39dcdff9438/R6_Guardian_1X1_FINAL.mp4">
                    </video>
                </div>
            </body>
            </html>
        """.trimIndent()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val youtubeUrl = "https://www.youtube-nocookie.com/embed/FOLGq0I6xWs?cc_lang_pref=en&cc_load_policy=1&hl=en"
        val youtubeUrl2 = "https://www.youtube.com/watch?v=FOLGq0I6xWs"
        val mp4Url = "https://staticctf.ubisoft.com/J3yJr34U2pZ2Ieem48Dwy9uqj5PNUQTn/30OCLazuhixNzrWmArWgzL/2b88ece25ac60854d0c6d7019fa7fe17/R6S_DN_Y8S3_BalancingMatrix_Att.mp4"
        val mp4Url2 = "https://staticctf.ubisoft.com/J3yJr34U2pZ2Ieem48Dwy9uqj5PNUQTn/1sQGrBqYLs5CVeB9oZ26Sn/1ea0162b2024e22589d0a39dcdff9438/R6_Guardian_1X1_FINAL.mp4"

        val utubeShort = "FOLGq0I6xWs"
        val aa = "<iframe " +
                "width=\"100%\" " +
                "height=\"100%\" " +
                "frameBorder=\"0\" " +
                "src=\"https://www.youtube.com/embed/$utubeShort?controls=0&showinfo=0\">" +
                "</iframe>"

        webView = WebView(requireActivity().applicationContext).apply {
            settings.mediaPlaybackRequiresUserGesture = true
            settings.javaScriptEnabled = true
            webChromeClient = WebChromeClient()
            loadData(htmlContent, "text/html", "utf-8")
        }.also {
            it.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            binding?.fl?.addView(it)
        }
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView?.onPause()
    }

    override fun onDestroyView() {
        webView?.run {
            binding?.fl?.removeView(webView)
            binding?.fl?.removeAllViews()
            stopLoading()
            settings.javaScriptEnabled = false
            clearView()
            webChromeClient = null
            clearHistory()
            clearCache(true)
            removeAllViews()
            destroy()
        }
        webView = null
        super.onDestroyView()
    }
}