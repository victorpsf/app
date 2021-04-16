package com.example.cantinhodecoracao.ViewModels

import com.example.cantinhodecoracao.Models.Login
import org.json.JSONObject

class LoginViewModel {
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