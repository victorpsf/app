package com.example.cantinho_de_s2

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class ConfigurationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_configuration)

        val support = super.getSupportActionBar();

        if (support !== null) {
            support.setDisplayHomeAsUpEnabled(true); // mostra o botão
            support.setHomeButtonEnabled(true); // ativar o botão
        }
    }

    fun backMain() {
        val intent = Intent(this, MainActivity::class.java);

        startActivity(intent);
        this.finishActivity(1);
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                backMain();
            }
        }

        return true;
    }
}