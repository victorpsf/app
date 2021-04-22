package com.example.cantinhodecoracao.Controllers

import android.util.Log
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.MainActivity
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Util.Convert
import com.example.cantinhodecoracao.Util.Directory
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.Util.JSONReader
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject
import java.security.PublicKey

class MainController {
    private var main_activity: MainActivity
    private var loading: Loading
    private var keys: JSONObject = JSONObject()
    private var directory: Directory;
    private var loginViewModel: LoginViewModel
    private var rsa: RSA = RSA()

    constructor(mainActivity: MainActivity, model: LoginViewModel) {
        this.main_activity = mainActivity
        this.directory = Directory(this.main_activity)
        this.loading = Loading(this.main_activity)
        this.loginViewModel = model
                .setActivity(this.main_activity)
                .setController(this)
                .setLogin(Login())
                .setMainController(this)
    }

    fun load() {
        this.loading.show()
        this.generateKeys()

        this.loginViewModel.setKeys(this.keys)

        this.sync()
    }

    private fun sync() {
        try {
            if (!(RequestJSON().connected(this.main_activity))) throw Exception("3")
            this.callServerSync(fun () {
                this.callAndSendTansferPublicKey(fun (response: JSONObject) {
                    this.importServerKey(response)
                })
            })
        } catch(error: Exception) {
            when(error.message.toString()) {
                "1" -> { this.printErrorAndExit("Falha no inicio da sincronização", "Não foi possível concluir a sincronização", "Ok") }
                "2" -> { this.printErrorAndExit("Falha no processo de sincronização", "Algo ocorreu inesperadamente", "Ok") }
                "3" -> { this.printErrorAndExit("Conexão", "Celular sem conexão com internet", "Ok") }
                "1233" -> { this.printErrorAndExit("conexão", "ocorreu um erro na conexão", "Ok") }
                else -> { this.printErrorAndExit("problema interno", error.message.toString(), "Ok") }
            }
        }

    }

    private fun callServerSync(listiner: () -> Unit) {
        RequestJSON()
                .setMethod("get")
                .setUrl("/api/v1")
                .call(
                        this.main_activity,
                        fun(error: Exception?, result: JSONObject?) {
                            if (error !== null) {
                                Log.e("error", error.toString())
                                throw Exception("1")
                            }
                            listiner();
                        }
                )
    }

    private fun callAndSendTansferPublicKey(listiner: (json: JSONObject) -> Unit) {
        RequestJSON()
                .setMethod("post")
                .setUrl("/api/v1")
                .appendBody("publicKey", this.keys.get("publicKey"))
                .call(
                        this.main_activity,
                        fun (error: Exception?, result: JSONObject?) {
                            if (error !== null) throw Exception("2")
                            listiner(result as JSONObject)
                        }
                )
    }

    fun importServerKey(result: JSONObject) {
        var serverPublicKeyString: Any = JSONReader().read(result, arrayOf("result", "publicKey"))

        try {
            var serverPublicKey: PublicKey = this.rsa.importPublicKey(serverPublicKeyString as String)

            this.keys.put("serverPublicKey", Convert().byteArrayToHex(serverPublicKey.encoded))
            this.loginViewModel.setKeys(this.keys)
        } catch (error: Exception) {
            return this.printErrorAndExit("falha na importação da chave", "sem importar chave criptografia não ocorre", "Ok")
        }

        this.loading.close()
    }

    private fun generateKeys() {
        try {
            var fileJSON = this.readKey()

            if (fileJSON.length != 0) {
                return this.convertFileInJSON(fileJSON)
            }
            return this.generateNewRSA()
        } catch (error: Exception) {
            when(error.message.toString()) {
                "1" -> { this.printErrorAndExit("Falha ao ler chave", "Ocorreu um erro interno", "Ok") }
                "2" -> { this.printErrorAndExit("Falha ao converter chave", "Ocorreu um erro interno", "Ok") }
                "3" -> { this.printErrorAndExit("Geração de chave", "Não foi possível criar chave de criptografia", "Ok") }
                "4" -> { this.printErrorAndExit("Armazenamento da chave", "Não foi possível salvar a chave de criptografia, pode indicar um problema interno.", "Ok") }
                else -> { this.printErrorAndExit("problema interno", error.message.toString(), "Ok") }
            }
        }
    }

    private fun readKey(): String {
        try {
            var fileJSON = directory.readFile("rsa", "keys.json")
            return fileJSON
        } catch(error: Exception) {
            throw Exception("1")
        }
    }

    private fun convertFileInJSON(file: String) {
        try {
            this.keys = Convert().stringToJSON(file)
        } catch (error: Exception) {
            // falha na conversão
            var deleted: Boolean = this.directory.deleteFile("rsa", "keys.json")

            if (deleted) return this.generateKeys()
            throw Exception("2")
        }
    }

    private fun generateNewRSA() {
        try {
            this.keys = this.rsa.gereratePair() as JSONObject
        } catch(error: Exception) {
            throw Exception("3")
        }

        return this.saveKey()
    }


    private fun saveKey() {
        try {
            this.directory.saveFile("rsa", "keys.json", this.keys.toString())
        } catch(error: Exception) {
            throw Exception("4")
        }
    }

    private fun printErrorAndExit(title: String, message: String, button: String) {
        this.loading.close()
        Information()
                .setTitle("Error: " + title)
                .setMessage(message)
                .setPositiveButtonLabel(button)
                .show(
                        this.main_activity,
                        fun(click: JSONObject) {
                            click;
                            this.main_activity.CloseApp()
                        }
                )
    }
}