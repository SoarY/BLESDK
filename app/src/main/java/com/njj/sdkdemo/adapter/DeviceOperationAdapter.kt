package com.njj.sdkdemo.adapter

import android.content.Context
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.njj.sdkdemo.R
import com.njj.sdkdemo.BR
import com.njj.sdkdemo.base.BaseAdapter
import com.njj.sdkdemo.bean.DeviceOperation
import com.njj.sdkdemo.bean.DeviceOperationBean
import com.njj.sdkdemo.databinding.ItemDeviceOperationBinding
import com.soar.cloud.util.CommonUtils
import com.soar.cloud.util.ToastUtils
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout

/**
 * NAME：YONG_
 * Created at: 2023/3/31 14
 * Describe:
 */
class DeviceOperationAdapter : BaseAdapter<DeviceOperationBean, ItemDeviceOperationBinding>(){

    private var onTagClickListener: OnTagClickListener?=null

    lateinit var context:Context

    override fun initLayoutId(): Int {
        return R.layout.item_device_operation
    }

    override fun onBindView(holder: RecyclerHolder<ItemDeviceOperationBinding>?, t: DeviceOperationBean?, position: Int) {
        context = holder!!.binding.root.context
        holder!!.binding.setVariable(BR.item, t)
        showTagView(holder!!.binding.tflContent, t!!.data)
    }

    private fun showTagView(tflContent: TagFlowLayout, articles: List<DeviceOperation>?) {
        tflContent.adapter=object : TagAdapter<DeviceOperation>(articles){
            override fun getView(parent: FlowLayout?, position: Int, t: DeviceOperation): View {
                val textView: TextView = getTextView()
                textView.text = Html.fromHtml(t.title)
                return textView
            }
        }

        tflContent.setOnTagClickListener{v:View,position:Int,parent:FlowLayout->
                onTagClickListener?.onTagClick(position,articles!![position])
            true
        }
    }

    fun interface OnTagClickListener {
        fun onTagClick(position:Int,bean:DeviceOperation)
    }

    fun setOnTagClickListener(onTagClickListener:OnTagClickListener) {
        this.onTagClickListener = onTagClickListener
    }

    private fun getTextView(): TextView {
        val hotText = TextView(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        hotText.layoutParams = lp
        hotText.textSize = 13f
        val left: Int
        val top: Int
        val right: Int
        val bottom: Int
        hotText.maxLines = 1
        bottom = CommonUtils.dip2px(5f)
        right = bottom
        top = right
        left = top
        hotText.setBackgroundResource(R.drawable.shape_navi_tag)
        hotText.gravity = Gravity.CENTER
        lp.setMargins(left, top, right, bottom)
        return hotText
    }
}