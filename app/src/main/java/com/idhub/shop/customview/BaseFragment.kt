package com.idhub.shop.customview

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.idhub.shop.ActivityCallback
import com.idhub.shop.UISetting
import com.idhub.shop.util.SPUtil
import com.idhub.shop.util.Util

open class BaseFragment : Fragment() {

    var name: String = this.javaClass.simpleName

    lateinit var mainCallback: ActivityCallback
    lateinit var uiSetting: UISetting
    lateinit var thisView: View
    lateinit var util: Util
    lateinit var spUtil: SPUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("$name.onCreate")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        thisView = view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context !is ActivityCallback) {
            throw Exception("Activity should implement ActivityCallback.")
        }
        if (context !is UISetting) {
            throw Exception("Activity should implement UISetting.")
        }
        util = Util(context)
        spUtil = SPUtil(context)
        mainCallback = context
        uiSetting = context
    }

    override fun onStart() {
        super.onStart()
        println("$name.onStart")
    }

    override fun onResume() {
        super.onResume()
        println("$name.onResume")
    }

    override fun onPause() {
        super.onPause()
        println("$name.onPause")
    }

    override fun onStop() {
        super.onStop()
        println("$name.onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        println("$name.onDestroy")
    }
}