package eamato.funn.r6companion.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.getDrawable
import eamato.funn.r6companion.core.glide.GlideApp
import eamato.funn.r6companion.core.glide.IDoAfterTerminateGlide
import eamato.funn.r6companion.databinding.FragmentNewsDetailsBinding
import eamato.funn.r6companion.ui.entities.news.details.AContentViewVideo
import eamato.funn.r6companion.ui.entities.news.details.IContentView
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.viewmodels.news.NewsArticleDetailsViewModel

@AndroidEntryPoint
class FragmentNewsDetails : ABaseFragment<FragmentNewsDetailsBinding>() {

    private val newsArticleDetailsViewModel: NewsArticleDetailsViewModel by viewModels()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentNewsDetailsBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
    }

    private fun setObservers() {
        newsArticleDetailsViewModel
            .newsArticleContentViews
            .observe(viewLifecycleOwner) { contentViews ->
                setContentViews(contentViews)
            }

        newsArticleDetailsViewModel.newsArticleImageUrl.observe(viewLifecycleOwner) { imageUrl ->
            binding?.ivNewsImage?.run {
                if (imageUrl == null) {
                    setImageDrawable(R.drawable.no_image_drawable.getDrawable(context))
                    return@observe
                }

                binding?.clpbNewsImage?.show()

                GlideApp.with(this)
                    .load(imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .transition(DrawableTransitionOptions.withCrossFade(500))
                    .error(R.drawable.no_image_drawable.getDrawable(context))
                    .listener(object : IDoAfterTerminateGlide {
                        override fun doAfterTerminate() {
                            binding?.clpbNewsImage?.hide()
                        }
                    })
                    .dontAnimate()
                    .into(this)
            }
        }
    }

    private fun setContentViews(contentViews: List<IContentView>) {
        binding?.llContent?.run {
            removeAllViews()
            contentViews.forEach { contentView ->
                if (contentView is AContentViewVideo) {
                    contentView.setApplicationContext(activity?.applicationContext)
                }
                contentView.createView(this, viewLifecycleOwner)?.run { addView(this) }
            }
        }
    }
}