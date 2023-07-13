package com.njj.sdkdemo.adapter

import com.njj.sdkdemo.BR
import com.njj.sdkdemo.R
import com.njj.sdkdemo.base.BaseAdapter
import com.njj.sdkdemo.databinding.ItemDeviceBinding
import com.soar.libraryble.entity.ABLEDevice

/**
 * NAME：YONG_
 * Created at: 2023/3/29 13
 * Describe:
 */
class DeviceAdapter : BaseAdapter<ABLEDevice, ItemDeviceBinding>(){
    override fun initLayoutId(): Int {
        return R.layout.item_device
    }

    override fun onBindView(holder: RecyclerHolder<ItemDeviceBinding>?, t: ABLEDevice?, position: Int) {
        holder!!.binding.setVariable(BR.item, t)
    }
}