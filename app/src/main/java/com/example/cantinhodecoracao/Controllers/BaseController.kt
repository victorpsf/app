package com.example.cantinhodecoracao.Controllers

import android.app.Activity
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.Util.Directory
import org.json.JSONObject

open class BaseController(activity: Activity, keys: JSONObject) {
    open var loading: Loading = Loading(activity)
    open var keys: JSONObject = keys
    open var directory: Directory = Directory(activity)
    open var rsa: RSA = RSA()
}