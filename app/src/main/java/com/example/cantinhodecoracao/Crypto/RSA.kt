package com.example.cantinhodecoracao.Crypto

import android.util.Log
import com.example.cantinhodecoracao.Util.Convert
import org.json.JSONObject
import java.security.KeyPairGenerator

class RSA {
    fun gereratePair(): JSONObject {
        var keysPair = JSONObject()
        var pair = KeyPairGenerator.getInstance("RSA")
        pair.initialize(2048)

        var keys = pair.genKeyPair()


        keysPair.put("publicKey", Convert().byteArrayToHex(keys.public.encoded))
        keysPair.put("privateKey", Convert().byteArrayToHex(keys.private.encoded))
        return keysPair
    }

    fun importPublicKey(key: String) {

    }
}