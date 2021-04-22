package com.example.cantinhodecoracao.Util

import android.text.TextUtils
import android.util.Patterns

class Validator {
    fun isValidEmail(target: String): Boolean {
        return !TextUtils.isEmpty(target) &&
                target.length > 0 &&
                Patterns.EMAIL_ADDRESS.matcher(target).matches()
    }
}