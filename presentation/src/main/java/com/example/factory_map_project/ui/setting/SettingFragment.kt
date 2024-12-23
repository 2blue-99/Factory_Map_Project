package com.example.factory_map_project.ui.setting

import androidx.fragment.app.viewModels
import com.example.factory_map_project.databinding.FragmentSettingBinding
import com.example.factory_map_project.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<FragmentSettingBinding, SettingViewModel>(
    FragmentSettingBinding::inflate
) {
    override val viewModel: SettingViewModel by viewModels()

    override fun setData() {

    }

    override fun setUI() {

    }

    override fun setObserver() {

    }
}