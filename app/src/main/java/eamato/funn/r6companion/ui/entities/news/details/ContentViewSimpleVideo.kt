package eamato.funn.r6companion.ui.entities.news.details

class ContentViewSimpleVideo(private val videoUrl: String) : AContentViewVideoWebView() {

    override val videoContent: String
        get() = """
        <html>
            <head></head>
            <body style='margin:0;padding:0;'>
                <div id="videoContainer">
                    <video width="100%" height="100%" preload="metadata" poster="null" controls>
                        <source src="${
                            if (videoUrl.startsWith("//")) {
                                "https:$videoUrl"
                            } else {
                                videoUrl
                            }
                        }">
                    </video>
                </div>
            </body>
        </html>
        """.trimIndent()
}