package com.self.base

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

open class BaseViewModel : ViewModel() {

    val showLoading by lazy { UnPeekLiveData<String>() }

    val dismissLoading by lazy { UnPeekLiveData<Boolean>() }

}