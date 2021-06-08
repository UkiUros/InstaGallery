package com.incode.instagallery.ui.details

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.signature.ObjectKey
import com.incode.instagallery.databinding.ActivityDetailsBinding
import com.incode.instagallery.domain.data.Feed

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailsBinding
    private var feed: Feed? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!intent.hasExtra(EXTRA_FEED)) throw IllegalStateException("You must pass a feed")

        feed = intent.getParcelableExtra(EXTRA_FEED)
        feed?.let {
            populateUI(it)
        }

    }

    private fun populateUI(feed: Feed) {
        title = feed.title
        binding.textViewTitle.text = feed.title
        binding.textViewDescription.text = feed.comment

        if (feed.pictureUrl != null) {
            Glide.with(this)
                .load(feed.pictureUrl)
                .fitCenter()
//                .apply(RequestOptions.skipMemoryCacheOf(true))
                .apply(RequestOptions.signatureOf(ObjectKey(feed.publishedAt!!)))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.DATA))
                .into(binding.imageViewPhoto)
        }
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

        private const val EXTRA_FEED = "extra_feed"

        fun start(context: Context, feed: Feed) {
            context.startActivity(Intent(context, DetailsActivity::class.java).apply {
                putExtra(EXTRA_FEED, feed)
            })
        }
    }
}