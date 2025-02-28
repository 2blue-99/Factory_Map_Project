package com.example.factory_map_project.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.example.factory_map_project.BR
import com.example.factory_map_project.ui.MainViewModel
import com.example.factory_map_project.ui.bottomDialog.DownloadBottomDialog
import com.example.factory_map_project.ui.bottomDialog.FilterBottomDialog
import com.example.factory_map_project.ui.bottomDialog.MarkerBottomDialog
import com.example.factory_map_project.ui.dialog.InputDialog
import com.example.factory_map_project.ui.dialog.SpinnerDialog
import com.example.factory_map_project.util.map.FactoryCluster
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


abstract class BaseActivity<VB : ViewDataBinding, VM : BaseViewModel>(
    @LayoutRes private val layoutRes: Int
) : AppCompatActivity() {
    //**********************************************************************************************
    // Mark: Variable
    //**********************************************************************************************
    protected abstract val viewModel: VM
    lateinit var binding: VB

    private var backKeyPressedTime: Long? = 0L

    /**
     * onCreate() | Data Setting
     */
    abstract fun setData()
    /**
     * onCreate() | Observer Setting
     */
    abstract fun setObserver()
    /**
     * onCreate() | Listener Setting
     */
    abstract fun setListener()


    //**********************************************************************************************
    // Mark: Lifecycle
    //**********************************************************************************************
    override fun onCreate(savedInstanceState: Bundle?) {
        Timber.i("onCreate [${this::class.simpleName}]")
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.apply {
            binding.setVariable(BR.viewModel, viewModel)
            lifecycleOwner = this@BaseActivity
        }
        setData()
        setObserver()
        setListener()
    }

    override fun onRestart() {
        Timber.i("onRestart [${this::class.simpleName}]")
        super.onRestart()
    }

    override fun onStart() {
        Timber.i("onStart [${this::class.simpleName}]")
        super.onStart()
    }

    override fun onResume() {
        Timber.i("onResume [${this::class.simpleName}]")
        super.onResume()
    }

    override fun onPause() {
        Timber.i("onPause [${this::class.simpleName}]")
        super.onPause()
    }

    override fun onStop() {
        Timber.i("onStop [${this::class.simpleName}]")
        super.onStop()
    }

    override fun onDestroy() {
        Timber.i("onDestroy [${this::class.simpleName}]")
        super.onDestroy()
    }


    //**********************************************************************************************
    // Mark: Function
    //**********************************************************************************************
    fun setLoading(state: Boolean){
        if(viewModel is MainViewModel){
            viewModel.isLoading.value = state
        }
    }

    fun showToast(text: String){
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    fun showSnackBar(view: View, text: String){
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).apply {
            setTextColor(Color.WHITE)
        }.show()
    }

    fun showDownloadBottomDialog(){
        DownloadBottomDialog
            .newInstance()
            .show(supportFragmentManager, "download_bottom_dialog")
    }

    fun showMarkerBottomDialog(
        item: FactoryCluster,
        updateCluster: (FactoryCluster) -> Unit,
        deleteCluster: () -> Unit
    ){
        MarkerBottomDialog
            .newInstance(item, updateCluster, deleteCluster)
            .show(supportFragmentManager, "cluster_bottom_dialog")
    }

    fun showSpinnerDialog(list: List<String>, position: Int, onSelect: (Int) -> Unit){
        SpinnerDialog
            .newInstance(list, position, onSelect)
            .show(supportFragmentManager, "spinner_dialog")
    }

    fun showInputDialog(text: String, onSave: (String) -> Unit){
        InputDialog
            .newInstance(text, onSave)
            .show(supportFragmentManager, "input_dialog")
    }

    fun showFilterDialog(){
        FilterBottomDialog
            .newInstance()
            .show(supportFragmentManager, "input_dialog")
    }

    fun onBackPressedFinish() {
        val pressedTime = backKeyPressedTime?.plus(2000) ?: 0
        if (System.currentTimeMillis() > pressedTime) {
            Toast.makeText(this, "‘뒤로’버튼을 한번 더 누르시면 종료돼요", Toast.LENGTH_SHORT).show()
            backKeyPressedTime = System.currentTimeMillis()
        } else {
            this.finish()
        }
    }
}