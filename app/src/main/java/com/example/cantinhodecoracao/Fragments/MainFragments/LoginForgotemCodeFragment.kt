package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import android.util.Log
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
import com.example.cantinhodecoracao.Dialog.Loading
import com.example.cantinhodecoracao.Models.Login
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.Request.RequestJSON
import com.example.cantinhodecoracao.Util.Information
import com.example.cantinhodecoracao.ViewModels.LoginViewModel
import org.json.JSONObject

class LoginForgotemCodeFragment: Fragment() {
    private val model: LoginViewModel by activityViewModels()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_forgotem_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.setProperties(view)
        this.listen(view)
    }

    private fun setProperties(view: View) {
        var login = this.model.getLogin()

        view.findViewById<TextView>(R.id.forgotem_code_view).text = login.getEmail()
    }

    private fun listen(view: View) {
        view.findViewById<Button>(R.id.forgotem_code_verification_button).setOnClickListener {
            Log.i("click", "click forgotem")
            var loading: Loading = Loading(this.model.getActivity())
            loading.show()

            val login: Login = this.model.getLogin()
            val jsonObject: JSONObject = JSONObject()

            var code: String = view.findViewById<EditText>(R.id.code_forgotem_input).text.toString()

            login.setCode(Integer.parseInt(code))
            this.model.setLogin(login)

            jsonObject.put(this.model.encrypt("email"), this.model.encrypt(login.getEmail()))
            jsonObject.put(this.model.encrypt("code"), this.model.encrypt(code))

            RequestJSON()
                    .setUrl("/api/v1/forgotem/code")
                    .setMethod("post")
                    .appendBody("forgotem", jsonObject)
                    .call(this.model.getActivity(), fun(error: Exception?, result: JSONObject?) {
                        loading.close()
                        if (error !== null) {
                            Information()
                                    .setTitle("Error")
                                    .setMessage("Código inválido")
                                    .setPositiveButtonLabel("Ok")
                                    .show(this.model.getActivity(), fun(click: JSONObject) {
                                    })
                        } else {
                            findNavController().navigate(R.id.action_code_to_forgotem)
                        }
                    })
        }
    }
}