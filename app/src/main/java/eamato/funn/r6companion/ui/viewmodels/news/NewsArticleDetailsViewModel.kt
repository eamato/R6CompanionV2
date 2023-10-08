package eamato.funn.r6companion.ui.viewmodels.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import eamato.funn.r6companion.R
import eamato.funn.r6companion.domain.entities.news.NewsArticle
import eamato.funn.r6companion.ui.entities.news.details.ContentViewDivider
import eamato.funn.r6companion.ui.entities.news.details.ContentViewImage
import eamato.funn.r6companion.ui.entities.news.details.ContentViewText
import eamato.funn.r6companion.ui.entities.news.details.ContentViewSimpleVideo
import eamato.funn.r6companion.ui.entities.news.details.ContentViewYoutubeVideo
import eamato.funn.r6companion.ui.entities.news.details.IContentView
import eamato.funn.r6companion.ui.fragments.home.FragmentNewsDetailsArgs

class NewsArticleDetailsViewModel(state: SavedStateHandle) : ViewModel() {

    private val _newsArticleContentViews = MutableLiveData<List<IContentView>>()
    val newsArticleContentViews: LiveData<List<IContentView>> = _newsArticleContentViews

    private val _newsArticleImageUrl = MutableLiveData<String?>()
    val newsArticleImageUrl: LiveData<String?> = _newsArticleImageUrl

    private val header1 = "(?<=[^#]|^)# (.*)".toRegex()
    private val header2 = "(?<=[^#]|^)#{2} (.*)".toRegex()
    private val header3 = "(?<=[^#]|^)#{3} (.*)".toRegex()
    private val imagePrefix = "(/{2}.*?\\.(?:jpg|gif|png|jpeg))".toRegex()
    private val italicPrefix = "(?<=\\*)(.*?)(?=\\*)".toRegex()
    private val italicPrefix2 = "(?<=\\*{2})(.*?)(?=\\*{2})".toRegex()
    private val italicPrefix3 = "\\*(.*?)\\*(?![\\s\\S]*\\*)".toRegex()
    private val videoPrefix = "\\[video]\\((.*)\\)".toRegex()
    private val videoControls = "<video.*\\s*<source[^>]*src=\"([^\"]*)".toRegex()
    private val videoControlsInsideImageTag = "(/{2}.*?\\.mp4)".toRegex()

    init {
        val newsArticle = FragmentNewsDetailsArgs.fromSavedStateHandle(state).article

        _newsArticleImageUrl.value = newsArticle.thumbnail?.url
        createNewsDetailsList(newsArticle)
    }

    private fun createNewsDetailsList(newsArticle: NewsArticle) {
        val contentViews = mutableListOf<IContentView>()

        contentViews.add(
            ContentViewText(
                text = newsArticle.title,
                style = R.style.R6Companion_ContentHeader1Style
            )
        )
        contentViews.addAll(newsArticle.content.contentToViewList())

        _newsArticleContentViews.value = contentViews
    }

    private fun String.contentToViewList(): List<IContentView> {
        val mainContentSplitter = "\n\n"
        val innerContentSplitter = "\n"
        val mainContent = split(mainContentSplitter)
        val contentViewList = mutableListOf<IContentView>()
        mainContent.forEach {
            when {
                it.contains(videoControls) -> {
                    it.contentToView()?.run { contentViewList.add(this) }
                }
                it.contains(innerContentSplitter) -> {
                    it
                        .split(innerContentSplitter)
                        .map { innerContent -> innerContent.contentToView() }
                        .filterNotNullTo(contentViewList)
                }
                else -> { it.contentToView()?.run { contentViewList.add(this) } }
            }
        }
        return contentViewList
    }

    private fun String.contentToView(): IContentView? {
        val text = replace("<br>", "\n\n")

        return when {
            text.startsWith("###") -> {
                header3.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewText(
                        text = nonNullValue,
                        style = R.style.R6Companion_ContentHeader1Style,
                        topMargin = 50
                    )
                }
            }

            text.startsWith("##") -> {
                header2.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewText(
                        text = nonNullValue,
                        style = R.style.R6Companion_ContentHeader2Style,
                        topMargin = 50
                    )
                }
            }

            text.startsWith("#") -> {
                header1.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewText(
                        text = nonNullValue,
                        style = R.style.R6Companion_ContentHeader1Style,
                        topMargin = 50
                    )
                }
            }

            text.startsWith("![") || this.startsWith("[![") -> {
                imagePrefix.find(text)
                    ?.groups
                    ?.get(1)
                    ?.value
                    ?.let { nonNullValue -> ContentViewImage("https:$nonNullValue") }
                    ?: videoControlsInsideImageTag.find(text)
                        ?.groups
                        ?.get(1)
                        ?.value
                        ?.let { nonNullValue -> ContentViewSimpleVideo("https:$nonNullValue") }
            }

            text.startsWith("**") -> {
                italicPrefix2.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewText(
                        text = nonNullValue,
                        style = R.style.R6Companion_ContentItalicStyle
                    )
                }
            }

            text.startsWith("*") -> {
                italicPrefix3.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewText(
                        text = nonNullValue,
                        style = R.style.R6Companion_ContentItalicStyle
                    )
                }
            }

            text.startsWith("-") -> {
                if (text == "---") {
                    ContentViewDivider()
                } else {
                    val bulletSymbol = '•'
                    ContentViewText(text = text.replaceFirst('-', bulletSymbol))
                }
            }

            text.contains(videoControls) -> {
                videoControls.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewSimpleVideo(nonNullValue)
                }
            }

            text.startsWith("[video]") -> {
                videoPrefix.find(text)?.groups?.get(1)?.value?.let { nonNullValue ->
                    ContentViewYoutubeVideo(nonNullValue)
                }
            }

            else -> ContentViewText(text = text)
        }
    }
}