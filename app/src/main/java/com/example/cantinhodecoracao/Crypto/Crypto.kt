package com.example.cantinhodecoracao.Crypto

import com.example.cantinhodecoracao.Util.Convert
import java.security.MessageDigest

class Crypto {
    fun hash(value: String): String {
        var messageDigest: MessageDigest = MessageDigest.getInstance("SHA-512")
        var digest = messageDigest.digest(value.toByteArray())

        return Convert().byteArrayToHex(digest);
    }
}