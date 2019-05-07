package com.idhub.shop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.model.Order
import com.idhub.shop.util.Constant
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_order_result.*
import kotlinx.android.synthetic.main.item_record.view.*

class OrderResultFragment : BaseFragment(), View.OnClickListener {
    private lateinit var order: Order
    private var isDone: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_order, TopBarBtnType.HOME, null)

        order = arguments!!.getParcelable(Constant.ORDER)!!
        isDone = arguments!!.getBoolean(Constant.PAY_RESULT)

        ivStatus.isSelected = isDone
        if (isDone) {
            tvStatus.text = "Success!"
            llOrder.llBottom.visibility = VISIBLE
            llOrder.tvOrderId.text = "Order#1932011000${order.id}"
            llOrder.tvProductName.text = order.product.name
            llOrder.tvTotalAmount.text = "Total Amount: ${order.product.price}"
            llOrder.tvRecipientName.text = order.recipientName
            llOrder.tvRecipientMobile.text = order.recipientMobile
            llOrder.tvRecipientAddress.text = order.recipientAddress
            tvErrorMsg.visibility = GONE
            btnRetry.visibility = GONE
        } else {
            tvStatus.text = "Failure!"
            llOrder.visibility = GONE
            tvErrorMsg.visibility = VISIBLE
            btnRetry.visibility = VISIBLE
            btnRetry.setOnClickListener(this)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnRetry -> {
                //TODO transaction retry
            }
        }
    }

}