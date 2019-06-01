package com.qyj.google.sdk;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.child.sdk.BaseChildApi;
import com.common.lib.entity.CosumerData;
import com.common.lib.entity.ProductData;
import com.common.lib.entity.UserInfo;
import com.common.lib.listener.AppLoginListener;
import com.common.lib.listener.FbShareListener;
import com.common.lib.listener.FloatAccountChangeListner;
import com.common.lib.listener.LogoutListener;
import com.common.lib.listener.OnGetServerListListener;
import com.common.lib.sdk.GameAction;
import com.common.lib.utils.CommonUtil;
import com.common.lib.utils.FastJson;
import com.common.lib.utils.L;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.ShareMediaContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.model.ShareVideo;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;

import static com.common.lib.BaseCommonApi.sCallbackManager;
import static com.common.lib.sdk.Constant.ERROR_MESSAGE_GOOGLE_PAY;
import static com.common.lib.sdk.Constant.REQUEST_CODE_LOGIN;
import static com.common.lib.sdk.Constant.REQUEST_CODE_PAY;
import static com.common.lib.sdk.Constant.RESULT_CODE_FACEBOOK_LOGIN_SUCCESS;
import static com.common.lib.sdk.Constant.RESULT_CODE_GOOGLE_LOGIN_SUCCESS;
import static com.common.lib.sdk.Constant.RESULT_CODE_GOOGLE_PAY_FAILED;
import static com.common.lib.sdk.Constant.RESULT_CODE_GOOGLE_PAY_SUCCESS;
import static com.common.lib.sdk.Constant.RESULT_CODE_LOGIN_SUCCESS;
import static com.common.lib.sdk.Constant.SUCCESS_MESSAGE_LOGIN;

