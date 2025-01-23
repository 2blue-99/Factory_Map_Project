package com.example.factory_map_project.ui.base

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.databinding.ViewDataBinding
import com.example.factory_map_project.BR
import com.example.factory_map_project.ui.MainActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BaseBottomDialog<VB: ViewDataBinding, VM: BaseViewModel>(
    private val inflate: Inflate<VB>
): BottomSheetDialogFragment() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
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

    protected fun mainActivity() = activity as MainActivity

    /**
     * onCreateDialog()
     */
    lateinit var bottomDialog: BottomSheetDialog

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

    abstract fun onClickNegative()
    abstract fun onClickPositive()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        bottomDialog = BottomSheetDialog(requireContext(), theme)
        bottomSheetBehavior = bottomDialog.behavior
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior.apply { skipCollapsed = true }
        setData()
        return bottomDialog
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUI()
        setObserver()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
}