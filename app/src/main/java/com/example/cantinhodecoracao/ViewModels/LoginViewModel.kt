package com.example.cantinhodecoracao.ViewModels

import androidx.lifecycle.ViewModel
import com.example.cantinhodecoracao.Models.Login
import org.json.JSONObject

class LoginViewModel: ViewModel() {
    private var keys: JSONObject = JSONObject()
    private var login: Login = Login()

    fun setKeys(json: JSONObject) {
        this.keys = json
    }

    fun getKeys(): JSONObject {
        return this.keys
    }

    fun getLogin(): Login {
        return this.login
    }

    fun setLogin(login: Login) {
        this.login = login
    }
}