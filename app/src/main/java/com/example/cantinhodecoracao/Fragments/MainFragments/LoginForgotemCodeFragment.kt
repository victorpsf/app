package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.ViewModels.LoginViewModel

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

        view.findViewById<Button>(R.id.forgotem_code_verification_button).setOnClickListener {
            findNavController().navigate(R.id.action_code_to_forgotem)
        }
    }
}