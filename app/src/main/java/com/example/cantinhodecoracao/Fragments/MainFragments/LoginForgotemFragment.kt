package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class LoginForgotemFragment: Fragment() {
    private val model: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotem, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setProperties(view)
        this.listen(view)
    }

    private fun setProperties(view: View) {
        val login: Login = this.model.getLogin()

        view.findViewById<TextView>(R.id.forgotem_fragment_email_view).text = login.getEmail()
    }

    private fun listen(view: View) {
        view.findViewById<Button>(R.id.forgotem_button).setOnClickListener {
            var loading: Loading = Loading(this.model.getActivity())
            loading.show()

            var pass: String = view.findViewById<EditText>(R.id.password_forgotem).text.toString()
            var conf: String = view.findViewById<EditText>(R.id.confirm_password_forgotem).text.toString()

            var hashPass: String = Crypto().hash(pass)
            var hashConf: String = Crypto().hash(conf)

            if (pass != conf) {
                loading.close()
                Information()
                        .setTitle("Error")
                        .setMessage("Senha está diferente da confirmação de senha")
                        .setPositiveButtonLabel("Ok")
                        .show(this.model.getActivity(), fun(click: JSONObject) {
                         })
            } else {
                val login: Login = this.model.getLogin()
                val jsonObject: JSONObject = JSONObject()

                login.setSenha(pass)
                login.setConfirm(conf)

                this.model.setLogin(login)

                jsonObject.put(this.model.encrypt("email"), this.model.encrypt(login.getEmail()))
                jsonObject.put(this.model.encrypt("code"), this.model.encrypt(login.getCode().toString()))
                jsonObject.put(this.model.encrypt("senha"), this.model.encrypt(hashPass))
                jsonObject.put(this.model.encrypt("confirm"), this.model.encrypt(hashConf))

                RequestJSON()
                        .setUrl("/api/v1/forgotem/change")
                        .setMethod("post")
                        .appendBody("forgotem", jsonObject)
                        .call(
                                this.model.getActivity(),
                                fun (response: ResponseServer) {
                                    loading.close()
                                    if (response.resultError()) {
                                        response.openDialog(
                                                this.model.getActivity(),
                                                "Erro na mudança da chave",
                                                null,
                                                "Ok",
                                                null,
                                                fun (click: JSONObject) {}
                                        )
                                    } else {
                                        response.openDialog(
                                                this.model.getActivity(),
                                                "Sucesso",
                                                "Senha alterada",
                                                "Ok",
                                                null,
                                                fun (click: JSONObject) {
                                                    findNavController().navigate(R.id.action_forgotem_to_login)
                                                }
                                        )
                                    }
                                }
                        )
            }
        }
    }
}