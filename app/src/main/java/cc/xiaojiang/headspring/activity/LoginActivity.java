package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;
import cc.xiaojiang.headspring.iotkit.IotKitAccountImpl;
import cc.xiaojiang.headspring.model.MobThrowable;
import cc.xiaojiang.headspring.model.http.LoginBody;
import cc.xiaojiang.headspring.utils.ToastUtils;
import cc.xiaojiang.headspring.view.CommonTextView;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

//todo:回复
public class LoginActivity extends BaseActivity {

    @BindView(R.id.ctv_get_verify_code)
    CommonTextView mCtvGetVerifyCode;
    @BindView(R.id.et_phone_number)
    EditText mCetPhoneNumber;
    @BindView(R.id.et_verify_code)
    EditText mEtVerifyCode;
    private CountDownTimer mCountDownTimer;
    private EventHandler mEventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            runOnUiThread(() -> {
                if (data instanceof Throwable) {
                    Throwable throwable = (Throwable) data;
                    MobThrowable mobThrowable = new Gson().fromJson(throwable.getMessage
                            (), MobThrowable.class);
                    ToastUtils.show(mobThrowable.getDetail());
                    Logger.e(throwable.getMessage());
                } else {
                    if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        mCountDownTimer.start();
                        // 验证码获取成功
                        Logger.d("验证码获取成功");
                        ToastUtils.show(R.string.login_get_verification_code_success);
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SMSSDK.registerEventHandler(mEventHandler);
        initCounterTimer();
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }
    /**
     * 初始化获取验证码定时器
     */
    private void initCounterTimer() {
        mCountDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mCtvGetVerifyCode.setEnabled(false);
                String tip = String.format(getResources().getString(R.string.login_second_retry),
                        millisUntilFinished / 1000);
                mCtvGetVerifyCode.setText(tip);
            }

            @Override
            public void onFinish() {
                mCtvGetVerifyCode.setText(R.string.login_get_verify_code);
                mCtvGetVerifyCode.setEnabled(true);
            }
        };
    }

    @OnClick({R.id.ctv_get_verify_code, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ctv_get_verify_code:
                final String phoneNumber = mCetPhoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.show(getString(R.string.login_toast_phone_not_null));
                    return;
                }
                SMSSDK.getVerificationCode("86", phoneNumber);
                break;
            case R.id.btn_login:
                login();
                break;
            default:
                break;
        }
    }

    private void login() {
        final String phoneNumber = mCetPhoneNumber.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.show(getString(R.string.login_toast_phone_not_null));
            return;
        }
        final String verifyCode = mEtVerifyCode.getText().toString();
        if (TextUtils.isEmpty(verifyCode)) {
            ToastUtils.show(getString(R.string.login_toast_verify_code_not_null));
            return;
        }
        LoginBody loginBody = new LoginBody();
        loginBody.setTelphone(Long.parseLong(phoneNumber));
        loginBody.setVerifyCode(Integer.parseInt(verifyCode));
        loginBody.setSource(IotKitAccountImpl.APP_Source);
        loginBody.setDeveloperKey(IotKitAccountImpl.DEVELOP_KEY);
        loginBody.setDeveloperSecret(IotKitAccountImpl.DEVELOP_SECRET);
        IotKitAccountManager.getInstance().login(this, loginBody, new IotKitAccountCallback() {
            @Override
            public void onSuccess() {
                Logger.d("login success");
                startToActivity(MainActivity.class);
                finish();
            }

            @Override
            public void onFailed(String msg) {
                Logger.e("login failed");

            }
        });
    }

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterEventHandler(mEventHandler);
        mCountDownTimer.cancel();
        super.onDestroy();
    }
}