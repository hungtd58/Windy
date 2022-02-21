package com.tdh.libase.base

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.tdh.libase.base.view.LiProgressDialog
import kotlin.math.roundToInt

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observeViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        setupView()

        return binding.root
    }

    abstract fun getLayoutId(): Int

    abstract fun getWidth(): Float

    abstract fun getHeight(): Float

    abstract fun observeViewModel()

    abstract fun setupView()

    override fun onResume() {
        super.onResume()
        val width = (Resources.getSystem().displayMetrics.widthPixels * getWidth()).roundToInt()
        val height = (Resources.getSystem().displayMetrics.heightPixels * getHeight()).roundToInt()
        dialog?.window?.setLayout(width, height)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun showProgress(title: String?, message: String = "Loading") {
        val transaction = childFragmentManager.beginTransaction()
        val progressDialog = LiProgressDialog(title, message, false)
        transaction.add(progressDialog, LiProgressDialog::class.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun hideProgress() {
        childFragmentManager.findFragmentByTag(LiProgressDialog::class.simpleName).let {
            (it as LiProgressDialog).dismissAllowingStateLoss()
        }
    }
}