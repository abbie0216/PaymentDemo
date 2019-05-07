package com.idhub.shop

import android.app.Application
import com.facebook.stetho.Stetho
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.web3j.crypto.Credentials
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import java.security.Security


class MainApplication : Application() {

    companion object {
        //        lateinit var apiService: APIService
        lateinit var web3j: Web3j
        lateinit var credentials: Credentials
    }

    override fun onCreate() {
        super.onCreate()
        init()

        Observable.create<Boolean> {
            runWeb3j()
        }
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                {},
                {})
    }

    private fun init() {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

//        val retrofit = Retrofit.Builder()
//            .baseUrl(API_BASE_URL)
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(GsonConverterFactory.create())
//            .client(httpClient.build())
//            .build()
//        apiService = retrofit.create(APIService::class.java)

        Stetho.initialize(
            Stetho.newInitializerBuilder(this)
                .enableDumpapp(
                    Stetho.defaultDumperPluginsProvider(this)
                )
                .enableWebKitInspector(
                    Stetho.defaultInspectorModulesProvider(this)
                )
                .build()
        )

        setupBouncyCastle()
    }

    private fun runWeb3j() {
        // We start by creating a new web3j instance to connect to remote nodes on the network
        // Enter your Infura token here
        // Request some Ether for the Rinkeby test network at https://www.rinkeby.io/#faucet
        // By using a public Google+ link (tutorial: https://gist.github.com/cryptogoth/10a98e8078cfd69f7ca892ddbdcf26bc)
        web3j =
            Web3j.build(HttpService("https://rinkeby.infura.io/v3/832a930cdb584366b03a5867f50672d0"))
        println("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().web3ClientVersion)
    }

    private fun setupBouncyCastle() {
        val provider = Security.getProvider(BouncyCastleProvider.PROVIDER_NAME)
            ?: // Web3j will set up the provider lazily when it's first used.
            return
        if (provider.javaClass == BouncyCastleProvider::class.java) {
            // BC with same package name, shouldn't happen in real life.
            return
        }
        // Android registers its own BC provider. As it might be outdated and might not include
        // all needed ciphers, we substitute it with a known BC bundled in the app.
        // Android's BC has its package rewritten to "com.android.org.bouncycastle" and because
        // of that it's possible to have another BC implementation loaded in VM.
        Security.removeProvider(BouncyCastleProvider.PROVIDER_NAME)
        Security.insertProviderAt(BouncyCastleProvider(), 1)
    }
}
