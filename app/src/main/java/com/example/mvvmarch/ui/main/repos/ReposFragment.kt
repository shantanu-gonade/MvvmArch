package com.example.mvvmarch.ui.main.repos

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.paging.LoadState
import com.example.mvvmarch.R
import com.example.mvvmarch.arch.base.view.fragment.BaseFragment
import com.example.mvvmarch.arch.ext.jumpBrowser
import com.example.mvvmarch.arch.ext.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReposFragment : BaseFragment() {

    private val mViewModel: ReposViewModel by viewModels()

    override val layoutId: Int = R.layout.fragment_repos

    private val mAdapter = ReposPagedAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu_repos_filter_type)

        mRecyclerView.adapter = mAdapter
        mRecyclerView.removeAllAnimation()

        binds()
    }

    private fun binds() {
        // swipe refresh event.
        mSwipeRefreshLayout.setOnRefreshListener {
            mAdapter.refresh()
        }

        // when button was clicked, scrolling list to top.
        fabTop.setOnClickListener {
            mRecyclerView.scrollToPosition(0)
        }

        // menu item clicked event.
        toolbar.setOnMenuItemClickListener {
            onMenuSelected(it)
            true
        }

        // list item clicked event.
        observe(mAdapter.getItemClickEvent(), requireActivity()::jumpBrowser)

        observe(mAdapter.loadStateFlow.asLiveData()) { loadStates ->
            mSwipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
        }

        observe(mViewModel.pagedListLiveData) {
            mAdapter.submitData(lifecycle, it)
            mRecyclerView.scrollToPosition(0)
        }
    }

    private fun onMenuSelected(menuItem: MenuItem) {
        val isKeyUpdated = mViewModel.setSortKey(
            when (menuItem.itemId) {
                R.id.menu_repos_letter -> ReposViewModel.sortByLetter
                R.id.menu_repos_update -> ReposViewModel.sortByUpdate
                R.id.menu_repos_created -> ReposViewModel.sortByCreated
                else -> throw IllegalArgumentException("failure menuItem id.")
            }
        )
        if (isKeyUpdated)
            mAdapter.refresh()
    }
}
