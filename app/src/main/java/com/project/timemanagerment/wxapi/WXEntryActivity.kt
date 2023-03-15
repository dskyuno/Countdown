package com.project.timemanagerment.wxapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.timemanagerment.R
import com.project.timemanagerment.base.constant.Constants
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory


class WXEntryActivity : AppCompatActivity() {
    lateinit var wxApi: IWXAPI
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wxentry)
        //registerWX
        wxApi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, true);
        wxApi.handleIntent(Intent(), object : IWXAPIEventHandler {
            override fun onReq(p0: BaseReq?) {

            }

            override fun onResp(p0: BaseResp?) {

            }

        })

    }
}