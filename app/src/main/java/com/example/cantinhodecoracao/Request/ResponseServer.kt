package com.example.cantinhodecoracao.Request

import android.app.Activity
import android.util.Log
import com.example.cantinhodecoracao.Util.Information
import org.json.JSONObject

class ResponseServer {
    private var responseJSON: JSONObject?
    private var exception: Exception?

    constructor(response: JSONObject?, error: Exception?) {
        this.responseJSON = response
        this.exception = error
    }

    private fun isEmptyException(): Boolean {
        return if (this.exception == null) true else false
    }

    private fun isEmptyResponse(): Boolean {
        return if (this.responseJSON == null) true else false
    }

    fun resultError(): Boolean {
        if (!this.isEmptyException()) return true
        if (this.getStatus() == "error") return true
        return false
    }

    fun getStatus(): String {
        if (this.isEmptyResponse()) return "error"
        else return this.responseJSON?.getString("status") as String
    }

    fun getCode(): Int {
        if (this.isEmptyResponse()) return 500
        return this.responseJSON?.getInt("code") as Int
    }

    fun getMessage(): String {
        if (this.isEmptyResponse()) return this.exception?.message.toString()
        return this.responseJSON?.getString("message") as String
    }

    fun getTime(): Int {
        if (this.isEmptyResponse()) return 0
        return this.responseJSON?.getInt("time") as Int
    }

    fun getResult(): Any? {
        if (this.isEmptyResponse()) return null
        return this.responseJSON?.get("result") as Any
    }

    fun openDialog(activity: Activity, title: String, message: String?, positiveButton: String?, negativeButton: String?, listiner: (click: JSONObject) -> Unit) {
        var _title: String = title
        var _message: String

        if (message == null) {
            _message = this.getMessage()
        } else {
            _message = message
        }

        var information = Information()

        information.setTitle(_title)
        information.setMessage(_message)

        if (positiveButton !== null) information.setPositiveButtonLabel(positiveButton)
        if (negativeButton !== null) information.setNegativeButtonLabel(negativeButton)

        information.show(activity, listiner)
    }
}