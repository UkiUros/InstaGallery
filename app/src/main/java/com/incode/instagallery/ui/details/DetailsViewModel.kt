package com.incode.instagallery.ui.details

import androidx.lifecycle.*
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val feedRepository: FeedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val feedLiveData: LiveData<Feed?>
        get() = mutableFeedLiveData

    private val mutableFeedLiveData: MutableLiveData<Feed?> =
        MutableLiveData()

    @ExperimentalCoroutinesApi
    fun loadFeed(feedId: String) {
        viewModelScope.launch {
            feedRepository.getFeedForId(feedId)
                .onEach { dataState ->
                    mutableFeedLiveData.postValue(dataState)
                }.launchIn(this)
        }
    }
}
