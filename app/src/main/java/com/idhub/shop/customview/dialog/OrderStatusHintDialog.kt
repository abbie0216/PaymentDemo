package com.idhub.shop.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.idhub.shop.R
import kotlinx.android.synthetic.main.dlg_app_info.*

class OrderStatusHintDialog(context: Context) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_order_status_hint)

        setCancelable(true)

        clDialog.setOnClickListener {
            dismiss()
        }
    }

}