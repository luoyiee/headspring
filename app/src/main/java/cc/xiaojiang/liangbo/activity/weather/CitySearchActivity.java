package cc.xiaojiang.liangbo.activity.weather;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.adapter.CitySearchAdapter;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.MyObserver;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.model.eventbus.MessageCityAdd;
import cc.xiaojiang.liangbo.model.http.CityAddBody;
import cc.xiaojiang.liangbo.model.http.CityQueryModel;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cc.xiaojiang.liangbo.utils.ToastUtils;
import cc.xiaojiang.liangbo.utils.ViewUtils;
import io.realm.Realm;

public class CitySearchActivity extends BaseActivity implements TextView.OnEditorActionListener,
        BaseQuickAdapter.OnItemClickListener {

    @BindView(R.id.edTxt_city_search_city)
    AppCompatEditText mEdTxtCitySearchCity;
    @BindView(R.id.rv_city_search)
    RecyclerView mRvCitySearch;
    @BindView(R.id.iv_city_search_del)
    ImageView mIvCitySearchDel;
    private Realm mRealm;

    private CitySearchAdapter mCitySearchAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRealm = Realm.getDefaultInstance();
        mEdTxtCitySearchCity.setOnEditorActionListener(this);
        init();
    }

    private void init() {
        mCitySearchAdapter = new CitySearchAdapter(R.layout.item_city_search, new ArrayList<>());
        mRvCitySearch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager
                .VERTICAL, false));
        mRvCitySearch.setAdapter(mCitySearchAdapter);
        mCitySearchAdapter.setOnItemClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_city_search;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            closeActivityByFade();
            return false;
        } else {
            return true;
        }
    }

    private void closeActivityByFade() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

//    private void searchCity(String city) {
//        if (TextUtils.isEmpty(city)) {
//            ToastUtils.show("城市不能为空");
//            return;
//        }
//        RealmResults<WeatherCityCodeRealm> cities = mRealm.where(WeatherCityCodeRealm.class)
//                .like("name", "*" + city + "*")
//                .findAll();
//        mCitySearchAdapter.setNewData(cities);
//    }

    @Override
    protected void onDestroy() {
        mRealm.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        closeActivityByFade();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            searchCity(v.getText().toString());
            ViewUtils.hideSoftKeyboard(this);
            searchCity2(v.getText().toString().trim());
        }
        return false;
    }

    private void searchCity2(String city) {
        RetrofitHelper.getService().cityQuery(city)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<List<CityQueryModel>>() {
                    @Override
                    public void onSuccess(List<CityQueryModel> list) {
                        mCitySearchAdapter.setNewData(list);
                    }
                });
    }


    @OnClick(R.id.iv_city_search_del)
    public void onViewClicked() {
        mEdTxtCitySearchCity.setText("");
        mCitySearchAdapter.setNewData(null);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        CityQueryModel CityQueryModel = (CityQueryModel) adapter.getItem(position);
        cityAdd(CityQueryModel.getId());
    }

    private void cityAdd(String id) {
        if (TextUtils.isEmpty(id)) {
            ToastUtils.show("城市暂时不支持");
            return;
        }
        CityAddBody cityAddBody = new CityAddBody(id);
        RetrofitHelper.getService().cityAdd(cityAddBody)
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new MyObserver<Object>() {
                    @Override
                    public void onSuccess(Object o) {
                        ToastUtils.show("添加成功");
                        EventBus.getDefault().post(new MessageCityAdd());
                        finish();
                    }
                });
    }
}
