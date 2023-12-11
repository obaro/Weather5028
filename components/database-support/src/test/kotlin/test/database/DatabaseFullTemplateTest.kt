package test.database

import io.collective.database.DatabaseFullTemplate

import com.zaxxer.hikari.HikariDataSource
import org.junit.Ignore
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DatabaseFullTemplateTest() {
    private val dataSource = hikariDataSource()
    private val template = DatabaseFullTemplate(dataSource)

    @Ignore
    fun testFind() {
        val id = 42
        val sql = "select id, name from (select 42 as id, 'apples' as name) as dates where id = ?"

        val names = template.query(sql, { ps -> ps.setInt(1, id) }, { rs -> rs.getString(2) })
        assertEquals("apples", names[0])
    }

    @Ignore
    fun testFindObject() {
        val sql = "select id, name from (select 42 as id, 'apples' as name) as dates where id = ?"

        var actual = template.findBy(sql, { ps -> ps.getInt(1) }, 42)
        assertEquals(42, actual)

        actual = template.findBy(sql, { ps -> ps.getInt(1) }, 44)
        assertNull(actual)
    }
}