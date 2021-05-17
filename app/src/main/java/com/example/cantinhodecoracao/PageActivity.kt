package com.example.cantinhodecoracao

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.Controllers.PageController
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import com.example.cantinhodecoracao.ViewModels.PageViewModel
import org.json.JSONObject
import java.lang.Exception

class PageActivity : AppCompatActivity() {
    private val model: PageViewModel by viewModels()
    private var pageController: PageController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_page)
        setSupportActionBar(findViewById(R.id.toolbar))

        //this.getExtra()
        //this.pageController?.load()
    }

    private fun getExtra() {
        var token: String = this.intent.getStringExtra("token").toString()
        var keys: String = this.intent.getStringExtra("keys").toString()

        this.pageController = PageController(this, this.model, token, JSONObject(keys))
    }

    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}