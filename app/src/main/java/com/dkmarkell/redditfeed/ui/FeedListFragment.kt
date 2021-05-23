package com.dkmarkell.redditfeed.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.util.ViewPreloadSizeProvider
import com.dkmarkell.redditfeed.R
import com.dkmarkell.redditfeed.adapters.FeedListAdapter
import com.dkmarkell.redditfeed.adapters.FeedListItemClickType
import com.dkmarkell.redditfeed.databinding.FragmentFeedListBinding
import com.dkmarkell.redditfeed.domain.FeedItem
import com.dkmarkell.redditfeed.viewmodels.FeedListViewModel


class FeedListFragment : Fragment() {

    private lateinit var binding: FragmentFeedListBinding
    private val viewModel: FeedListViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private var tabIndex = 0

    companion object {
        private const val ARG_INDEX = "com.dkmarkell.redditfeed.ui.INDEX_ARGUMENT"

        @JvmStatic
        fun newInstance(tabIndex: Int) =
            FeedListFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_INDEX, tabIndex)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed_list, container, false
        )

        /**
         * Argument will be tab number of which tab was selected from the tabview
         */
        val bundle = arguments
        bundle?.let { tabIndex = it.getInt(ARG_INDEX) }

        setupViews()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setupViews() {
        /**
         * Create a request builder for the recyclerview adapter
         *
         * This will be used to load the thumbnail images
         */
        val preloadRequestBuilder: RequestBuilder<Bitmap> =
            Glide.with(this)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .centerCrop()

        val preloadSizeProvider = ViewPreloadSizeProvider<FeedItem>()

        /**
         * Create the list adapter and pass in the listener callback to be used
         * when an item is selected or pinned
         */
        val listAdapter = FeedListAdapter(preloadRequestBuilder) {
            when(it) {
                is FeedListItemClickType.PinItem -> {
                    viewModel.onFeedItemPinClick(it.item)
                }
                is FeedListItemClickType.EnterItem -> {
                    viewModel.onFeedItemClick(it.item)
                }
            }
        }

        /**
         * Glide preloader to help with loading thumbails into the recyclerview items
         */
        val preloader: RecyclerViewPreloader<FeedItem> = RecyclerViewPreloader(
            Glide.with(this),
            listAdapter,
            preloadSizeProvider,
            10)

        binding.feedListRecyclerView.addOnScrollListener(preloader)

        binding.feedListRecyclerView.adapter = listAdapter

        /**
         * Depending on which tab is selected, set the appropriate list to the the adapter
         */
        if(tabIndex == 0) {
            viewModel.feedList.observe(viewLifecycleOwner, {
                it?.let {
                    listAdapter.submitList(it)
                    binding.feedListSwipeLayout.isRefreshing = false
                }
            })
        }
        else {
            viewModel.pinnedList.observe(viewLifecycleOwner, {
                it?.let {
                    listAdapter.submitList(it)
                }
            })
        }

        if(tabIndex == 0) {
            binding.feedListSwipeLayout.setOnRefreshListener {
                viewModel.onRefreshList()
            }
        } else {
            binding.feedListSwipeLayout.setOnRefreshListener {
                binding.feedListSwipeLayout.isRefreshing = false
            }
        }
    }
}