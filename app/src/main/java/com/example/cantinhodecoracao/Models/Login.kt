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
        var email = JSONReader().read(json, arrayOf("email"))
        var senha = JSONReader().read(json, arrayOf("senha"))
        var confirm = JSONReader().read(json, arrayOf("confirm"))
        var code = JSONReader().read(json, arrayOf("code"))

        if (email !== null && email is String) this.email = email
        if (senha !== null && senha is String) this.senha = senha
        if (confirm !== null && confirm is String) this.confirm = confirm
        if (code !== null && code is Int) this.code = code

        return this
    }

    fun getPropertiesJSON(): JSONObject {
        var login: JSONObject = JSONObject()

        if (this.email.length > 0) login.put("email", this.email)
        if (this.senha.length > 0) login.put("senha", this.email)
        if (this.confirm.length > 0) login.put("confirm", this.confirm)
        if (this.code > 0) login.put("code", this.code)

        return login;
    }

}