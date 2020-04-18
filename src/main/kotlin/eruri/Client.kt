package eruri

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.riversun.okhttp3.OkHttp3CookieHelper


class Client {
    private val cookieHelper = OkHttp3CookieHelper()
    private var client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(cookieHelper.cookieJar())
        .build()

    private var loggedIn = false

    private fun createUrl(path: String): String {
        return "https://eruri.kangwon.ac.kr${path}"
    }

    fun login(username: String, password: String): Response {
        val request = Request.Builder()
            .url(createUrl("/login/index.php"))
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            )
            .post("{\"username\": \"$username\", \"password\": \"$password\"}".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        val response = client.newCall(request).execute()

        if (response.code != 200) {
            throw Error("Login failed; http status code: ${response.code}")
        }

        loggedIn = true
        
        return response
    }

    fun createBuilder(path: String): Request.Builder {
        if (!loggedIn) throw Error("You can't create request builder before login.")
        return Request.Builder()
            .url(createUrl(path))
            .header(
                "Referer",
                "https://eruri.kangwon.ac.kr/"
            )
            .header(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            )
    }

    fun execute(request: Request): Response {
        return client.newCall(request).execute()
    }
}