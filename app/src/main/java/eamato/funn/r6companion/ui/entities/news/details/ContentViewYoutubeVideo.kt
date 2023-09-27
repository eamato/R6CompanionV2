package eamato.funn.r6companion.ui.entities.news.details

import android.net.Uri

class ContentViewYoutubeVideo(private val videoUrl: String) : AContentViewVideoWebView() {

    override val videoContent: String
        get() = """
        <html>
            <head></head>
            <body style='margin:0;padding:0;'>
                <div id="videoContainer">
                    <iframe width="100%" height="100%" frameBorder="0" 
                    src="https://www.youtube.com/embed/${getShortVideoUrl()}?controls=0&showinfo=0">
                    </iframe>
                </div>
            </body>
        </html>
        """.trimIndent()

    private fun getShortVideoUrl(): String {
        return Uri.parse(videoUrl).lastPathSegment ?: ""
    }
}