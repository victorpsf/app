package com.example.cantinhodecoracao.Util

import org.json.JSONObject

class Convert {
    fun byteArrayToHex(byteArray: ByteArray): String {
        var hex = StringBuffer(byteArray.size * 2)

        for(byte in byteArray) {
            hex.append(String.format("%02x", byte))
        }

        return hex.toString().toUpperCase()
    }

    fun hexToByteArray(hexadecimal: String): ByteArray {
        var arrayByte: ByteArray = ByteArray(hexadecimal.length / 2, { 0 })

        for (i in 0 until hexadecimal.length) {
            val index = i * 2
            val j: Int = hexadecimal.substring(index, index + 2).toInt(16)
            arrayByte.set(i, j.toByte())
        }

        return arrayByte
    }

    fun stringToJSON(string: String): JSONObject {
        return JSONObject(string)
    }

}
