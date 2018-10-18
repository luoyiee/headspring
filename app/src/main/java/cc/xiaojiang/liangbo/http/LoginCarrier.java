package cc.xiaojiang.liangbo.http;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author :jinjiafeng
 * date:  on 18-7-27
 * description:
 */
public class LoginCarrier implements Parcelable{
    public String mTargetAction;
    public Bundle mBundle;

    private LoginCarrier(Parcel in) {
        mTargetAction = in.readString();
        mBundle = in.readBundle(Bundle.class.getClassLoader());
    }

    /**
     * 重新构造函数  用于传参
     *
     * @param target 目标activity
     * @param bundle 向目标传递的参数
     */
    public LoginCarrier(String target, Bundle bundle) {
        mTargetAction = target;
        mBundle = bundle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * 通过writeToParcel将你的对象映射成Parcel对象
     *
     * @param dest
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTargetAction);
        dest.writeBundle(mBundle);
    }

    /**
     * 已经登录的状态下执行的方法
     *
     * @param ctx
     */
    public void invoke(Context ctx) {
        Intent intent = new Intent();
        intent.setClassName(ctx, mTargetAction);
        if (mBundle != null) {
            intent.putExtras(mBundle);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ctx.startActivity(intent);
    }

    public static final Creator<LoginCarrier> CREATOR = new Creator<LoginCarrier>() {
        @Override
        public LoginCarrier createFromParcel(Parcel in) {
            return new LoginCarrier(in);
        }

        @Override
        public LoginCarrier[] newArray(int size) {
            return new LoginCarrier[size];
        }
    };
}
