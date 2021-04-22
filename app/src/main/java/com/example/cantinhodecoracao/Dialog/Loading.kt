package com.example.cantinhodecoracao.Dialog

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.cantinhodecoracao.R

class Loading {
    private var activity: Activity
    private var dialog: AlertDialog

    constructor(activity: Activity) {
        this.activity = activity
        this.dialog = this.startDialog()
    }

    private fun startDialog(): AlertDialog {
        var builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)
        var inflater: LayoutInflater = this.activity.layoutInflater


        builder.setView(inflater.inflate(R.layout.custom_progress, null))
        builder.setCancelable(false)

        return builder
                .create()
    }

    fun show() {
        this.dialog.show()
    }

    fun close() {
        this.dialog.dismiss()
    }
}