public class MainActivity extends AppCompatActivity {
    private BaseChildApi baseChildApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        baseChildApi = new BaseChildApi(this);
        baseChildApi.active();
        //注册悬浮窗切换账号监听
        baseChildApi.setOnFloatAccountChangeListener(new FloatAccountChangeListner() {
            @Override
            public void onsuccess() {
                baseChildApi.logout(new LogoutListener() {
                    @Override
                    public void onLogoutSuccess() {
//                        Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                        CommonUtil.commitGameExit("123123");
                        baseChildApi.checkIsLogin(MainActivity.this, new AppLoginListener() {
                            @Override
                            public void onLoginSuccess(String s) {
                                baseChildApi.showFloatView(MainActivity.this);
                                CommonUtil.commitGameLogin("123123");
                            }

                            @Override
                            public void onLoginFailed(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onLogoutFailed(String errorMsg) {
                        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onfailed() {

            }
        });

        findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseChildApi.logout(new LogoutListener() {
                    @Override
                    public void onLogoutSuccess() {
                        Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();
                        CommonUtil.commitGameExit("123123");
                        baseChildApi.checkIsLogin(MainActivity.this, new AppLoginListener() {
                            @Override
                            public void onLoginSuccess(String s) {
                                baseChildApi.showFloatView(MainActivity.this);
                                CommonUtil.commitGameExit("123123");
                            }

                            @Override
                            public void onLoginFailed(int i, String s) {
                                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onLogoutFailed(String errorMsg) {
                        Toast.makeText(MainActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        findViewById(R.id.google_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String productId = "qyj_14.99";
                String extension = "透传参数111";//如果cp没有透传参数,请传空字符串

                CosumerData cosumerData = new CosumerData();
                cosumerData.setRole_id("3908415944392762");//角色id
                cosumerData.setRole_name("嘎董");//角色姓名
                cosumerData.setServer_id("63");//游戏服务器id

                baseChildApi.sdkPay(MainActivity.this, productId, cosumerData, extension);

            }
        });

        findViewById(R.id.wap_pay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CosumerData cosumerData = new CosumerData();
                cosumerData.setRole_id("3908415944392738");//角色id
                cosumerData.setRole_name("晉↑豪");//角色姓名
                cosumerData.setServer_id("63");//平台游戏服ID
                cosumerData.setCpsid("909999");//cp游戏服ID
                String extension = "透传参数";//如果cp没有透传参数,请传空字符串
                baseChildApi.webPay(MainActivity.this, cosumerData, extension);
            }
        });

        findViewById(R.id.sdk_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseChildApi.sdkRegister(MainActivity.this);
            }
        });

        findViewById(R.id.sdk_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                baseChildApi.sdkLogin(MainActivity.this);
                baseChildApi.checkIsLogin(MainActivity.this, new AppLoginListener() {
                    @Override
                    public void onLoginSuccess(String s) {
                        baseChildApi.showFloatView(MainActivity.this);
                        L.e("登陆成功--->", s);
                        UserInfo userInfo = (UserInfo) FastJson.pareseObject(s, UserInfo.class);
                        CommonUtil.commitGameLogin("123123");
                    }

                    @Override
                    public void onLoginFailed(int i, String s) {
                        L.e("登陆失败--->", s);
                    }
                });

            }
        });

        findViewById(R.id.email_verify).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseChildApi.emailVerify(MainActivity.this);
            }
        });


        findViewById(R.id.get_server_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseChildApi.getServerList("0", new OnGetServerListListener() {
                    @Override
                    public void onSuccess(String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(int i, String s) {
                        Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        testShare();
//        submmitInfo();

//        submmitPayInfo();

    }

    private void testShare() {
        findViewById(R.id.fb_share_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseChildApi.fbShare(MainActivity.this, "https://www.2a.com/godsfall/wap/event/facebook.html", new FbShareListener() {
                    @Override
                    public void onSuccess(Sharer.Result result) {
                        Toast.makeText(MainActivity.this,"share success",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {
                        Toast.makeText(MainActivity.this,"user canceled",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException e) {
                        Toast.makeText(MainActivity.this,"share error",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void submmitPayInfo() {
        ProductData productData = new ProductData();
        productData.setPrice_amount("590");//商品价格,只能填数值不要带货币单位
        productData.setId("hy_590");//product_id
        productData.setPrice_currency_code("TWD");//currency_code,货币单位
        baseChildApi.submitPayInfo(this, productData);
    }

    private void submmitInfo() {
        CosumerData cosumerData = new CosumerData();
        cosumerData.setRole_id("3908415944392762");//角色id
        cosumerData.setRole_name("嘎董");//角色姓名
        cosumerData.setRole_level("20");//角色等级
        cosumerData.setServer_id("63");//平台游戏服ID
        cosumerData.setCpsid("909999");//cp游戏服ID

//        baseChildApi.submitUserInfo(this, GameAction.ENTER_SERVER,cosumerData);
//        baseChildApi.submitUserInfo(this, GameAction.CREATE_ROLE,cosumerData);
//        baseChildApi.submitUserInfo(this, GameAction.LEVEL_ACHEVIED_65,cosumerData);
//        baseChildApi.submitUserInfo(this, GameAction.LEVEL_ACHEVIED_100,cosumerData);
//        baseChildApi.submitUserInfo(this, GameAction.LEVEL_ACHEVIED_150,cosumerData);
        baseChildApi.submitUserInfo(this, GameAction.LEVEL_ACHEVIED, cosumerData);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_LOGIN: //sdk登陆註冊
                onLoginResult(resultCode, data);
                break;
            case REQUEST_CODE_PAY: //sdk支付
                onPayResult(resultCode, data);
                break;
            default:
                break;
        }
        if (null!=sCallbackManager){
            sCallbackManager.onActivityResult(requestCode,resultCode,data);//fb分享
        }
    }

    private void onPayResult(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CODE_GOOGLE_PAY_SUCCESS:
                Toast.makeText(MainActivity.this, "支付校驗成功", Toast.LENGTH_SHORT).show();
                break;
            case RESULT_CODE_GOOGLE_PAY_FAILED:
                String errorMsg = "支付校驗失敗";
                if (data != null && data.hasExtra(ERROR_MESSAGE_GOOGLE_PAY)) {
                    errorMsg = data.getStringExtra(ERROR_MESSAGE_GOOGLE_PAY);
                }
//                Toast.makeText(MainActivity.this,errorMsg, Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private void onLoginResult(int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_CODE_LOGIN_SUCCESS://账号密码登陆成功
            case RESULT_CODE_FACEBOOK_LOGIN_SUCCESS://facebook登陆成功
            case RESULT_CODE_GOOGLE_LOGIN_SUCCESS://google登陆成功
                L.e("login-->", " google");
                if (data.hasExtra(SUCCESS_MESSAGE_LOGIN)) {
                    String msg = data.getStringExtra(SUCCESS_MESSAGE_LOGIN);
                    UserInfo userInfo = (UserInfo) FastJson.pareseObject(msg, UserInfo.class);
                }
                baseChildApi.showFloatView(this);
                CommonUtil.commitGameLogin("123123");
                break;
            default:
                break;
        }

    }


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        CommonUtil.commitGameExit("123123");
        baseChildApi.destroySDK();

        super.onDestroy();
    }

}
