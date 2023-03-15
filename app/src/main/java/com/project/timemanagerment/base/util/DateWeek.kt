package com.project.timemanagerment.base.util

enum class DateWeek(val weekInt: Int, val weekString: String) {
    MON(1, "星期一"),
    TUE(2, "星期二"),
    WED(3, "星期三"),
    THU(4, "星期四"),
    FRI(5, "星期五"),
    SAT(6, "星期六"),
    SUN(7, "星期日");

    companion object {
        fun getWeekStringByWeekIndex(weekInt: Int): String {
            for (element in values()) {
                if (element.weekInt == weekInt) {
                    return element.weekString
                }
            }
            return "ERROR"
        }
    }
}