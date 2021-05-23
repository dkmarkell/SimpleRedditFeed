package com.dkmarkell.redditfeed.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dkmarkell.redditfeed.ui.FeedListFragment

class FeedListViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    companion object {
        private const val NUMBER_OF_TABS = 2
    }

    override fun getItemCount() = NUMBER_OF_TABS

    override fun createFragment(position: Int): Fragment {
        return FeedListFragment.newInstance(position)
    }
}