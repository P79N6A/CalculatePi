package com.example.shixingyi.calculatepidemo

import java.math.BigDecimal
import java.math.RoundingMode

object CalculatePi {
   fun calculatePi(n: Int): Double {
        var digit = n
        var n = n.toDouble()
        n++
        var res = BigDecimal(n)
        return res.setScale(digit, RoundingMode.HALF_UP).toDouble()
    }
}