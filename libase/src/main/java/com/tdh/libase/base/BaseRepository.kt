package com.tdh.libase.base

import androidx.lifecycle.MutableLiveData

object BaseRepository {
    val isLoading = MutableLiveData(false)
    val message = MutableLiveData<Pair<String, String>>()
}