package eamato.funn.r6companion.ui.adapters.recyclerviews

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IntDef
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdOptions
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.IDoAfterTerminateGlide
import eamato.funn.r6companion.databinding.NewsAdvertisingItemRowBinding
import eamato.funn.r6companion.databinding.NewsArticleItemRowBinding
import eamato.funn.r6companion.databinding.NewsPlaceholderItemRowBinding
import eamato.funn.r6companion.domain.entities.news.AdvertisedNewsArticle

class AdapterNewsArticles :
    PagingDataAdapter<AdvertisedNewsArticle, AdapterNewsArticles.ViewHolder>(DIFF_ITEM_CALLBACK) {

    companion object {
        val DIFF_ITEM_CALLBACK = object : DiffUtil.ItemCallback<AdvertisedNewsArticle>() {
            override fun areItemsTheSame(
                oldItem: AdvertisedNewsArticle,
                newItem: AdvertisedNewsArticle
            ): Boolean {
                return oldItem.isAd == newItem.isAd &&
                        oldItem.newsArticle?.id == newItem.newsArticle?.id
            }

            override fun areContentsTheSame(
                oldItem: AdvertisedNewsArticle,
                newItem: AdvertisedNewsArticle
            ): Boolean {
                return oldItem.isAd == newItem.isAd &&
                        oldItem.newsArticle?.id == newItem.newsArticle?.id &&
                        oldItem.newsArticle?.title == newItem.newsArticle?.title &&
                        oldItem.newsArticle?.subtitle == newItem.newsArticle?.subtitle &&
                        oldItem.newsArticle?.content == newItem.newsArticle?.content &&
                        oldItem.newsArticle?.thumbnail?.url == newItem.newsArticle?.thumbnail?.url
            }
        }

        @IntDef(ADVERTISE_VIEW_TYPE, NEWS_ARTICLE_VIEW_TYPE, PLACEHOLDER_VIEW_TYPE)
        @Retention(AnnotationRetention.SOURCE)
        annotation class ItemViewType

        private const val ADVERTISE_VIEW_TYPE = 1
        private const val NEWS_ARTICLE_VIEW_TYPE = 2
        private const val PLACEHOLDER_VIEW_TYPE = 3
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ADVERTISE_VIEW_TYPE -> {
                ViewHolder.NewsAdvertiseViewHolder(
                    NewsAdvertisingItemRowBinding.inflate(layoutInflater, parent, false)
                )
            }
            NEWS_ARTICLE_VIEW_TYPE -> {
                ViewHolder.NewsArticleViewHolder(
                    NewsArticleItemRowBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> {
                ViewHolder.PlaceholderViewHolder(
                    NewsPlaceholderItemRowBinding.inflate(layoutInflater, parent, false)
                )
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    @ItemViewType
    override fun getItemViewType(position: Int): Int {
        val itemAtPosition = getItem(position) ?: return PLACEHOLDER_VIEW_TYPE

        if (itemAtPosition.isAd) {
            return ADVERTISE_VIEW_TYPE
        }

        return NEWS_ARTICLE_VIEW_TYPE
    }

    fun getItemAtPosition(position: Int): AdvertisedNewsArticle? {
        return getItem(position)
    }

    sealed class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        abstract fun bind(newsArticle: AdvertisedNewsArticle?)

        class NewsArticleViewHolder(
            private val binding: NewsArticleItemRowBinding
        ) : ViewHolder(binding.root) {

            private val errorDrawable = R.drawable.no_image_drawable.getDrawable(itemView.context)

            override fun bind(newsArticle: AdvertisedNewsArticle?) {
                if (newsArticle?.newsArticle == null) {
                    binding.clpbNewsImage.show()
                    binding.ivNewsImage.setImageDrawable(errorDrawable)
                    binding.tvNewsTitle.text = ""
                    binding.tvNewsSubtitle.text = ""
                    binding.tvNewsDate.text = ""

                    return
                }

                binding.clpbNewsImage.show()

                GlideApp.with(binding.ivNewsImage)
                    .load(newsArticle.newsArticle.thumbnail?.url)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .centerCrop()
                    .error(errorDrawable)
                    .listener(object : IDoAfterTerminateGlide {
                        override fun doAfterTerminate() {
                            binding.clpbNewsImage.hide()
                        }
                    })
                    .dontAnimate()
                    .into(binding.ivNewsImage)

                binding.tvNewsTitle.text = newsArticle.newsArticle.title
                binding.tvNewsSubtitle.text = newsArticle.newsArticle.subtitle
                binding.tvNewsDate.text = newsArticle.newsArticle.date
            }
        }

        class NewsAdvertiseViewHolder(
            private val binding: NewsAdvertisingItemRowBinding
        ) : ViewHolder(binding.root) {

            private var currentNativeAd: NativeAd? = null

            override fun bind(newsArticle: AdvertisedNewsArticle?) {
                hideAdViews()
                currentNativeAd?.destroy()

                val adLoader = AdLoader.Builder(
                    itemView.context,
                    itemView.context.getString(R.string.ad_mod_first_ad_unit)
                )
                    .forNativeAd { ad ->
                        currentNativeAd = ad

                        populateAd(ad)
                    }
                    .withNativeAdOptions(NativeAdOptions.Builder().build())
                    .build()

                adLoader.loadAd(AdRequest.Builder().build())
            }

            private fun hideAdViews() {
                binding.run {
                    adAppIcon.visibility = View.INVISIBLE
                    adHeadline.visibility = View.INVISIBLE
                    adAdvertiser.visibility = View.INVISIBLE
                    adStars.visibility = View.INVISIBLE
                    adBody.visibility = View.INVISIBLE
                    adMedia.visibility = View.INVISIBLE
                    adPrice.visibility = View.INVISIBLE
                    adStore.visibility = View.INVISIBLE
                    adCallToAction.visibility = View.INVISIBLE
                }
            }

            private fun populateAd(nativeAd: NativeAd) {
                binding.run {
                    adHeadline.text = nativeAd.headline

                    nativeAd.mediaContent?.run {
                        adMedia.visibility = View.VISIBLE
                        adMedia.mediaContent = this
                    } ?: kotlin.run { adMedia.visibility = View.INVISIBLE  }

                    nativeAd.body?.run {
                        adBody.visibility = View.VISIBLE
                        adBody.text = this
                    } ?: kotlin.run { adBody.visibility = View.INVISIBLE }

                    nativeAd.callToAction?.run {
                        adCallToAction.visibility = View.VISIBLE
                        adCallToAction.text = this
                    } ?: kotlin.run { adCallToAction.visibility = View.INVISIBLE }

                    nativeAd.icon?.run {
                        adAppIcon.visibility = View.VISIBLE
                        adAppIcon.setImageDrawable(drawable)
                    } ?: kotlin.run { adAppIcon.visibility = View.INVISIBLE }

                    nativeAd.price?.run {
                        adPrice.visibility = View.VISIBLE
                        adPrice.text = this
                    } ?: kotlin.run { adPrice.visibility = View.INVISIBLE }

                    nativeAd.store?.run {
                        adStore.visibility = View.VISIBLE
                        adStore.text = this
                    } ?: kotlin.run { adStore.visibility = View.INVISIBLE }

                    nativeAd.starRating?.run {
                        adStars.visibility = View.VISIBLE
                        adStars.rating = toFloat()
                    } ?: kotlin.run { adStars.visibility = View.INVISIBLE }

                    nativeAd.advertiser?.run {
                        adAdvertiser.visibility = View.VISIBLE
                        adAdvertiser.text = this
                    } ?: kotlin.run { adAdvertiser.visibility = View.INVISIBLE }

                    unav.headlineView = adHeadline
                    unav.bodyView = adBody
                    unav.callToActionView = adCallToAction
                    unav.iconView = adAppIcon
                    unav.priceView = adPrice
                    unav.starRatingView = adStars
                    unav.storeView = adStore
                    unav.advertiserView = adAdvertiser
                    unav.mediaView = adMedia

                    nativeAd.mediaContent?.run { unav.mediaView?.setMediaContent(this) }

                    unav.setNativeAd(nativeAd)
                }
            }
        }

        class PlaceholderViewHolder(
            binding: NewsPlaceholderItemRowBinding
        ) : ViewHolder(binding.root) {

            override fun bind(newsArticle: AdvertisedNewsArticle?) {}
        }
    }
}