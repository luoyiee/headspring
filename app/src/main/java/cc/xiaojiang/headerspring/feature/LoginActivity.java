package cc.xiaojiang.headerspring.feature;

import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.baselibrary.http.model.BaseModel;
import cc.xiaojiang.baselibrary.http.progress.ProgressObserver;
import cc.xiaojiang.baselibrary.util.RxUtils;
import cc.xiaojiang.baselibrary.util.ToastUtils;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;
import cc.xiaojiang.headerspring.http.HttpResultFunc;
import cc.xiaojiang.headerspring.http.RetrofitHelper;
import cc.xiaojiang.headerspring.iotkit.IotKitAccountImpl;
import cc.xiaojiang.headerspring.model.MobThrowable;
import cc.xiaojiang.headerspring.model.http.LoginBody;
import cc.xiaojiang.headerspring.model.http.LoginModel;
import cc.xiaojiang.headerspring.utils.DbUtils;
import cc.xiaojiang.headerspring.view.CommonTextView;
import cc.xiaojiang.iotkit.IotKit;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.iotkit.account.IotKitLoginParams;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

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
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void createInit() {
        SMSSDK.registerEventHandler(mEventHandler);
        initCounterTimer();
    }

    @Override
    protected void resumeInit() {

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
//        startToActivity(ProductListActivity.class);
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
        loginBody.setDeveloperId(IotKitAccountImpl.DEVELOP_KEY);
        loginBody.setName(null);
        IotKitLoginParams iotKitLoginParams = new IotKitLoginParams();
        iotKitLoginParams.setArg1(loginBody);
        iotKitLoginParams.setContext(this);
        IotKitAccountManager.getInstance().login(iotKitLoginParams, new IotKitAccountCallback() {
            @Override
            public void onSuccess() {
                Logger.d("login success");
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