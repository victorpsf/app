package com.example.cantinhodecoracao.ViewModels

import androidx.lifecycle.ViewModel
import com.example.cantinhodecoracao.Controllers.MainController
import com.example.cantinhodecoracao.Controllers.PageController
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.MainActivity
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.PageActivity
import org.json.JSONObject
import java.security.PrivateKey
import java.security.PublicKey

class PageViewModel: ViewModel() {
    private var pageController: PageController? = null
    private var activity: PageActivity? = null

    private var rsa: RSA = RSA()
    private var keys: JSONObject = JSONObject()

    fun setActivity(activity: PageActivity): PageViewModel {
        this.activity = activity
        return this
    }

    fun setController(pageController: PageController) : PageViewModel {
        this.pageController = pageController
        return this
    }

    fun setKeys(json: JSONObject): PageViewModel {
        this.keys = json
        return this
    }

    fun getController(): PageController {
        return this.pageController as PageController
    }

    fun getActivity(): PageActivity {
        return this.activity as PageActivity
    }

    fun getKeys(): JSONObject {
        return this.keys as JSONObject
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