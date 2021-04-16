package com.example.cantinhodecoracao

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Util.Directory
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: LoginViewModel by viewModels()

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        var directory = Directory(this)
        var fileJSON = directory.readFile("rsa", "keys.json")

        if (fileJSON.length == 0) {
            var keys = RSA().gereratePair()
            directory.saveFile("rsa", "keys.json", keys.toString())
            Toast.makeText(this, "Chave de cryptografia gerada", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this, "Chave de cryptografia lida", Toast.LENGTH_LONG).show()
        }
    }

    private fun CloseApp() {
        finish()
    }
}