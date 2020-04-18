package eruri

import io.github.cdimascio.dotenv.Dotenv
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ClientTest {
    private val client = Client()
    private val dotEnv = Dotenv.load()
    private val username = dotEnv["ERURI_USERNAME"]
    private val password = dotEnv["ERURI_PASSWORD"]

    @Test
    fun login() {
        val response = client.login(username!!, password!!)
        assertThat(response.isSuccessful, CoreMatchers.`is`(true))
    }

    @Test
    fun mainPage() {
        login()

        val request = client.createBuilder("/").build()

        val response = client.execute(request)

        assertThat(response.body!!.string(), CoreMatchers.containsString("My Page"))
    }
}