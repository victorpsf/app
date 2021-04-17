package com.example.cantinhodecoracao

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import androidx.activity.viewModels
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Util.Convert
import com.example.cantinhodecoracao.Util.Directory
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.Util.Validator
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject
/*
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import android.view.Menu
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Util.Information
 */

class MainActivity : AppCompatActivity() {
    private var keys: JSONObject = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val model: LoginViewModel by viewModels()

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        this.generateKeys()
        model.setActivity(this)
             .setLogin(Login())
             .setKeys(this.keys)
    }

    fun generateKeys() {
        var directory = Directory(this)
        var rsa = RSA()
        var fileJSON = directory.readFile("rsa", "keys.json")

        if (fileJSON.length == 0) {
            this.keys = rsa.gereratePair() as JSONObject
            directory.saveFile("rsa", "keys.json", keys.toString())
            Toast.makeText(this, "Chave de cryptografia gerada", Toast.LENGTH_LONG).show()
        } else {
            try {
                this.keys = Convert().stringToJSON(fileJSON) as JSONObject
                Toast.makeText(this, "Chave de cryptografia lida", Toast.LENGTH_LONG).show()
            } catch (error: Exception) {
                Toast.makeText(this, "Não foi possivel ler arquivos de cryptografia", Toast.LENGTH_LONG).show()
                var bool = directory.deleteFile("rsa", "keys.json")

                if (bool) this.generateKeys()
                else this.CloseApp()
            }
        }
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

    private fun CloseApp() {
        finish()
    }
}