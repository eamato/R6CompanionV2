package eamato.funn.r6companion.domain.mappers.news

import eamato.funn.r6companion.core.NEWS_RESULT_PATTEN
import eamato.funn.r6companion.core.NEWS_SOURCE_PATTERN_1
import eamato.funn.r6companion.core.NEWS_SOURCE_PATTERN_2
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.data.entities.Updates
import eamato.funn.r6companion.domain.entities.news.NewsArticle
import eamato.funn.r6companion.domain.usecases.IUseCaseMapper
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object NewsArticleUseCaseMapper : IUseCaseMapper<Updates.Item, NewsArticle?> {

    private val inputDateFormat = SimpleDateFormat(NEWS_SOURCE_PATTERN_1, Locale.US)
    private val inputDateFormat2 = SimpleDateFormat(NEWS_SOURCE_PATTERN_2, Locale.US)

    private val outputDateFormat = SimpleDateFormat(NEWS_RESULT_PATTEN, Locale.US)

    override fun map(input: Updates.Item): NewsArticle? {
        return input.toDomainNewsArticle(
            listOf(
                inputDateFormat, inputDateFormat2
            ),
            outputDateFormat
        )
    }
}

private fun Updates.Item.toDomainNewsArticle(
    inputDateFormats: List<SimpleDateFormat>,
    outputDateFormat: SimpleDateFormat
): NewsArticle? {
    val id = this.id ?: return null
    val title = this.title ?: ""
    val subtitle = this.abstract ?: ""
    val content = this.content ?: ""
    val thumbnail = this.thumbnail?.url?.let { thumbnailUrl -> NewsArticle.Thumbnail(thumbnailUrl) }
    val date = this.date
        ?.let { updateDate ->
            for (inputDateFormat in inputDateFormats) {
                val date: Date? = kotlin.runCatching { inputDateFormat.parse(updateDate) }.getOrNull()
                if (date != null) {
                    return@let date
                }
            }
            return null
        }
        ?.let { date -> outputDateFormat.format(date) }
        ?: ""

    DefaultAppLogger.getInstance().i(Message.message {
        clazz = this@toDomainNewsArticle::class.java
        message = "Date: $date"
    })

    return NewsArticle(
        id = id,
        title = title,
        subtitle = subtitle,
        content = content,
        thumbnail = thumbnail,
        date = date
    )
}