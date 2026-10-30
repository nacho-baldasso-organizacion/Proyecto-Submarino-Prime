package submarino

import kotlin.test.Test
import kotlin.test.assertEquals

class TestPosicionCheck {
    @Test
    fun testPosicion() {
        val p = Posicion(5, 10)
        assertEquals(5, p.posX)
        assertEquals(10, p.posY)
    }
}
