package com.example.cantinho_de_s2

import com.example.cantinho_de_s2.RequestJSON
import android.content.Intent
//import android.net.Uri
//import android.os.Build
import android.os.Bundle
//import android.os.Environment
import android.util.Log
//import com.google.android.material.floatingactionbutton.FloatingActionButton
//import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
//import android.view.MenuInflater
import android.view.MenuItem
//import android.widget.Button
//import androidx.appcompat.view.menu.MenuView
//import androidx.navigation.fragment.findNavController
import org.json.JSONObject
//import java.io.File
import java.lang.Exception
//import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    /**
     * override sobrescrita da função onCreate
     * vinda do AppCompatActivity
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        // salvando instancia
        super.onCreate(savedInstanceState)

        // colocando visualização no activity main
        setContentView(R.layout.activity_main)
        // adicionando barra de menu
        setSupportActionBar(findViewById(R.id.toolbar));

        // chamada ao servidor
        this.callAPI()
    }

    // sucesso na resposta
    fun responseApiSuccess(response: JSONObject) {
        Log.i("request-success", response.toString());
    }

    // erro na resposta
    fun responseApiError(error: Exception) {
        Log.e("request-error", error.toString());
    }

    // chamada da api
    fun callAPI() {

        // criação do objeto json em kotlin
        var bodyObject: JSONObject = JSONObject()
        var queryObject: JSONObject = JSONObject()

        // adicionando valor ao json
        bodyObject.put("body-text", "teste-body")
        queryObject.put("query-text", "teste-query")

        try {
            // requisição para o servidor
            RequestJSON.instance()
                       .setURL("/")
                       .setQuery(queryObject)
                       .setData(bodyObject)
                       .setMethod("POST")
                       .send(this, this::responseApiSuccess, this::responseApiError);
        } catch (error: Exception) {
            error.printStackTrace();
        }
    }


    fun ShowConfigurationApp() {
        var intent = Intent(this, ConfigurationActivity::class.java);
        startActivity(intent);
        this.finishActivity(1);
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> {
                ShowConfigurationApp();
                true;
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}