package com.example.cantinhodecoracao.ViewModels

import androidx.lifecycle.ViewModel
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.MainActivity
import com.example.cantinhodecoracao.Models.Login
import org.json.JSONObject
import java.security.PrivateKey
import java.security.PublicKey

class LoginViewModel: ViewModel() {
    private var activity: MainActivity? = null;
    private var mainController: MainController? = null
    private var keys: JSONObject = JSONObject()
    private var login: Login = Login()
    private var rsa: RSA = RSA()

    fun setMainController(mainController: MainController): LoginViewModel {
        this.mainController = mainController
        return this
    }

    fun setActivity(activity: MainActivity): LoginViewModel {
        this.activity = activity
        return this
    }

    fun setController(mainController: MainController) : LoginViewModel {
        this.mainController = mainController
        return this
    }

    fun setKeys(json: JSONObject): LoginViewModel {
        this.keys = json
        return this
    }

    fun setLogin(login: Login): LoginViewModel {
        this.login = login
        return this
    }

    fun getMainController(): MainController {
        return this.mainController as MainController
    }

    fun getActivity(): MainActivity {
        return this.activity as MainActivity
    }

    fun getKeys(): JSONObject {
        return this.keys
    }

    fun getLogin(): Login {
        return this.login
    }

    fun getRSA(): RSA {
        return this.rsa
    }

    fun encrypt(value: String): String {
        var publicKeyHex: String = this.keys.get("serverPublicKey") as String
        var publicKey: PublicKey = this.rsa.importPublicKey(publicKeyHex)

        return this.rsa.encryptPublicKey(publicKey, value)
    }

    fun decrypt(value: String): String {
        var privateKeyHex: String = this.keys.get("privateKey") as String
        var privateKey: PrivateKey = this.rsa.importPrivateKey(privateKeyHex)

        return this.rsa.decryptPrivateKey(privateKey, value)
    }
}