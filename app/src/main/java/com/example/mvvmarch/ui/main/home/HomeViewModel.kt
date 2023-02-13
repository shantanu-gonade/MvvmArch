package com.example.mvvmarch.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mvvmarch.arch.base.viewmodel.BaseViewModel
import com.example.mvvmarch.entity.ReceivedEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
@SuppressWarnings("checkResult")
class HomeViewModel @Inject constructor(
        repository: HomeRepository
) : BaseViewModel() {

    val eventListLiveData: LiveData<PagingData<ReceivedEvent>> =
            repository.fetchPager().flow.cachedIn(viewModelScope).asLiveData()
}
