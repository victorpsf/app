package com.example.cantinhodecoracao

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.ViewModels.LoginViewModel

class MainActivity : AppCompatActivity() {
    private val model: LoginViewModel by viewModels()
    private var mainController: MainController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        this.mainController = MainController(this, this.model)

        this.model.setActivity(this).setMainController(this.mainController as MainController)
        //this.mainController?.load()
    }

    fun handlePage(token: String, keys: String) {
        val intent: Intent = Intent(this, PageActivity::class.java)
        //this.sharedProperties(intent, token, keys)
        this.startActivity(intent)
    }

    private fun sharedProperties(intent: Intent, token: String, keys: String) {
        /*
        intent.putExtra("token", token)
        intent.putExtra("keys", keys)
         */
    }

    fun CloseApp() {
        finish()
    }
}