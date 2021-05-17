package com.example.cantinhodecoracao.Controllers

import android.util.Log
import com.example.cantinhodecoracao.PageActivity
import com.example.cantinhodecoracao.ViewModels.PageViewModel
import org.json.JSONObject

class PageController(pageActivity: PageActivity, model: PageViewModel, token: String, keys: JSONObject): BaseController(pageActivity, keys) {
    private var pageActivity: PageActivity = pageActivity
    private val token: String = token
    private val pageViewModel: PageViewModel = model

    fun load() {

        Log.i("token", this.token)
        Log.i("Keys", this.keys.toString())
    }
}