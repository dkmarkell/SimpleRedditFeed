package com.dkmarkell.redditfeed.viewmodels

import com.dkmarkell.redditfeed.domain.FeedItem
import androidx.lifecycle.*
import java.util.*

class FeedItemViewModel(private val feedItem: FeedItem) : ViewModel() {

    private val _subreddit = MutableLiveData(feedItem.subreddit)
    val subreddit: LiveData<String>
        get() = _subreddit

    private val _title = MutableLiveData(feedItem.title)
    val title: LiveData<String>
        get() = _title

    private val _author = MutableLiveData(feedItem.author)
    val author: LiveData<String>
        get() = _author

    private val _date = MutableLiveData(feedItem.dateFormatted)
    val date: LiveData<String>
        get() = _date

    private val _pinned = MutableLiveData(feedItem.pinned)
    val pinned: LiveData<Boolean>
        get() = _pinned

    /**
     * If the Item thumbnail contains a valid website and then check if the url is a jpg image
     * If it is, set the url as the imgUrl observable
     *
     * If its not an image, set the thumbnail as the imageUrl
     */
    private val _imgUrl = MutableLiveData(feedItem.thumbnail).map {
            val url = feedItem.url.toLowerCase(Locale.ROOT)
            if(it.contains("https")) {
                if((url.contains(".jpg") || url.contains(".jpeg"))) {
                    feedItem.url
                } else {
                    it
                }
            } else
                ""
        }
    val imgUrl: LiveData<String>
        get() = _imgUrl

}

@Suppress("UNCHECKED_CAST")
class FeedItemViewModelFactory(private val feedItem: FeedItem) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FeedItemViewModel(feedItem) as T
    }
}