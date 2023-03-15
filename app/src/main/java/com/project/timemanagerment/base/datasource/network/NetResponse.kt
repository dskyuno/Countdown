package com.project.timemanagerment.base.datasource.network

class NetResponse<T>(
    var code: Int = 0,
    var message: String = "",
    var data: T? = null
) {
    constructor(code: Int, message: String) : this(code, message, null)

    fun isSuccess(): Boolean {
        return this.code == CODE_SUCCESS
    }

    companion object {
        private const val CODE_SUCCESS = 200
    }
}