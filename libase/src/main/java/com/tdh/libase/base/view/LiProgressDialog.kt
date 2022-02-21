package com.tdh.libase.base.view

import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tdh.libase.R
import com.tdh.libase.databinding.FragmentDialogLiProgressBinding
import isNothing
import kotlin.math.roundToInt


class LiProgressDialog() : DialogFragment() {

    private var _binding: FragmentDialogLiProgressBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var message: String? = null

    constructor(title: String?, message: String?, cancelable: Boolean) : this() {
        val args = Bundle()
        args.putString("title", title)
        args.putString("message", message)

        isCancelable = cancelable
        arguments = args
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        context?.theme?.applyStyle(R.style.AppTheme, true)
        _binding = FragmentDialogLiProgressBinding.inflate(inflater, container, false)

        title = arguments?.getString("title")
        message = arguments?.getString("message")

        if (title.isNothing()) {
            binding.titleTv.visibility = View.GONE
        } else {
            binding.titleTv.visibility = View.VISIBLE
            binding.titleTv.text = title
        }

        if (message.isNothing()) {
            binding.contentTv.text = getString(R.string.loading)
        } else {
            binding.contentTv.text = message
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = Resources.getSystem().displayMetrics.widthPixels
        dialog?.window?.setLayout((width * 0.95f).roundToInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
