import org.junit.Test
import kotlin.test.assertEquals
class TestTest {
    @Test
    fun testParser(){
        assertEquals(arrayListOf(""),parser("{{}}"))
    }
}