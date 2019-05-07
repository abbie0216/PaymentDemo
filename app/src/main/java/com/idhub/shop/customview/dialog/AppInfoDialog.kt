package com.idhub.shop.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.idhub.shop.R
import kotlinx.android.synthetic.main.dlg_app_info.*

class AppInfoDialog(context: Context) :
    Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_app_info)

        setCancelable(true)

        tvAppName.setText(R.string.app_name)
        tvAppVersion.text =
            "v${context.packageManager.getPackageInfo(context.packageName, 0).versionName}"
        clDialog.setOnClickListener {
            dismiss()
        }
    }
}