package cc.xiaojiang.liangbo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.PermissionManager.TPermissionType;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.orhanobut.logger.Logger;
import com.qiniu.android.storage.UploadManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;

import java.io.File;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.iotkit.account.IotKitAccountCallback;
import cc.xiaojiang.iotkit.account.IotKitAccountManager;
import cc.xiaojiang.liangbo.Constant;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.model.BaseModel;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.bean.AreaJsonBean;
import cc.xiaojiang.liangbo.model.event.LoginEvent;
import cc.xiaojiang.liangbo.model.http.UserInfoModel;
import cc.xiaojiang.liangbo.model.http.UserModifyBody;
import cc.xiaojiang.liangbo.utils.DbUtils;
import cc.xiaojiang.liangbo.utils.GetJsonDataUtil;
import cc.xiaojiang.liangbo.utils.ImageLoader;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.TakePhotoUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.ViewUtils;
import cc.xiaojiang.liangbo.widget.OptionPickerHelper;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.schedulers.Schedulers;

public class PersonalInfoActivity extends BaseActivity implements TakePhoto.TakeResultListener,
        InvokeListener, TextWatcher {
    private static final int SIZE_UPDATE_MAP = 6;
    private static final String FILE_NAME = "area.json";
    @BindView(R.id.civ_avatar)
    CircleImageView mCivAvatar;
    @BindView(R.id.et_nickname)
    EditText mEtNickname;
    @BindView(R.id.tv_phone_number)
    TextView mTvPhoneNumber;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.tv_area)
    TextView mTvArea;

    private TakePhoto mTakePhoto;
    private InvokeParam mInvokeParam;
    private OptionsPickerView mSexPicker;
    private TimePickerView mAgePicker;
    private ArrayMap<String, Object> mUpdateMap;
    private Date mDate = new Date();
    private File mSelectAvatarFile;
    private String mSex, province, city;
    private ArrayList<AreaJsonBean> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private OptionsPickerView pvCityOptions;
    private UploadManager mUploadManager;
    private String mNickname = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUpdateMap = new ArrayMap<>(SIZE_UPDATE_MAP);
        Schedulers.io().scheduleDirect(this::initJsonData);
        mEtNickname.addTextChangedListener(this);
        RetrofitHelper.getService().userInfo()
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<BaseModel<UserInfoModel>>(this) {
                    @Override
                    public void onSuccess(BaseModel<UserInfoModel> userInfoModel) {
                        UserInfoModel data = userInfoModel.getData();
                        if (data != null) {
                            initUserInfo(data);
                        }
                    }
                });
    }

    private void initUserInfo(UserInfoModel userInfo) {
        mSex = userInfo.getGender();

        if (TextUtils.isEmpty(userInfo.getImgUrl())) {
            ImageLoader.loadImage(this, R.drawable.not_login_avatar, mCivAvatar);
        } else {
            ImageLoader.loadImage(this, userInfo.getImgUrl(), mCivAvatar);
        }
        mNickname = userInfo.getNickname();
        mEtNickname.setText(mNickname);
        mTvPhoneNumber.setText(getString(R.string.int2String, userInfo.getTelphone()));
        if ("".equals(mSex)) {
            mTvSex.setText(mSex);
        } else {
            mTvSex.setText("M".equals(mSex) ? R.string.personal_male : R.string.personal_female);
        }
        mTvArea.setText(userInfo.getArea());
        if ((userInfo.getBirthday() == 0)) {
            mTvBirthday.setText("请选择");
            mDate = new Date();
        } else {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale
                    .getDefault());
            final String format = sdf.format(new Date(userInfo.getBirthday()));
            mTvBirthday.setText(format);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_personal_info;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (mUpdateMap.size() == 0 && mSelectAvatarFile == null) {
                ToastUtils.show(R.string.personal_not_update);
                return false;
            }
            if (mSelectAvatarFile != null) {
                requestQiniuToken();
                return true;
            }
            requestEditInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 用户上传头像请求
     */
    private void requestQiniuToken() {
        mUploadManager = new UploadManager();
        RetrofitHelper.getService().qiniuToken()
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<String>(this) {
                    @Override
                    public void onSuccess(String token) {
                        uploadQiniu(token);
                    }
                });
    }

    /**
     * 用户不上传时请求
     */
    private void requestEditInfo() {
        Gson gson = new Gson();
        UserModifyBody userModifyBody = gson.fromJson(gson.toJson(mUpdateMap), UserModifyBody
                .class);
        RetrofitHelper.getService().userModify(userModifyBody)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<Object>(this) {
                    @Override
                    public void onSuccess(Object token) {
                        ToastUtils.show(R.string.personal_update_success);
                        finish();
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_LOGIN));
                    }
                });
    }

    private void uploadQiniu(String token) {
        mUploadManager.put(mSelectAvatarFile, null, token,
                (key, info, res) -> {
                    //res包含hash、key等信息，具体字段取决于上传策略的设置
                    if (info.isOK()) {
                        try {
                            String key2 = res.getString("key");
                            mUpdateMap.put(Constant.KEY_IMG_URL, Constant.QINIU_HOST + key2);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Logger.d("Upload qiniu Fail");
                        //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                    }
                    requestEditInfo();
                    Logger.i(key + ",\r\n " + info + ",\r\n " + res);
                }, null);
    }

    private void initJsonData() {
        /*
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, FILE_NAME);//获取assets目录下的json文件数据

        ArrayList<AreaJsonBean> areaJsonBeans = parseData(JsonData);//用Gson 转成实体
        options1Items = new ArrayList<>(areaJsonBeans.size());
        options2Items = new ArrayList<>(areaJsonBeans.size());
        /*
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = areaJsonBeans;
        final int provinceSize = areaJsonBeans.size();
        for (int i = 0; i < provinceSize; i++) {//遍历省份
            final List<AreaJsonBean.CityBean> city = areaJsonBeans.get(i).getCity();
            final int citySize = city.size();
            ArrayList<String> cityList = new ArrayList<>(citySize);//该省的城市列表（第二级）
            for (int c = 0; c < citySize; c++) {//遍历该省份的所有城市
                String CityName = city.get(c).getName();
                cityList.add(CityName);//添加城市
            }
            //添加城市数据
            options2Items.add(cityList);
        }
    }

    public ArrayList<AreaJsonBean> parseData(String result) {//Gson 解析
        Type type = new TypeToken<List<AreaJsonBean>>() {
        }.getType();
        final Gson gson = new Gson();
        return gson.fromJson(result, type);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @OnClick({R.id.rl_avatar, R.id.rl_sex, R.id.rl_birthday, R.id.btn_log_out,
            R.id.rl_area})
    public void onViewClicked(View view) {
        ViewUtils.hideSoftKeyboard(this);
        switch (view.getId()) {
            case R.id.rl_avatar:
                showTakePhotoDialog();
                break;
            case R.id.rl_sex:
                showSexPicker();
                break;
            case R.id.rl_birthday:
                showBirthdayPicker();
                break;
            case R.id.rl_area:
                showCityPicker();
                break;
            case R.id.btn_log_out:
                IotKitAccountManager.getInstance().logout(new IotKitAccountCallback() {
                    @Override
                    public void onCompleted(boolean isSucceed, String msg) {
                        DbUtils.clear();
                        ToastUtils.show("退出成功");
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.CODE_LOGOUT));
                        finish();
                    }
                });
                break;
            default:
                break;
        }
    }

    /**
     * 显示dialog
     */
    protected void showTakePhotoDialog() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        @SuppressLint("InflateParams") View pickerView = getLayoutInflater().inflate(R.layout
                .layout_avatar_picker_bottom_view, null);
        bottomSheetDialog.setContentView(pickerView);
        TextView camera = pickerView.findViewById(R.id.tv_avatar_picker_camera);
        camera.setOnClickListener(v -> {
            getTakePhoto().onPickFromCaptureWithCrop(TakePhotoUtils.uri(), getCropOptions());
            bottomSheetDialog.dismiss();
        });
        TextView album = pickerView.findViewById(R.id.tv_avatar_picker_album);
        album.setOnClickListener(v -> {
            getTakePhoto().onPickFromGalleryWithCrop(TakePhotoUtils.uri(), getCropOptions());
            bottomSheetDialog.dismiss();
        });
        TextView cancel = pickerView.findViewById(R.id.tv_avatar_picker_cancel);
        cancel.setOnClickListener(v -> bottomSheetDialog.dismiss());
        bottomSheetDialog.show();
    }

    private void showSexPicker() {
        if (mSexPicker == null) {
            int s = "M".equals(mSex) ? 0 : 1;
            final ArrayList<String> optionsSex = new ArrayList<>(2);
            optionsSex.add("男");
            optionsSex.add("女");
            mSexPicker = OptionPickerHelper.createPicker(this, "性别", optionsSex, s, (options1,
                                                                                     options2,
                                                                                     options3, v)
                    -> {
                String sexTx = optionsSex.get(options1);
                mTvSex.setText(sexTx);
                mUpdateMap.put(Constant.KEY_GENDER, options1 == 0 ? "M" : "F");
            });
        }
        mSexPicker.show();
    }

    private void showBirthdayPicker() {
        if (mAgePicker == null) {
            Calendar selectedDate = Calendar.getInstance();
            Calendar startDate = Calendar.getInstance();
            startDate.set(1920, 1, 1);
            Calendar endDate = Calendar.getInstance();

            endDate.setTime(new Date());

            selectedDate.setTime(mDate);
            mAgePicker = new TimePickerView.Builder(this, (date, v) -> {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String dateStr = sdf.format(date);
                mTvBirthday.setText(dateStr);
                mUpdateMap.put(Constant.KEY_BIRTHDAY, date.getTime());
            })
                    .setTitleText("选择出生年月")
                    .setCancelColor(ContextCompat.getColor(this, R.color.personal_picker_cancel))
                    .setSubmitColor(ContextCompat.getColor(this, R.color.black))
                    .setType(new boolean[]{true, true, true, false, false, false})
                    .setLabel("", "", "", "", "", "")
                    .setRangDate(startDate, endDate)
                    .setDate(selectedDate)
                    .build();
        }
        mAgePicker.show();
    }

    private void showCityPicker() {
        if (pvCityOptions == null) {
            pvCityOptions = new OptionsPickerView.Builder(this, (options1, options2, options3, v)
                    -> {
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
                mTvArea.setText(String.format("%s%s", province, city));
                mUpdateMap.put(Constant.KEY_AREA, province + city);
            })
                    .setTitleText("城市选择")
                    .setSubmitText("确定")//确定按钮文字
                    .setCancelText("取消")//取消按钮文字
                    .setSubmitColor(ContextCompat.getColor(this, R.color.black))
                    .setCancelColor(ContextCompat.getColor(this, R.color.personal_picker_cancel))
                    .setSelectOptions(0, 0)  //设置默认选中项
                    .build();
            pvCityOptions.setPicker(options1Items, options2Items);
        }
        pvCityOptions.show();
    }

    /**
     * 获取TakePhoto实例
     *
     * @return TakePhoto
     */
    protected TakePhoto getTakePhoto() {
        if (mTakePhoto == null) {
            mTakePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl
                    (this, this));
            mTakePhoto.onEnableCompress(CompressConfig.ofDefaultConfig(), true);
        }
        return mTakePhoto;
    }

    protected CropOptions getCropOptions() {
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setOutputX(352).setOutputY(352).setAspectX(1).setAspectY(1);
        return builder.create();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode,
                permissions, grantResults);
        PermissionManager.handlePermissionsResult(this, type, mInvokeParam, this);
    }

    @Override
    public void takeSuccess(TResult result) {
        mSelectAvatarFile = new File(result.getImage().getCompressPath());
        ImageLoader.loadImage(this, mSelectAvatarFile, mCivAvatar);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Logger.d("take photo fail");
    }

    @Override
    public void takeCancel() {
        Logger.d("take photo stopWifiConfig");
    }

    @Override
    public TPermissionType invoke(InvokeParam invokeParam) {
        TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this),
                invokeParam.getMethod());
        if (TPermissionType.WAIT.equals(type)) {
            this.mInvokeParam = invokeParam;
        }
        return type;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String value = s.toString();
        if (!value.equals(mNickname)) {
            mUpdateMap.put(Constant.KEY_NICKNAME, value);
        }
    }
}
