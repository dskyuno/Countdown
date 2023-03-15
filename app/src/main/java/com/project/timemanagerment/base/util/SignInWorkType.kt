package com.project.timemanagerment.base.util

enum class SignInWorkType(val typeInt: Int, val typeString: String) {
    GENERAL(1, "普通"),
    URGENCY(2, "紧急");


    companion object {
        fun getSignInWorkTypeStringByInt(typeInt: Int): String {
            for (element in values()) {
                if (element.typeInt == typeInt) {
                    return element.typeString
                }
            }
            return "ERROR"
        }

        fun getSignInWorkTypeIntBYString(typeString: String): Int {
            for (element in values()) {
                if (element.typeString == typeString) {
                    return element.typeInt
                }
            }
            return 1
        }
    }
}