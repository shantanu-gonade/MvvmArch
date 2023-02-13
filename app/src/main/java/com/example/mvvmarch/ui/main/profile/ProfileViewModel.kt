package com.example.mvvmarch.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mvvmarch.arch.base.viewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
        private val repo: ProfileRepository
) : BaseViewModel() {

    private val _viewStateLiveData: MutableLiveData<ProfileViewState> = MutableLiveData(ProfileViewState.initial())
    val viewStateLiveData: LiveData<ProfileViewState> = _viewStateLiveData
}