package cc.xiaojiang.headerspring.feature;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import cc.xiaojiang.headerspring.R;
import cc.xiaojiang.headerspring.base.BaseActivity;

public class LoginActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {
    @BindView(R.id.rb_login_login)
    RadioButton rbLoginLogin;
    @BindView(R.id.rb_login_register)
    RadioButton rbLoginRegister;
    @BindView(R.id.rg_login)
    RadioGroup rgLogin;
    @BindView(R.id.fl_login)
    FrameLayout flLogin;
    private LoginFragment mLoginFragment;
    private RegisterFragment mRegisterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void createInit() {
        rgLogin.setOnCheckedChangeListener(this);
        rbLoginLogin.setChecked(true);
    }

    @Override
    protected void resumeInit() {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (checkedId == R.id.rb_login_login) {
            if (mLoginFragment == null) {
                mLoginFragment = LoginFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_login, mLoginFragment);

        }
        if (checkedId == R.id.rb_login_register) {
            if (mRegisterFragment == null) {
                mRegisterFragment = RegisterFragment.newInstance();
            }
            fragmentTransaction.replace(R.id.fl_login, mRegisterFragment);
        }
        fragmentTransaction.commit();
    }
}
