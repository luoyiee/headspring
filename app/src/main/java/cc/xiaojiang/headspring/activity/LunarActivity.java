package cc.xiaojiang.headspring.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarView;

import butterknife.BindView;
import cc.xiaojiang.headspring.R;
import cc.xiaojiang.headspring.base.BaseActivity;

public class LunarActivity extends BaseActivity implements CalendarView.OnDateSelectedListener,
        CalendarView.OnYearChangeListener {

    @BindView(R.id.calendarView)
    CalendarView mCalendarView;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
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
        mTvTitle.setText(getString(R.string.lunar_year_month, mCalendarView.getCurYear(), mCalendarView.getCurMonth()));
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_lunar;
    }

    @Override
    public void onDateSelected(Calendar calendar, boolean isClick) {
        mTvTitle.setText(getString(R.string.lunar_year_month, calendar.getYear(), calendar.getMonth()));
        mYear = calendar.getYear();
    }

    @Override
    public void onYearChange(int year) {
        mTvTitle.setText(String.valueOf(year));
    }
}
