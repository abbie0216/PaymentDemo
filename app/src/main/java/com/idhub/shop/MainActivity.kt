package com.idhub.shop

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.idhub.shop.customview.BaseActivity
import com.idhub.shop.customview.dialog.AppInfoDialog
import com.idhub.shop.customview.dialog.OrderStatusHintDialog
import com.idhub.shop.customview.dialog.SignInDialog
import com.idhub.shop.fragment.*
import com.idhub.shop.model.Order
import com.idhub.shop.model.Product
import com.idhub.shop.util.Constant.Companion.HOME
import com.idhub.shop.util.Constant.Companion.ORDER
import com.idhub.shop.util.Constant.Companion.PAY_RESULT
import com.idhub.shop.util.Constant.Companion.PRODUCT
import com.idhub.shop.util.Constant.Companion.SIGN_UP_PAGE_TYPE
import com.idhub.shop.util.SPUtil
import com.idhub.shop.util.SPUtil.Companion.PWD
import com.idhub.shop.util.SPUtil.Companion.WALLET_PATH
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.activity_main.*
import me.aflak.libraries.view.Fingerprint

class MainActivity : BaseActivity(),
    ActivityCallback,
    UISetting {

    companion object {
        var isSkipSignIn = false
    }

    private lateinit var context: Context
    private lateinit var dlgSignIn: SignInDialog
    private val dlgSignInCallback = object : SignInDialog.DialogCallback {
        override fun onDone() {
            dlgSignIn = SignInDialog(context, this)
            setFragment(HomeFragment())
//                onScanned(Product(1, " AKX47 HAPPY DAYS (CD)", 99))
        }

        override fun onGoSetting() {
            startActivity(Intent(Settings.ACTION_SECURITY_SETTINGS))
        }
    }
    private lateinit var dlgAppInfo: AppInfoDialog
    private lateinit var dlgOrderStatusHint: OrderStatusHintDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        name = javaClass.simpleName
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.addOnBackStackChangedListener {
            println("OnBackStackChanged: backStackEntryCount=${supportFragmentManager.backStackEntryCount}")
        }

        context = this

        dlgSignIn = SignInDialog(this, dlgSignInCallback)
        dlgAppInfo = AppInfoDialog(this)
        dlgOrderStatusHint = OrderStatusHintDialog(this)
    }

    override fun onResume() {
        super.onResume()

        if (!isSkipSignIn)
            when {
                SPUtil(this).getString(WALLET_PATH) != "" -> { // already signed up
                    if (!dlgSignIn.isShowing)
                        dlgSignIn.show()
                }
                SPUtil(this).getString(PWD) == "" -> // haven't signed up
                    onSignUp(SignUpFragment.SignUpPageType.FILL_IN_INFO)
                else -> // haven't signed up and back from fingerprint setting
                    if (Fingerprint.isAvailable(this))
                        onSignUp(SignUpFragment.SignUpPageType.PROCESSING)
                    else
                        onSignUp(SignUpFragment.SignUpPageType.SET_FINGERPRINT)
            }
        else
            isSkipSignIn = false
    }

    override fun onSignUp(type: SignUpFragment.SignUpPageType) {
        val fragment = SignUpFragment()
        val bundle = Bundle()
        bundle.putSerializable(SIGN_UP_PAGE_TYPE, type)
        fragment.arguments = bundle
        setFragment(fragment)
    }

    override fun onSignUpDone() {
        cleanBackStack()
        if (!dlgSignIn.isShowing)
            dlgSignIn.show()
    }

    override fun onScanned(product: Product) {
        val bundle = Bundle()
        val fragment = OrderCreateFragment()
        bundle.putParcelable(PRODUCT, product)
        fragment.arguments = bundle
        setFragment(fragment)
    }

    override fun onPayResult(isDone: Boolean, order: Order) {
        val bundle = Bundle()
        val fragment = OrderResultFragment()
        bundle.putBoolean(PAY_RESULT, isDone)
        bundle.putParcelable(ORDER, order)
        fragment.arguments = bundle
        setFragment(fragment)
    }

    override fun onRecordDetail(order: Order) {
        val bundle = Bundle()
        val fragment = RecordDetailFragment()
        bundle.putParcelable(ORDER, order)
        fragment.arguments = bundle
        setFragment(fragment)
    }

    private fun back2HomePage() {
        supportFragmentManager.popBackStack(HOME, 0)
    }

    private fun go2MemberPage() {
        setFragment(MemberFragment())
    }

    private fun go2RecordPage() {
        setFragment(RecordListFragment())
    }

    private fun showAppInfo() {
        dlgAppInfo.show()
    }

    private fun showOrderHint() {
        dlgOrderStatusHint.show()
    }

    override fun setTopBar(titleRes: Int, lBtnType: TopBarBtnType?, rBtnType: TopBarBtnType?) {
        tvTitle.setText(titleRes)

        when (lBtnType) {
            TopBarBtnType.INFO -> {
                btnLeft.visibility = VISIBLE
                btnLeft.setImageResource(R.drawable.ic_info)
                btnLeft.setOnClickListener { showAppInfo() }
            }
            TopBarBtnType.HOME -> {
                btnLeft.visibility = VISIBLE
                btnLeft.setImageResource(R.drawable.ic_home)
                btnLeft.setOnClickListener { back2HomePage() }
            }
            TopBarBtnType.BACK -> {
                btnLeft.visibility = VISIBLE
                btnLeft.setImageResource(R.drawable.ic_back)
                btnLeft.setOnClickListener { onBackPressed() }
            }
            else -> {
                btnLeft.visibility = INVISIBLE
                btnLeft.setOnClickListener(null)
            }
        }

        when (rBtnType) {
            TopBarBtnType.MEMBER -> {
                btnRight.visibility = VISIBLE
                btnRight.setImageResource(R.drawable.ic_member)
                btnRight.setOnClickListener { go2MemberPage() }
            }
            TopBarBtnType.RECORD -> {
                btnRight.visibility = VISIBLE
                btnRight.setImageResource(R.drawable.ic_order_status_1)
                btnRight.setOnClickListener { go2RecordPage() }
            }
            TopBarBtnType.HINT -> {
                btnRight.visibility = VISIBLE
                btnRight.setImageResource(R.drawable.ic_hint)
                btnRight.setOnClickListener { showOrderHint() }
            }
            else -> {
                btnRight.visibility = INVISIBLE
                btnRight.setOnClickListener(null)
            }
        }
    }

    override fun showLoading() {
        rlLoading.visibility = VISIBLE
    }

    override fun hideLoading() {
        rlLoading.visibility = GONE
    }

    private fun setFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        if (fragment !is HomeFragment) {
            transaction.setCustomAnimations(
                R.anim.slide_left_in,
                R.anim.slide_left_out,
                R.anim.slide_right_in,
                R.anim.slide_right_out
            )
        }
        val tag =
            if (fragment is OrderCreateFragment) ORDER else if (fragment is HomeFragment) HOME else null
        transaction.replace(R.id.content, fragment).addToBackStack(tag)
        transaction.commit()
    }

    private fun cleanBackStack() {
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finishApp()
        } else {
            if (supportFragmentManager.fragments[0] is OrderResultFragment)
                supportFragmentManager.popBackStack(
                    ORDER,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                ) // 包含 OrderCreateFragment 與其上層全部 pop
            else
                supportFragmentManager.popBackStack()
        }
    }

    private fun finishApp() {
        moveTaskToBack(true)
        finish()
    }
}

interface ActivityCallback {
    fun onSignUp(type: SignUpFragment.SignUpPageType)
    fun onSignUpDone()
    fun onScanned(product: Product)
    fun onPayResult(isDone: Boolean, order: Order)
    fun onRecordDetail(order: Order)
}

interface UISetting {
    fun setTopBar(titleRes: Int, lBtnType: TopBarBtnType?, rBtnType: TopBarBtnType?)
    fun showLoading()
    fun hideLoading()
}