package com.example.cantinhodecoracao.Util

import org.json.JSONObject

class JSONReader {
    private var json: JSONObject? = null
    private var count: Int = 0

    constructor(json: JSONObject) {
        this.json = json
    }

    fun read(keys: Array<String>): Any {
        var key: String = keys.get(this.count)
        this.count++
        var value: Any? = null;

        try {
            value = this.json?.get(key)
        } catch(error: Exception) { value = "" }

        if (value is JSONObject) return this.read(keys);
        return value as Any
    }
}