package com.designedby6pm.awesomecalc.view

import org.junit.Test

import org.junit.Assert.*
import java.util.*
import kotlin.test.*

class CalculationTest {

    @Test
    fun calculateSucess() {
        val calc1 = Calculation("2*2", null, false, Date())
        try {
            calc1.calculate()
            assertEquals(calc1.result, 4.0)
        } catch (ex: Exception) {
            println("ERROR $ex")
            assertNotNull(ex)
        }
    }

    @Test
    fun calculateFail() {
        val calc2 = Calculation("2*3", null, false, Date())
        try {
            calc2.calculate()
            assertNotEquals(calc2.result, 5.0)
        }  catch (ex: Exception) {
            println("ERROR $ex")
            assertNotNull(ex)
        }
    }

    @Test (expected = AssertionError::class)
    fun calculateAssertionError() {
        val calc3 = Calculation("2x*3", null, false, Date())
        assertFailsWith<AssertionError> { calc3.calculate() }
    }

    @Test (expected = AssertionError::class)
    fun calculateAssertionError1() {
        val calc4 = Calculation("1/0", null, false, Date())
        assertFailsWith<AssertionError> { calc4.calculate() }
    }
}