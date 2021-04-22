package com.example.cantinhodecoracao.Util

import org.json.JSONObject

class JSONReader {
    fun read(json: JSONObject, keys: Array<String>, count: Int = 0): Any {
        var key: String = keys.get(count)
        var value: Any;

        try {
            value = json.get(key)
        } catch(error: Exception) {
            value = ""
        }

        if (value is JSONObject) return this.read(value, keys, count + 1);
        return value as Any
    }
}