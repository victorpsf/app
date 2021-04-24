package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cantinhodecoracao.Crypto.RSA
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Request.ResponseServer
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.example.cantinhodecoracao.Util.Validator
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class LoginForgotemEmailFragment : Fragment() {
    private val model: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotem_email, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<Button>(R.id.forgotem_email_back_button).setOnClickListener {
            findNavController().navigate(R.id.action_forgotem_email_to_login)
        }

        view.findViewById<Button>(R.id.forgotem_email_send_button).setOnClickListener {
            var email: String = view.findViewById<TextInputEditText>(R.id.email_forgotem_input).text.toString()

            if (Validator().isValidEmail(email) == false) {
                Information()
                        .setTitle("error: invalid e-mail")
                        .setMessage("Invalid e-mail format, please inform other e-mail")
                        .setPositiveButtonLabel("Ok")
                        .show(this.model.getActivity(), fun (click: JSONObject) {})
            } else {
                var loading: Loading = Loading(this.model.getActivity())
                loading.show()

                var jsonObject: JSONObject = JSONObject()
                var login = this.model.getLogin()
                this.model.setLogin(login.setEmail(email))

                jsonObject.put(this.model.encrypt("email"), this.model.encrypt(email))

                RequestJSON()
                        .setUrl("/api/v1/forgotem")
                        .setMethod("post")
                        .appendBody("forgotem", jsonObject)
                        .call(
                            this.model.getActivity(),
                            fun (response: ResponseServer) {
                                loading.close()
                                if (response.resultError()) {
                                    response.openDialog(
                                            this.model.getActivity(),
                                            "Não foi possível enviar código",
                                            null,
                                            "Ok",
                                            null,
                                            fun (click: JSONObject) {}
                                    )
                                } else {
                                    response.openDialog(
                                            this.model.getActivity(),
                                            "Sucesso",
                                            "Código enviado",
                                            "Ok",
                                            null,
                                            fun (click: JSONObject) {
                                                findNavController().navigate(R.id.action_forgotem_email_to_code)
                                            }
                                    )
                                }
                            }
                        )
            }
        }
    }
}