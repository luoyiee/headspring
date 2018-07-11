package cc.xiaojiang.headspring.activity;

import android.os.Bundle;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class LunarActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCalendarView.setOnDateSelectedListener(this);
        mCalendarView.setOnYearChangeListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lunar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onYearChange(int year) {

    }
}
