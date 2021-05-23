package com.dkmarkell.redditfeed.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dkmarkell.redditfeed.domain.FeedItem
import com.dkmarkell.redditfeed.domain.toModel
import com.dkmarkell.redditfeed.repository.FeedRepository
import com.dkmarkell.redditfeed.room.FeedItemDatabase
import com.dkmarkell.redditfeed.util.toSingleEvent
import kotlinx.coroutines.launch

class FeedListViewModel(application: Application) : AndroidViewModel(application) {
    private val repo = FeedRepository(FeedItemDatabase.getInstance(application))

    private val _feedList: MutableLiveData<List<FeedItem>> = MutableLiveData(emptyList())
    val feedList: LiveData<List<FeedItem>>
        get() = _feedList

    val pinnedList = repo.pinnedList.map { it.map { entity -> entity.toModel() } }

    private val _navigateToFeedItem = MutableLiveData<FeedItem>()
    val navigateToFeedItem = _navigateToFeedItem.toSingleEvent()

    init {
        onRefreshList()
    }

    fun onRefreshList() {
        /**
         * Get the hot feed from the repo and sort by date descending
         */
        viewModelScope.launch {
            val list = repo.getFeed().sortedByDescending { it.created }
            _feedList.value = list
        }
    }


    fun onFeedItemClick(item: FeedItem) {
        /**
         * A feed item, was clicked set the clicked item to the navigate observable
         */
        _navigateToFeedItem.value = item
    }

    fun onFeedItemPinClick(item: FeedItem) {
        /**
         * If an item was pinned, add it to the database
         *
         * If it was unpinned, remove it from the database and refresh the list
         */
        viewModelScope.launch {
            item.pinned = !item.pinned
            if (item.pinned) {
                repo.addPinnedItem(item)
            } else {
                repo.deletePinnedItem(item)
                onRefreshList()
            }
        }

    }

}