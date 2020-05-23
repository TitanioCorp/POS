package com.titaniocorp.pos.util

import java.util.*

object DateUtil {
    fun todayRange(): Pair<Long, Long> {
        val startDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val finishDate = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        return Pair(startDate.timeInMillis, finishDate.timeInMillis)
    }

    fun getRange(year: Int, month: Int, dayOfMonth: Int): Pair<Long, Long>{
        val startDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val finishDate = Calendar.getInstance().apply {
            set(Calendar.YEAR, year)
            set(Calendar.MONTH, month)
            set(Calendar.DAY_OF_MONTH, dayOfMonth)

            set(Calendar.HOUR_OF_DAY, 23)
            set(Calendar.MINUTE, 59)
            set(Calendar.SECOND, 59)
        }

        return Pair(startDate.timeInMillis, finishDate.timeInMillis)
    }
}