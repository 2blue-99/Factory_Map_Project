package com.example.factory_map_project.ui.base

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.example.factory_map_project.BR
import com.example.factory_map_project.ui.MainActivity

abstract class BaseDialog<VB: ViewDataBinding, VM: BaseViewModel>(
    private val inflate: Inflate<VB>
): DialogFragment() {

    /**
     * View Model
     */
    protected abstract val viewModel: VM

    /**
     * 뷰 데이터 바인딩
     */
    private var _binding: VB? = null
    protected val binding get() = _binding!!

    protected fun mainActivity() = activity as MainActivity
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
     *
     */
    abstract fun onClickNegative()
    abstract fun onClickPositive()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val root = RelativeLayout(requireActivity())
        root.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val dialog = Dialog(requireContext())
        dialog.setContentView(root)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        return dialog
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
        setData()
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
}