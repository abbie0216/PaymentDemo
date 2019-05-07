package com.idhub.shop.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.idhub.shop.MainActivity.Companion.isSkipSignIn
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.customview.dialog.UpdateInfoDialog
import com.idhub.shop.util.RequestCode
import com.idhub.shop.util.SPUtil.Companion.ADDRESS
import com.idhub.shop.util.SPUtil.Companion.AVATAR_URI
import com.idhub.shop.util.SPUtil.Companion.EMAIL
import com.idhub.shop.util.SPUtil.Companion.MOBILE
import com.idhub.shop.util.SPUtil.Companion.NAME
import com.idhub.shop.util.TopBarBtnType
import kotlinx.android.synthetic.main.fragment_member.*


class MemberFragment : BaseFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_member, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_member, TopBarBtnType.BACK, TopBarBtnType.RECORD)

        ivAvatar.setOnClickListener(this)
        llName.setOnClickListener(this)
        llMobile.setOnClickListener(this)
        llEmail.setOnClickListener(this)
        llAddress.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        val strUri = spUtil.getString(AVATAR_URI)
        if (strUri != "") {
            val uri = Uri.parse(strUri)
            ivAvatar.setImageURI(uri)
        } else {
            ivAvatar.setImageResource(R.drawable.ic_avatar)
        }

        tvName.text = spUtil.getString(NAME)
        tvMobile.text = spUtil.getString(MOBILE)
        tvEmail.text = spUtil.getString(EMAIL)
        tvAddress.text = spUtil.getString(ADDRESS)

    }

    private val dlgUpdateCallback = object : UpdateInfoDialog.DialogCallback {
        override fun onDone() {
            tvName.text = spUtil.getString(NAME)
            tvMobile.text = spUtil.getString(MOBILE)
            tvEmail.text = spUtil.getString(EMAIL)
            tvAddress.text = spUtil.getString(ADDRESS)
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.ivAvatar -> {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "image/*"
                startActivityForResult(intent, RequestCode.PICK_PHOTO)
            }
            R.id.llName -> {
                UpdateInfoDialog(context!!, NAME, dlgUpdateCallback).show()
            }
            R.id.llMobile -> {
                UpdateInfoDialog(context!!, MOBILE, dlgUpdateCallback).show()
            }
            R.id.llEmail -> {
                UpdateInfoDialog(context!!, EMAIL, dlgUpdateCallback).show()
            }
            R.id.llAddress -> {
                UpdateInfoDialog(context!!, ADDRESS, dlgUpdateCallback).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        println("MemberFragment/onActivityResult/requestCode:$requestCode")
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.PICK_PHOTO -> {
                isSkipSignIn = true
                if (resultCode == Activity.RESULT_OK) {
                    spUtil.putString(AVATAR_URI, data!!.data!!.toString())
                }
            }
        }
    }
}