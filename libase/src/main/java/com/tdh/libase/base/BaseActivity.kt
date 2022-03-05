package com.tdh.libase.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.tdh.libase.R
import com.tdh.libase.base.view.LiProgressDialog

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)

        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container_frame, initFirstFragment())
        transaction.commitAllowingStateLoss()
    }

    abstract fun initFirstFragment(): Fragment

    fun showProgress(title: String?, message: String = "Loading") {
        val transaction = supportFragmentManager.beginTransaction()
        val progressDialog = LiProgressDialog(title, message, false)
        transaction.add(progressDialog, LiProgressDialog::class.simpleName)
        transaction.commitAllowingStateLoss()
    }

    fun hideProgress() {
        supportFragmentManager.findFragmentByTag(LiProgressDialog::class.simpleName)?.let {
            (it as LiProgressDialog).dismissAllowingStateLoss()
        }
    }

    fun showDialogFragment(fragment: DialogFragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(fragment, fragment::class.simpleName)
        transaction.commitAllowingStateLoss()
    }
}