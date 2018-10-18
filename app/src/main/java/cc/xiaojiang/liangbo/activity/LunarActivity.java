package cc.xiaojiang.liangbo.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.RetrofitHelperMob2;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.LunarBean;
import cc.xiaojiang.liangbo.model.http.LunarInfoModel;
import cc.xiaojiang.liangbo.utils.RxUtils;
import cn.iwgang.simplifyspan.SimplifySpanBuild;
import cn.iwgang.simplifyspan.unit.SpecialTextUnit;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LunarActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_calendar_day)
    TextView mTvCalendarDay;
    @BindView(R.id.tv_lunar_lunar)
    TextView mTvLunarLunar;
    @BindView(R.id.tv_lunar_lunarYear)
    TextView mTvLunarLunarYear;
    @BindView(R.id.tv_lunar_suit)
    TextView mTvLunarSuit;
    @BindView(R.id.tv_lunar_avoid)
    TextView mTvLunarAvoid;
    @BindView(R.id.tv_lunar_week)
    TextView mTvLunarWeek;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mYear = mCalendarView.getCurYear();
        mTvTitle.setOnClickListener(v -> {
            mCalendarView.showYearSelectLayout(mYear);
            mTvTitle.setText(String.valueOf(mYear));
        });
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTvTitle.setText(getString(R.string.lunar_year_month, mCalendarView.getCurYear(),
                mCalendarView.getCurMonth()));

        request(mCalendarView.getSelectedCalendar());
    }

    private void request(Calendar calendar) {
        getLunar(calendar);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lunar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        if (isClick) {
            request(calendar);
            return;
        }
        mTvTitle.setText(getString(R.string.lunar_year_month, calendar.getYear(), calendar
                .getMonth()));
        mYear = calendar.getYear();
    }


    private void getLunar(Calendar calendar) {
        int year = calendar.getYear();
        int month = calendar.getMonth();
        int day = calendar.getDay();
        String date = year + "-" + (month < 10 ? "0" + month : month) + "-" + (day < 10 ? "0" +
                day : day);
        RetrofitHelperMob2.getService().lunar("2672c36a91131", date)
                .compose(bindToLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LunarBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(LunarBean lunarBean) {
                        if (lunarBean != null) {
                            if ("200".equals(lunarBean.getRetCode())) {
                                if (lunarBean.getResult() != null) {
                                    LunarBean.ResultBean resultBean = lunarBean.getResult();
                                    mTvLunarSuit.setText(new SimplifySpanBuild()
                                            .append(new SpecialTextUnit("宜", Color.parseColor
                                                    ("#16a04c")))
                                            .append(" ")
                                            .append(resultBean.getSuit()).build());
                                    mTvLunarAvoid.setText(new SimplifySpanBuild()
                                            .append(new SpecialTextUnit("忌", Color.parseColor
                                                    ("#ed152a")))
                                            .append(" ")
                                            .append(resultBean.getAvoid()).build());

                                    mTvLunarWeek.setText(resultBean.getWeekday());
                                    mTvLunarLunarYear.setText(new SimplifySpanBuild()
                                            .append(resultBean.getLunarYear())
                                            .append("【")
                                            .append(resultBean.getZodiac())
                                            .append("】").build());
                                    mTvLunarLunar.setText(resultBean.getLunar());
                                    mTvCalendarDay.setText(day + "");

                                }
                            }
                        }


                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onYearChange(int year) {
        mTvTitle.setText(String.valueOf(year));
    }
}
