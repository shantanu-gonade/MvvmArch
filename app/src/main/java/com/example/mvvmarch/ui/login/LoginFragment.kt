package com.example.mvvmarch.ui.login

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.viewModels
import com.example.mvvmarch.R
import com.example.mvvmarch.arch.base.view.fragment.BaseFragment
import com.example.mvvmarch.arch.ext.observe
import com.example.mvvmarch.http.Errors
import com.example.mvvmarch.ui.MainActivity
import com.example.mvvmarch.utils.toast
import dagger.hilt.android.AndroidEntryPoint
import retrofit2.HttpException

@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_login

    private val mViewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binds()
    }

    private fun binds() {
        mBtnSignIn.setOnClickListener {
            mViewModel.login(tvUsername.text.toString(), tvPassword.text.toString())
        }

        observe(mViewModel.stateLiveData, this::onNewState)
        observe(mViewModel.autoLoginLiveData, this::onAutoLogin)
    }

    private fun onAutoLogin(autoLoginEvent: AutoLoginEvent) {
        if (autoLoginEvent.autoLogin) {
            tvUsername.setText(autoLoginEvent.username, TextView.BufferType.EDITABLE)
            tvPassword.setText(autoLoginEvent.password, TextView.BufferType.EDITABLE)

            mViewModel.login(autoLoginEvent.username, autoLoginEvent.password)
        }
    }

    private fun onNewState(state: LoginViewState) {
        if (state.throwable != null) {
            when (state.throwable) {
                is Errors.EmptyInputError -> "username or password can't be null."
                is HttpException ->
                    when (state.throwable.code()) {
                        401 -> "username or password failure."
                        else -> "network failure"
                    }
                else -> "Github Api"
            }.also { str ->
                toast(str)
            }
        }

        mProgressBar.visibility = if (state.isLoading) View.VISIBLE else View.GONE

        if (state.loginInfo != null) {
            MainActivity.launch(requireActivity())
        }
    }
}
