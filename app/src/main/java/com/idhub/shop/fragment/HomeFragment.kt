package com.idhub.shop.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.idhub.shop.MainActivity.Companion.isSkipSignIn
import com.idhub.shop.R
import com.idhub.shop.customview.BaseFragment
import com.idhub.shop.model.Product
import com.idhub.shop.util.RequestCode
import com.idhub.shop.util.TopBarBtnType
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DefaultDecoderFactory
import com.journeyapps.barcodescanner.camera.CameraSettings
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*

class HomeFragment : BaseFragment(), View.OnClickListener {

    private var scannerCallback = object : BarcodeCallback {
        override fun barcodeResult(result: BarcodeResult?) {
            val scanView = scannerView
            scanView.pause()
            println("Scanned result: ${result!!.text}")
            try {
                val product = Gson().fromJson<Product>(result.text, Product::class.java)
                mainCallback.onScanned(product)
            } catch (e: Exception) {
                MaterialDialog(context!!).show {
                    title(R.string.dlg_title_fail)
                    message(R.string.dlg_msg_scan_syntax_fail)
                    positiveButton(R.string.dlg_btn_confirm) {
                        scanView.resume()
                    }
                    cancelable(false)
                }
            }
        }

        override fun possibleResultPoints(resultPoints: MutableList<ResultPoint>?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        name = this.javaClass.simpleName
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        uiSetting.setTopBar(R.string.top_bar_title_home, TopBarBtnType.INFO, TopBarBtnType.MEMBER)

        scannerView.barcodeView.cameraSettings.isAutoFocusEnabled = true
        scannerView.barcodeView.cameraSettings.requestedCameraId = CAMERA_FACING_BACK
        scannerView.barcodeView.cameraSettings.focusMode = CameraSettings.FocusMode.CONTINUOUS
        scannerView.barcodeView.cameraSettings.isContinuousFocusEnabled = true
        val decodeFormats = Arrays.asList(
            BarcodeFormat.QR_CODE
        )
        scannerView.barcodeView.decoderFactory = DefaultDecoderFactory(decodeFormats, null, null, 0)

        btnGallery.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()

        scannerView.decodeContinuous(scannerCallback)
        scannerView.resume()
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            R.id.btnGallery -> {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent, RequestCode.PICK_PHOTO)
            }
        }

    }

    override fun onPause() {
        super.onPause()

        scannerView.pause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            RequestCode.PICK_PHOTO -> {
                isSkipSignIn = true
                if (resultCode == Activity.RESULT_OK) {
                    val scanView = scannerView
                    uiSetting.showLoading()
                    scanView.pause()
                    Observable.create<Product> {
                        try {
                            val input = context!!.contentResolver.openInputStream(data!!.data!!)
                            val bitmap = BitmapFactory.decodeStream(input!!)
                            val intArray = IntArray(bitmap.width * bitmap.height)
                            bitmap.getPixels(
                                intArray,
                                0,
                                bitmap.width,
                                0,
                                0,
                                bitmap.width,
                                bitmap.height
                            )
                            val source = RGBLuminanceSource(bitmap.width, bitmap.height, intArray)
                            val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
                            val result = MultiFormatReader().decode(binaryBitmap)
                            println("QR code from photo = ${result.text}")

                            val product = Gson().fromJson<Product>(result.text, Product::class.java)
                            it.onNext(product)
                        } catch (e: JsonSyntaxException) {
                            it.onError(Exception(getString(R.string.dlg_msg_scan_syntax_fail)))
                        } catch (eOthers: Exception) {
                            it.onError(Exception(getString(R.string.dlg_msg_unexpected_fail)))
                        }
                    }
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            { product ->
                                mainCallback.onScanned(product)
                                uiSetting.hideLoading()
                            },
                            { error ->
                                uiSetting.hideLoading()
                                MaterialDialog(context!!).show {
                                    title(R.string.dlg_title_fail)
                                    message(null, error.message)
                                    positiveButton(R.string.dlg_btn_confirm) {
                                        scanView.resume()
                                    }
                                    cancelable(false)
                                }
                            })
                }

            }
        }
    }
}