package com.example.cantinhodecoracao.Crypto

import com.example.cantinhodecoracao.Util.Convert
import org.json.JSONObject
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher

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

    fun importPublicKey(key: String): PublicKey {
        var bytes: ByteArray = Convert().hexToByteArray(key)
        var keyFactory: KeyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePublic(X509EncodedKeySpec(bytes))
    }

    fun importPrivateKey(key: String): PrivateKey {
        var bytes: ByteArray = Convert().hexToByteArray(key)
        var keyFactory: KeyFactory = KeyFactory.getInstance("RSA")

        return keyFactory.generatePrivate(X509EncodedKeySpec(bytes))
    }

    fun encryptPublicKey(key: PublicKey, value: String): String {
        var cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        var byteArray: ByteArray = cipher.doFinal(value.toByteArray())
        return Convert().byteArrayToHex(byteArray)
    }

    fun decryptPrivateKey(key: PrivateKey, value: String): String {
        var cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding")
        cipher.init(Cipher.ENCRYPT_MODE, key)

        var byteArray: ByteArray = cipher.doFinal(Convert().hexToByteArray(value))
        return byteArray.toString()
    }
}