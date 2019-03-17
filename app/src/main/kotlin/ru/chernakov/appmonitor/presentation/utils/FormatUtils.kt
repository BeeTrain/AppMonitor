package ru.chernakov.appmonitor.presentation.utils

import java.math.BigDecimal


class FormatUtils {
    companion object {

        const val KB: Long = 1024

        const val MB: Long = KB * 1024

        fun convertBytesToMB(bytes: Long): Double {
            val b: Double = bytes.toDouble()
            return b / MB
        }

        fun round(unrounded: Double): String {
            val bd = BigDecimal(unrounded)
            val rounded = bd.setScale(2, BigDecimal.ROUND_CEILING)
            return rounded.toString()
        }
    }
}