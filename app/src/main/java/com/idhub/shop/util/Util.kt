package com.idhub.shop.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.widget.Toast
import com.idhub.shop.MainApplication
import com.idhub.shop.MainApplication.Companion.credentials
import com.idhub.shop.R
import com.idhub.shop.util.SPUtil.Companion.WALLET_PATH
import org.web3j.crypto.CipherException
import org.web3j.crypto.WalletUtils
import org.web3j.protocol.core.DefaultBlockParameterName
import java.io.File
import java.io.IOException
import java.math.BigInteger
import java.security.InvalidAlgorithmParameterException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException

class Util(var context: Context) {

    fun haveCredentials(): Boolean {
        return try {
            credentials.address
            true
        } catch (e: Exception) {
            false
        }
    }

    @Throws(
        CipherException::class,
        InvalidAlgorithmParameterException::class,
        NoSuchAlgorithmException::class,
        NoSuchProviderException::class,
        IOException::class
    )

    fun createLightWallet(): String {
        println("Creating light wallet...")
        var walletPath =
            context.filesDir.absolutePath + "/light"//Environment.getExternalStorageDirectory().absolutePath + "/light"
        val walletFile = File(walletPath)

        walletFile.mkdirs()
        try {
            val name = WalletUtils.generateLightNewWalletFile("123456", walletFile)
            println("Light wallet created! ($name)")
            walletPath = "$walletPath/$name"
            SPUtil(context).putString(WALLET_PATH, walletPath)
        } catch (e: Exception) {
            e.printStackTrace()
            walletPath = ""
        }

        return walletPath
    }

    fun loadCredentials(walletPath: String) {
        println("Credentials loading...")
        MainApplication.credentials = WalletUtils.loadCredentials("123456", walletPath)
        println("Credentials loaded! Wallet address = ${MainApplication.credentials.address}")
    }

    fun getBalance(address: String): BigInteger {
        val ethGetBalance =
            MainApplication.web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .sendAsync().get()
        val wei = ethGetBalance.balance
        println("Balance from [$address]: $wei Wei")

        return wei
    }

    fun copy2Clipboard(value: String) {
        println("copy2Clipboard: Copy [$value] to clipboard.")
        val clipboard: ClipboardManager =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.primaryClip = ClipData.newPlainText(value, value)
        Toast.makeText(
            context,
            R.string.toast_copied,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun getRandomNum(min: Int, max: Int): Int {
        return (Math.random() * max + min).toInt()
    }

    fun getVersionName(): String {
        val packageInfo: PackageInfo
        var versionName = ""
        try {
            packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionName = packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return versionName
    }

    fun generateOrderId(): Long {
        return getRandomNum(1, 30000).toLong()
    }

}