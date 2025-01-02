package com.example.factory_map_project.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import timber.log.Timber
import com.example.factory_map_project.BR
import com.example.factory_map_project.ui.MainActivity


/**
 * Fragment binding inflate
 */
typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB: ViewDataBinding, VM:BaseViewModel>(
    private val inflate: Inflate<VB>
): Fragment() {

    /**
     * View Model
     */
    protected abstract val viewModel: VM
    /**
     * 뷰 데이터 바인딩
     */
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    /**
     * Main Activity 접근
     */
    protected val mainActivity = activity as MainActivity


    /**
     * onCreate() | Data Setting
     */
    abstract fun setData()
    /**
     * onViewCreated() | Data Setting
     */
    abstract fun setUI()
    /**
     * onViewCreated() | Observer Setting
     */
    abstract fun setObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate ${this::class.simpleName}")
        super.onCreate(savedInstanceState)
        setData()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Timber.i("onCreateView ${this::class.simpleName}")
        _binding = inflate.invoke(inflater, container, false)
        _binding.apply {
            binding.setVariable(BR.viewModel, viewModel)
            binding.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Timber.i("onViewCreated ${this::class.simpleName}")
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setObserver()
    }

    override fun onStart() {
        Timber.i("onStart ${this::class.simpleName}")
        super.onStart()
    }

    override fun onResume() {
        Timber.i("onResume ${this::class.simpleName}")
        super.onResume()
    }

    override fun onPause() {
        Timber.i("onPause ${this::class.simpleName}")
        super.onPause()
    }

    override fun onStop() {
        Timber.i("onStop ${this::class.simpleName}")
        super.onStop()
    }

    override fun onDestroyView() {
        Timber.i("onDestroyView ${this::class.simpleName}")
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        Timber.i("onDestroy ${this::class.simpleName}")
        super.onDestroy()
    }
}