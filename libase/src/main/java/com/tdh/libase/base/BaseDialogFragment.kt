package com.tdh.libase.base

import android.content.DialogInterface
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.tdh.libase.base.view.LiProgressDialog
import kotlin.math.roundToInt

abstract class BaseDialogFragment<T : ViewDataBinding> : DialogFragment() {
    private var _binding: T? = null
    protected val binding get() = _binding!!

    private val viewModels: MutableList<BaseViewModel> = mutableListOf()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModels()
        observeBaseViewModel()
        observeViewModel()
        setupView()
        initData()
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

    abstract fun initViewModels()

    fun addViewModel(viewModel: BaseViewModel) {
        viewModels.add(viewModel)
    }

    abstract fun observeViewModel()

    private fun observeBaseViewModel() {
        for (viewModel in viewModels) {
            viewModel.isLoading.observe(viewLifecycleOwner) {
                if (it) showProgress() else hideProgress()
            }

            viewModel.messageDialog.observe(viewLifecycleOwner) {
                showMessage(
                    it.title,
                    it.message,
                    it.positiveText,
                    it.positiveListener,
                    it.negativeText,
                    it.negativeListener,
                    it.neutralText,
                    it.neutralListener,
                    it.dismissListener
                )
            }
        }
    }

    abstract fun setupView()

    abstract fun initData()

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

    fun showProgress(title: String? = null, message: String? = "Loading") {
        if (activity == null || !isResumed) return
        val transaction = childFragmentManager.beginTransaction()
        val progressDialog = LiProgressDialog(title, message, false)
        transaction.add(progressDialog, LiProgressDialog::class.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun hideProgress() {
        if (activity == null || !isResumed) return
        childFragmentManager.findFragmentByTag(LiProgressDialog::class.simpleName)?.let {
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
        listener: DialogInterface.OnDismissListener? = null
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
                .setOnDismissListener(listener)
                .show()
        }
    }
}