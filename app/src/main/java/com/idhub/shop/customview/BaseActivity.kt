package com.idhub.shop.customview

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    var name: String = this.javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("$name.onCreate")
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