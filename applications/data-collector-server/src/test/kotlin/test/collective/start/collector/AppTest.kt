package test.collective.start.collector

import io.collective.data.getSystemEnv
import io.collective.database.DataCollectorDataGateway
import io.collective.database.getDbCollector
import io.collective.start.collector.module
import io.ktor.http.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class AppTest {
    @Before
    fun prepare() {
        mockkStatic(::getSystemEnv)
        mockkStatic(::getDbCollector)
        val testDbCollector = mockk<DataCollectorDataGateway>()

        every { getSystemEnv("DB_USER") } returns "TestUser"
        every { getSystemEnv("DB_PASS") } returns "TestPass"
        every { getSystemEnv("DB_URL") } returns "localhost"
        every { getSystemEnv("DB_PORT") } returns "5555"
        every { getDbCollector("TestUser", "TestPass", "localhost", "5555") } returns testDbCollector
    }

    @Test
    fun testEmptyHome() = testApp {
        handleRequest(HttpMethod.Get, "/").apply {
            assertEquals(200, response.status()?.value)
            assertTrue(response.content!!.contains("hi!"))
        }
    }

    private fun testApp(callback: TestApplicationEngine.() -> Unit) {
        withTestApplication({ module() }) { callback() }
    }
}