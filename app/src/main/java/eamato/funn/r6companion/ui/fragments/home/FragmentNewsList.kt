package eamato.funn.r6companion.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewbinding.ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import eamato.funn.r6companion.R
import eamato.funn.r6companion.core.extenstions.setItemDecoration
import eamato.funn.r6companion.core.extenstions.setOnItemClickListener
import eamato.funn.r6companion.core.utils.logger.DefaultAppLogger
import eamato.funn.r6companion.core.utils.logger.Message
import eamato.funn.r6companion.core.utils.recyclerview.RecyclerViewItemClickListener
import eamato.funn.r6companion.databinding.FragmentNewsListBinding
import eamato.funn.r6companion.domain.entities.news.NewsCategory
import eamato.funn.r6companion.domain.entities.news.NewsCategory.Companion.NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE
import eamato.funn.r6companion.ui.adapters.recyclerviews.AdapterNewsArticles
import eamato.funn.r6companion.ui.fragments.ABaseFragment
import eamato.funn.r6companion.ui.recyclerviews.decorations.SpacingItemDecoration
import eamato.funn.r6companion.ui.viewmodels.news.NewsViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FragmentNewsList : ABaseFragment<FragmentNewsListBinding>() {

    private val logger = DefaultAppLogger.getInstance()

    override val bindingInitializer: (LayoutInflater) -> ViewBinding = FragmentNewsListBinding::inflate

    private val newsViewModel: NewsViewModel by viewModels()

    private val adapterNewsArticles = AdapterNewsArticles()

    private var buttonsByCategory: HashMap<String, View>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initNewsCategoryButtons()
        setObservers()
        initNewsRecyclerView()
        initGoToTopDestinationAction()
        initSwipeRefreshLayout()
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

            val linearLayoutManager = LinearLayoutManager(context)
            layoutManager = linearLayoutManager

            adapter = adapterNewsArticles

            val spacingDecoration = SpacingItemDecoration
                .linear()
                .setSpacingRes(
                    R.dimen.dp_4,
                    R.dimen.dp_4,
                    R.dimen.dp_4,
                    R.dimen.dp_4
                )
                .create(context)
            setItemDecoration(spacingDecoration)

            adapterNewsArticles.addLoadStateListener { combinedLoadStates ->
                logger.i(Message.message {
                    clazz = this@FragmentNewsList::class.java
                    message = "News load state: $combinedLoadStates"
                })

                if (combinedLoadStates.source.refresh is LoadState.Loading) {
                    binding?.clpbWaiting?.show()
                } else {
                    binding?.clpbWaiting?.hide()
                    binding?.srlRefreshNews?.isRefreshing = false
                }

                val error = combinedLoadStates.source.append as? LoadState.Error
                    ?: combinedLoadStates.source.prepend as? LoadState.Error
                    ?: combinedLoadStates.source.refresh as? LoadState.Error
                    ?: combinedLoadStates.append as? LoadState.Error
                    ?: combinedLoadStates.prepend as? LoadState.Error
                    ?: combinedLoadStates.refresh as? LoadState.Error

                if (error != null) {
                    // TODO on error occurred
                }
            }

            val clickListener = RecyclerViewItemClickListener(
                context,
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

    private fun onLocaleChanged(locale: String) {
        newsViewModel.refreshNews(newsLocale = locale)
    }

    private fun initGoToTopDestinationAction() {
        mainNavigationViewModel.backToGraphRootEvent
            .flowWithLifecycle(viewLifecycleOwner.lifecycle)
            .onEach { binding?.rvNews?.smoothScrollToPosition(0) }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initSwipeRefreshLayout() {
        binding?.srlRefreshNews?.run {
            setColorSchemeResources(R.color.blue, R.color.yellow, R.color.red)
            setOnRefreshListener { adapterNewsArticles.refresh() }
        }
    }

    private fun initNewsCategoryButtons() {
        binding?.run {
            buttonsByCategory = hashMapOf(
                NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE to btnNewsCategoryAll
            )
        }
    }

    private fun setObservers() {
        newsViewModel.news.observe(viewLifecycleOwner) { pagingData ->
            adapterNewsArticles.submitData(viewLifecycleOwner.lifecycle, pagingData)
        }

        newsViewModel.newsCategoryValue.observe(viewLifecycleOwner) { categoryValue ->
            when (categoryValue) {
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_ALL_VALUE -> {

                }
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_ESPORTS_VALUE -> {}
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_GAME_UPDATES_VALUE -> {}
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_COMMUNITY_VALUE -> {}
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_PATCH_NOTES_VALUE -> {}
                NewsCategory.NEWS_CATEGORIES_FILTER_PARAM_STORE_VALUE -> {}
            }
        }
    }
}