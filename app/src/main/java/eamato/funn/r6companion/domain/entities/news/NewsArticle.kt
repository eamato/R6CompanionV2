package eamato.funn.r6companion.domain.entities.news

import android.os.Parcelable
import eamato.funn.r6companion.R
import kotlinx.parcelize.Parcelize

@Parcelize
data class NewsArticle(
    val id: String,
    val title: String,
    val subtitle: String,
    val content: String,
    val thumbnail: Thumbnail?,
    val date: String
) : Parcelable {

    @Parcelize
    data class Thumbnail(val url: String) : Parcelable
}
