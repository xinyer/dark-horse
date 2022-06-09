package com.android.architecture.travel.view

import com.android.architecture.R
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class OrderListAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.order_item) {

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.orderIdTextView, item)
    }
}
