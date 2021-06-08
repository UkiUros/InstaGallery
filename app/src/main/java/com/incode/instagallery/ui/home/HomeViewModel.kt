package com.incode.instagallery.ui.home

import androidx.lifecycle.*
import com.incode.instagallery.domain.DataState
import com.incode.instagallery.domain.data.Feed
import com.incode.instagallery.repository.FeedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val feedRepository: FeedRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    val feedDataState: LiveData<DataState<List<Feed>>>
        get() = mutableFeedDataState

    private val mutableFeedDataState: MutableLiveData<DataState<List<Feed>>> =
        MutableLiveData()

    @ExperimentalCoroutinesApi
    fun loadFeeds() {
        viewModelScope.launch(Dispatchers.IO) {
            feedRepository.getFeeds()
                .onEach { dataState ->
                    mutableFeedDataState.postValue(dataState)
                }
                .launchIn(this)
        }
    }
}
