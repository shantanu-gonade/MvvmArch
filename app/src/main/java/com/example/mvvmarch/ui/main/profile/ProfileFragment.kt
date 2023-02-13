package com.example.mvvmarch.ui.main.profile

import android.os.Bundle
import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mvvmarch.R
import com.example.mvvmarch.arch.base.view.fragment.BaseFragment
import com.example.mvvmarch.arch.ext.observe
import com.example.mvvmarch.utils.toast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : BaseFragment() {

    private val mViewModel: ProfileViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_profile

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binds()
    }

    private fun binds() {
        observe(mViewModel.viewStateLiveData, this::onNewState)
        mBtnEdit.setOnClickListener { toast("coming soon...") }
    }

    private fun onNewState(state: ProfileViewState) {
        if (state.throwable != null) {
            // handle throwable.
        }

        if (state.userInfo != null) {
            Glide.with(requireContext())
                .load(state.userInfo.avatarUrl)
                .apply(RequestOptions().circleCrop())
                .into(mIvAvatar)

            mTvNickname.text = state.userInfo.name
            mTvBio.text = state.userInfo.bio
            mTvLocation.text = state.userInfo.location
        }
    }
}
