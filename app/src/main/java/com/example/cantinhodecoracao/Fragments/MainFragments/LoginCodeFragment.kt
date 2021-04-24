package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class LoginCodeFragment: Fragment() {
    private val model: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setAttributesView(view)
        this.listen(view)
    }

    private fun setAttributesView(view: View) {

    }

    private fun listen(view: View) {
        view.findViewById<Button>(R.id.code_login_verification_button).setOnClickListener {
            val loading: Loading = Loading(this.model.getActivity())
            loading.show()
            var codeString: String = view.findViewById<EditText>(R.id.code_login_input).text.toString()
            var login: Login = this.model.getLogin()
            login.setCode(Integer.parseInt(codeString))
            this.model.setLogin(login)

            var jsonObject: JSONObject = JSONObject()
            jsonObject.put(this.model.encrypt("email"), this.model.encrypt(login.getEmail()))
            jsonObject.put(this.model.encrypt("senha"), this.model.encrypt(Crypto().hash(login.getSenha())))
            jsonObject.put(this.model.encrypt("code"), this.model.encrypt(codeString))

            RequestJSON()
                    .setUrl("/api/v1/auth/code")
                    .setMethod("post")
                    .appendBody("login", jsonObject)
                    .call(this.model.getActivity(), fun (response: ResponseServer) {
                        loading.close()
                        if (response.resultError()) {
                            response.openDialog(
                                    this.model.getActivity(),
                                    "Còdigo de verificação",
                                    null,
                                    "Ok",
                                    null,
                                    fun (click: JSONObject) {}
                            )
                        } else {
                            var result: JSONObject = response.getResult() as JSONObject
                            var token = result.getString("token")

                            if (token.isEmpty()) {
                                response.openDialog(
                                        this.model.getActivity(),
                                        "Còdigo de verificação",
                                        "Não foi possível realizar login",
                                        "Ok",
                                        null,
                                        fun (click: JSONObject) {
                                            findNavController().navigate(R.id.action_login_code_to_login)
                                        }
                                )
                            } else {
                                response.openDialog(
                                        this.model.getActivity(),
                                        "Còdigo de verificação",
                                        null,
                                        "Ok",
                                        null,
                                        fun (click: JSONObject) {
                                            this.model.getActivity().handlePage(token)
                                        }
                                )
                            }
                        }
                    })
        }
    }
}