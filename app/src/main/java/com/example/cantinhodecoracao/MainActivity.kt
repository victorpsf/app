package com.example.cantinhodecoracao

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val model: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        MainController(this, this.model).load()
    }

    fun handlePage(token: String) {
        val intent: Intent = Intent(this, PageActivity::class.java)

        this.sharedProperties(intent)
        // intent.putExtra()
        this.startActivity(intent)
    }

    private fun sharedProperties(intent: Intent) {
        /*
        var keys = keys

        intent.putExtra("privateKey", )
         */
    }

    fun CloseApp() {
        finish()
    }
}