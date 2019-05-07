package com.idhub.shop.fragment

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.util.Constant.Companion.SIGN_UP_PAGE_TYPE
import com.idhub.shop.util.SPUtil
import com.idhub.shop.util.SPUtil.Companion.WALLET_PATH
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_sign_up_fingerprint.*
import kotlinx.android.synthetic.main.fragment_sign_up_form.*
import kotlinx.android.synthetic.main.fragment_sign_up_pwd.*
import me.aflak.libraries.view.Fingerprint

class SignUpFragment : BaseFragment() {

    private lateinit var type: SignUpPageType

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        type = arguments!!.getSerializable(SIGN_UP_PAGE_TYPE)!! as SignUpPageType
        val res: Int = when (type) {
            SignUpPageType.FILL_IN_INFO -> {
                R.layout.fragment_sign_up_form
            }
            SignUpPageType.SET_PASSWORD -> {
                R.layout.fragment_sign_up_pwd
            }
            SignUpPageType.SET_FINGERPRINT -> {
                R.layout.fragment_sign_up_fingerprint
            }
            SignUpPageType.PROCESSING -> {
                R.layout.fragment_sign_up_processing
            }
        }

        return inflater.inflate(res, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_sign_up, TopBarBtnType.INFO, null)

        when (type) {
            SignUpPageType.FILL_IN_INFO -> {
                btnFormNext.setOnClickListener(btnFormNextClickListener)
            }
            SignUpPageType.SET_PASSWORD -> {
                btnPwdNext.setOnClickListener(btnPwdNextClickListener)
            }
            SignUpPageType.SET_FINGERPRINT -> {
                btnSetupFingerprint.setOnClickListener(btnSetupFingerprintClickListener)
                btnIgnoreFingerprint.setOnClickListener(btnIgnoreFingerprintClickListener)
            }
            SignUpPageType.PROCESSING -> {
                Handler().postDelayed({
                    spUtil.putString(WALLET_PATH, "test")
                    mainCallback.onSignUpDone()
                }, 3000)
            }
        }
    }

    private val btnFormNextClickListener = View.OnClickListener {
        var valid = true
        val name = etName.text.toString()
        val mobile = etMobile.text.toString()
        val email = etEmail.text.toString()
        val address = etAddress.text.toString()
        if (name == "") {
            etName.error = "Please fill in this field."
            valid = false
        }
        if (mobile == "") {
            etMobile.error = "Please fill in this field."
            valid = false
        }
        if (email == "") {
            etEmail.error = "Please fill in this field."
            valid = false
        }

        if (valid) {
            spUtil.putString(SPUtil.NAME, name)
            spUtil.putString(SPUtil.MOBILE, mobile)
            spUtil.putString(SPUtil.EMAIL, email)
            spUtil.putString(SPUtil.ADDRESS, address)

            mainCallback.onSignUp(SignUpPageType.SET_PASSWORD)
        }
    }

    private val btnPwdNextClickListener = View.OnClickListener {
        var valid = true
        val pwd = etPassword.text.toString()
        val confirmPwd = etConfirmPassword.text.toString()
        if (pwd == "") {
            etPassword.error = "Please fill in this field."
            valid = false
        }
        if (confirmPwd == "") {
            etConfirmPassword.error = "Please fill in this field."
            valid = false
        }
        if (pwd != confirmPwd) {
            etConfirmPassword.error = "Confirm password is not the same."
            valid = false
        }

        if (valid) {
            spUtil.putString(SPUtil.PWD, pwd)

            if (Fingerprint.isAvailable(context)) {
                mainCallback.onSignUp(SignUpPageType.PROCESSING)
            } else {
                mainCallback.onSignUp(SignUpPageType.SET_FINGERPRINT)
            }
        }
    }

    private val btnSetupFingerprintClickListener = View.OnClickListener {
        startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
    }

    private val btnIgnoreFingerprintClickListener = View.OnClickListener {
        mainCallback.onSignUp(SignUpPageType.PROCESSING)
    }

    enum class SignUpPageType {
        FILL_IN_INFO,
        SET_PASSWORD,
        SET_FINGERPRINT,
        PROCESSING
    }
}