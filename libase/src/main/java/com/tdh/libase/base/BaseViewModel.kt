package com.tdh.libase.base

import androidx.lifecycle.ViewModel

open class BaseViewModel<T : BaseNavigator> : ViewModel() {
    protected var navigator: T? = null
}