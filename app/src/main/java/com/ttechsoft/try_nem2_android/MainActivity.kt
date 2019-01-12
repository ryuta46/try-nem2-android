package com.ttechsoft.try_nem2_android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.nem.sdk.infrastructure.AccountHttp
import io.nem.sdk.model.account.Address
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class MainActivity : AppCompatActivity() {
    val disposeBag = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val address = Address.createFromRawAddress("SC7A4H-7CYCSH-4CP4XI-ZS4G2G-CDZ7JP-PR5FRG-2VBU")
        val accountHttp = AccountHttp("http://192.168.10.13:3000")
        val subscription = accountHttp
            .getAccountInfo(address)
            .subscribeOn(Schedulers.io())
            .subscribe {
                print(it)
            }

        disposeBag.add(subscription)
    }
}
