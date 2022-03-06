package app.skyzar.skyzarmod.util

import com.google.common.collect.Maps
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.zip.GZIPInputStream

class HttpUtils {
    
    data class HttpRequest(val url: String, val method: String, val headers: Map<String, String>?, val body: String)
    data class HttpResponse(val status: Int, val headers: Map<String, String>, val body: String)
    
    fun sendRequest(req: HttpRequest): HttpResponse {
        var url = URL(req.url)
        val conn = url.openConnection() as HttpURLConnection

        conn.requestMethod = req.method
        conn.doOutput = true
        conn.setRequestProperty("Accept-Encoding", "gzip")

        if (req.headers != null) {
            for (header in req.headers) {
                conn.setRequestProperty(header.key, header.value)
            }
        }

        if (listOf("GET", "DELETE", "OPTIONS").contains(req.method)) {
            var status = conn.responseCode
            var content = ""
            var stream = if (status < 299) {
                conn.inputStream
            } else {
                conn.errorStream
            }

            stream = if (conn.contentEncoding == "gzip") {
                GZIPInputStream(stream)
            } else {
                stream
            }

            var reader = BufferedReader(InputStreamReader(stream))

            while (true) {
                content += reader.readLine() ?: break
            }

            reader.close()
            conn.disconnect()

            var headers = Maps.newHashMap<String, String>()

            for (i in 0 until conn.headerFields.size) {
                headers[conn.headerFields.keys.elementAt(i)] = conn.headerFields.values.elementAt(i).elementAt(0)
            }

            return HttpResponse(status, headers, content)

        } else if (listOf("POST", "PUT", "PATCH").contains(req.method)) {
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Content-Length", req.body.toByteArray(StandardCharsets.UTF_8).size.toString())
            var wr = OutputStreamWriter(conn.outputStream)
            wr.write(req.body)
            wr.flush()
            wr.close() //there probably is but idk any packages and raw http requests are probably faster anyways

            var status = conn.responseCode
            var content = ""
            var stream = if (status < 299) {
                conn.inputStream
            } else {
                conn.errorStream
            }


            stream = if (conn.contentEncoding == "gzip") {
                GZIPInputStream(stream)
            } else {
                stream
            }

            var reader = BufferedReader(InputStreamReader(stream))

            while (true) {
                content += reader.readLine() ?: break
            }

            reader.close()
            conn.disconnect()

            var headers = Maps.newHashMap<String, String>()

            for (i in 0 until conn.headerFields.size) {
                headers[conn.headerFields.keys.elementAt(i)] = conn.headerFields.values.elementAt(i).elementAt(0)
            }

            return HttpResponse(status, headers, content)
        }

        return HttpResponse(0, Maps.newHashMap(), "")
    }
} // that should work

