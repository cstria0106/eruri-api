package eruri

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.*
import kotlin.collections.HashMap


class Client {
    private var loggedIn: Boolean = false
    private var client: OkHttpClient = OkHttpClient.Builder()
        .cookieJar(object : CookieJar {
            val cookies: HashMap<String, ArrayList<Cookie>> = HashMap()

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                for ((index, cookie) in cookies.withIndex()) {
                    print(cookie.name)
                    if (index != cookies.size - 1) {
                        print(", ")
                    }
                }

                println()

                if (this.cookies[url.host] == null) {
                    this.cookies[url.host] = ArrayList()
                }
                this.cookies[url.host]!!.addAll(cookies)
            }

            override fun loadForRequest(url: HttpUrl): List<Cookie> {
                if (cookies[url.host] == null) {
                    cookies[url.host] = ArrayList()
                }
                return cookies[url.host]!!.toList()
            }
        })
        .build()

    private fun createUrl(path: String): String {
        return "https://eruri.kangwon.ac.kr${path}"
    }

    fun login(username: String, password: String) {
        val request = Request.Builder()
            .url(createUrl("/login/index.php"))
            .header(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64)"
            )
            .post("{\"username\": \"$username\", \"password\": \"$password\"}".toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        client.newCall(request).execute()

        loggedIn = true
    }

    fun createBuilder(path: String): Request.Builder {
        return Request.Builder()
            .url(createUrl(path))
            .header(
                "user-agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Apple"
            )
    }

    fun execute(request: Request): Response {
        if (!loggedIn) throw Error("You can't send request before login.")
        return client.newCall(request).execute()
    }
}