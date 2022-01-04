package com.saidul.paysera.core.base

import androidx.appcompat.app.AppCompatActivity
import com.afollestad.materialdialogs.MaterialDialog

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

    interface PositiveCallBack {
        fun onClick()
    }
}