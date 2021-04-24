package com.example.cantinhodecoracao.Fragments.MainFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.cantinhodecoracao.R
import com.example.cantinhodecoracao.ViewModels.LoginViewModel

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

    }
}