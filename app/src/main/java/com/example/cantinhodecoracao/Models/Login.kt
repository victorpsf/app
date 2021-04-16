package com.example.cantinhodecoracao.Models

import com.example.cantinhodecoracao.Util.JSONReader
import org.json.JSONObject

class Login {
    private var email: String = ""
    private var senha: String = ""
    private var confirm: String = ""
    private var code: Int = 0

    fun getEmail(): String { return this.email }
    fun getSenha(): String { return this.senha }
    fun getConfirm(): String { return this.confirm }
    fun getCode(): Int { return this.code }

    fun setEmail(value: String): Login { this.email = value; return this }
    fun setSenha(value: String): Login { this.senha = value; return this }
    fun setConfirm(value: String): Login { this.confirm = value; return this }
    fun setCode(value: Int): Login { this.code = value; return this }

    fun setPropertiesUsingJSON(json: JSONObject): Login {
        var email = JSONReader(json).read(arrayOf("email"))
        var senha = JSONReader(json).read(arrayOf("senha"))
        var confirm = JSONReader(json).read(arrayOf("confirm"))
        var code = JSONReader(json).read(arrayOf("code"))

        if (email !== null && email is String) this.email = email
        if (senha !== null && senha is String) this.senha = senha
        if (confirm !== null && confirm is String) this.confirm = confirm
        if (code !== null && code is Int) this.code = code

        return this
    }
}