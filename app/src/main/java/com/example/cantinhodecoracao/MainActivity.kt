package com.example.cantinhodecoracao

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: LoginViewModel by viewModels()

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        MainController(this, model).load()
    }

    fun singIn(login: Login) {
        RequestJSON()
                .setUrl("/api/v1/auth")
                .setMethod("post")
                .appendBody("email", login.getEmail())
                .appendBody("senha", login.getSenha())
                .call(this, fun (error: Exception?, result: JSONObject?) {
                    if (error !== null) {
                        Information()
                                .setTitle("Error")
                                .setMessage("Falha no login")
                                .setPositiveButtonLabel("ok")
                                .show(this, fun (response: JSONObject) { })
                        return
                    }
                })
    }

    fun CloseApp() {
        finish()
    }
}