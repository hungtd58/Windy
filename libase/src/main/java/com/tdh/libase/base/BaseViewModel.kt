package com.tdh.libase.base

import android.content.DialogInterface
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tdh.libase.base.model.MessageDialogState

open class BaseViewModel : ViewModel() {
    val isLoading = MutableLiveData<Boolean>(false)
    val messageDialog = MutableLiveData<MessageDialogState>()

    fun showProgress() {
        isLoading.value = true
    }

    fun hideProgress() {
        isLoading.value = false
    }

    fun showMessage(title: String? = null, message: String?) {
        messageDialog.postValue(MessageDialogState(title, message))
    }

    fun showMessage(
        title: String? = null,
        message: String?,
        listener: DialogInterface.OnDismissListener? = null
    ) {
        messageDialog.postValue(
            MessageDialogState(
                title,
                message,
                null,
                null,
                null,
                null,
                null,
                null,
                listener
            )
        )
    }

    fun showMessage(
        title: String? = null,
        message: String?,
        positiveText: String? = null,
        positiveListener: DialogInterface.OnClickListener? = null,
        negativeText: String? = null,
        negativeListener: DialogInterface.OnClickListener? = null,
        neutralText: String? = null,
        neutralListener: DialogInterface.OnClickListener? = null,
    ) {
        messageDialog.postValue(
            MessageDialogState(
                title,
                message,
                positiveText,
                positiveListener,
                negativeText,
                negativeListener,
                neutralText,
                neutralListener
            )
        )
    }
}