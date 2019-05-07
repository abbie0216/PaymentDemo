package com.idhub.shop.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.view.View
import com.idhub.shop.R
import com.idhub.shop.util.SPUtil
import com.idhub.shop.util.SPUtil.Companion.ADDRESS
import com.idhub.shop.util.SPUtil.Companion.EMAIL
import com.idhub.shop.util.SPUtil.Companion.MOBILE
import com.idhub.shop.util.SPUtil.Companion.NAME
import kotlinx.android.synthetic.main.dlg_update_info.*

class UpdateInfoDialog(context: Context, var spName: String, var dlgCallback: DialogCallback) :
    Dialog(context),
    View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_update_info)

        setCancelable(true)

        when (spName) {
            NAME -> {
                tvTitle.text = "Update Name"
                etInputLayout.hint = "Name"
                etInput.inputType = InputType.TYPE_CLASS_TEXT
                etInput.maxLines = 1
                etInput.setSingleLine(true)
            }
            MOBILE -> {
                tvTitle.text = "Update Mobile"
                etInputLayout.hint = "Mobile"
                etInput.inputType = InputType.TYPE_CLASS_PHONE
                etInput.maxLines = 1
                etInput.setSingleLine(true)
            }
            EMAIL -> {
                tvTitle.text = "Update Email"
                etInputLayout.hint = "Email"
                etInput.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                etInput.maxLines = 1
                etInput.setSingleLine(true)
            }
            ADDRESS -> {
                tvTitle.text = "Update Address"
                etInputLayout.hint = "Address"
                etInput.inputType = InputType.TYPE_CLASS_TEXT
                etInput.maxLines = 3
            }
        }

        btnCancel.setOnClickListener(this)
        btnUpdate.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnCancel -> {
                dismiss()
            }
            R.id.btnUpdate -> {
                val value = etInput.text.toString()
                if (value == "") {
                    etInput.error = "Wrong input!"
                } else {
                    SPUtil(context).putString(spName, value)
                    dlgCallback.onDone()
                    dismiss()
                }
            }
        }
    }

    interface DialogCallback {
        fun onDone()
    }
}