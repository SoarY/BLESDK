package com.soar.libraryble.callback

import com.soar.libraryble.entity.ABLEDevice


interface SearchCallBack {

    fun onSearchStarted()

    fun onDeviceFounded(device: ABLEDevice)

    fun onSearchStopped()

    fun onSearchCanceled()
}