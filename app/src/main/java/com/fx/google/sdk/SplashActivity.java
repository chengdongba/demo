package com.fx.google.sdk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by Administrator on 2018/12/20.
 */

public class SplashActivity extends com.child.sdk.SplashActivity{
    @Override
    public void onSplashBefore(Context context, Bundle bundle) {

    }

    @Override
    public void onSplashFinish() {
        startActivity(new Intent(SplashActivity.this,MainActivity.class));
        finish();
    }
}
