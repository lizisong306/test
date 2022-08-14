package com.maidiantech.wxapi;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.maidiantech.ActiveActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;


import application.MyApplication;

/**
 * Created by lizisong on 2017/7/17.
 */

public class WXPayEntryActivity extends ActiveActivity implements IWXAPIEventHandler {
    private static IWXAPI api;
    private static OnWeiChatPayListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
                api = MyApplication.api;
                api.handleIntent(getIntent(), this);
        }catch(Exception e){

        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api = MyApplication.api;
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if(baseResp.errCode == 0){
                if(listener !=null){
                    Log.d("lizisong", "支付成功的回调");
                    listener.onSuccess();
                }
//				Toast.makeText(WXPayEntryActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
            }else{
                if(listener !=null){

                    Log.d("lizisong", "支付失败的回调"+baseResp.errCode+","+baseResp.transaction+","+baseResp.errStr);
                    listener.onFailed(baseResp.errStr);
                }
//				Toast.makeText(WXPayEntryActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
            }
            WXPayEntryActivity.this.finish();
        }
    }

    public static void WeiChatPay(OnWeiChatPayListener listener){
        WXPayEntryActivity.listener = listener;
    }

    public interface OnWeiChatPayListener{
        public void onSuccess();
        public void onFailed(String errMessage);
    }


}
