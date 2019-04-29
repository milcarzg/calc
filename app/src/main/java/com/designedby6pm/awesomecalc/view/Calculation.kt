package com.designedby6pm.awesomecalc.view

import net.objecthunter.exp4j.ExpressionBuilder
import java.util.*

data class Calculation (var calculation: String = "",
                        var result: Double? = null,
                        var saved: Boolean = false,
                        var date: Date) {

    fun calculate() {
        result = ExpressionBuilder(calculation).build().evaluate()
    }

    fun reset() {
        calculation = ""
        result = null
        saved = false
        date = Date()
    }
}

