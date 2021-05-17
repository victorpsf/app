package com.example.cantinhodecoracao.Controllers

import com.example.cantinhodecoracao.MainActivity
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.Util.Convert
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject
import java.security.PublicKey
import com.example.cantinhodecoracao.Controllers.BaseController

class MainController(mainActivity: MainActivity, model: LoginViewModel): BaseController(mainActivity, JSONObject()) {
    private var main_activity = mainActivity
    public var loginViewModel: LoginViewModel = model

    fun load() {
        this.loginViewModel = this.loginViewModel
                .setActivity(this.main_activity)
                .setController(this)
                .setLogin(Login())
                .setMainController(this)

        this.loading.show()
        this.generateKeys()

        this.loginViewModel.setKeys(this.keys)

        this.sync()
    }

    fun getKeysString(): String {
        return this.keys.toString()
    }

    private fun sync() {
        try {
            if (!(RequestJSON().connected(this.main_activity))) throw Exception("3")
            this.callServerSync(this, fun () {
                this.callAndSendTansferPublicKey(this, fun (response: JSONObject) {
                    this.importServerKey(response)
                })
            })
        } catch(error: Exception) {
            this.printErrorAndExit("problema interno", error.message.toString(), "Ok")
        }

    }

    private fun callServerSync(mainController: MainController, listiner: () -> Unit) {
        RequestJSON()
                .setMethod("get")
                .setUrl("/api/v1")
                .call(
                        this.main_activity,
                        fun (response: ResponseServer) {
                            if (response.resultError()) {
                                response.openDialog(
                                        mainController.main_activity,
                                        "Falha no inicio da sincronização",
                                        "Não foi possível concluir a sincronização",
                                        "Ok",
                                        null,
                                        fun (click: JSONObject) {}
                                )
                            } else {
                                listiner()
                            }
                        }
                )
    }

    private fun callAndSendTansferPublicKey(mainController: MainController ,listiner: (json: JSONObject) -> Unit) {
        RequestJSON()
                .setMethod("post")
                .setUrl("/api/v1")
                .appendBody("publicKey", this.keys.get("publicKey"))
                .call(
                        this.main_activity,
                        fun (response: ResponseServer) {
                            if (response.resultError()) {
                                response.openDialog(
                                        mainController.main_activity,
                                        "Falha no processo de sincronização",
                                        "Algo ocorreu inesperadamente",
                                        "Ok",
                                        null,
                                        fun (click: JSONObject) {}
                                )
                            } else {
                                listiner(response.getResult() as JSONObject)
                            }
                        }
                )
    }

    fun importServerKey(result: JSONObject) {
        var serverPublicKeyString: Any = result.get("publicKey")

        try {
            if (serverPublicKeyString == null) throw Exception("")
            var serverPublicKey: PublicKey = this.rsa.importPublicKey(serverPublicKeyString as String)

            this.keys.put("serverPublicKey", Convert().byteArrayToHex(serverPublicKey.encoded))
            this.loginViewModel.setKeys(this.keys)
        } catch (error: Exception) {
            return this.printErrorAndExit("falha na importação da chave", "sem importar chave, criptografia não ocorre.", "Ok")
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