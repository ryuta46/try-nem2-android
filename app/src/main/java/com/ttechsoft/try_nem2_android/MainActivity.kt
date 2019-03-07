package com.ttechsoft.try_nem2_android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.Gson
import io.nem.sdk.infrastructure.AccountHttp
import io.nem.sdk.infrastructure.TransactionHttp
import io.nem.sdk.model.account.Account
import io.nem.sdk.model.account.Address
import io.nem.sdk.model.blockchain.NetworkType
import io.nem.sdk.model.mosaic.Mosaic
import io.nem.sdk.model.mosaic.MosaicId
import io.nem.sdk.model.mosaic.XEM
import io.nem.sdk.model.transaction.Deadline
import io.nem.sdk.model.transaction.Message
import io.nem.sdk.model.transaction.PlainMessage
import io.nem.sdk.model.transaction.TransferTransaction
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import org.threeten.bp.temporal.ChronoUnit
import java.math.BigInteger
import java.util.*


class MainActivity : AppCompatActivity() {
    val disposeBag = CompositeDisposable()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //val address = Address.createFromRawAddress("SC7A4H-7CYCSH-4CP4XI-ZS4G2G-CDZ7JP-PR5FRG-2VBU")
        val account = Account.createFromPrivateKey(
            "C6EBC101947B54864CCFF3F14FF9A4319A62F735E63BD044E0A9E74C3B57748C",
            NetworkType.MIJIN_TEST
        )
        val accountHttp = AccountHttp(HOST)
        accountHttp
            .getAccountInfo(account.address)
            .subscribeOn(Schedulers.io())

            .subscribe( {
                Log.i(TAG, Gson().toJson(it))
            },
                {
                    Log.e(TAG, it.message)
                }
            ).addTo(disposeBag)

        accountHttp
            .transactions(account.publicAccount)
            .subscribeOn(Schedulers.io())

            .subscribe( {
                Log.i(TAG, Gson().toJson(it))
            },
                {
                    Log.e(TAG, it.message)
                }
            ).addTo(disposeBag)



        val transaction = TransferTransaction.create(
            Deadline(1, ChronoUnit.HOURS),
            account.address,
            listOf(XEM.createAbsolute(BigInteger.valueOf(1))),
                PlainMessage.create(""),
                NetworkType.MIJIN_TEST
        )

        val signedTransaction = account.sign(transaction)
        val transactionHttp = TransactionHttp(HOST)
        /*
        transactionHttp
            .announce(signedTransaction)
            .subscribeOn(Schedulers.io())
            .subscribe( {
                Log.i(TAG, Gson().toJson(it))
            },
                {
                    Log.e(TAG, it.message)
                }
            ).addTo(disposeBag)
*/

        Log.i(TAG, account.address.plain())

    }


    companion object {
        private const val HOST = "http://192.168.10.13:3000"
        private const val TAG = "Nem2Android"
    }
}
