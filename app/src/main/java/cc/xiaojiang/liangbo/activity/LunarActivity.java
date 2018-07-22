package cc.xiaojiang.liangbo.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import cc.xiaojiang.liangbo.R;
import cc.xiaojiang.liangbo.base.BaseActivity;
import cc.xiaojiang.liangbo.http.HttpResultFunc;
import cc.xiaojiang.liangbo.http.RetrofitHelper;
import cc.xiaojiang.liangbo.http.progress.ProgressObserver;
import cc.xiaojiang.liangbo.model.http.LunarInfoModel;
import cc.xiaojiang.liangbo.utils.RxUtils;

public class LunarActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_calendar_day)
    TextView mTvCalendarDay;
    @BindView(R.id.tv_solar_terms)
    TextView mTvSolarTerms;
    @BindView(R.id.tv_lunar_time)
    TextView mTvLunarTime;
    @BindView(R.id.tv_lunar_year)
    TextView mTvLunarYear;
    @BindView(R.id.tv_lunar_month_day)
    TextView mTvLunarMonthDay;
    private int mYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mYear = mCalendarView.getCurYear();
        mTvTitle.setOnClickListener(v -> {
            mCalendarView.showYearSelectLayout(mYear);
            // TODO: 2018/7/22 年视图文字重叠
            mTvTitle.setText(String.valueOf(mYear));
        });
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
        mTvTitle.setText(getString(R.string.lunar_year_month, mCalendarView.getCurYear(),
                mCalendarView.getCurMonth()));

        requestLunarInfo(mCalendarView.getSelectedCalendar());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_lunar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        if (isClick) {
            requestLunarInfo(calendar);
            return;
        }
        mTvTitle.setText(getString(R.string.lunar_year_month, calendar.getYear(), calendar
                .getMonth()));
        mYear = calendar.getYear();
    }

    private void requestLunarInfo(Calendar calendar) {
        RetrofitHelper.getService().lunarInfo(calendar.toString())
                .map(new HttpResultFunc<>())
                .compose(RxUtils.rxSchedulerHelper())
                .compose(bindToLifecycle())
                .subscribe(new ProgressObserver<LunarInfoModel>(this) {
                    @Override
                    public void onSuccess(LunarInfoModel lunarInfoModel) {
                        mTvLunarTime.setText(getString(R.string.lunar_info_time, lunarInfoModel
                                        .getLunarYear(),
                                lunarInfoModel.getLunarMonthName(), lunarInfoModel
                                        .getLunarDayName()));
                        mTvLunarYear.setText(getString(R.string.lunar_info_year, lunarInfoModel
                                        .getGanzhiYear(),
                                lunarInfoModel.getZodiac()));
                        mTvLunarMonthDay.setText(getString(R.string.lunar_info_month_day,
                                lunarInfoModel.getGanzhiMonth(),
                                lunarInfoModel.getGanzhiDay()));
                        mTvSolarTerms.setText(lunarInfoModel.getSolarTerm());
                        mTvCalendarDay.setText(getString(R.string.int2String, calendar.getDay()));
                    }
                });
    }

    @Override
    public void onYearChange(int year) {
        mTvTitle.setText(String.valueOf(year));
    }
}
