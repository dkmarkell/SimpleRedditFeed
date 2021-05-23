package com.dkmarkell.redditfeed.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.dkmarkell.redditfeed.R
import com.dkmarkell.redditfeed.databinding.FragmentFeedListScreenBinding
import com.dkmarkell.redditfeed.adapters.FeedListViewPagerAdapter
import com.dkmarkell.redditfeed.viewmodels.FeedListViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator


class FeedListScreenFragment : Fragment() {

    private lateinit var binding: FragmentFeedListScreenBinding
    private val viewModel: FeedListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed_list_screen, container, false
        )

        setupViews()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setupViews() {
        val viewPager = binding.feedListPager
        viewPager.adapter = FeedListViewPagerAdapter(this)

        val tabLayout = binding.feedListScreenTabLayout

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = resources.getString(R.string.tab_option_hot)
                1 -> tab.text = resources.getString(R.string.tab_option_pinned)
            }
        }.attach()

        /**
         * Refresh the list when going back to the Hot tab
         */
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    if(tab.position == 0) {
                        viewModel.onRefreshList()
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        /**
         * Navigate to the FeedItem screen and pass the selected FeedItem object
         */
        viewModel.navigateToFeedItem.observe(viewLifecycleOwner, {
            it?.let {
                this.findNavController().navigate(
                    FeedListScreenFragmentDirections.actionFeedListToFeedItem(it)
                )
            }
        })
    }
}