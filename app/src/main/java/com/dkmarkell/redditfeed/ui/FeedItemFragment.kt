package com.dkmarkell.redditfeed.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.dkmarkell.redditfeed.R
import com.dkmarkell.redditfeed.databinding.FragmentFeedItemBinding
import com.dkmarkell.redditfeed.viewmodels.FeedItemViewModel
import com.dkmarkell.redditfeed.viewmodels.FeedItemViewModelFactory

class FeedItemFragment : Fragment() {

    private lateinit var binding: FragmentFeedItemBinding
    private val args: FeedItemFragmentArgs by navArgs()
    private val viewModel: FeedItemViewModel by viewModels { FeedItemViewModelFactory(args.FeedItemArgs) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_feed_item, container, false
        )

        /**
         * Handle back navigation
         */
        binding.toolbarFeedItem.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        /**
         * Setup observers for view components
         */
        setupObservers()

        binding.lifecycleOwner = this

        return binding.root
    }

    private fun setupObservers() {
        viewModel.subreddit.observe(viewLifecycleOwner ) {
            it?.let {
                binding.toolbarFeedItem.title = it
            }
        }

        viewModel.title.observe(viewLifecycleOwner ) {
            it?.let {
                binding.feedItemTitle.text = it
            }
        }

        viewModel.author.observe(viewLifecycleOwner ) {
            it?.let {
                binding.feedItemAuthorText.text = it
            }
        }

        viewModel.date.observe(viewLifecycleOwner ) {
            it?.let {
                binding.feedItemDate.text = it
            }
        }

        viewModel.pinned.observe(viewLifecycleOwner ) {
            it?.let {
                if(it) binding.feedItemPinIcon.visibility = View.VISIBLE
                else binding.feedItemPinIcon.visibility = View.GONE

            }
        }

        viewModel.imgUrl.observe(viewLifecycleOwner, ) {
            it?.let {
                /**
                 * If an image exists, load it using Glide, otherwise hide the view
                 */
                if(it.isEmpty()) {
                    binding.feedItemImage.visibility = View.GONE
                } else {
                    binding.feedItemImage.visibility = View.VISIBLE
                    Glide.with(this)
                        .asBitmap()
                        .load(it)
                        .placeholder(R.drawable.ic_baseline_image_24)
                        .error(R.drawable.ic_baseline_image_24)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .fitCenter()
                        .into(binding.feedItemImage)
                }
            }
        }
    }

}