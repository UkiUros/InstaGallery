package com.incode.instagallery.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.incode.instagallery.databinding.ActivityHomeBinding
import com.incode.instagallery.domain.DataState
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.ui.details.DetailsActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeViewModel by viewModels()

    private val adapter = FeedAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.swipeRefreshLayout.isEnabled = false // just using it as indicator

        binding.recyclerView.adapter = adapter
        adapter.onItemClickListener = object : FeedAdapter.OnItemClickListener {
            override fun onItemClicked(feed: Feed) {
                DetailsActivity.start(this@HomeActivity, feed)
            }
        }

        subscribeToObservables()
        viewModel.loadFeeds()

    }

    private fun subscribeToObservables() {
        viewModel.feedDataState.observe(this, {
            when (it) {
                is DataState.Error -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    Timber.e(it.throwable)
                }
                DataState.Loading -> {
                    binding.swipeRefreshLayout.isRefreshing = true
                }
                is DataState.Success -> {
                    binding.swipeRefreshLayout.isRefreshing = false
                    adapter.submitList(it.data)
                }
            }
        })
    }
}
