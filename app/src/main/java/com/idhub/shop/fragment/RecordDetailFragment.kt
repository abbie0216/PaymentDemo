package com.idhub.shop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.model.Order
import com.idhub.shop.util.Constant
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_order_result.*
import kotlinx.android.synthetic.main.item_record.view.*

class RecordDetailFragment : BaseFragment() {
    private lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_record_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_record, TopBarBtnType.BACK, TopBarBtnType.HINT)

        order = arguments!!.getParcelable(Constant.ORDER)!!

        llOrder.llBottom.visibility = VISIBLE
        llOrder.tvOrderId.text = "Order#1932011000${order.id}"
        llOrder.tvProductName.text = order.product.name
        llOrder.tvTotalAmount.text = "Total Amount: ${order.product.price}"
        llOrder.tvRecipientName.text = order.recipientName
        llOrder.tvRecipientMobile.text = order.recipientMobile
        llOrder.tvRecipientAddress.text = order.recipientAddress

        when (order.status) {
            Order.Status.PACKAGE_PROCESSING -> {
                llOrder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_1)
            }
            Order.Status.PACKAGE_TRANSPORTING -> {
                llOrder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_2)
            }
            Order.Status.PACKAGE_ARRIVED -> {
                llOrder.tvOrderId.setTextColor(context!!.getColor(R.color.pink_light_d372bd))
                llOrder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_3)
                llOrder.ivOrderStatus.setBackgroundResource(R.color.pink_light_d372bd)
                llOrder.ivStatusBg.setBackgroundResource(R.color.pink_light_d372bd)
                llOrder.tvProductName.setTextColor(context!!.getColor(R.color.pink_light_d372bd))
                llOrder.tvTotalAmount.setTextColor(context!!.getColor(R.color.gray_dark_626262))
            }
            Order.Status.PACKAGE_DONE -> {
                llOrder.tvOrderId.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.ivOrderStatus.setImageResource(R.drawable.ic_order_status_4)
                llOrder.ivOrderStatus.setBackgroundResource(R.color.gray_light_9e9e9e)
                llOrder.ivStatusBg.setBackgroundResource(R.color.gray_light_9e9e9e)
                llOrder.tvProductName.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.tvTotalAmount.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.tvRecipientInfo.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.tvRecipientName.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.tvRecipientMobile.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.tvRecipientAddress.setTextColor(context!!.getColor(R.color.gray_light_9e9e9e))
                llOrder.llTop.setBackgroundResource(R.color.gray_light_e0e0e0)
                llOrder.llBottom.setBackgroundResource(R.color.gray_light_e0e0e0)
                llOrder.setBackgroundResource(R.color.gray_light_e0e0e0)
            }
        }
    }
}