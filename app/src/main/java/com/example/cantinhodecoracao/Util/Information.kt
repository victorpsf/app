package com.example.cantinhodecoracao.Util

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import org.json.JSONObject


class Information {
    private var title: String = ""
    private var message: String = ""
    private var positiveButtonLabel = ""
    private var negativeButtonLabel = ""
    private var neutralButtonLabel = ""

    fun setTitle(title: String): Information {
        this.title = title
        return this
    }

    fun setMessage(message: String): Information {
        this.message = message
        return this
    }

    fun setPositiveButtonLabel(label: String): Information {
        this.positiveButtonLabel = label
        return this
    }

    fun setNegativeButtonLabel(label: String): Information {
        this.negativeButtonLabel = label
        return this
    }

    fun setNeutralButtonLabel(label: String): Information {
        this.neutralButtonLabel = label
        return this
    }

    fun show(context: Context, listiner: (click: JSONObject) -> Unit) {
        var result = JSONObject()
        var alertDialog = AlertDialog.Builder(context).create();

        alertDialog.setTitle(this.title)
        alertDialog.setMessage(this.message)

        if (this.positiveButtonLabel.length !== 0)
            alertDialog.setButton(
                    AlertDialog.BUTTON_POSITIVE,
                    this.positiveButtonLabel,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, id: Int ->
                        result.put("button", "positive")
                        result.put("dialog", dialog)
                        result.put("id", id)
                        listiner(result)
                    }
            )

        if (this.neutralButtonLabel.length !== 0)
            alertDialog.setButton(
                    AlertDialog.BUTTON_NEUTRAL,
                    this.neutralButtonLabel,
                    DialogInterface.OnClickListener { dialog: DialogInterface?, id: Int ->
                        result.put("button", "neutral")
                        result.put("dialog", dialog)
                        result.put("id", id)
                        listiner(result)
                    }
            )

        if (this.negativeButtonLabel.length !== 0)
            alertDialog.setButton(
                    AlertDialog.BUTTON_NEGATIVE,
                    this.negativeButtonLabel,
                    DialogInterface.OnClickListener { dialog: DialogInterface, id: Int ->
                        result.put("button", "negative")
                        result.put("dialog", dialog)
                        result.put("id", id)
                        listiner(result)
                    }
            )

        alertDialog.show()
    }
}