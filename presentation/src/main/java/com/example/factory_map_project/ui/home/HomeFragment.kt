package com.example.factory_map_project.ui.home

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.FragmentHomeBinding
import com.example.factory_map_project.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>(
    FragmentHomeBinding::inflate
) {
    override val viewModel: HomeViewModel by viewModels()

    override fun setData() {

    }

    override fun setUI() {

    }

    override fun setObserver() {

    }

    override fun setListener() {

    }

}