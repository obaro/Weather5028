package test.collective.start

import io.collective.start.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AppTest {

    @Ignore
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertTrue(response.content!!.contains("CSCA 5028 Final Project."))
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
//        withTestApplication({ module("TestUser", "TestPassword", "TestDB", "5555") }) { callback() }
    }
}
