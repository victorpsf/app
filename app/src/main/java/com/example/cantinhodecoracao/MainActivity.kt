package com.example.cantinhodecoracao

import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Util.Information
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
    }

    private fun CloseApp() {
        System.exit(1)
    }
}