package com.idhub.shop.customview.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.widget.TextView
import com.idhub.shop.R
import com.idhub.shop.util.SPUtil
import com.idhub.shop.util.SPUtil.Companion.PWD
import kotlinx.android.synthetic.main.dlg_sign_in.*
import me.aflak.libraries.callback.FingerprintCallback
import me.aflak.libraries.view.Fingerprint

class SignInDialog(context: Context, var dlgCallback: DialogCallback) :
    Dialog(context, R.style.DialogTheme) {

    private lateinit var pwd: String

    override fun onCreate(savedInstanceState: Bundle?) {
        println("SignInDialog.onCreate")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dlg_sign_in)

        setCancelable(false)
        setCanceledOnTouchOutside(true)

        vFingerprint.callback(fingerprintCallback)
        btnSwitch.setOnClickListener(btnSwitchClickListener)
        etPassword.setOnEditorActionListener(onEditorActionListener)

        if (!Fingerprint.isAvailable(context)) {
            btnSwitch.text = context.getString(R.string.set_fingerprint_lock)
            tvLoginTitle.text = context.getString(R.string.use_manual_password_to_sign_in)
            vFingerprint.visibility = View.GONE
            tilPassword.visibility = View.VISIBLE
        } else {
            vFingerprint.authenticate()
        }

        pwd = SPUtil(context).getString(PWD)
    }

    private val fingerprintCallback = object : FingerprintCallback {
        override fun onAuthenticationSucceeded() {
            println("onAuthenticationSucceeded")
            tvErrorMsg.text = ""
            Handler().postDelayed({
                signInDone()
            }, 1500)
        }

        override fun onAuthenticationError(errorCode: Int, error: String?) {
            println("onAuthenticationError: $error($errorCode)")
        }

        override fun onAuthenticationFailed() {
            println("onAuthenticationFailed")
            tvErrorMsg.text = "Authentication failed"
        }
    }

    private val btnSwitchClickListener = View.OnClickListener {
        when (btnSwitch.text) {
            context.getString(R.string.use_manual_password) -> {
                vFingerprint.cancel()
                btnSwitch.text = context.getString(R.string.use_fingerprint_lock)
                tvLoginTitle.text = context.getString(R.string.use_manual_password_to_sign_in)
                vFingerprint.visibility = View.GONE
                tilPassword.visibility = View.VISIBLE
                etPassword.setText("")
                tvErrorMsg.text = ""
            }
            context.getString(R.string.use_fingerprint_lock) -> {
                vFingerprint.authenticate()
                btnSwitch.text = context.getString(R.string.use_manual_password)
                tvLoginTitle.text = context.getString(R.string.use_fingerprint_lock_to_sign_in)
                vFingerprint.visibility = View.VISIBLE
                tilPassword.visibility = View.GONE
                tvErrorMsg.text = ""
            }
            context.getString(R.string.set_fingerprint_lock) -> {
                tvErrorMsg.text = ""
                dlgCallback.onGoSetting()
            }
        }
    }

    private val onEditorActionListener = object : TextView.OnEditorActionListener {
        override fun onEditorAction(view: TextView?, actionId: Int, event: KeyEvent?): Boolean {
            if (pwd == etPassword.text.toString()) {
                signInDone()
            } else {
                tvErrorMsg.text = "Wrong password"
            }
            return true
        }
    }

    private fun signInDone() {
        dlgCallback.onDone()
        dismiss()
    }

    interface DialogCallback {
        fun onGoSetting()
        fun onDone()
    }
}