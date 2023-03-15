package com.project.timemanagerment

import com.project.timemanagerment.base.util.DateFormatUtil
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun testCalendarDateInterval() {
        val targetTimeYMD =  DateFormatUtil.getFormatYMD(Date().time)
        print(targetTimeYMD)
    }
    @Test
    fun testGetTomorrow(){
        val calendar = Calendar.getInstance()
        val today = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time
        print(today)
        print(beginOfDay(tomorrow))
    }
    fun beginOfDay(date: Date?): Date? {
        val cal = Calendar.getInstance()
        cal.time = date
        cal[Calendar.HOUR_OF_DAY] = 0
        cal[Calendar.MINUTE] = 0
        cal[Calendar.SECOND] = 0
        cal[Calendar.MILLISECOND] = 0
        return cal.time
    }
    @Test
    fun testEmpty(){
        val s =" "
       val bb =  s.isNullOrEmpty()
        print(bb)
    }
}