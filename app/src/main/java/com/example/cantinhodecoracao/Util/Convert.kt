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
        var length: Int = hexadecimal.length
        var count: Int = 0
        var index: Int = 0
        var arrayBytes: ByteArray = ByteArray(length / 2)

        while(count <= length) {
            var hex: String

            if (count + 2 < length) {
                hex = hexadecimal.substring(count, count + 2)
            } else {
                hex = hexadecimal.substring(count)
            }


            if (hex.isNullOrEmpty()) {
                // nothing
            } else {
                var hexInt: Int = Integer.parseInt(hex, 16)
                arrayBytes.set(index, hexInt.toByte())

                index += 1
            }

            count += 2
        }

        return arrayBytes
    }

    fun stringToJSON(string: String): JSONObject {
        return JSONObject(string)
    }

}
