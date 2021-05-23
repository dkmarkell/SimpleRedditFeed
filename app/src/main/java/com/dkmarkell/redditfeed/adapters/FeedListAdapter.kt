package com.dkmarkell.redditfeed.adapters


import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.ListPreloader
import com.bumptech.glide.RequestBuilder
import com.dkmarkell.redditfeed.R
import com.dkmarkell.redditfeed.databinding.FeedListItemBinding
import com.dkmarkell.redditfeed.domain.FeedItem
import java.util.*


class FeedListAdapter(
        private val preloadRequestBuilder: RequestBuilder<Bitmap>,
        itemClickListener: (item: FeedListItemClickType) -> Unit) :
    ListAdapter<FeedItem, FeedListAdapter.ViewHolder>(FeedItemDiffCallback()),
    ListPreloader.PreloadModelProvider<FeedItem> {

    private val categoryItemClickListener = FeedListItemClickListener(itemClickListener)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val categoryItem = getItem(position)
        holder.bind(categoryItem, categoryItemClickListener, preloadRequestBuilder)
    }

    class ViewHolder private constructor(private val binding: FeedListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedItem,
                 itemClickListener: FeedListItemClickListener,
                 preloadRequestBuilder: RequestBuilder<Bitmap>) {
            binding.feedListItemCard.setOnClickListener { itemClickListener.onEnterClick(item) }
            binding.feedListItemPinButton.setOnClickListener { itemClickListener.onPinClick(item) }
            binding.feedListItemTitle.text = item.title
            binding.feedListItemCategory.text = item.subreddit
            binding.feedListItemDateText.text = item.dateFormatted
            binding.feedListItemPinButton.isChecked = item.pinned
            preloadRequestBuilder.load(item.thumbnail).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_image_24).into(binding.feedListItemImage)
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FeedListItemBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }

    override fun getPreloadItems(position: Int): List<FeedItem> {
        val item = currentList[position]
        return if (item.url.isEmpty()) {
            Collections.emptyList()
        } else Collections.singletonList(item)
    }

    override fun getPreloadRequestBuilder(item: FeedItem): RequestBuilder<*>? {
        if(item.url.isNotEmpty()) {
            return preloadRequestBuilder.load(item.url).placeholder(R.drawable.ic_baseline_image_24).error(R.drawable.ic_baseline_image_24)
        }

        return null;
    }
}

class FeedItemDiffCallback : DiffUtil.ItemCallback<FeedItem>() {
    override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem): Boolean {
        return oldItem == newItem
    }
}

/**
 * Listener class to handle the different item clicks within FeedItem
 */
class FeedListItemClickListener(val clickListener: (item: FeedListItemClickType) -> Unit) {
    fun onPinClick(item: FeedItem) = clickListener(FeedListItemClickType.PinItem(item))
    fun onEnterClick(item: FeedItem) = clickListener(FeedListItemClickType.EnterItem(item))
}

/**
 * ClickType for the listener to use to distinguish between different click events
 */
sealed class FeedListItemClickType {
    data class PinItem(val item: FeedItem) : FeedListItemClickType()
    data class EnterItem(val item: FeedItem) : FeedListItemClickType()
}