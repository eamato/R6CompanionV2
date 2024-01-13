package eamato.funn.r6companion.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.DEFAULT_NEWS_LOCALE
import eamato.funn.r6companion.core.NEWS_LIST_GRID_COUNT_LANDSCAPE
import eamato.funn.r6companion.core.NEWS_LIST_GRID_COUNT_PORTRAIT
import eamato.funn.r6companion.core.SCREEN_NAME_HOME
import eamato.funn.r6companion.core.extenstions.applySystemInsetsIfNeeded
import eamato.funn.r6companion.core.extenstions.isLandscape
import eamato.funn.r6companion.core.extenstions.onTrueInvoke
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentNewsListBinding
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_COMMUNITY_VALUE
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_ESPORTS_VALUE
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_GAME_UPDATES_VALUE
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_PATCH_NOTES_VALUE
import eamato.funn.r6companion.data.entities.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_STORE_VALUE
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterNewsArticles
import eamato.funn.r6companion.ui.delegates.AnalyticsLogger
import eamato.funn.r6companion.ui.delegates.IAnalyticsLogger
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.news.NewsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentNewsList : ABaseFragment<FragmentNewsListBinding>(),
    IAnalyticsLogger by AnalyticsLogger(FragmentNewsList::class.java.simpleName, SCREEN_NAME_HOME) {

    private val logger = DefaultAppLogger.getInstance()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding =
        FragmentNewsListBinding::inflate

    private val newsViewModel: NewsViewModel by viewModels()

    private val adapterNewsArticles = AdapterNewsArticles()

    private var buttonsByCategory: HashMap<String, View>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        registerLifecycleOwner(viewLifecycleOwner, view.context)

        initNewsCategoryButtons()
        setObservers()
        initNewsRecyclerView()
        initGoToTopDestinationAction()
        initSwipeRefreshLayout()
        applySystemInsetsIfNeeded()
    }

    override fun onDestroyView() {
        binding?.rvNews?.adapter = null
        buttonsByCategory?.clear()
        buttonsByCategory = null

        super.onDestroyView()
    }

    private fun initNewsRecyclerView() {
        binding?.rvNews?.run {
            setHasFixedSize(true)

            layoutManager = createNewsRecyclerViewLayoutManager()

            adapter = adapterNewsArticles

            setNewsRecyclerViewItemDecorations()

            adapterNewsArticles.addLoadStateListener { combinedLoadStates ->
                logger.i(Message.message {
                    clazz = this@FragmentNewsList::class.java
                    message = "News load state: $combinedLoadStates"
                })

                if (combinedLoadStates.source.refresh is LoadState.Loading) {
                    binding?.clpbWaiting?.show()
                    buttonsByCategory?.values?.forEach { categoryButton ->
                        categoryButton.isEnabled = false
                    }
                } else {
                    binding?.clpbWaiting?.hide()
                    binding?.srlRefreshNews?.isRefreshing = false
                    buttonsByCategory?.values?.forEach { categoryButton ->
                        categoryButton.isEnabled = true
                    }
                }

                val error = combinedLoadStates.source.append as? LoadState.Error
                    ?: combinedLoadStates.source.prepend as? LoadState.Error
                    ?: combinedLoadStates.source.refresh as? LoadState.Error
                    ?: combinedLoadStates.append as? LoadState.Error
                    ?: combinedLoadStates.prepend as? LoadState.Error
                    ?: combinedLoadStates.refresh as? LoadState.Error

                if (error != null) {
                    showError(error.error)
                }
            }

            val clickListener = RecyclerViewItemClickListener(
                this,
                object : RecyclerViewItemClickListener.OnItemTapListener {
                    override fun onItemClicked(view: View, position: Int) {
                        val selectedNewsArticle =
                            adapterNewsArticles.getItemAtPosition(position)?.newsArticle ?: return

                        findNavController().navigate(
                            R.id.FragmentNewsDetails,
                            FragmentNewsDetailsArgs(selectedNewsArticle).toBundle()
                        )
                    }
                }
            )

            setOnItemClickListener(clickListener)
        }
    }

    private fun createNewsRecyclerViewLayoutManager(): RecyclerView.LayoutManager {
        val spanCount = context
            .isLandscape()
            .onTrueInvoke { NEWS_LIST_GRID_COUNT_LANDSCAPE }
            ?: NEWS_LIST_GRID_COUNT_PORTRAIT

        return GridLayoutManager(context, spanCount)
    }

    private fun setNewsRecyclerViewItemDecorations() {
        binding?.rvNews?.run {
            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(R.dimen.dp_4, R.dimen.dp_4, R.dimen.dp_4, R.dimen.dp_4)
                .create(context)

            setItemDecoration(spacingDecoration)
        }
    }

    private fun onNewsLocaleChanged(newsLocale: String) {
        val currentNewsCategory =
            newsViewModel.newsCategoryValue.value ?: NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE
        newsViewModel.refreshNews(newsLocale = newsLocale, newsCategory = currentNewsCategory)
    }

    private fun initGoToTopDestinationAction() {
        mainNavigationViewModel.backToGraphRootEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { binding?.rvNews?.scrollToPosition(0) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSwipeRefreshLayout() {
        binding?.srlRefreshNews?.run {
            setColorSchemeResources(R.color.blue, R.color.yellow, R.color.red)
            setOnRefreshListener { adapterNewsArticles.refresh() }
        }
    }

    private fun applySystemInsetsIfNeeded() {
        binding?.root?.applySystemInsetsIfNeeded { insets ->
            binding?.clHeaderButtons?.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = insets.top
                leftMargin = insets.left
                rightMargin = insets.right
            }
        }
    }

    private fun initNewsCategoryButtons() {
        binding?.run {
            buttonsByCategory = hashMapOf(
                NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE to btnNewsCategoryAll,
                NEWS_CATEGORIES_FILTER_PARAM_ESPORTS_VALUE to btnNewsCategoryEsport,
                NEWS_CATEGORIES_FILTER_PARAM_GAME_UPDATES_VALUE to btnNewsCategoryGameUpdates,
                NEWS_CATEGORIES_FILTER_PARAM_COMMUNITY_VALUE to btnNewsCategoryCommunity,
                NEWS_CATEGORIES_FILTER_PARAM_PATCH_NOTES_VALUE to btnNewsCategoryPatchNotes,
                NEWS_CATEGORIES_FILTER_PARAM_STORE_VALUE to btnNewsCategoryStore
            )
        }

        buttonsByCategory?.forEach { (category, button) ->
            button.setOnClickListener {
                val currentNewsLocale = newsViewModel.newsLocale.value ?: DEFAULT_NEWS_LOCALE
                newsViewModel.refreshNews(newsLocale = currentNewsLocale, newsCategory = category)
            }
        }
    }

    private fun setObservers() {
        newsViewModel.news.observe(viewLifecycleOwner) { pagingData ->
            adapterNewsArticles.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        newsViewModel.newsCategoryValue.observe(viewLifecycleOwner) { categoryValue ->
            buttonsByCategory?.forEach { (category, button) ->
                button.isSelected = category == categoryValue
            }
        }

        newsViewModel.newsLocale.observe(viewLifecycleOwner) { newsLocale ->
            onNewsLocaleChanged(newsLocale)
        }
    }
}