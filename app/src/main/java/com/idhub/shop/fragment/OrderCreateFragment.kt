package com.idhub.shop.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.model.Order
import com.idhub.shop.model.Product
import com.idhub.shop.util.Constant
import com.idhub.shop.util.SPUtil.Companion.ADDRESS
import com.idhub.shop.util.SPUtil.Companion.MOBILE
import com.idhub.shop.util.SPUtil.Companion.NAME
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_order.*

class OrderCreateFragment : BaseFragment(), View.OnClickListener {
    lateinit var product: Product
    lateinit var order: Order

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_order, TopBarBtnType.BACK, null)

        product = arguments!!.getParcelable(Constant.PRODUCT)!!
        tvProductName.text = product.name
        tvProductPrice.text = "Price: ${product.price} IDHUB"

        etRecipientName.setText(spUtil.getString(NAME))
        etRecipientMobile.setText(spUtil.getString(MOBILE))
        etRecipientAddress.setText(spUtil.getString(ADDRESS))

        btnPay.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnPay -> {
                var valid = true
                val name = etRecipientName.text.toString()
                val mobile = etRecipientMobile.text.toString()
                val address = etRecipientAddress.text.toString()
                if (name == "") {
                    etRecipientName.error = "Please fill in this field."
                    valid = false
                }
                if (mobile == "") {
                    etRecipientMobile.error = "Please fill in this field."
                    valid = false
                }
                if (address == "") {
                    etRecipientAddress.error = "Please fill in this field."
                    valid = false
                }

                if (valid) {
                    order = Order(
                        util.generateOrderId(),
                        product,
                        name,
                        mobile,
                        address,
                        Order.Status.PACKAGE_PROCESSING
                    )

                    //TODO transaction
                    val result = true

                    mainCallback.onPayResult(result, order)
                }
            }
        }
    }
}