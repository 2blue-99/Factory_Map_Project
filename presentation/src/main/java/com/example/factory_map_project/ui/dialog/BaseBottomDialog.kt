package com.example.factory_map_project.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.example.factory_map_project.BR
import com.example.factory_map_project.databinding.BottomSheetDialogBinding
import com.example.factory_map_project.ui.base.BaseViewModel
import com.example.factory_map_project.ui.base.Inflate
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomDialog<VB: ViewDataBinding, VM: BaseViewModel>(
    private val inflate: Inflate<VB>
): BottomSheetDialogFragment() {

    /**
     * View Model
     */
    protected abstract val viewModel: VM

    /**
     * 뷰 데이터 바인딩
     */
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

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
    /**
     * onViewCreated() | Listener Setting
     */
    abstract fun setListener()

    /**
     *
     */
    abstract fun onClickNegative(): View.OnClickListener
    abstract fun onClickPositive(): View.OnClickListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetBehavior = dialog.behavior
        bottomSheetBehavior.apply { skipCollapsed = true }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        setData()
//        bottomSheetBehavior.bottomSheetCallBack
        return super.onCreateDialog(savedInstanceState)
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        _binding.apply {
            binding.setVariable(BR.viewModel, viewModel)
            binding.lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}