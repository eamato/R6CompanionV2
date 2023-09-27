package eamato.funn.r6companion.domain.mappers.news

import eamato.funn.r6companion.core.NEWS_RESULT_PATTEN
import eamato.funn.r6companion.core.NEWS_SOURCE_PATTERN_1
import eamato.funn.r6companion.data.entities.Updates
import eamato.funn.r6companion.domain.entities.news.NewsArticle
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper
import java.text.SimpleDateFormat
import java.util.Locale

object NewsArticleUseCaseMapper : IUseCaseMapper<Updates.Item, NewsArticle?> {

    private val inputDateFormat = SimpleDateFormat(NEWS_SOURCE_PATTERN_1, Locale.US)
    private val outputDateFormat = SimpleDateFormat(NEWS_RESULT_PATTEN, Locale.US)

    override fun map(input: Updates.Item): NewsArticle? {
        return input.toDomainNewsArticle(inputDateFormat, outputDateFormat)
    }
}

private fun Updates.Item.toDomainNewsArticle(
    inputDateFormat: SimpleDateFormat,
    outputDateFormat: SimpleDateFormat
): NewsArticle? {
    val id = this.id ?: return null
    val title = this.title ?: ""
    val subtitle = this.abstract ?: ""
    val content = this.content ?: ""
    val thumbnail = this.thumbnail?.url?.let { thumbnailUrl -> NewsArticle.Thumbnail(thumbnailUrl) }
    val date = this.date
        ?.let { updateDate -> kotlin.runCatching { inputDateFormat.parse(updateDate) }.getOrNull() }
        ?.let { date -> outputDateFormat.format(date) }
        ?: ""

    return NewsArticle(
        id = id,
        title = title,
        subtitle = subtitle,
        content = content,
        thumbnail = thumbnail,
        date = date
    )
}