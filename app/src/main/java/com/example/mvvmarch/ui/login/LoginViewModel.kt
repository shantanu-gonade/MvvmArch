package com.example.mvvmarch.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.mvvmarch.app.Results
import com.example.mvvmarch.arch.base.viewmodel.BaseViewModel
import com.example.mvvmarch.arch.ext.postNext
import com.example.mvvmarch.http.Errors
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO: Check if supress warning necessary?
@HiltViewModel
@SuppressWarnings("checkResult")
class LoginViewModel @Inject constructor(
    private val repo: LoginRepository
) : BaseViewModel() {

    private val _stateLiveData: MutableLiveData<LoginViewState> = MutableLiveData(LoginViewState.initial())
    private val autoLoginInfoFlow: Flow<AutoLoginEvent> = repo.fetchAutoLogin().take(1)

    val autoLoginLiveData = autoLoginInfoFlow.asLiveData()
    val stateLiveData: LiveData<LoginViewState> = _stateLiveData

    fun login(username: String?, password: String?) {
        when (username.isNullOrEmpty() || password.isNullOrEmpty()) {
            true -> _stateLiveData.postNext { state ->
                state.copy(isLoading = false, throwable = Errors.EmptyInputError, loginInfo = null)
            }
            false -> viewModelScope.launch {
                _stateLiveData.postNext {
                    it.copy(isLoading = true, throwable = null, loginInfo = null)
                }
                when (val result = repo.login(username, password)) {
                    is Results.Failure -> _stateLiveData.postNext {
                        it.copy(isLoading = false, throwable = result.error, loginInfo = null)
                    }
                    is Results.Success -> _stateLiveData.postNext {
                        it.copy(isLoading = false, throwable = null, loginInfo = result.data)
                    }
                }
            }
        }
    }
}
