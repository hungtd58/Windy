package com.tdh.libase.base

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.tdh.libase.base.view.LiProgressDialog

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
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

    abstract fun observeViewModel()

    abstract fun setupView()

    fun showProgress(title: String? = null, message: String? = "Loading") {
        if (activity == null || !isResumed) return
        val transaction = childFragmentManager.beginTransaction()
        val progressDialog = LiProgressDialog(title, message, false)
        transaction.add(progressDialog, LiProgressDialog::class.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun hideProgress() {
        if (activity == null || !isResumed) return
        childFragmentManager.findFragmentByTag(LiProgressDialog::class.simpleName).let {
            (it as LiProgressDialog).dismissAllowingStateLoss()
        }
    }

    fun showMessage(title: String? = null, message: String?) {
        if (activity == null || !isResumed) return
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .show()
        }
    }

    fun showMessage(
        title: String? = null,
        message: String?,
        listener: DialogInterface.OnDismissListener? = null
    ) {
        if (activity == null || !isResumed) return
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setOnDismissListener(listener)
                .show()
        }
    }

    fun showMessage(
        title: String? = null,
        message: String?,
        listener: DialogInterface.OnCancelListener? = null
    ) {
        if (activity == null || !isResumed) return
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setOnCancelListener(listener)
                .show()
        }
    }

    fun showMessage(
        title: String? = null,
        message: String?,
        positiveText: String? = null,
        positiveListener: DialogInterface.OnClickListener? = null,
        negativeText: String? = null,
        negativeListener: DialogInterface.OnClickListener? = null,
        neutralBtn: String? = null,
        neutralListener: DialogInterface.OnClickListener? = null,
    ) {
        if (activity == null || !isResumed) return
        context?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(true)
                .setPositiveButton(positiveText, positiveListener)
                .setNegativeButton(negativeText, negativeListener)
                .setNeutralButton(neutralBtn, neutralListener)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}