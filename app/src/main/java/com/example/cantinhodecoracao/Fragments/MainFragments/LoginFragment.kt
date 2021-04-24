package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cantinhodecoracao.Crypto.Crypto
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.Util.Directory
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.Util.JSONReader
import com.example.cantinhodecoracao.Util.Validator
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {
    private val model: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setAttributesView(view)
        this.listen(view)
    }

    private fun setAttributesView(view: View) {
        var loginModel: Login = this.model.getLogin()

        view.findViewById<TextInputEditText>(R.id.inputEmail).setText(loginModel.getEmail())
        view.findViewById<TextInputEditText>(R.id.inputSenha).setText(loginModel.getSenha())
    }

    private fun listen(view: View) {
        view.findViewById<TextView>(R.id.forgotem_password_view).setOnClickListener {
            findNavController().navigate(R.id.action_login_to_forgotem_email)
        }

        view.findViewById<Button>(R.id.login_button).setOnClickListener {
            var email = view.findViewById<TextInputEditText>(R.id.inputEmail).text.toString()
            var senha = view.findViewById<TextInputEditText>(R.id.inputSenha).text.toString()

            if (!Validator().isValidEmail(email)) {
                Information()
                        .setTitle("Error")
                        .setMessage("E-mail inválido")
                        .setPositiveButtonLabel("Ok")
                        .show(this.model.getActivity(), fun (result: JSONObject) {  })
            } else {
                var loading: Loading = Loading(this.model.getActivity())
                var jsonObject: JSONObject = JSONObject()
                var loginModel: Login = this.model.getLogin()
                loading.show()

                loginModel.setEmail(email)
                loginModel.setSenha(senha)
                this.model.setLogin(loginModel)

                jsonObject.put(this.model.encrypt("email"), this.model.encrypt(loginModel.getEmail()))
                jsonObject.put(this.model.encrypt("senha"), this.model.encrypt(Crypto().hash(senha)))

                RequestJSON()
                        .setUrl("/api/v1/auth/")
                        .setMethod("post")
                        .appendBody("login", jsonObject)
                        .call(this.model.getActivity(), fun (response: ResponseServer) {
                            loading.close()
                            if (response.resultError()) {
                                response.openDialog(
                                        this.model.getActivity(),
                                        "Falha no login",
                                        null,
                                        "Ok",
                                        null,
                                        fun(click: JSONObject) { }
                                )
                            } else {
                                response.openDialog(
                                        this.model.getActivity(),
                                        "Sucesso no login",
                                        null,
                                        "Ok",
                                        null,
                                        fun(click: JSONObject) {
                                            findNavController().navigate(R.id.action_login_to_code)
                                        }
                                )
                            }
                        })
            }
        }
    }
}