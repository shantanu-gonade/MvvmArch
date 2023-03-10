package com.example.mvvmarch.ui.search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.mvvmarch.arch.base.viewmodel.BaseViewModel
import com.example.mvvmarch.entity.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
        private val repository: SearchRepository
) : BaseViewModel() {

    private val mSearchKeyLiveData = MutableLiveData<String>(SEARCH_KEY_DEFAULT)

    val repoListLiveData: LiveData<PagingData<Repo>> =
            mSearchKeyLiveData.asFlow().flatMapLatest { repository.fetchPager(it).flow.cachedIn(viewModelScope) }.asLiveData()

    fun search(keyWord: String?) {
        if (!keyWord.isNullOrEmpty()) {
            mSearchKeyLiveData.postValue(keyWord)
        }
    }

    companion object {
        private const val SEARCH_KEY_DEFAULT = "kotlin"
    }
}
