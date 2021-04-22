package com.example.cantinhodecoracao.Request

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject


class RequestJSON {
    private val baseURL: String = "http://192.168.90.102"
    private var url: String = ""
    private var method: String = "get"

    private var body: JSONObject = JSONObject()
    private var query: JSONObject = JSONObject()
    private var headers: HashMap<String, String> = HashMap()

    fun connected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        if (activeNetwork == null) return false
        return activeNetwork?.isConnectedOrConnecting == true
    }

    fun setUrl(url: String): RequestJSON {
        this.url = url
        return this
    }

    fun setMethod(method: String): RequestJSON {
        this.method = method.toLowerCase()
        return this
    }

    fun appendBody(key: String, value: Any): RequestJSON {
        this.body.put(key, value)
        return this
    }

    fun appendQuery(key: String, value: Any): RequestJSON {
        this.query.put(key, value)
        return this
    }

    fun appendHeader(key: String, value: String): RequestJSON {
        this.headers[key] = value
        return this
    }

    private fun getMethod(): Int {
        return when(this.method) {
            "get" -> {
                Request.Method.GET
            }
            "post" -> {
                Request.Method.POST
            }
            "put" -> {
                Request.Method.PUT
            }
            "delete" -> {
                Request.Method.DELETE
            }
            else -> {
                Request.Method.GET
            }
        }
    }

    private fun getQuery(): String {
        var len: Int = 0
        var count: Int = 0;
        var keys: MutableIterator<String> = this.query.keys()
        var queryString = ""

        for (key in keys) { len++; }

        for(key in keys) {
            queryString += if (count == 0) "?" + key + "=" + this.query.get(key).toString() else "&" + key + "=" + this.query.get(key).toString()
            count++;
        }

        this.query = JSONObject()
        return queryString
    }

    private fun getBody(): JSONObject {
        val json: JSONObject = this.body;
        this.body = JSONObject()
        return json;
    }

    private fun getUrl(): String {
        val url: String = this.url
        this.url = ""
        var query: String = this.getQuery()
        return this.baseURL + url + query
    }

    private fun getHeaders(headers: MutableMap<String, String>): MutableMap<String, String> {
        val keys: MutableSet<String> = this.headers.keys

        for(key in keys) {
            headers[key] = this.headers.getValue(key)
        }

        this.headers = HashMap()
        return headers
    }

    fun call(context: Activity, listiner: (error: Exception?, result: JSONObject?) -> Unit) {
        val queue: RequestQueue = Volley.newRequestQueue(context)

        val request = object : JsonObjectRequest(
                this.getMethod(),
                this.getUrl(),
                this.getBody(),
                fun(response) {
                    queue.stop()
                    listiner(null, response)
                },
                fun(error) {
                    listiner(error, null)
                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    var params: MutableMap<String, String> = super.getHeaders()
                    if (params.isEmpty()) params = mutableMapOf<String, String>()
                    return getHeaders(params)
            }
        }

        queue.add(
                request.setRetryPolicy(DefaultRetryPolicy(
                        30000,
                        0,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
                )
        )
    }
}