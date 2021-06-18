package com.incode.instagallery.ui.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.incode.instagallery.databinding.ActivityDetailsBinding
import com.incode.instagallery.domain.data.Feed
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding

    private val viewModel: DetailsViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!intent.hasExtra(EXTRA_FEED_ID)) throw IllegalStateException("You must pass a feed ID")
        val feedId = intent.getStringExtra(EXTRA_FEED_ID)
            ?: throw IllegalArgumentException("Feed ID must not be null!!")

        subscribeToObservables()
        viewModel.loadFeed(feedId)
    }

    private fun subscribeToObservables() {
        viewModel.feedLiveData.observe(this, { feed ->
            if (feed != null) {
                populateUI(feed)
            }
        })
    }

    private fun populateUI(feed: Feed) {
        title = feed.title
        binding.textViewTitle.text = feed.title
        binding.textViewDescription.text = feed.comment

        if (feed.title.isNullOrEmpty() && feed.comment.isNullOrEmpty()) {
            binding.textContainer.visibility = View.GONE
        }

        val uriToLoad = if (feed.isFromNetwork) {
            Uri.parse(feed.pictureUri)
        } else {
            Uri.parse(feed.pictureUri).path
        }

        Glide.with(this)
            .load(uriToLoad)
            .fitCenter()
            .apply(RequestOptions.signatureOf(ObjectKey(feed.id)))
            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
            .into(binding.imageViewPhoto)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {

        private const val EXTRA_FEED_ID = "extra_feed_ID"

        fun start(context: Context, feedId: String) {
            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_FEED_ID, feedId)
            })
        }
    }
}