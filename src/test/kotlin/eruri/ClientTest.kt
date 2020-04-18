package eruri

import io.github.cdimascio.dotenv.Dotenv
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class ClientTest {
    private val client = Client()
    private val dotEnv = Dotenv.load()
    private val username = dotEnv["USERNAME"]
    private val password = dotEnv["PASSWORD"]

    @Test
    fun login() {
        client.login(username!!, password!!)
    }

    @Test
    fun mainPage() {
        client.login(username!!, password!!)

        val request = client.createBuilder("/").build()
        val response = client.execute(request)

        assertThat(response.body!!.string(), CoreMatchers.containsString("My Page"))
    }
}