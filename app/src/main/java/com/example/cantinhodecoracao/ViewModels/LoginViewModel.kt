package com.example.cantinhodecoracao.ViewModels

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.cantinhodecoracao.MainActivity
import com.example.cantinhodecoracao.Models.Login
import org.json.JSONObject

class LoginViewModel: ViewModel() {
    private var activity: MainActivity? = null;
    private var keys: JSONObject = JSONObject()
    private var login: Login = Login()

    fun setActivity(activity: MainActivity): LoginViewModel {
        this.activity = activity
        return this
    }

    fun setKeys(json: JSONObject): LoginViewModel {
        this.keys = json
        return this
    }

    fun setLogin(login: Login): LoginViewModel {
        this.login = login
        return this
    }

    fun getActivity(): MainActivity {
        return this.activity as MainActivity
    }

    fun getKeys(): JSONObject {
        return this.keys
    }

    fun getLogin(): Login {
        return this.login
    }
}