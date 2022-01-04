package com.saidul.paysera.core.base

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.android.material.snackbar.Snackbar

open class BaseActivity : AppCompatActivity() {

    fun showSimpleFailureDialog(
        title: String,
        content: String,
        agree: String,
        isCancelAble: Boolean = false,
        positiveCallBack: PositiveCallBack
    ) {
        MaterialDialog.Builder(this)
            .title(title)
            .cancelable(isCancelAble)
            .content(content)
            .positiveText(agree).onPositive { dialog, which ->
                positiveCallBack.onClick()
            }.show()
    }

    fun showSnackBarMessage(it: String?) {
        Snackbar.make(
            findViewById(android.R.id.content),
            it.toString(),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    interface PositiveCallBack {
        fun onClick()
    }
}