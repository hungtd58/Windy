package com.tdh.libase.base.model

import android.content.DialogInterface

data class MessageDialogState(
    val title: String? = null,
    val message: String?,
    val positiveText: String? = null,
    val positiveListener: DialogInterface.OnClickListener? = null,
    val negativeText: String? = null,
    val negativeListener: DialogInterface.OnClickListener? = null,
    val neutralText: String? = null,
    val neutralListener: DialogInterface.OnClickListener? = null,
    val dismissListener: DialogInterface.OnDismissListener? = null
)